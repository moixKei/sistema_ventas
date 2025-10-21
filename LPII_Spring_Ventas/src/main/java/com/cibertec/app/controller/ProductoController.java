package com.cibertec.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cibertec.app.entity.Producto;
import com.cibertec.app.service.CategoriaService;
import com.cibertec.app.service.ProductoService;

@Controller
public class ProductoController {

	@Autowired
	private ProductoService productoService;

	@Autowired
	private CategoriaService categoriaService;	

	@GetMapping("/producto")
    	public String listProductos(Model model) {
        	model.addAttribute("productos", productoService.listarTodosProductos());
        	model.addAttribute("categoriaList", categoriaService.listarTodosCategoria());	        
        	return "producto/index";
    	}	  
    
    	@GetMapping("/producto/new")
    	public String createProducto(Model model){	        
    		Producto producto = new Producto();	       
       		model.addAttribute("producto", producto);	        
        	model.addAttribute("categoriaList", categoriaService.listarTodosCategoria());	        
        	return "producto/create";	     
    	}	
	
    	@PostMapping("/producto")
    	public String saveProducto(Producto producto) {	 
    		productoService.guardarProducto(producto);		 
        	return "redirect:/producto";
    	}
	
    	@GetMapping("/producto/edit/{id}")
    	public String editProducto(@PathVariable Integer id, Model model) {
       		Producto st = productoService.buscarProductoById(id);	       
	        model.addAttribute("producto", st);	 
	        model.addAttribute("categoriaList", categoriaService.listarTodosCategoria());	        
        	return "producto/edit";
    	}	
	
	@PostMapping("/producto/{id}")
	public String updateProducto(@PathVariable Integer id, Producto producto, Model model) {
		Producto existentProducto = productoService.buscarProductoById(id);
	        existentProducto.setIdProd(id);
		existentProducto.setDescripcion(producto.getDescripcion());
		existentProducto.setPrecioVenta(producto.getPrecioVenta());
		existentProducto.setPrecioCompra(producto.getPrecioCompra());
		existentProducto.setStock(producto.getStock());
		existentProducto.setCategoria(producto.getCategoria());
        	productoService.actualizarProducto(existentProducto);	        
	    	return "redirect:/producto";
	}	    
	
    	@GetMapping("/producto/{id}")
	public String deleteProducto(@PathVariable Integer id) {
	    	productoService.eliminarProductoById(id);
	    	return "redirect:/producto";
    }	
}