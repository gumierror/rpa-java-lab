package com.nexum.rpajavalab.infrastructure.adapters.in.scheduler;

import com.nexum.rpajavalab.domain.ports.out.BrowserPort;
import com.nexum.rpajavalab.domain.ports.in.YoutubeSearchUsePort;
import com.nexum.rpajavalab.domain.ports.in.TxtParaExcelUsePort;
import com.nexum.rpajavalab.domain.ports.in.EnviarPorEmailPort;
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
public class BotJobScheduler {

    private final BrowserPort browserPort;
    private final YoutubeSearchUsePort youtubeSearchUsePort;
    private final TxtParaExcelUsePort txtParaExcelUsePort;
    private final EnviarPorEmailPort enviarPorEmailPort;

    @PostConstruct
    public void inicializarNavegador() {
        log.info("=== Inicializando Chrome na inicialização da aplicação ===");

        try {
            browserPort.abrirChrome();
            log.info("Chrome aberto com sucesso - Aguardando execução das tarefas agendadas");
        } catch (Exception e) {
            log.error("Erro ao inicializar Chrome: ", e);
        }
    }

    @Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE)
    public void executarAutomacaoYoutube() {
        log.info("=== Iniciando automação YouTube ===");

        try {
            youtubeSearchUsePort.executarBuscaEClicarPrimeiroVideo("movements daylily");
            log.info("=== Automação YouTube concluída com sucesso ===");

            // Executar conversão para Excel após a automação
            log.info("=== Iniciando conversão TXT para Excel ===");
            String arquivoExcel = txtParaExcelUsePort.converterUltimoTxtParaExcel();

            if (arquivoExcel != null) {
                log.info("=== Conversão para Excel concluída: {} ===", arquivoExcel);
            } else {
                log.error("=== Falha na conversão para Excel ===");
            }

            enviarPorEmailPort.enviarUltimaPlanilhaPorEmail("gui.nobrega@hotmail.com");
            log.info("=== Email enviado com sucesso ===");

        } catch (Exception e) {
            log.error("Erro durante automação YouTube: ", e);
        }

        try {
            browserPort.fecharNavegador();
            log.info("=== Navegador fechado com sucesso ===");
        } catch (Exception e) {
            log.error("Erro ao fechar navegador: ", e);
        }
    }
}
