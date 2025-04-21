package com.project.ecommerceApi.Service.Impl;

import com.project.ecommerceApi.DTO.ItemsRequested;
import com.project.ecommerceApi.DTO.OrderRequest;
import com.project.ecommerceApi.DTO.PaymentRequest;
import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Entity.Order;
import com.project.ecommerceApi.Entity.OrderedProduct;
import com.project.ecommerceApi.Entity.Product;
import com.project.ecommerceApi.Entity.User;
import com.project.ecommerceApi.Exception.GeneralUseException;
import com.project.ecommerceApi.Exception.OutOfStock;
import com.project.ecommerceApi.Repository.OrderRepository;
import com.project.ecommerceApi.Repository.OrderedProductRepository;
import com.project.ecommerceApi.Repository.ProductRepository;
import com.project.ecommerceApi.Repository.UserRepository;
import com.project.ecommerceApi.Service.OrderService;
import com.project.ecommerceApi.Service.UserCheckService;
import com.project.ecommerceApi.Utility.Mapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderedProductRepository orderedProductRepository;
    private final UserCheckService userCheckService;

    @Value("${api.stripe.secretKey}")
    private String stripeSecretKey;

    @Override
    public Response addOrder(Long userId, OrderRequest orderRequest) {
        Response response = new Response();
        Order order = new Order();
        User user;

        /*Check if the user exists*/
        try {
            user = userRepository.findById(userId).orElseThrow(() -> new GeneralUseException("User " + userId + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }

        /*Check if the logged user is accessing its resources or not*/
        if (!userCheckService.checkIfCurrentUserIsAdmin() &&
                !((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().equals(userId)) {
            response.setHttpCode(403);
            response.setMessage("User not authorized");
            return response;
        }

        List<OrderedProduct> orderedProducts;
        /*Generate a list of the ordered products*/
        try {
            orderedProducts = orderRequest.getItemsRequestedList().stream().map(itemsRequested -> generateOrderedItem(order, itemsRequested)).toList();
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        } catch (OutOfStock e) {
            response.setHttpCode(200);
            response.setMessage(e.getMessage());
            return response;
        }

        order.setOrderCode(UUID.randomUUID().toString());
        order.setCreationDate(LocalDateTime.now());
        order.setUser(user);

        /*Generate the total price of the order based on the product and quantity*/
        double priceTOT = orderedProducts.stream().mapToDouble(orderedProduct -> orderedProduct.getQuantity() * orderedProduct.getProduct().getPrice()).sum();
        order.setPriceTotal(priceTOT);
        order.setStatus("Pending");
        orderRepository.save(order);

        orderedProducts.forEach(orderedProduct -> orderedProductRepository.save(orderedProduct));

        response.setHttpCode(200);
        return response;
    }

    @Override
    public Response getAllOrders() {
        Response response = new Response();
        List<Order> orders = orderRepository.findAll();

        /*Check if there are orders or not*/
        if (orders.isEmpty()) {
            response.setHttpCode(404);
            response.setMessage("No order found");
        } else {
            response.setHttpCode(200);
            response.setOrderDTOS(Mapper.mapOrderListToOrderDTOList(orders));
        }
        return response;
    }

    @Override
    public Response checkoutOrder(PaymentRequest paymentRequest) {
        Long orderId = paymentRequest.getOrderDTO().getId();

        /*Check if the order can be searched*/
        Response response = isValidSearch(orderId);
        if (response.getHttpCode() != 200) {
            return response;
        }
        Order order = response.getOrder();
        response.setOrder(null);

        Stripe.apiKey = stripeSecretKey;

        /*Create productData*/
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(paymentRequest.getOrderDTO().getOrderCode())
                        .build();

        /*Create priceData with productData*/
        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(paymentRequest.getCurrency() != null ? paymentRequest.getCurrency() : "EUR")
                        .setUnitAmount((long) (paymentRequest.getOrderDTO().getPriceTotal() * 100))
                        .setProductData(productData)
                        .build();

        /*Create lineItem with priceData*/
        SessionCreateParams.LineItem lineItem =
                SessionCreateParams
                        .LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();

        /*Create the session's parameters with lineItem*/
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/successfulPayment")
                        .setCancelUrl("http://localhost:8080/cancelPayment")
                        .addLineItem(lineItem)
                        .build();

        /*Check it's possible to create a session*/
        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            response.setHttpCode(400);
            response.setMessage("Failed to create the session\n" + e.getMessage());
            return response;
        }
        order.setStatus("Paid");
        orderRepository.save(order);
        response.setSessionId(session.getId());
        response.setSessionUrl(session.getUrl());
        return response;
    }

    @Override
    public Response addItemToOrder(Long id, ItemsRequested itemsRequested) {

        /*Check if the order can be searched*/
        Response response = isValidSearch(id);
        if (response.getHttpCode() != 200) {
            return response;
        }
        Order order = response.getOrder();
        response.setOrder(null);

        /*Check if the order is not yet paid*/
        if (!order.getStatus().equals("Pending")) {
            response.setMessage("Paid orders can't be changed");
            return response;
        }
        OrderedProduct orderedProduct;

        /*Generate an ordered product*/
        try {
            orderedProduct = generateOrderedItem(order, itemsRequested);
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        } catch (OutOfStock e) {
            response.setHttpCode(200);
            response.setMessage(e.getMessage());
            return response;
        }
        orderedProductRepository.save(orderedProduct);

        /*Adjust the total price of the order to the new value accordingly*/
        order.setPriceTotal(order.getPriceTotal() + itemsRequested.getQuantity() * orderedProduct.getProduct().getPrice());
        orderRepository.save(order);
        return response;
    }

    @Override
    @Transactional
    public Response removeOrderedProduct(Long orderId, Long itemId) {

        /*Check if the order can be searched*/
        Response response = isValidSearch(orderId);
        if (response.getHttpCode() != 200) {
            return response;
        }
        Order order = response.getOrder();
        response.setOrder(null);

        /*Check if the order is not yet paid*/
        if (!order.getStatus().equals("Pending")) {
            response.setMessage("Paid orders can't be changed");
            return response;
        }
        OrderedProduct orderedProduct;

        /*Check if the ordered product is actually in the order*/
        try {
            orderedProduct = orderedProductRepository.findById(itemId).orElseThrow((() -> new GeneralUseException("Ordered product " + itemId + " not found")));
            if (order.getOrderedProducts().stream().noneMatch(orderedItem -> orderedItem.getId().equals(orderedProduct.getId()))) {
                throw new GeneralUseException("Ordered product " + itemId + " not found");
            }
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }

        /*Adjust the total price of the order to the new value accordingly*/
        order.setPriceTotal(order.getPriceTotal() - orderedProduct.getQuantity() * orderedProduct.getProduct().getPrice());
        orderRepository.save(order);
        Product product = orderedProduct.getProduct();

        /*Restock the product related to the removed ordered item*/
        product.setStockQuantity(product.getStockQuantity() + orderedProduct.getQuantity());
        productRepository.save(product);
        orderedProductRepository.deleteById(itemId);
        return response;
    }

    @Override
    public Response getOrderById(Long id) {

        /*Check if the order can be searched*/
        Response response = isValidSearch(id);
        if (response.getHttpCode() == 200) {
            response.setOrderDTO(Mapper.mapOrderToOrderDTO(response.getOrder()));
            response.setOrder(null);
        }
        return response;
    }

    @Override
    @Transactional
    public Response getProductsByOrderId(Long id) {

        /*Check if the order can be searched*/
        Response response = isValidSearch(id);
        if (response.getHttpCode() == 200) {
            response.setOrderDTO(Mapper.mapOrderToOrderDTOPlusProductsDTO(response.getOrder()));
            response.setOrder(null);
        }
        return response;
    }

    @Override
    @Transactional
    public Response deleteOrder(Long id) {

        /*Check if the order can be searched*/
        Response response = isValidSearch(id);
        if (response.getHttpCode() == 200) {
            Order order = response.getOrder();
            response.setOrder(null);

            /*Check if the order is not yet paid*/
            if (!order.getStatus().equals("Pending")) {
                response.setMessage("Paid orders can't be changed");
                return response;
            }

            /*Restock the products of the order*/
            order.getOrderedProducts().forEach(orderedProduct -> {
                Product product = orderedProduct.getProduct();
                product.setStockQuantity(product.getStockQuantity() + orderedProduct.getQuantity());
                productRepository.save(product);
            });
            orderRepository.deleteById(id);
        }
        return response;
    }

    /*Check if the products are in stock*/
    private boolean isInStock(Product product, int quantity) {
        return product.getStockQuantity() - quantity >= 0;
    }

    /*Check if the order is actually stored in the table*/
    private Response isValidSearch(Long id) {
        Response response = new Response();
        Order order;
        try {
            order = orderRepository.findById(id).orElseThrow(() -> new GeneralUseException("Order " + id + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }
        return supplemetaryValidation(order, response);
    }

    /*Check if the user is accessing its own resources*/
    private Response supplemetaryValidation(Order order, Response response) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userCheckService.checkIfCurrentUserIsAdmin() &&
                !user.getId().equals(order.getUser().getId())) {
            response.setHttpCode(403);
            response.setMessage("User not authorized");
            return response;
        }
        response.setHttpCode(200);
        response.setOrder(order);
        return response;
    }

    /*Generate the objects containing the ordered product*/
    private OrderedProduct generateOrderedItem(Order order, ItemsRequested itemsRequested) throws GeneralUseException, OutOfStock {
        Product product;
        Long productId = itemsRequested.getId();

        /*Check if the product is actually stored in the table*/
        product = productRepository.findById(productId).orElseThrow(() -> new GeneralUseException("Product " + productId + " not found"));

        /*Check if the products are in stock*/
        if (!isInStock(product, itemsRequested.getQuantity())) {
            throw new OutOfStock("Product not in stock");
        }

        /*Set the product stock accordingly to the order*/
        product.setStockQuantity(product.getStockQuantity() - itemsRequested.getQuantity());
        productRepository.save(product);
        OrderedProduct orderedProduct = new OrderedProduct();
        orderedProduct.setQuantity(itemsRequested.getQuantity());
        orderedProduct.setProduct(product);
        orderedProduct.setOrder(order);
        return orderedProduct;
    }
}
