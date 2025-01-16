package br.ufrn.imd.SIGResAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ufrn.imd.SIGResAPI.models.SystemConfig;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
}
