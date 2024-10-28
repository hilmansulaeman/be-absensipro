package com.juaracoding.repo;

import com.juaracoding.model.Userz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Userz, Long> {
    Optional<Userz> findByEmail(String email);
    Optional<Userz> findByUsername(String username);

    // Menambahkan metode pencarian untuk mendukung Pageable
    Page<Userz> findByEmailContaining(String email, Pageable pageable);
    Page<Userz> findByUsernameContaining(String username, Pageable pageable);

}