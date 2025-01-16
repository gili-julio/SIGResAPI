package br.ufrn.imd.SIGResAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.imd.SIGResAPI.models.Desk;
import br.ufrn.imd.SIGResAPI.models.Order;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByDesk(Desk desk);
}
