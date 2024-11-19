package com.juaracoding.repo;

import com.juaracoding.model.Userz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Userz, Long> {
    Optional<Userz> findByEmail(String email);
    Optional<Userz> findByUsername(String username);

    // Menambahkan metode pencarian untuk mendukung Pageable
    Page<Userz> findByIsDelete(Pageable page , byte byteIsDelete);
    List<Userz> findByIsDelete(byte byteIsDelete);

    public List<Userz> findByEmailOrNoHPOrUsername(String emails, String noHP, String userName);

    Page<Userz> findByIsDeleteAndNamaLengkapContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);

    List<Userz> findByIsDeleteAndNamaLengkapContainsIgnoreCase(byte byteIsDelete, String values);

    Page<Userz> findByIsDeleteAndEmailContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);

    List<Userz> findByIsDeleteAndEmailContainsIgnoreCase(byte byteIsDelete, String values);

    Page<Userz> findByIsDeleteAndUsernameContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);

    List<Userz> findByIsDeleteAndUsernameContainsIgnoreCase(byte byteIsDelete, String values);


    Page<Userz> findByIsDeleteAndNoHPContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);

    List<Userz> findByIsDeleteAndNoHPContainsIgnoreCase(byte byteIsDelete, String values);


    Page<Userz> findByIsDeleteAndIdUser(Pageable page , byte byteIsDelete, Long values);
    List<Userz> findByIsDeleteAndIdUser(byte byteIsDelete, Long values);

    Page<Userz> findByEmailContaining(String value, Pageable pageable);

    Page<Userz> findByUsernameContaining(String value, Pageable pageable);
}