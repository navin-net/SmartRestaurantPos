package com.example.smartrestaurant.dto.response;

import com.example.smartrestaurant.model.Permissions;

public record PermissionResponse(int id, String name, String description) {
    public static PermissionResponse from(Permissions p) {
        return new PermissionResponse(p.getId(), p.getName(), p.getDescription());
    }
}