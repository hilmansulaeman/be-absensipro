package com.juaracoding.repo;

import com.juaracoding.model.Absen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbsenRepository extends JpaRepository<Absen, Long> {
    List<Absen> findByUserzId(Long userId);
    List<Absen> findByIsDelete(Byte isDelete);

    List<Absen> findByAbsenOutAndUserzIdUser(Object o, Long userId);
}
