package com.project.ecommerceApi.Service;

import com.project.ecommerceApi.Entity.User;

public interface UserCheckService {

    boolean checkIfCurrentUserIsAdmin();

    User findMyInfo(String email);
}
