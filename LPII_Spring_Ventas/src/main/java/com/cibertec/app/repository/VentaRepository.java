package com.cibertec.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.app.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>{

}