package br.com.nexum.exemplo_pedido.application.scheduler;

import br.com.nexum.exemplo_pedido.application.service.BrowserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class Scheduler {

    private final BrowserService browserService;

    @PostConstruct
    private void schedullerRobot() {
        log.info("Iniciando tarefa agendada...");

        browserService.abrirNavegador(); // agora com Selenium
        log.info("Tarefa concluída.");
    }
}
