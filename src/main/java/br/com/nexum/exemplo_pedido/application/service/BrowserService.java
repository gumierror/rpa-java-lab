package br.com.nexum.exemplo_pedido.application.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BrowserService {

    private WebDriver driver;

    public void abrirNavegador() {
        try {
            log.info("Configurando ChromeDriver...");
            WebDriverManager.chromedriver().setup(); // Baixa e configura automaticamente

            driver = new ChromeDriver();
            driver.get("https://www.youtube.com"); // Altere para a URL desejada

            log.info("Navegador aberto com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao iniciar navegador: ", e);
        }
    }

    @PreDestroy
    public void fecharNavegador() {
        if (driver != null) {
            log.info("Fechando navegador...");
            driver.quit();
        }
    }
}
