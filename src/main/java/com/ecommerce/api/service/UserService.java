package com.ecommerce.api.service;

import com.ecommerce.api.dto.request.UserRegisterRequest;
import com.ecommerce.api.dto.request.UserUpdateRequest;
import com.ecommerce.api.dto.response.PagedResponse;
import com.ecommerce.api.dto.response.UserResponse;

public interface UserService {
    UserResponse register(UserRegisterRequest request);
    UserResponse getById(Long id);
    UserResponse getByUsername(String username);
    PagedResponse<UserResponse> getAllUsers(int page, int size, String sortBy, String sortDir);
    UserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
}
