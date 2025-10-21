package com.cibertec.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.app.entity.AsesorVenta;

@Repository
public interface AsesorVentaRepository extends JpaRepository<AsesorVenta, Integer>{

}