package com.example.smartrestaurant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.smartrestaurant.model.Permissions;

import java.util.Collection;
import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions, Integer> {
    List<Permissions> findByNameIn(Collection<String> names);
    boolean existsByName(String name);
}
