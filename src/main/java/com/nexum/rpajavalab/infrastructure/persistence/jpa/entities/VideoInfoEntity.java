package com.nexum.rpajavalab.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "video_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoInfoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome_video", nullable = false, length = 500)
    private String nomeVideo;
    
    @Column(name = "nome_canal", nullable = false, length = 200)
    private String nomeCanal;
    
    @Column(name = "quantidade_visualizacoes", nullable = false, length = 100)
    private String quantidadeVisualizacoes;
    
    @Column(name = "data_lancamento", nullable = false, length = 100)
    private String dataLancamento;
    
    @Column(name = "nome_arquivo_excel", nullable = false, length = 300)
    private String nomeArquivoExcel;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
