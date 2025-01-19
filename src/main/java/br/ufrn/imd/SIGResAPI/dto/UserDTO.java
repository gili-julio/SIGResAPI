package br.ufrn.imd.SIGResAPI.dto;

import java.util.Set;

import br.ufrn.imd.SIGResAPI.models.Role;

public record UserDTO(String username, String password, Set<Role> roles, Long userId) {
}
