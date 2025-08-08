package com.nexum.rpajavalab.application.usecase;

import com.nexum.rpajavalab.domain.port.in.YoutubeSearchUseCase;
import com.nexum.rpajavalab.domain.port.out.BrowserPort;
import com.nexum.rpajavalab.domain.model.VideoInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class YoutubeSearchUseCaseImpl implements YoutubeSearchUseCase {

    private final BrowserPort browserPort;

    @Override
    public void executarBuscaEClicarPrimeiroVideo(String termoBusca) {
        log.info("Iniciando busca no YouTube: {}", termoBusca);

        abrirYoutube();
        buscarVideo(termoBusca);
        clicarPrimeiroVideo();
        pularAnuncio();
        VideoInfo videoInfo = coletarInformacoesVideo();
        gerarArquivoTxt(videoInfo);

        log.info("Busca concluída com sucesso.");
    }

    private void abrirYoutube() {
        try {
            log.info("Abrindo YouTube...");
            browserPort.getDriver().get("https://www.youtube.com");
            log.info("YouTube aberto com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao abrir YouTube: ", e);
        }
    }

    private void buscarVideo(String termoBusca) {
        try {
            log.info("Buscando por: {}", termoBusca);

            WebElement barraPesquisa = browserPort.getWait().until(
                    ExpectedConditions.elementToBeClickable(By.name("search_query"))
            );

            barraPesquisa.clear();
            barraPesquisa.sendKeys(termoBusca);
            barraPesquisa.sendKeys(org.openqa.selenium.Keys.ENTER);

            log.info("Busca realizada com sucesso.");

        } catch (Exception e) {
            log.error("Erro ao realizar busca: ", e);
        }
    }

    private void clicarPrimeiroVideo() {
        try {
            log.info("Clicando no primeiro vídeo dos resultados...");

            browserPort.getWait().until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("ytd-video-renderer")
            ));

            List<WebElement> videos = browserPort.getDriver().findElements(
                    By.cssSelector("ytd-video-renderer #video-title")
            );

            if (!videos.isEmpty()) {
                String tituloVideo = videos.get(0).getAttribute("title");
                log.info("Clicando no vídeo: {}", tituloVideo);
                videos.get(0).click();
            } else {
                log.warn("Nenhum vídeo encontrado nos resultados");
            }

        } catch (Exception e) {
            log.error("Erro ao clicar no primeiro vídeo: ", e);
        }
    }

    private void pularAnuncio() {
        try {
            WebDriverWait wait = new WebDriverWait(browserPort.getDriver(), Duration.ofSeconds(10));
            WebElement skipButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class, 'ytp-ad-skip-button') or contains(@class, 'ytp-skip-ad-button')]")
            ));
            skipButton.click();
            Thread.sleep(1000);
            log.info("Anúncio pulado com sucesso.");
        } catch (TimeoutException e) {
            log.info("Nenhum anúncio encontrado ou já foi pulado.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private VideoInfo coletarInformacoesVideo() {
        try {
            log.info("Coletando informações do vídeo...");

            // Aguardar carregamento da página
            Thread.sleep(3000);

            // Título do vídeo
            WebElement tituloElement = browserPort.getWait().until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1.style-scope.ytd-watch-metadata yt-formatted-string"))
            );
            String nomeVideo = tituloElement.getText();

            // Nome do canal
            WebElement canalElement = browserPort.getWait().until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("ytd-channel-name#channel-name yt-formatted-string a"))
            );
            String nomeCanal = canalElement.getText();

            // Capturar visualizações e data do elemento info
            String visualizacoesExatas = "Visualizações não encontradas";
            String dataLancamento = "Data não encontrada";

            try {
                // Buscar o elemento que contém tanto visualizações quanto data
                WebElement infoElement = browserPort.getWait().until(
                        ExpectedConditions.presenceOfElementLocated(By.cssSelector("yt-formatted-string#info"))
                );

                // Buscar todos os spans dentro do elemento info
                List<WebElement> spans = infoElement.findElements(By.cssSelector("span.bold"));

                if (spans.size() >= 2) {
                    // Primeiro span contém visualizações
                    visualizacoesExatas = spans.get(0).getText().trim();
                    // Terceiro span contém a data (segundo span é apenas espaço)
                    if (spans.size() >= 3) {
                        dataLancamento = spans.get(2).getText().trim();
                    }
                } else {
                    // Fallback: tentar capturar o texto completo e fazer split
                    String textoCompleto = infoElement.getText();
                    log.info("Texto completo do info: {}", textoCompleto);

                    // Dividir por múltiplos espaços em branco
                    String[] partes = textoCompleto.split("\\s{2,}");
                    if (partes.length >= 2) {
                        visualizacoesExatas = partes[0].trim();
                        dataLancamento = partes[1].trim();
                    }
                }

            } catch (Exception e) {
                log.warn("Não foi possível capturar informações do elemento info: {}", e.getMessage());

                // Fallback para visualizações
                try {
                    WebElement visualizacoesElement = browserPort.getWait().until(
                            ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), 'visualizações') or contains(text(), 'views')]"))
                    );
                    visualizacoesExatas = visualizacoesElement.getText();
                } catch (Exception ex) {
                    log.warn("Não foi possível capturar as visualizações");
                }

                // Fallback para data
                try {
                    WebElement dataElement = browserPort.getDriver().findElement(
                            By.xpath("//span[contains(text(), 'de ') and contains(@class, 'bold')]")
                    );
                    dataLancamento = dataElement.getText();
                } catch (Exception ex) {
                    log.warn("Não foi possível capturar a data do vídeo");
                }
            }

            VideoInfo videoInfo = new VideoInfo(nomeVideo, nomeCanal, visualizacoesExatas, dataLancamento);
            log.info("Informações coletadas: {}", videoInfo);

            return videoInfo;

        } catch (Exception e) {
            log.error("Erro ao coletar informações: ", e);
            return new VideoInfo("Erro", "Erro", "Erro", "Erro");
        }
    }

    private void gerarArquivoTxt(VideoInfo videoInfo) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String nomeArquivo = "video_info_" + timestamp + ".txt";

            try (FileWriter writer = new FileWriter(nomeArquivo)) {
                writer.write("=== INFORMAÇÕES DO VÍDEO ===\n\n");
                writer.write("Nome do Vídeo: " + videoInfo.getNomeVideo() + "\n");
                writer.write("Nome do Canal: " + videoInfo.getNomeCanal() + "\n");
                writer.write("Visualizações: " + videoInfo.getQuantidadeVisualizacoes() + "\n");
                writer.write("Data de Lançamento: " + videoInfo.getDataLancamento() + "\n");
                writer.write("\nArquivo gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            }

            log.info("Arquivo gerado com sucesso: {}", nomeArquivo);

        } catch (IOException e) {
            log.error("Erro ao gerar arquivo: ", e);
        }
    }
}
