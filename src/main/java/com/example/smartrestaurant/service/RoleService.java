package com.example.smartrestaurant.service;

import com.example.smartrestaurant.exception.ApiException;
import com.example.smartrestaurant.model.Permissions;
import com.example.smartrestaurant.model.Role;
import com.example.smartrestaurant.repositories.PermissionRepository;
import com.example.smartrestaurant.repositories.RoleRepository;
import com.example.smartrestaurant.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private static final String ADMIN = "ADMIN";

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Role findById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Role", "តួនាទី", id));
    }

    @Transactional
    public Role create(String name, String description, Set<String> permissionNames) {
        String normalized = name.trim().toUpperCase();
        if (roleRepository.existsByName(normalized)) {
            throw ApiException.duplicate("Role already exists", "តួនាទីមានរួចហើយ");
        }
        Role role = new Role();
        role.setName(normalized);
        role.setDescription(description);
        role.setPermissions(resolve(permissionNames));
        return roleRepository.save(role);
    }

    @Transactional
    public Role update(Integer id, String name, String description, Set<String> permissionNames) {
        Role role = findById(id);
        String normalized = name.trim().toUpperCase();
        if (ADMIN.equals(role.getName()) && !ADMIN.equals(normalized)) {
            throw ApiException.forbidden("The ADMIN role cannot be renamed",
                    "មិនអាចប្តូរឈ្មោះតួនាទី ADMIN បានទេ");
        }
        if (!normalized.equals(role.getName()) && roleRepository.existsByName(normalized)) {
            throw ApiException.duplicate("Role already exists", "តួនាទីមានរួចហើយ");
        }
        role.setName(normalized);
        role.setDescription(description);
        if (permissionNames != null) {
            role.setPermissions(resolve(permissionNames));
        }
        return roleRepository.save(role);
    }

    @Transactional
    public void delete(Integer id) {
        Role role = findById(id);
        if (ADMIN.equals(role.getName())) {
            throw ApiException.forbidden("The ADMIN role cannot be deleted",
                    "មិនអាចលុបតួនាទី ADMIN បានទេ");
        }
        if (userRepository.existsByRoleId(id)) {
            throw ApiException.duplicate("Role is assigned to users",
                    "តួនាទីកំពុងត្រូវបានប្រើដោយអ្នកប្រើ");
        }
        roleRepository.delete(role);
    }

    private Set<Permissions> resolve(Set<String> names) {
        if (names == null || names.isEmpty()) {
            return new HashSet<>();
        }
        List<Permissions> found = permissionRepository.findByNameIn(names);
        if (found.size() != names.size()) {
            throw ApiException.badRequest("Unknown permission in list",
                    "មានសិទ្ធិមិនស្គាល់ក្នុងបញ្ជី");
        }
        return new HashSet<>(found);
    }
}