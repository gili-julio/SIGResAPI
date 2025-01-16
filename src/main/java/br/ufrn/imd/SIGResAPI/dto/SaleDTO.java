package br.ufrn.imd.SIGResAPI.dto;

public record SaleDTO(Boolean isVariant, Long productId, int amount, Long userId) {
}
