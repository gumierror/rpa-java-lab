package br.com.nexum.exemplo_pedido.application.scheduler;

import br.com.nexum.exemplo_pedido.application.service.BrowserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Scheduler {

    private final BrowserService browserService = new BrowserService();

    @PostConstruct
    public void init() {
        log.info("Scheduler initialized successfully.");

        log.info("Opening Google Chrome...");
        browserService.openChrome();
        log.info("Google Chrome should now be open.");

        // Here you can add any scheduled tasks or initializations needed for your application.
    }

}
