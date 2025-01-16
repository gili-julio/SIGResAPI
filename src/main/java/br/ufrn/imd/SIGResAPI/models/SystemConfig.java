package br.ufrn.imd.SIGResAPI.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "system_config")
@Getter
@Setter
public class SystemConfig {

    @Id
    private Long id = 1L; // ID fixo para garantir uma única instância

    private int numMesas;
    private String nomeEstabelecimento;
    private boolean batePapoAtivo;
}
