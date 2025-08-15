package com.nexum.rpajavalab.infrastructure.persistence.jpa.repositories;

import com.nexum.rpajavalab.infrastructure.persistence.jpa.entities.VideoInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoInfoRepository extends JpaRepository<VideoInfoEntity, Long> {
    
    /**
     * Busca por nome do arquivo Excel
     */
    Optional<VideoInfoEntity> findByNomeArquivoExcel(String nomeArquivoExcel);
    
    /**
     * Busca por nome do canal (ignora case)
     */
    List<VideoInfoEntity> findByNomeCanalContainingIgnoreCase(String nomeCanal);
    
    /**
     * Busca por período de criação
     */
    List<VideoInfoEntity> findByDataCriacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
    
    /**
     * Busca ordenado por data de criação (mais recente primeiro)
     */
    List<VideoInfoEntity> findAllByOrderByDataCriacaoDesc();
    
    /**
     * Busca por nome do vídeo (ignora case)
     */
    List<VideoInfoEntity> findByNomeVideoContainingIgnoreCase(String nomeVideo);
    
    /**
     * Conta registros por canal
     */
    @Query("SELECT COUNT(v) FROM VideoInfoEntity v WHERE LOWER(v.nomeCanal) = LOWER(:nomeCanal)")
    long countByNomeCanal(@Param("nomeCanal") String nomeCanal);
    
    /**
     * Busca os últimos N registros
     */
    @Query("SELECT v FROM VideoInfoEntity v ORDER BY v.dataCriacao DESC LIMIT :limite")
    List<VideoInfoEntity> findTopNByOrderByDataCriacaoDesc(@Param("limite") int limite);
    
    /**
     * Verifica se existe arquivo Excel com o nome
     */
    boolean existsByNomeArquivoExcel(String nomeArquivoExcel);
}
