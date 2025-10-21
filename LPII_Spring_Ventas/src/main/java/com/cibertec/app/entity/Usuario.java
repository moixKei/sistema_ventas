package com.cibertec.app.entity;

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
@Table(name = "usuario")
public class Usuario {
	
	@Id
	@Column(name = "idusuario")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nombres")
	private String nombres;
	
	@Column(name = "apellidos")
	private String apellidos;
	
	@Column(name = "usuario")
	private String username;
	
	@Column(name = "clave")
	private String clave;
	
	@ManyToOne
	@JoinColumn(name= "idrol")
	private Rol rol;
	
	public Usuario(String nombres, String apellidos,String username ,String clave, Rol rol) {
		this.nombres=nombres;
		this.apellidos=apellidos;
		this.username=username;
		this.clave=clave;
		this.rol=rol;	
	}
}