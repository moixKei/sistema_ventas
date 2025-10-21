package com.cibertec.app.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "detalle_ventas")
public class DetalleVenta implements Serializable{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
    private DetalleVentaId id;
	
	@Column(name = "cantidad_vta")
	private int cantidad_vta;
	
	@Column(name = "precio_vta")
	private BigDecimal precio_vta;
	
	@Column(name = "importe_vta")
	private BigDecimal importe_vta;
		
	@Column(name = "precio_compra")
	private BigDecimal precioCompra;
}