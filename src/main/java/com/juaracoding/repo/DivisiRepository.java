package com.juaracoding.repo;

import com.juaracoding.model.Divisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisiRepository extends JpaRepository<Divisi, Long> {
    boolean existsByNamaDivisi(String namaDivisi);
    boolean existsByKodeDivisi(String kodeDivisi);
}
