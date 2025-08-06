package br.com.nexum.rpajavalab.application.usecase;


import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Comparator;
import java.util.Optional;

@Slf4j
@Component
public class EnviarPorEmailUseCase {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailRemetente;

    public EnviarPorEmailUseCase(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean enviarUltimaPlanilhaPorEmail(String emailDestinatario) {
        log.info("Iniciando envio da última planilha Excel por email para: {}", emailDestinatario);

        try {
            Optional<File> ultimaPlanilha = encontrarUltimaPlanilhaExcel();

            if (ultimaPlanilha.isEmpty()) {
                log.error("Nenhuma planilha Excel encontrada");
                return false;
            }

            enviarEmailComAnexo(emailDestinatario, ultimaPlanilha.get());

            log.info("Email enviado com sucesso para: {}", emailDestinatario);
            return true;

        } catch (Exception e) {
            log.error("Erro durante o envio do email: ", e);
            return false;
        }
    }

    private Optional<File> encontrarUltimaPlanilhaExcel() {
        try {
            File diretorioAtual = new File(".");
            File[] arquivos = diretorioAtual.listFiles((dir, name) ->
                    name.startsWith("video_info_") && name.endsWith(".xlsx"));

            if (arquivos == null || arquivos.length == 0) {
                return Optional.empty();
            }

            File ultimoArquivo = java.util.Arrays.stream(arquivos)
                    .max(Comparator.comparing(File::lastModified))
                    .orElse(null);

            log.info("Última planilha Excel encontrada: {}", ultimoArquivo.getName());
            return Optional.ofNullable(ultimoArquivo);

        } catch (Exception e) {
            log.error("Erro ao procurar planilha Excel: ", e);
            return Optional.empty();
        }
    }

    private void enviarEmailComAnexo(String emailDestinatario, File anexo) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(emailRemetente);
        helper.setTo(emailDestinatario);
        helper.setSubject("Relatório de Informações do Vídeo - " + anexo.getName());

        String corpoEmail = String.format(
                "Olá,\n\n" +
                        "Segue em anexo o relatório com as informações do vídeo processado.\n\n" +
                        "Arquivo: %s\n" +
                        "Data de geração: %s\n\n" +
                        "Atenciosamente,\n" +
                        "Sistema RPA Java Lab",
                anexo.getName(),
                new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(anexo.lastModified())
        );

        helper.setText(corpoEmail);
        helper.addAttachment(anexo.getName(), anexo);

        mailSender.send(message);
    }
}