package com.example.smartrestaurant.dto.response;

import com.example.smartrestaurant.model.Users;

public record UserRoleResponse(int userId, String username, Integer roleId, String roleName) {
    public static UserRoleResponse from(Users user) {
        var role = user.getRole();
        return new UserRoleResponse(user.getId(), user.getUsername(),
                role == null ? null : role.getId(), role == null ? null : role.getName());
    }
}