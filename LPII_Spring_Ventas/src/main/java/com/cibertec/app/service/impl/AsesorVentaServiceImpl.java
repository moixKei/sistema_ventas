package com.cibertec.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.app.entity.AsesorVenta;
import com.cibertec.app.repository.AsesorVentaRepository;
import com.cibertec.app.service.AsesorVentaService;

@Service
public class AsesorVentaServiceImpl implements AsesorVentaService{

	@Autowired
	AsesorVentaRepository asesorVentaRepository;
	
	@Override
	public List<AsesorVenta> listarTodosAsesorVenta() {
		return asesorVentaRepository.findAll();
	}
}