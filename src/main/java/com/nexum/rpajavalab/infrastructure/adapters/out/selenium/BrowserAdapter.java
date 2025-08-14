package com.nexum.rpajavalab.infrastructure.adapters.out.selenium;

import com.nexum.rpajavalab.domain.ports.out.BrowserPort;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Slf4j
@Service
public class BrowserAdapter implements BrowserPort {

    private WebDriver driver;
    private WebDriverWait wait;

    @Override
    public void abrirChrome() {
        try {
            log.info("Configurando ChromeDriver...");
            WebDriverManager.chromedriver().setup();

            driver = new ChromeDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            log.info("Chrome aberto com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao iniciar Chrome: ", e);
        }
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    @Override
    public WebDriverWait getWait() {
        return wait;
    }

    @Override
    @PreDestroy
    public void fecharNavegador() {
        if (driver != null) {
            log.info("Fechando navegador...");
            driver.quit();
        }
    }
}
