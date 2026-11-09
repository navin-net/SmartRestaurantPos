package com.example.smartrestaurant.repositories;

import com.example.smartrestaurant.model.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    @EntityGraph(attributePaths = "role")
    Optional<Users> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByRoleId(int roleId);

    @Modifying
    @Transactional
    @Query("update Users u set u.lastLoginAt = :now where u.id = :id")
    void touchLogin(@Param("id") int id, @Param("now") Instant now);
}
