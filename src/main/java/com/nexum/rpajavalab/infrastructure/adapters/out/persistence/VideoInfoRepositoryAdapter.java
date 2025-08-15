package com.nexum.rpajavalab.infrastructure.adapters.out.persistence;

import com.nexum.rpajavalab.domain.models.VideoInfo;
import com.nexum.rpajavalab.domain.ports.out.VideoInfoRepositoryPort;
import com.nexum.rpajavalab.infrastructure.persistence.jpa.entities.VideoInfoEntity;
import com.nexum.rpajavalab.infrastructure.persistence.jpa.mappers.VideoInfoMapper;
import com.nexum.rpajavalab.infrastructure.persistence.jpa.repositories.VideoInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoInfoRepositoryAdapter implements VideoInfoRepositoryPort {
    
    private final VideoInfoRepository videoInfoRepository;
    private final VideoInfoMapper videoInfoMapper;
    
    @Override
    public VideoInfo salvar(VideoInfo videoInfo, String nomeArquivoExcel) {
        log.info("Salvando informações do vídeo no banco de dados. Arquivo Excel: {}", nomeArquivoExcel);
        
        try {
            // Verificar se já existe um registro com o mesmo arquivo Excel
            if (videoInfoRepository.existsByNomeArquivoExcel(nomeArquivoExcel)) {
                log.warn("Já existe um registro para o arquivo Excel: {}", nomeArquivoExcel);
            }
            
            VideoInfoEntity entity = videoInfoMapper.toEntity(videoInfo, nomeArquivoExcel);
            VideoInfoEntity savedEntity = videoInfoRepository.save(entity);
            
            log.info("Informações do vídeo salvas com sucesso. ID: {}", savedEntity.getId());
            return videoInfoMapper.toDomain(savedEntity);
            
        } catch (Exception e) {
            log.error("Erro ao salvar informações do vídeo no banco de dados", e);
            throw new RuntimeException("Falha ao salvar no banco de dados", e);
        }
    }
    
    @Override
    public List<VideoInfo> buscarTodos() {
        log.info("Buscando todas as informações de vídeos do banco de dados");
        
        try {
            List<VideoInfoEntity> entities = videoInfoRepository.findAllByOrderByDataCriacaoDesc();
            List<VideoInfo> result = videoInfoMapper.toDomainList(entities);
            
            log.info("Encontrados {} registros de vídeos", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("Erro ao buscar todas as informações de vídeos", e);
            throw new RuntimeException("Falha ao buscar dados do banco", e);
        }
    }
    
    @Override
    public Optional<VideoInfo> buscarPorNomeArquivoExcel(String nomeArquivoExcel) {
        log.info("Buscando informações por nome do arquivo Excel: {}", nomeArquivoExcel);
        
        try {
            Optional<VideoInfoEntity> entity = videoInfoRepository.findByNomeArquivoExcel(nomeArquivoExcel);
            
            if (entity.isPresent()) {
                VideoInfo result = videoInfoMapper.toDomain(entity.get());
                log.info("Informações encontradas para o arquivo: {}", nomeArquivoExcel);
                return Optional.of(result);
            } else {
                log.info("Nenhuma informação encontrada para o arquivo: {}", nomeArquivoExcel);
                return Optional.empty();
            }
            
        } catch (Exception e) {
            log.error("Erro ao buscar informações por nome do arquivo Excel", e);
            throw new RuntimeException("Falha ao buscar por arquivo Excel", e);
        }
    }
    
    @Override
    public List<VideoInfo> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        log.info("Buscando informações por período: {} até {}", dataInicio, dataFim);
        
        try {
            List<VideoInfoEntity> entities = videoInfoRepository.findByDataCriacaoBetween(dataInicio, dataFim);
            List<VideoInfo> result = videoInfoMapper.toDomainList(entities);
            
            log.info("Encontrados {} registros no período especificado", result.size());
            return result;
            
        } catch (Exception e) {
            log.error("Erro ao buscar informações por período", e);
            throw new RuntimeException("Falha ao buscar por período", e);
        }
    }
    
    @Override
    public List<VideoInfo> buscarPorCanal(String nomeCanal) {
        log.info("Buscando informações por canal: {}", nomeCanal);
        
        try {
            List<VideoInfoEntity> entities = videoInfoRepository.findByNomeCanalContainingIgnoreCase(nomeCanal);
            List<VideoInfo> result = videoInfoMapper.toDomainList(entities);
            
            log.info("Encontrados {} registros para o canal: {}", result.size(), nomeCanal);
            return result;
            
        } catch (Exception e) {
            log.error("Erro ao buscar informações por canal", e);
            throw new RuntimeException("Falha ao buscar por canal", e);
        }
    }
    
    @Override
    public void removerPorId(Long id) {
        log.info("Removendo informações do vídeo com ID: {}", id);
        
        try {
            if (videoInfoRepository.existsById(id)) {
                videoInfoRepository.deleteById(id);
                log.info("Informações do vídeo removidas com sucesso. ID: {}", id);
            } else {
                log.warn("Tentativa de remover registro inexistente. ID: {}", id);
                throw new RuntimeException("Registro não encontrado para remoção");
            }
            
        } catch (Exception e) {
            log.error("Erro ao remover informações do vídeo", e);
            throw new RuntimeException("Falha ao remover registro", e);
        }
    }
    
    @Override
    public long contarTotal() {
        log.info("Contando total de registros de vídeos");
        
        try {
            long total = videoInfoRepository.count();
            log.info("Total de registros encontrados: {}", total);
            return total;
            
        } catch (Exception e) {
            log.error("Erro ao contar registros", e);
            throw new RuntimeException("Falha ao contar registros", e);
        }
    }
}
