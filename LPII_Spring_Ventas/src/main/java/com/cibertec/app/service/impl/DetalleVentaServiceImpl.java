package com.cibertec.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.app.entity.DetalleVenta;
import com.cibertec.app.repository.DetalleVentaRepository;
import com.cibertec.app.service.DetalleVentaService;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService{

	@Autowired
	DetalleVentaRepository detalleVentaRepository;

	@Override
	public DetalleVenta guardarDetalleVenta(DetalleVenta userEntity) {
		return detalleVentaRepository.save(userEntity);
	}

	@Override
	public List<DetalleVenta> listarTodosDetalleVenta() {
		return detalleVentaRepository.findAll();
	}

	@Override
	public List<DetalleVenta> buscarDetalleVentaByNroVenta(Long nroVenta) {
		return detalleVentaRepository.buscarByNroVenta(nroVenta);
	}		
}