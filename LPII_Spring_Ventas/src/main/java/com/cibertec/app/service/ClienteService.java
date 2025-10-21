package com.cibertec.app.service;

import java.util.List;

import com.cibertec.app.entity.Cliente;

public interface ClienteService {

	public Cliente guardarCliente(Cliente userEntity);

	public List<Cliente> listarTodosCliente();

	public Cliente actualizarCliente(Cliente userEntity);

	public void eliminarClienteById(Integer id);
	
	public Cliente buscarClienteById(Integer id);
}
