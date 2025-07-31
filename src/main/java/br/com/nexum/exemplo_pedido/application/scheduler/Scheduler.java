package br.com.nexum.exemplo_pedido.application.scheduler;

import br.com.nexum.exemplo_pedido.application.service.BrowserService;
import br.com.nexum.exemplo_pedido.application.usecase.YoutubeSearchUseCase;
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

    @Scheduled(fixedDelay = 30000, initialDelay = 5000) // Executa 5 segundos após inicialização, depois a cada 30 segundos
    public void executarAutomacaoYoutube() {
        log.info("=== Iniciando automação YouTube ===");

        try {
            // Executar todas as ações do YouTube: abrir site + buscar + clicar primeiro vídeo
            youtubeSearchUseCase.executarBuscaEClicarPrimeiroVideo("movements daylily");

            log.info("=== Automação YouTube concluída com sucesso ===");

        } catch (Exception e) {
            log.error("Erro durante automação YouTube: ", e);
        }
    }
}