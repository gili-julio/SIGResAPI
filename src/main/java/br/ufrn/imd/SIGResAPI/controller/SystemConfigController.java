package br.ufrn.imd.SIGResAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.ufrn.imd.SIGResAPI.models.SystemConfig;
import br.ufrn.imd.SIGResAPI.service.SystemConfigService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService configService;

    @Autowired
    DeskController deskController;

    @GetMapping
    public ResponseEntity<SystemConfig> getConfig() {
        return ResponseEntity.ok(configService.getConfig());
    }

    @PutMapping
    public ResponseEntity<SystemConfig> updateConfig(@RequestBody SystemConfig newConfig) {
        if (newConfig.getNumMesas() < 0 || newConfig.getNumMesas() > 1000) {
            return ResponseEntity.status(405).build();
        }
        configService.updateConfig(newConfig);
        deskController.activeDesks(newConfig.getNumMesas());
        return ResponseEntity.ok(newConfig);
    }
}
