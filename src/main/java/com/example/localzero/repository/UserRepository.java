package com.example.localzero.repository;
import com.example.localzero.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
}
