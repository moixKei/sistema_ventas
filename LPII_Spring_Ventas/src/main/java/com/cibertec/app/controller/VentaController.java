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

  this.guardarCarrito(new ArrayList<>(), request );

  }

   

  @GetMapping (value = "/venta/new")

  public String interfazVender(Model model , HttpServletRequest request) {

  Venta venta = new Venta();

  model.addAttribute("venta", venta);

  model.addAttribute("clienteList" , clienteService.listarTodosCliente());

  model.addAttribute("asesorVentaList", asesorVentaService.listarTodosAsesorVenta());

  model.addAttribute("producto",new Producto());

  float total = 0;

  ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);

  for (ProductoParaVender p: carrito) total+= p.getTotal();

  model.addAttribute("total", total);

  return "venta/create";

  }

   

  @PostMapping(value = "/venta/agregar")

  public String agregarAlCarrito(@ModelAttribute Producto producto, HttpServletRequest request,

                  RedirectAttributes redirectAttrs) {

    ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request); // creo un List y lo empiezo a llenar

    // Busco un producto según código de producto ingresado como parámetro desde la pag. y lo guardo en un objeto producto

    Producto productoBuscadoPorCodigo = productoService.buscarProductoByCodigo(producto.getCodigo());

    if (productoBuscadoPorCodigo == null) {

      redirectAttrs

          .addFlashAttribute("mensaje", "El producto con el código " + producto.getCodigo() + " no existe")

          .addFlashAttribute("clase", "warning");

      return "redirect:/venta/new"; // si no existe el producto redirecciono un atributo mensaje a la página

    }

    if (productoBuscadoPorCodigo.sinExistencia()) {

      redirectAttrs

          .addFlashAttribute("mensaje", "El producto está agotado")

          .addFlashAttribute("clase", "warning");

      return "redirect:/venta/new";

    }

    // si el producto tiene stock o menor, redirecciono un atributo mensaje a la página

    boolean encontrado = false;

    for (ProductoParaVender productoParaVenderActual : carrito) { // obtengo cada objeto guardado en carrito

      if (productoParaVenderActual.getCodigo().equals(productoBuscadoPorCodigo.getCodigo())) {

        productoParaVenderActual.aumentarCantidad();

        encontrado = true;

        break;

     }

    }

    // buscamos si ya habíamos ingresado ese producto a carrito, si es así aumentamos una cantidad más sin ingresarlo

    if (!encontrado) { // si encontrado es falso (no encontró el producto en carrito)

      carrito.add(new ProductoParaVender(

      productoBuscadoPorCodigo.getIdProd(),

      productoBuscadoPorCodigo.getCodigo(),

      productoBuscadoPorCodigo.getDescripcion(),

      productoBuscadoPorCodigo.getPrecioCompra(),

      productoBuscadoPorCodigo.getPrecioVenta(),

      productoBuscadoPorCodigo.getStock(), 1

      )); }

    this.guardarCarrito(carrito, request);

    return "redirect:/venta/new";

  }

   

  @PostMapping(value = "/venta/terminar")

 public String terminarVenta(HttpServletRequest request,RedirectAttributes redirectAttrs,@ModelAttribute("venta") Venta miVenta) {

    ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);

    if (carrito == null || carrito.size() <= 0) {

    return "redirect:/venta/new";

    }

  miVenta.setFecha(new Date());

  Venta v = ventaService.guardarVenta(miVenta);

  BigDecimal subtotal = new BigDecimal(0);

  BigDecimal importeVenta = new BigDecimal(0);

  BigDecimal importeCompra = new BigDecimal(0);

  BigDecimal ganancia = new BigDecimal(0);



  for (ProductoParaVender productoParaVender : carrito) {

  Producto p = productoService.buscarProductoById(productoParaVender.getIdProd());

  if (p == null ) continue;

   

  p.restarExistencia(productoParaVender.getCantidad());

  productoService.guardarProducto(p);

  DetalleVentaId objDetalleVentaId = new DetalleVentaId();

  objDetalleVentaId.setVenta(v);

  objDetalleVentaId.setProducto(p);

   

 importeVenta = new BigDecimal(productoParaVender.getCantidad()).multiply(productoParaVender.getPrecioVenta());

 importeVenta = new BigDecimal(productoParaVender.getCantidad()).multiply(productoParaVender.getPrecioCompra());

DetalleVenta productoVendido = new DetalleVenta(objDetalleVentaId, productoParaVender.getCantidad(),

productoParaVender.getPrecioVenta(), importeVenta,productoParaVender.getPrecioCompra());

   

detalleVentaService.guardarDetalleVenta(productoVendido);



subtotal = subtotal.add(importeVenta);

ganancia = ganancia.add(importeVenta.subtract(importeCompra));

  }

   

   

  miVenta.setId(v.getId());

  miVenta.setSubtotal(miVenta.getSubtotal().multiply(new BigDecimal(0.18)));

  miVenta.setIgv(miVenta.getSubtotal().add(miVenta.getIgv()));

  miVenta.setTotal(miVenta.getSubtotal().add(miVenta.getIgv()));

  miVenta.setGanancia(ganancia);

  ventaService.guardarVenta(miVenta);

   

  this.limpiarCarrito(request);

   

  redirectAttrs

  .addFlashAttribute("mensaje","Venta realizada correctamente")

  .addFlashAttribute("clase","succes");



  return "redirect:/venta/new";

   

  } 

   

  @GetMapping(value = "/venta/limpiar")

  public String cancelarVenta(HttpServletRequest request, RedirectAttributes redirectAttrs) {

  this.limpiarCarrito(request);

  redirectAttrs

  .addFlashAttribute("mensaje","venta cancelada")

  .addFlashAttribute("clase", "info");

  return "redirect:/venta/new";

  

  } 

   

  @GetMapping("/venta/verDetalle/{id}")

  public String verDetalleForm(@PathVariable Long id, Model model) {

  List<DetalleVenta> dv = detalleVentaService.buscarDetalleVentaByNroVenta(id);

  model.addAttribute("listDetalleVenta",dv);

  return "venta/edit";

  

  

  }

   

   

   

}



   



  

   

   