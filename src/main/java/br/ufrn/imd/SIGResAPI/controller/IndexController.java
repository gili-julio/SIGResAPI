package br.ufrn.imd.SIGResAPI.controller;

import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController("/")
@RequiredArgsConstructor
public class IndexController {

    @GetMapping("/hello-world")
    public String index() {
        return "Bem vindo a API do SIGRes";
    }

}
