package com.cibertec.app.service;

import java.util.List;

import com.cibertec.app.entity.Rol;


public interface RolService {

	public List<Rol> listarTodosRol();
	
	public Rol buscarById(Integer id);


}
