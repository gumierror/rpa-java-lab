package br.com.nexum.rpajavalab.application.usecase;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

@Slf4j
@Component
public class TxtParaExcelUseCase {

    public String converterUltimoTxtParaExcel() {
        log.info("Iniciando conversão do último arquivo TXT para Excel");

        try {
            Optional<File> ultimoArquivoTxt = encontrarUltimoArquivoTxt();

            if (ultimoArquivoTxt.isEmpty()) {
                log.error("Nenhum arquivo TXT encontrado");
                return null;
            }

            String conteudoTxt = lerArquivoTxt(ultimoArquivoTxt.get());
            String nomeArquivoExcel = gerarArquivoExcel(conteudoTxt);

            log.info("Conversão concluída com sucesso: {}", nomeArquivoExcel);
            return nomeArquivoExcel;

        } catch (Exception e) {
            log.error("Erro durante a conversão: ", e);
            return null;
        }
    }

    private Optional<File> encontrarUltimoArquivoTxt() {
        try {
            File diretorioAtual = new File(".");
            File[] arquivos = diretorioAtual.listFiles((dir, name) ->
                    name.startsWith("video_info_") && name.endsWith(".txt"));

            if (arquivos == null || arquivos.length == 0) {
                return Optional.empty();
            }

            File ultimoArquivo = java.util.Arrays.stream(arquivos)
                    .max(Comparator.comparing(File::lastModified))
                    .orElse(null);

            log.info("Último arquivo TXT encontrado: {}", ultimoArquivo.getName());
            return Optional.ofNullable(ultimoArquivo);

        } catch (Exception e) {
            log.error("Erro ao procurar arquivo TXT: ", e);
            return Optional.empty();
        }
    }

    private String lerArquivoTxt(File arquivo) throws IOException {
        StringBuilder conteudo = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                conteudo.append(linha).append("\n");
            }
        }
        return conteudo.toString();
    }

    private String gerarArquivoExcel(String conteudoTxt) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String nomeArquivoExcel = "video_info_" + timestamp + ".xlsx";

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Informações do Vídeo");

            // Criar cabeçalho
            Row headerRow = sheet.createRow(0);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Atributo");
            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Valor");

            // Estilizar cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerCell1.setCellStyle(headerStyle);
            headerCell2.setCellStyle(headerStyle);

            // Processar conteúdo do TXT
            String[] linhas = conteudoTxt.split("\n");
            int rowNum = 1;

            for (String linha : linhas) {
                if (linha.contains(":") && !linha.startsWith("===") && !linha.trim().isEmpty()) {
                    String[] partes = linha.split(":", 2);
                    if (partes.length == 2) {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(partes[0].trim());
                        row.createCell(1).setCellValue(partes[1].trim());
                    }
                }
            }

            // Ajustar largura das colunas
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            // Salvar arquivo
            try (FileOutputStream fileOut = new FileOutputStream(nomeArquivoExcel)) {
                workbook.write(fileOut);
            }
        }

        return nomeArquivoExcel;
    }
}