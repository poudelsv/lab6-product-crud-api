package com.elkabani.mydb.controllers;

import com.elkabani.mydb.dtos.ProductDto;
import com.elkabani.mydb.entities.Category;
import com.elkabani.mydb.entities.Product;
import com.elkabani.mydb.mappers.ProductMapper;
import com.elkabani.mydb.repositories.CategoryRepository;
import com.elkabani.mydb.repositories.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductController(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @GetMapping
    public List<ProductDto> getAllProducts(
            @RequestParam(name = "categoryId", required = false)
            Long categoryId) {

        List<Product> products = categoryId == null
                ? productRepository.findAll()
                : productRepository.findByCategoryId(categoryId);

        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable("id") Long id) {
        return productMapper.toDto(findProduct(id));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto) {

        Category category = findCategory(productDto.getCategoryId());

        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return ResponseEntity
                .created(URI.create("/products/" + savedProduct.getId()))
                .body(productMapper.toDto(savedProduct));
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(
            @PathVariable("id") Long id,
            @RequestBody ProductDto productDto) {

        Product product = findProduct(id);
        Category category = findCategory(productDto.getCategoryId());

        productMapper.update(productDto, product);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("id") Long id) {

        Product product = findProduct(id);
        productRepository.delete(product);

        return ResponseEntity.noContent().build();
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found"));
    }

    private Category findCategory(Long categoryId) {
        if (categoryId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "categoryId is required");
        }

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Category not found"));
    }
}