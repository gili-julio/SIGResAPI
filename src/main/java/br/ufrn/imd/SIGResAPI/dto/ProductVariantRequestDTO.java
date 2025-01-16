package br.ufrn.imd.SIGResAPI.dto;

import br.ufrn.imd.SIGResAPI.models.Product;

public record ProductVariantRequestDTO(String name, float price, Product product) {
}
