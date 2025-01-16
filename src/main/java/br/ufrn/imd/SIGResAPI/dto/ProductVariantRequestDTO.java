package br.ufrn.imd.SIGResAPI.dto;

public record ProductVariantRequestDTO(String name, float price, boolean inHappyHour, float priceInHappyHour,
        Long productId) {
}
