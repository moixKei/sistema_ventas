package com.cibertec.app.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "ventas")
public class Venta implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "nroventa")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name= "idclie")
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name= "codasesor")
	private AsesorVenta asesorVenta;
	
	@Column(name = "subtotal")
	private BigDecimal subtotal;
	
	@Column(name = "igv")
	private BigDecimal igv;	
	
	@Column(name = "total")
	private BigDecimal total;
	
	@Column(name = "ganancia")
	private BigDecimal ganancia;
}