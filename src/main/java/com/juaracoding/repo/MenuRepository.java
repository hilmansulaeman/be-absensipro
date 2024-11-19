package com.juaracoding.repo;

import com.juaracoding.model.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Page<Menu> findByIsDelete(Pageable page , byte byteIsDelete);

    List<Menu> findByIsDelete(byte byteIsDelete);
    Page<Menu> findByIsDeleteAndNamaMenuContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Menu> findByIsDeleteAndPathMenuContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Menu> findByIsDeleteAndEndPointContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Menu> findByIsDeleteAndIdMenu(Pageable page , byte byteIsDelete, Long values);

}
