package br.com.nexum.rpajavalab.application.scheduler;

import br.com.nexum.rpajavalab.application.service.BrowserService;
import br.com.nexum.rpajavalab.application.usecase.EnviarPorEmailUseCase;
import br.com.nexum.rpajavalab.application.usecase.TxtParaExcelUseCase;
import br.com.nexum.rpajavalab.application.usecase.YoutubeSearchUseCase;
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
    private final TxtParaExcelUseCase txtParaExcelUseCase;
    private final EnviarPorEmailUseCase enviarPorEmailUseCase;

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

    @Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE)
    public void executarAutomacaoYoutube() {
        log.info("=== Iniciando automação YouTube ===");

        try {
            youtubeSearchUseCase.executarBuscaEClicarPrimeiroVideo("movements daylily");
            log.info("=== Automação YouTube concluída com sucesso ===");

            // Executar conversão para Excel após a automação
            log.info("=== Iniciando conversão TXT para Excel ===");
            String arquivoExcel = txtParaExcelUseCase.converterUltimoTxtParaExcel();

            if (arquivoExcel != null) {
                log.info("=== Conversão para Excel concluída: {} ===", arquivoExcel);
            } else {
                log.error("=== Falha na conversão para Excel ===");
            }

            enviarPorEmailUseCase.enviarUltimaPlanilhaPorEmail("gui.nobrega@hotmail.com");
            log.info("=== Email enviado com sucesso ===");

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