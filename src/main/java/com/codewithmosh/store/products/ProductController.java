package com.codewithmosh.store.products;
import com.codewithmosh.store.users.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserMapper userMapper;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam(required = false,defaultValue = "",name = "categoryId")  Byte categoryId) {
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        }else{
            products = productRepository.findAll();
        }
        return products.stream().map(productMapper ::toDto).toList();

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
         if (product == null) {
             return ResponseEntity.notFound().build();
         }
         return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto request,
            UriComponentsBuilder uriBuilder) {

          var category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category ID"));

          var product = productMapper.toEntity(request);

          product.setCategory(category);

          productRepository.save(product);

          var productDto = productMapper.toDto(product);

          var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();

          return  ResponseEntity.created(uri).body(productDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable(name = "id") Long id,
            @RequestBody ProductDto productDto){

        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
         productMapper.update(productDto, product);
         product.setCategory(category);
         productRepository.save(product);
         productDto.setId(product.getId());
         return ResponseEntity.ok(productDto);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
