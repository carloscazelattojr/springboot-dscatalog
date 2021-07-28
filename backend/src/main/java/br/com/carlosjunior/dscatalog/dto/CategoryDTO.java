package br.com.carlosjunior.dscatalog.dto;

import java.io.Serializable;

import br.com.carlosjunior.dscatalog.entities.Category;

public class CategoryDTO implements Serializable {

private static final long serialVersionUID = 1L;
	private Long id;
	private String name;

	public CategoryDTO() {

	}

	public CategoryDTO(Category categoryEntity) {
		this.id = categoryEntity.getId();
		this.name = categoryEntity.getName();
	}
	
	//Este construtor irá ser utilizado para conversao de um Category para CategoryDTO
	public CategoryDTO(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
