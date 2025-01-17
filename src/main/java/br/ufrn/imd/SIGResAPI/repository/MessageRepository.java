package br.ufrn.imd.SIGResAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufrn.imd.SIGResAPI.models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
