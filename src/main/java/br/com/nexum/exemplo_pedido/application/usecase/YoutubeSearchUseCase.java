package br.com.nexum.exemplo_pedido.application.usecase;

import br.com.nexum.exemplo_pedido.application.service.BrowserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class YoutubeSearchUseCase {

    private final BrowserService browserService;

    public void executarBuscaEClicarPrimeiroVideo(String termoBusca) {
        log.info("Iniciando busca no YouTube: {}", termoBusca);

        abrirYoutube();
        buscarVideo(termoBusca);
        clicarPrimeiroVideo();

        log.info("Busca concluída com sucesso.");
    }

    private void abrirYoutube() {
        try {
            log.info("Abrindo YouTube...");
            browserService.getDriver().get("https://www.youtube.com");
            log.info("YouTube aberto com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao abrir YouTube: ", e);
        }
    }

    private void buscarVideo(String termoBusca) {
        try {
            log.info("Buscando por: {}", termoBusca);

            WebElement barraPesquisa = browserService.getWait().until(
                    ExpectedConditions.elementToBeClickable(By.name("search_query"))
            );

            barraPesquisa.clear();
            barraPesquisa.sendKeys(termoBusca);

            // Alternativa 1: Pressionar Enter direto na barra
            barraPesquisa.sendKeys(org.openqa.selenium.Keys.ENTER);

            log.info("Busca realizada com sucesso.");

        } catch (Exception e) {
            log.error("Erro ao realizar busca: ", e);
        }
    }

    private void clicarPrimeiroVideo() {
        try {
            log.info("Clicando no primeiro vídeo dos resultados...");

            browserService.getWait().until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("ytd-video-renderer")
            ));

            List<WebElement> videos = browserService.getDriver().findElements(
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
}