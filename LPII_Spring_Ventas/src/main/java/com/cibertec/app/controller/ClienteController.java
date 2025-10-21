package com.cibertec.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cibertec.app.entity.Cliente;
import com.cibertec.app.service.ClienteService;

@Controller
public class ClienteController {

	@Autowired
	private ClienteService service;

	@GetMapping("/cliente")
	public String listClientes(Model model) {
		model.addAttribute("clientes", service.listarTodosCliente());
	    return "cliente/index";
	}	  
	    
	@GetMapping("/cliente/new")
	public String createCliente(Model model){	        
	  	Cliente cliente = new Cliente();	       
	    model.addAttribute("cliente", cliente);	       
	    return "cliente/create";	     
	}	
	
	@PostMapping("/cliente")
	public String saveCliente(Cliente cliente) {
	    service.guardarCliente(cliente);
	    return "redirect:/cliente";
	}
	
	@GetMapping("/cliente/edit/{id}")
	public String editCliente(@PathVariable Integer id, Model model) {
	    Cliente st = service.buscarClienteById(id);	       
	    model.addAttribute("cliente", st);	        
	    return "cliente/edit";
	}	
	
	@PostMapping("/cliente/{id}")
	public String updateCliente(@PathVariable Integer id, Cliente cliente, Model model) {
        Cliente existentCliente = service.buscarClienteById(id);
        existentCliente.setIdClie(id);
        existentCliente.setRazonSoc(cliente.getRazonSoc());
        existentCliente.setNombreCiudad(cliente.getNombreCiudad());
        existentCliente.setDireccionClie(cliente.getDireccionClie());
        existentCliente.setTelefono(cliente.getTelefono());
        service.actualizarCliente(existentCliente);	        
        return "redirect:/cliente";
    }
	    
	@GetMapping("/cliente/{id}")
    public String deleteCliente(@PathVariable Integer id) {
        service.eliminarClienteById(id);
        return "redirect:/cliente";
    }	
}