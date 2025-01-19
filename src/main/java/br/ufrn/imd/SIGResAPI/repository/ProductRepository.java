package br.ufrn.imd.SIGResAPI.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.imd.SIGResAPI.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll(Sort sort);
}
