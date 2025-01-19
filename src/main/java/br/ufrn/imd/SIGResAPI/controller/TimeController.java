package br.ufrn.imd.SIGResAPI.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

import java.time.ZoneId;

@Controller
@RequiredArgsConstructor
public class TimeController {

    public static Date getLocalDateTime() {
        // Obtém a data e hora local atual
        LocalDateTime localDateTime = LocalDateTime.now();

        // Converte LocalDateTime para Date usando o fuso horário do sistema
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String getLocalDateTimeFormatted() {
        // Formata a data e hora para exibição em String no formato desejado
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM - HH:mm");
        return localDateTime.format(formatter);
    }
}
