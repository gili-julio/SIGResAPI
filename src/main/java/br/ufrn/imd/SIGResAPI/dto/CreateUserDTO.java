package br.ufrn.imd.SIGResAPI.dto;

import java.util.Set;

public record CreateUserDTO(String username, String password, Set<String> roles) {
}
