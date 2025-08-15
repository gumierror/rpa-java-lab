package com.nexum.rpajavalab.infrastructure.persistence.jpa.mappers;

import com.nexum.rpajavalab.domain.models.VideoInfo;
import com.nexum.rpajavalab.infrastructure.persistence.jpa.entities.VideoInfoEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VideoInfoMapper {
    
    /**
     * Converte VideoInfo (domain) para VideoInfoEntity (persistence)
     * @param videoInfo Objeto do domínio
     * @param nomeArquivoExcel Nome do arquivo Excel gerado
     * @return Entidade JPA
     */
    public VideoInfoEntity toEntity(VideoInfo videoInfo, String nomeArquivoExcel) {
        if (videoInfo == null) {
            return null;
        }
        
        VideoInfoEntity entity = new VideoInfoEntity();
        entity.setNomeVideo(videoInfo.getNomeVideo());
        entity.setNomeCanal(videoInfo.getNomeCanal());
        entity.setQuantidadeVisualizacoes(videoInfo.getQuantidadeVisualizacoes());
        entity.setDataLancamento(videoInfo.getDataLancamento());
        entity.setNomeArquivoExcel(nomeArquivoExcel);
        // dataCriacao e dataAtualizacao são definidas automaticamente no @PrePersist
        
        return entity;
    }
    
    /**
     * Converte VideoInfoEntity (persistence) para VideoInfo (domain)
     * @param entity Entidade JPA
     * @return Objeto do domínio
     */
    public VideoInfo toDomain(VideoInfoEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new VideoInfo(
                entity.getNomeVideo(),
                entity.getNomeCanal(),
                entity.getQuantidadeVisualizacoes(),
                entity.getDataLancamento()
        );
    }
    
    /**
     * Converte VideoInfoEntity para VideoInfo incluindo metadados
     * @param entity Entidade JPA
     * @return VideoInfo com informações estendidas
     */
    public VideoInfoWithMetadata toDomainWithMetadata(VideoInfoEntity entity) {
        if (entity == null) {
            return null;
        }
        
        VideoInfo videoInfo = toDomain(entity);
        return new VideoInfoWithMetadata(
                entity.getId(),
                videoInfo,
                entity.getNomeArquivoExcel(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao()
        );
    }
    
    /**
     * Converte lista de entidades para lista de objetos do domínio
     */
    public List<VideoInfo> toDomainList(List<VideoInfoEntity> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Converte lista de entidades para lista com metadados
     */
    public List<VideoInfoWithMetadata> toDomainWithMetadataList(List<VideoInfoEntity> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDomainWithMetadata)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualiza uma entidade existente com novos dados do domínio
     */
    public void updateEntity(VideoInfoEntity entity, VideoInfo videoInfo, String nomeArquivoExcel) {
        if (entity == null || videoInfo == null) {
            return;
        }
        
        entity.setNomeVideo(videoInfo.getNomeVideo());
        entity.setNomeCanal(videoInfo.getNomeCanal());
        entity.setQuantidadeVisualizacoes(videoInfo.getQuantidadeVisualizacoes());
        entity.setDataLancamento(videoInfo.getDataLancamento());
        entity.setNomeArquivoExcel(nomeArquivoExcel);
        // dataAtualizacao será definida automaticamente no @PreUpdate
    }
    
    /**
     * Classe auxiliar para retornar VideoInfo com metadados
     */
    public static class VideoInfoWithMetadata {
        private final Long id;
        private final VideoInfo videoInfo;
        private final String nomeArquivoExcel;
        private final LocalDateTime dataCriacao;
        private final LocalDateTime dataAtualizacao;
        
        public VideoInfoWithMetadata(Long id, VideoInfo videoInfo, String nomeArquivoExcel, 
                                   LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
            this.id = id;
            this.videoInfo = videoInfo;
            this.nomeArquivoExcel = nomeArquivoExcel;
            this.dataCriacao = dataCriacao;
            this.dataAtualizacao = dataAtualizacao;
        }
        
        // Getters
        public Long getId() { return id; }
        public VideoInfo getVideoInfo() { return videoInfo; }
        public String getNomeArquivoExcel() { return nomeArquivoExcel; }
        public LocalDateTime getDataCriacao() { return dataCriacao; }
        public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    }
}
