package com.cibertec.app.service;

import java.util.List;

import com.cibertec.app.entity.Venta;

public interface VentaService {

	public Venta guardarVenta(Venta userEntity);

	public List<Venta> listarTodosVentas();

}