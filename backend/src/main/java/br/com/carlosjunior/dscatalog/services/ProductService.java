package br.com.carlosjunior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.carlosjunior.dscatalog.dto.CategoryDTO;
import br.com.carlosjunior.dscatalog.dto.ProductDTO;
import br.com.carlosjunior.dscatalog.entities.Category;
import br.com.carlosjunior.dscatalog.entities.Product;
import br.com.carlosjunior.dscatalog.repositories.CategoryRepository;
import br.com.carlosjunior.dscatalog.repositories.ProductRepository;
import br.com.carlosjunior.dscatalog.services.exceptions.DatabaseException;
import br.com.carlosjunior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllpaged(Pageable pageable) {
		
		Page<Product> list = productRepository.findAll(pageable);
		
		// Convertendo Product para ProductDTO.
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = productRepository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new ProductDTO(entity, entity.getCategories());

	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = productRepository.save(entity);
		return new ProductDTO(entity);
	}


	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = productRepository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = productRepository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found: " + id);
		}
	}

	public void delete(Long id) {
		try {
			productRepository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found: "+id);
		} catch ( DataIntegrityViolationException e  ) {
			throw new DatabaseException("Integrity violation.");
		}
	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(dto.getDate());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for( CategoryDTO catDTO : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDTO.getId());
			entity.getCategories().add(category);
		}
		
	}

	
}
