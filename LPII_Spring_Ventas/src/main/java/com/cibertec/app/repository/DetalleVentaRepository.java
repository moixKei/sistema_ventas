package com.cibertec.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cibertec.app.entity.DetalleVenta;
import com.cibertec.app.entity.DetalleVentaId;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, DetalleVentaId>{

	@Query(value="SELECT p.nroventa, p.idproducto, p.cantidad_vta, p.precio_vta, p.importe_vta, "
			+ "p.precio_compra FROM detalle_ventas p where p.nroventa = :nroVenta",nativeQuery = true)
	public List<DetalleVenta> buscarByNroVenta(Long nroVenta);
}