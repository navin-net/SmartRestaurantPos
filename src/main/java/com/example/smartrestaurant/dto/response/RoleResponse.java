package com.example.smartrestaurant.dto.response;

import com.example.smartrestaurant.model.Permissions;
import com.example.smartrestaurant.model.Role;
import java.util.List;

public record RoleResponse(int id, String name, String description, List<String> permissions) {
    public static RoleResponse from(Role role) {
        return new RoleResponse(role.getId(), role.getName(), role.getDescription(),
                role.getPermissions().stream().map(Permissions::getName).sorted().toList());
    }
}