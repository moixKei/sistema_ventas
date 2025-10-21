package com.cibertec.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cibertec.app.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer>{

	@Query(value="SELECT p.idproducto, p.codigo, p.descripcion, p.precio_compra,"
			+ "p.precio_venta, p.stock, p.idcate FROM producto p where p.codigo = :codigo",nativeQuery = true)
	 public Producto buscarProductoByCodigo(String codigo);
}
