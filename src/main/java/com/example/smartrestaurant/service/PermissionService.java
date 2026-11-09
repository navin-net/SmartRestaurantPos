package com.example.smartrestaurant.service;

import com.example.smartrestaurant.exception.ApiException;
import com.example.smartrestaurant.model.Permissions;
import com.example.smartrestaurant.repositories.PermissionRepository;
import com.example.smartrestaurant.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<Permissions> findAll() {
        return permissionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Permissions findById(Integer id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Permission", "សិទ្ធិ", id));
    }

    @Transactional
    public Permissions create(String name, String description) {
        String normalized = name.trim();
        if (permissionRepository.existsByName(normalized)) {
            throw ApiException.duplicate("Permission already exists", "សិទ្ធិមានរួចហើយ");
        }
        Permissions permission = new Permissions();
        permission.setName(normalized);
        permission.setDescription(description);
        return permissionRepository.save(permission);
    }

    @Transactional
    public Permissions update(Integer id, String name, String description) {
        Permissions permission = findById(id);
        String normalized = name.trim();
        if (!normalized.equals(permission.getName()) && permissionRepository.existsByName(normalized)) {
            throw ApiException.duplicate("Permission already exists", "សិទ្ធិមានរួចហើយ");
        }
        permission.setName(normalized);
        permission.setDescription(description);
        return permissionRepository.save(permission);
    }

    @Transactional
    public void delete(Integer id) {
        Permissions permission = findById(id);
        if (roleRepository.existsByPermissions_Id(id)) {
            throw ApiException.duplicate("Permission is assigned to roles",
                    "សិទ្ធិកំពុងត្រូវបានប្រើដោយតួនាទី");
        }
        permissionRepository.delete(permission);
    }
}
