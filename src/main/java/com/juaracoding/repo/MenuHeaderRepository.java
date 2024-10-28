package com.juaracoding.repo;

import com.juaracoding.model.MenuHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuHeaderRepository extends JpaRepository<MenuHeader, Long> {
}
