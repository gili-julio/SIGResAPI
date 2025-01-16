package br.ufrn.imd.SIGResAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.imd.SIGResAPI.models.Desk;
import java.util.List;

@Repository
public interface DeskRepository extends JpaRepository<Desk, Long> {
    List<Desk> findByActive(boolean active);
}
