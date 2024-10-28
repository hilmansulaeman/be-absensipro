package com.juaracoding.repo;

import com.juaracoding.model.Karyawan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KaryawanRepository extends JpaRepository<Karyawan, Long> {
    boolean existsByEmail(String email);
}
