package com.cibertec.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "asesor_vta")
public class AsesorVenta implements Serializable{

	private static final long serialVersionUID = 1L;	

	@Id
	@Column(name = "codasesor")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "nombres")
	private String nombres;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "telefono")
	private String telefono;
}