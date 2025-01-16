package br.ufrn.imd.SIGResAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.ufrn.imd.SIGResAPI.models.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
}
