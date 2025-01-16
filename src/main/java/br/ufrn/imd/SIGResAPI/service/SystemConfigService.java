package br.ufrn.imd.SIGResAPI.service;

import org.springframework.stereotype.Service;
import br.ufrn.imd.SIGResAPI.models.SystemConfig;
import br.ufrn.imd.SIGResAPI.repository.SystemConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemConfigService {

    private final SystemConfigRepository configRepository;
    private SystemConfig config;

    @PostConstruct
    public void init() {
        // Carrega a configuração ao iniciar a aplicação
        config = configRepository.findById(1L).orElseGet(() -> {
            SystemConfig newConfig = new SystemConfig();
            newConfig.setNumMesas(200);
            newConfig.setNomeEstabelecimento("Meu Restaurante");
            newConfig.setBatePapoAtivo(true);
            return configRepository.save(newConfig);
        });
    }

    public SystemConfig getConfig() {
        return config;
    }

    public void updateConfig(SystemConfig updatedConfig) {
        updatedConfig.setId(1L); // Garantir a chave primária fixa
        config = configRepository.save(updatedConfig);
    }
}
