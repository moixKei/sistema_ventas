package com.cibertec.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.app.entity.Venta;
import com.cibertec.app.repository.VentaRepository;
import com.cibertec.app.service.VentaService;

@Service
public class VentaServiceImpl implements VentaService{

	@Autowired
	VentaRepository ventaRepository;
    
	@Override
	public Venta guardarVenta(Venta userEntity) {
		return ventaRepository.save(userEntity);
	}

	@Override
	public List<Venta> listarTodosVentas() {
		return ventaRepository.findAll();
	}	
}