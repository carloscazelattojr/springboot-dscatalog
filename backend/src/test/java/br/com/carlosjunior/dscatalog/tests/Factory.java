package br.com.carlosjunior.dscatalog.tests;

import java.time.Instant;

import br.com.carlosjunior.dscatalog.dto.ProductDTO;
import br.com.carlosjunior.dscatalog.entities.Category;
import br.com.carlosjunior.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product p = new Product(1L, "Phone","REDMI 7.1", 800.0, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/16-big.jpg",Instant.parse("2021-08-20T03:00:00Z"));
		p.getCategories().add(new Category(2L,"Electornical"));
		return p;
	}
	
	public static ProductDTO createProductDTO() {
		Product p = createProduct();
		return new ProductDTO(p, p.getCategories());
	}

}
