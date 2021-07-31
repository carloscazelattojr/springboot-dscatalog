package br.com.carlosjunior.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.carlosjunior.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
