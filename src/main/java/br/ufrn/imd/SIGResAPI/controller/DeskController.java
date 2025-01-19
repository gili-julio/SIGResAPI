package br.ufrn.imd.SIGResAPI.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.ufrn.imd.SIGResAPI.models.Desk;
import br.ufrn.imd.SIGResAPI.models.Order;
import br.ufrn.imd.SIGResAPI.repository.DeskRepository;
import br.ufrn.imd.SIGResAPI.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/desk")
@RequiredArgsConstructor
public class DeskController {

    @Autowired
    DeskRepository deskRepository;

    @Autowired
    OrderRepository orderRepository;

    @GetMapping
    public List<Desk> allActiveDesks() {
        return deskRepository.findByActive(true)
                .stream()
                .sorted((d1, d2) -> d1.getId().compareTo(d2.getId()))
                .toList();
    }

    @PostMapping("/create-desks")
    public ResponseEntity<Void> createDesks() {
        List<Desk> desksToCreate = new ArrayList<>();
        for (Long i = 1L; i <= 1000L; i++) {
            if (!deskRepository.existsById(i)) {
                desksToCreate.add(new Desk(i, false, false, null));
            }
        }
        deskRepository.saveAll(desksToCreate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/active/{amount}")
    public ResponseEntity<Void> activeDesks(@PathVariable int amount) {
        if (amount > deskRepository.count()) {
            return ResponseEntity.badRequest().build();
        }
        List<Desk> desks = deskRepository.findAll();
        for (Desk desk : desks) {
            if (desk.getId() <= amount) {
                desk.setActive(true);
            } else {
                desk.setActive(false);
            }
        }
        deskRepository.saveAll(desks);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Desk> getDesk(@PathVariable Long id) {
        Desk desk = deskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Desk not found"));
        if (!desk.isActive()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(desk);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Desk> deskFill(@PathVariable Long id) {
        Desk desk = deskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Desk not found"));
        if (!desk.isActive()) {
            return ResponseEntity.badRequest().build();
        }

        if (desk.isFill()) {
            List<Order> ordersInDesk = orderRepository.findByDesk(desk);
            for (Order order : ordersInDesk) {
                order.setActive(false);
            }
            orderRepository.saveAll(ordersInDesk);
            desk.setFill(false);
        } else {
            desk.setFill(true);
        }
        deskRepository.save(desk);
        return ResponseEntity.ok(desk);
    }
}
