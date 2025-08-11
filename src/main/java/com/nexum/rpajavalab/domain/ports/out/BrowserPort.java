package com.nexum.rpajavalab.domain.ports.out;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface BrowserPort {
    void abrirChrome();
    WebDriver getDriver();
    WebDriverWait getWait();
    void fecharNavegador();
}
