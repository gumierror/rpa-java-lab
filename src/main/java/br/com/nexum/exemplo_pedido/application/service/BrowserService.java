package br.com.nexum.exemplo_pedido.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BrowserService {

    public void openChrome() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder processBuilder;

            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("cmd", "/c", "start", "chrome", "https://www.youtube.com");
            } else if (os.contains("mac")) {
                processBuilder = new ProcessBuilder("open", "-a", "Google Chrome");
            } else {
                processBuilder = new ProcessBuilder("google-chrome");
            }

            processBuilder.start();
            log.info("Google Chrome opened successfully");
        } catch (Exception e) {
            log.error("Failed to open Google Chrome: {}", e.getMessage());
        }
    }
}