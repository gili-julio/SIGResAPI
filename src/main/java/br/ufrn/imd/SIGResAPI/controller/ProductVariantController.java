package br.ufrn.imd.SIGResAPI.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.ufrn.imd.SIGResAPI.dto.ProductVariantRequestDTO;
import br.ufrn.imd.SIGResAPI.models.Product;
import br.ufrn.imd.SIGResAPI.models.ProductVariant;
import br.ufrn.imd.SIGResAPI.repository.ProductRepository;
import br.ufrn.imd.SIGResAPI.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/product-variant")
@RequiredArgsConstructor
public class ProductVariantController {

    @Autowired
    ProductVariantRepository productVariantRepository;

    @Autowired
    ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<ProductVariant>> allProductsVariants() {
        return ResponseEntity.ok(productVariantRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariant> productVariant(@PathVariable Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Variant not found"));
        return ResponseEntity.ok(productVariant);
    }

    @GetMapping("/by-product/{id}")
    public ResponseEntity<List<ProductVariant>> allProductVariants(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(productVariantRepository.findByProduct(product));
    }

    @PostMapping
    public ResponseEntity<ProductVariant> createProductVariant(@RequestBody ProductVariantRequestDTO body) {
        Product product = productRepository.findById(body.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        ProductVariant productVariant = new ProductVariant(null, body.name(), body.price(), 0, body.inHappyHour(),
                body.priceInHappyHour(), product, null, null);
        productVariantRepository.save(productVariant);
        return ResponseEntity.ok(productVariant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductVariant> updateProductVariant(@PathVariable Long id,
            @RequestBody ProductVariantRequestDTO body) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productVariant.setName(body.name());
        productVariant.setPrice(body.price());
        productVariant.setInHappyHour(body.inHappyHour());
        productVariant.setPriceInHappyHour(body.priceInHappyHour());
        productVariantRepository.save(productVariant);
        return ResponseEntity.ok(productVariant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductVariant(@PathVariable Long id) {
        if (!productVariantRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productVariantRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
