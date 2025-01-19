package br.ufrn.imd.SIGResAPI.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.ufrn.imd.SIGResAPI.dto.ProductRequestDTO;
import br.ufrn.imd.SIGResAPI.models.Product;
import br.ufrn.imd.SIGResAPI.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> allProducts() {
        return ResponseEntity.ok(productRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> product(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDTO body) {
        Product product = new Product();
        product.setName(body.name());
        product.setPrice(body.price());
        product.setAmount(0);
        product.setInHappyHour(body.inHappyHour());
        product.setPriceInHappyHour(body.priceInHappyHour());
        productRepository.save(product);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/{id}/add")
    public ResponseEntity<Void> incrementProductAmount(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setAmount(product.getAmount() + 1);
        productRepository.save(product);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/remove")
    public ResponseEntity<Void> decrementProductAmount(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getAmount() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        product.setAmount(product.getAmount() - 1);
        productRepository.save(product);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO body) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(body.name());
        product.setPrice(body.price());
        product.setInHappyHour(body.inHappyHour());
        product.setPriceInHappyHour(body.priceInHappyHour());
        productRepository.save(product);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
