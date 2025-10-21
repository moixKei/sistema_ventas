package com.cibertec.app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.app.entity.DetalleVenta;
import com.cibertec.app.entity.DetalleVentaId;
import com.cibertec.app.entity.Producto;
import com.cibertec.app.entity.ProductoParaVender;
import com.cibertec.app.entity.Venta;
import com.cibertec.app.service.AsesorVentaService;
import com.cibertec.app.service.ClienteService;
import com.cibertec.app.service.DetalleVentaService;
import com.cibertec.app.service.ProductoService;
import com.cibertec.app.service.VentaService;

@Controller
public class VentaController {

	@Autowired
	private ProductoService productoService;
	@Autowired
	private VentaService ventaService;	
	@Autowired
	private DetalleVentaService detalleVentaService;	
	@Autowired
	private ClienteService clienteService;	
	@Autowired
	private AsesorVentaService asesorVentaService;
	
    @GetMapping("/venta")
    public String listVentas(Model model) {
        model.addAttribute("ventas", ventaService.listarTodosVentas());
        model.addAttribute("clienteList", clienteService.listarTodosCliente());
        model.addAttribute("asesorVentaList", asesorVentaService.listarTodosAsesorVenta());        
        return "venta/index";
    }
    
    private ArrayList<ProductoParaVender> obtenerCarrito(HttpServletRequest request) {
        @SuppressWarnings("unchecked") //request obtiene info que envía la página al servidor, aquí capturamos el carrito
		ArrayList<ProductoParaVender> carrito = 
		(ArrayList<ProductoParaVender>) request.getSession().getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        return carrito;
    }

    private void guardarCarrito(ArrayList<ProductoParaVender> carrito, HttpServletRequest request) {
        request.getSession().setAttribute("carrito", carrito);       
    }
    
    @PostMapping(value = "/venta/quitar/{indice}")
    public String quitarDelCarrito(@PathVariable int indice, HttpServletRequest request) {
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
        if (carrito != null && carrito.size() > 0 && carrito.get(indice) != null) {
            carrito.remove(indice);
            this.guardarCarrito(carrito, request);
        }
        return "redirect:/venta/new";
    }
    
    private void limpiarCarrito(HttpServletRequest request) {
    	this.guardarCarrito(new ArrayList<>(), request);
    }
    
