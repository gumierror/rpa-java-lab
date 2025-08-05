package br.com.nexum.rpajavalab.application.scheduler;

import br.com.nexum.RpaJavaLab.application.service.BrowserService;
import br.com.nexum.RpaJavaLab.application.usecase.YoutubeSearchUseCase;
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
    private final YoutubeSearchUseCase youtubeSearchUseCase;

    @PostConstruct
    public void inicializarNavegador() {
        log.info("=== Inicializando Chrome na inicialização da aplicação ===");

        try {
            browserService.abrirChrome();
            log.info("Chrome aberto com sucesso - Aguardando execução das tarefas agendadas");
        } catch (Exception e) {
            log.error("Erro ao inicializar Chrome: ", e);
        }
    }

    @Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE) // Executa 5 segundos após inicialização, depois não executa mais
    public void executarAutomacaoYoutube() {
        log.info("=== Iniciando automação YouTube ===");

        try {
            youtubeSearchUseCase.executarBuscaEClicarPrimeiroVideo("movements daylily");

            log.info("=== Automação YouTube concluída com sucesso ===");

        } catch (Exception e) {
            log.error("Erro durante automação YouTube: ", e);
        }
        try {
            browserService.fecharNavegador();
            log.info("=== Navegador fechado com sucesso ===");
        } catch (Exception e) {
            log.error("Erro ao fechar navegador: ", e);
        }
    }
}