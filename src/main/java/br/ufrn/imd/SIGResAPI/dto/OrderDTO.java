package br.ufrn.imd.SIGResAPI.dto;

public record OrderDTO(Boolean isVariant, Long productId, int amount, Long deskId) {
}
