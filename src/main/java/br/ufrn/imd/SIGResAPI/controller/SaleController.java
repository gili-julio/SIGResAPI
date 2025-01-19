package br.ufrn.imd.SIGResAPI.controller;

import br.ufrn.imd.SIGResAPI.dto.SaleDTO;
import br.ufrn.imd.SIGResAPI.models.Product;
import br.ufrn.imd.SIGResAPI.models.ProductVariant;
import br.ufrn.imd.SIGResAPI.models.Sale;
import br.ufrn.imd.SIGResAPI.models.User;
import br.ufrn.imd.SIGResAPI.repository.ProductRepository;
import br.ufrn.imd.SIGResAPI.repository.ProductVariantRepository;
import br.ufrn.imd.SIGResAPI.repository.SaleRepository;
import br.ufrn.imd.SIGResAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SaleController {

    private final SaleRepository saleRepository;

    private final ProductRepository productRepository;

    private final ProductVariantRepository productVariantRepository;

    private final UserRepository userRepository;

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public List<Sale> getSales(Date initDate, Date finalDate) {
        List<Sale> sales = getAllSales();
        sales.removeIf(sale -> (sale.getTime().before(initDate)) || (sale.getTime().after(finalDate)));
        return sales;
    }

    public Sale getSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        return sale;
    }

    public Sale doSale(SaleDTO saleDTO) {
        User user = userRepository.findById(saleDTO.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Sale sale = new Sale();
        sale.setAmount(saleDTO.amount());
        sale.setMadeBy(user);
        if (saleDTO.isVariant()) {
            ProductVariant productVariant = productVariantRepository.findById(saleDTO.productId())
                    .orElseThrow(() -> new RuntimeException("Product Variant not found"));
            if (productVariant.getAmount() < saleDTO.amount()) {
                new RuntimeException("Product Variant amount less than the requested");
            }
            sale.setProductVariant(productVariant);
            productVariant.setAmount(productVariant.getAmount() - saleDTO.amount());
            productVariantRepository.save(productVariant);
        } else {
            Product product = productRepository.findById(saleDTO.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (product.getAmount() < saleDTO.amount()) {
                new RuntimeException("Product amount less than the requested");
            }
            sale.setProduct(product);
            product.setAmount(product.getAmount() - saleDTO.amount());
            productRepository.save(product);
        }
        sale.setTime(TimeController.getLocalDateTime());
        sale.setVariant(saleDTO.isVariant());
        saleRepository.save(sale);
        return sale;
    }
}
