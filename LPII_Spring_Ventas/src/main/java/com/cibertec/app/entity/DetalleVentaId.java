package com.cibertec.app.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class DetalleVentaId implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name= "nroventa")
	private Venta venta;
	
	@ManyToOne
	@JoinColumn(name= "idproducto")
	private Producto producto;
}