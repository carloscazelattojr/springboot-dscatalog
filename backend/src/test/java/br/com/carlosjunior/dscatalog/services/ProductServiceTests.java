package br.com.carlosjunior.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.carlosjunior.dscatalog.dto.ProductDTO;
import br.com.carlosjunior.dscatalog.entities.Product;
import br.com.carlosjunior.dscatalog.repositories.ProductRepository;
import br.com.carlosjunior.dscatalog.services.exceptions.DatabaseException;
import br.com.carlosjunior.dscatalog.services.exceptions.ResourceNotFoundException;
import br.com.carlosjunior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product product;

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		
		page = new PageImpl<>(List.of(product));
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}

	@Test
	public void deleteShouldDoNothinWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenDoesNotIdExists() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}	
	
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDoesNotIdExists() {

		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});

		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}	
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllpaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}
	
	
}
