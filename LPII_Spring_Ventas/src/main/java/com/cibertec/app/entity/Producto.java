package com.cibertec.app.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "producto")
public class Producto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "idproducto")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idProd;
	
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "descripcion")
	private String descripcion;
		
	@Column(name = "precio_compra")
	private BigDecimal precioCompra;
	
	@Column(name = "precio_venta")
	private BigDecimal precioVenta;
	
	@Column(name = "stock")
	private int stock;

	@ManyToOne
	@JoinColumn(name= "idcate")
	private Categoria categoria;
	
	public Producto(String codigo) {
		this.codigo = codigo;
	}
	
	public Producto(Integer idProd, String codigo, String descripcion, 
			BigDecimal precioCompra, BigDecimal precioVenta, int stock) {
		this.idProd = idProd;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.precioCompra = precioCompra;
		this.precioVenta = precioVenta;
		this.stock = stock;
	}
	    
	public void restarExistencia(int stock) {
	    this.stock -= stock;
	}
	    
	public boolean sinExistencia() {
	    return this.stock <= 0;
	}
}