package br.ufrn.imd.SIGResAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.imd.SIGResAPI.dto.MessageDTO;
import br.ufrn.imd.SIGResAPI.models.Message;
import br.ufrn.imd.SIGResAPI.models.User;
import br.ufrn.imd.SIGResAPI.repository.MessageRepository;
import br.ufrn.imd.SIGResAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody MessageDTO messageDTO) {
        User user = userRepository.findById(messageDTO.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Message message = new Message();
        message.setBody(messageDTO.body());
        message.setSentBy(user);
        message.setSentAt(TimeController.getLocalDateTimeFormatted());
        messageRepository.save(message);
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageRepository.findAll();
        return ResponseEntity.ok(messages);
    }
}
