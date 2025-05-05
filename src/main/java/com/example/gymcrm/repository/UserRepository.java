package com.example.gymcrm.repository;

import com.example.gymcrm.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Long countByIsActive(Boolean isActive);

    @Query("SELECT COUNT(a) > 0 FROM Admin a WHERE a.user.username = :username")
    boolean isAdminByUsername(@Param("username") String username);

}
