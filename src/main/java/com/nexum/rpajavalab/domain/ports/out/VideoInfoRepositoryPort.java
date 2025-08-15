package com.nexum.rpajavalab.domain.ports.out;

import com.nexum.rpajavalab.domain.models.VideoInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VideoInfoRepositoryPort {
    
    /**
     * Salva as informações do vídeo no banco de dados
     * @param videoInfo Informações do vídeo
     * @param nomeArquivoExcel Nome do arquivo Excel gerado
     * @return VideoInfo salvo com ID gerado
     */
    VideoInfo salvar(VideoInfo videoInfo, String nomeArquivoExcel);
    
    /**
     * Busca todas as informações de vídeos salvos
     * @return Lista de todas as informações de vídeos
     */
    List<VideoInfo> buscarTodos();
    
    /**
     * Busca informações por nome do arquivo Excel
     * @param nomeArquivoExcel Nome do arquivo Excel
     * @return Informações do vídeo se encontrado
     */
    Optional<VideoInfo> buscarPorNomeArquivoExcel(String nomeArquivoExcel);
    
    /**
     * Busca informações criadas em um período específico
     * @param dataInicio Data de início
     * @param dataFim Data de fim
     * @return Lista de informações no período
     */
    List<VideoInfo> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim);
    
    /**
     * Busca informações por nome do canal
     * @param nomeCanal Nome do canal
     * @return Lista de vídeos do canal
     */
    List<VideoInfo> buscarPorCanal(String nomeCanal);
    
    /**
     * Remove informações por ID
     * @param id ID do registro
     */
    void removerPorId(Long id);
    
    /**
     * Conta total de registros salvos
     * @return Número total de registros
     */
    long contarTotal();
}
