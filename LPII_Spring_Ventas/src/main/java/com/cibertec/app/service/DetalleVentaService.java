package com.cibertec.app.service;

import java.util.List;

import com.cibertec.app.entity.DetalleVenta;

public interface DetalleVentaService {

	public DetalleVenta guardarDetalleVenta(DetalleVenta userEntity);

	public List<DetalleVenta> listarTodosDetalleVenta();
	
	public List<DetalleVenta> buscarDetalleVentaByNroVenta(Long nroVenta);

}