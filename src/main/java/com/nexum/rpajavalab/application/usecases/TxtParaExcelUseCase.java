package com.nexum.rpajavalab.application.usecases;

import com.nexum.rpajavalab.domain.models.VideoInfo;
import com.nexum.rpajavalab.domain.ports.in.TxtParaExcelUsePort;
import com.nexum.rpajavalab.domain.ports.out.VideoInfoRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TxtParaExcelUseCase implements TxtParaExcelUsePort {

    private final VideoInfoRepositoryPort videoInfoRepositoryPort;

    @Override
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
            
            // Extrair dados do TXT e salvar no banco de dados
            VideoInfo videoInfo = extrairVideoInfoDoTxt(conteudoTxt);
            if (videoInfo != null && nomeArquivoExcel != null) {
                try {
                    videoInfoRepositoryPort.salvar(videoInfo, nomeArquivoExcel);
                    log.info("Dados salvos no banco de dados com sucesso");
                } catch (Exception e) {
                    log.error("Erro ao salvar dados no banco de dados: ", e);
                    // Não interrompe o fluxo, apenas loga o erro
                }
            }

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

            // Processar conteúdo do TXT para extrair atributos e valores
            String[] linhas = conteudoTxt.split("\n");
            java.util.List<String> atributos = new java.util.ArrayList<>();
            java.util.List<String> valores = new java.util.ArrayList<>();

            for (String linha : linhas) {
                if (linha.contains(":") && !linha.startsWith("===") && !linha.trim().isEmpty()) {
                    String[] partes = linha.split(":", 2);
                    if (partes.length == 2) {
                        atributos.add(partes[0].trim());
                        valores.add(partes[1].trim());
                    }
                }
            }

            // Criar linha dos cabeçalhos (atributos)
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < atributos.size(); i++) {
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue(atributos.get(i));
                headerCell.setCellStyle(headerStyle);
            }

            // Criar linha dos valores
            Row valueRow = sheet.createRow(1);
            for (int i = 0; i < valores.size(); i++) {
                Cell valueCell = valueRow.createCell(i);
                valueCell.setCellValue(valores.get(i));
            }

            // Ajustar largura das colunas
            for (int i = 0; i < atributos.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Salvar arquivo
            try (FileOutputStream fileOut = new FileOutputStream(nomeArquivoExcel)) {
                workbook.write(fileOut);
            }
        }

        return nomeArquivoExcel;
    }
    
    /**
     * Extrai as informações do vídeo do conteúdo do arquivo TXT
     */
    private VideoInfo extrairVideoInfoDoTxt(String conteudoTxt) {
        try {
            String[] linhas = conteudoTxt.split("\n");
            String nomeVideo = "";
            String nomeCanal = "";
            String visualizacoes = "";
            String dataLancamento = "";
            
            for (String linha : linhas) {
                if (linha.contains(":") && !linha.startsWith("===") && !linha.trim().isEmpty()) {
                    String[] partes = linha.split(":", 2);
                    if (partes.length == 2) {
                        String chave = partes[0].trim().toLowerCase();
                        String valor = partes[1].trim();
                        
                        switch (chave) {
                            case "nome do vídeo":
                                nomeVideo = valor;
                                break;
                            case "nome do canal":
                                nomeCanal = valor;
                                break;
                            case "visualizações":
                                visualizacoes = valor;
                                break;
                            case "data de lançamento":
                                dataLancamento = valor;
                                break;
                        }
                    }
                }
            }
            
            // Verificar se todos os dados essenciais foram encontrados
            if (!nomeVideo.isEmpty() && !nomeCanal.isEmpty()) {
                log.info("VideoInfo extraído: Vídeo={}, Canal={}, Visualizações={}, Data={}",
                        nomeVideo, nomeCanal, visualizacoes, dataLancamento);
                return new VideoInfo(nomeVideo, nomeCanal, visualizacoes, dataLancamento);
            } else {
                log.warn("Dados insuficientes para criar VideoInfo. Vídeo: '{}', Canal: '{}'", nomeVideo, nomeCanal);
                return null;
            }
            
        } catch (Exception e) {
            log.error("Erro ao extrair VideoInfo do TXT: ", e);
            return null;
        }
    }
}
