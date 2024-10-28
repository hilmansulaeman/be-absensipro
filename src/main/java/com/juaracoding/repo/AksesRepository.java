package com.juaracoding.repo;

import com.juaracoding.model.Akses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AksesRepository extends JpaRepository<Akses, Long> {
    Optional<Akses> findByNamaAkses(String namaAkses);
}