    @GetMapping(value = "/venta/new")
    public String interfazVender(Model model, HttpServletRequest request) {
    	Venta venta = new Venta();
    	model.addAttribute("venta", venta); //enviamos objeto a la pàgina create
    	model.addAttribute("clienteList", clienteService.listarTodosCliente()); //enviamos datos de lciente
    	model.addAttribute("asesorVentaList", asesorVentaService.listarTodosAsesorVenta()); //misma wea de arriba pero con asesor
    	model.addAttribute("producto", new Producto()); //enviamos un objeto producto con atributos vacìos
    	float total = 0;
    	ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request); //se crea un List con el carrito vacìo
    	for(ProductoParaVender p: carrito) total += p.getTotal(); //por cada producto agregado se suma el total
    	model.addAttribute("total", total);//enviamos el total a la pàgina create
    	return "venta/create";
    }
    
    @GetMapping(value = "venta/agregar")
    public String agregarAlCarrito(@ModelAttribute Producto producto, HttpServletRequest request, RedirectAttributes redirectAttrs) {
    	ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
    	Producto productoBuscadoPorCodigo = productoService.buscarProductoByCodigo(producto.getCodigo());
    	if(productoBuscadoPorCodigo == null) {
    		redirectAttrs
    					.addFlashAttribute("mensaje", "El producto con el còdigo " + producto.getCodigo() + "no existe")
    					.addFlashAttribute("clase", "warning");
    		return "redirect:/venta/new";
    	}
    	if(productoBuscadoPorCodigo.sinExistencia()) {
    		redirectAttrs
			    		.addFlashAttribute("mensaje", "El producto està agotado")
						.addFlashAttribute("clase", "warning");
    		return "redirect:/venta/new";
    	}
    	boolean encontrado = false;
    	for (ProductoParaVender productoParaVenderActual : carrito) {
    		if(productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.getCodigo())) {
    			productoParaVenderActual.aumentarCantidad();
    			encontrado = true;
    			break;
    		}
    	}
    	if(!encontrado) {
    		carrito.add(new ProductoParaVender(
    				productoBuscadoPorCodigo.getIdProd(), 
    				productoBuscadoPorCodigo.getCodigo(), 
    				productoBuscadoPorCodigo.getDescripcion(), 
    				productoBuscadoPorCodigo.getPrecioCompra(), 
    				productoBuscadoPorCodigo.getPrecioVenta(),
    				productoBuscadoPorCodigo.getStock(), 1 //cantidad insertada arbitrariamente
    				));
    	}//entonces ingresamos el nuevo producto a carrito
    	this.guardarCarrito(carrito, request);
    	return "redirect:/venta/new";
    }
    
    @PostMapping(value = "/venta/terminar")
    public String terminarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs, @ModelAttribute("venta") Venta miVenta) {
    	ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
    	if(carrito == null || carrito.size() <= 0) {
    		return "redirect:/venta/new";
    	}
    	miVenta.setFecha(new Date());
    	Venta v = ventaService.guardarVenta(miVenta);
    	//vraiables locales
    	BigDecimal subtotal = new BigDecimal(0);
    	BigDecimal importeVenta = new BigDecimal(0);
    	BigDecimal importeCompra = new BigDecimal(0);
    	BigDecimal ganancia = new BigDecimal(0);
    	//recorrer el carrito
    	for (ProductoParaVender productoParaVender : carrito) {
    		Producto p = productoService.buscarProductoById(productoParaVender.getIdProd());
    		if(p == null) continue;
    		p.restarExistencia(productoParaVender.getCantidad());
    		productoService.guardarProducto(p);
    		DetalleVentaId objDetalleVentaId = new DetalleVentaId();
    		objDetalleVentaId.setVenta(v);
    		objDetalleVentaId.setProducto(p);
    		//calculamos los importes
    		importeVenta = new BigDecimal(productoParaVender.getCantidad()).multiply(productoParaVender.getPrecioVenta());
    		importeCompra = new BigDecimal(productoParaVender.getCantidad()).multiply(productoParaVender.getPrecioCompra());
    		//creaciòn de nuevo detalle de venta
    		DetalleVenta productoVendido = new DetalleVenta(objDetalleVentaId, productoParaVender.getCantidad(),
    				productoParaVender.getPrecioVenta(), importeVenta, productoParaVender.getPrecioCompra());
    		//y lo guardamos
    		detalleVentaService.guardarDetalleVenta(productoVendido);
    		//subtotal
    		subtotal = subtotal.add(importeVenta);
    		ganancia = ganancia.add(importeVenta).subtract(importeCompra);
    	}
    	
    	//actualizar los datos de la venta nueca y guardarla finalmente
    	miVenta.setId(v.getId());
    	miVenta.setSubtotal(subtotal);
    	miVenta.setIgv(miVenta.getSubtotal().multiply(new BigDecimal(0.18)));
    	miVenta.setTotal(miVenta.getSubtotal().add(miVenta.getIgv()));
    	miVenta.setGanancia(ganancia);
    	ventaService.guardarVenta(miVenta);
    	//al final limpiarmos el carrito
    	this.limpiarCarrito(request);
    	//e indicamos una venta exitosa
    	redirectAttrs
    				.addFlashAttribute("mensaje", "Venta realizada exitosamente")
    				.addFlashAttribute("calse", "success");
    	return "redirect:/venta/new";
    }
    
    @GetMapping(value="/venta/limpiar")
    public String cancelarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs) {
    	this.limpiarCarrito(request);
    	redirectAttrs
    				.addFlashAttribute("mensaje", "Venta cancelada")
    				.addFlashAttribute("clase", "info");
    	return "redirect:/venta/new";
    }
    
    @GetMapping("/venta/verDetalle/{id}")
    public String verDetaleForm(@PathVariable Long id, Model model) {
    	List<DetalleVenta> dv = detalleVentaService.buscarDetalleVentaByNroVenta(id);
    	model.addAttribute("listDetalleVenta", dv);
    	return "venta/edit";
    }

}
