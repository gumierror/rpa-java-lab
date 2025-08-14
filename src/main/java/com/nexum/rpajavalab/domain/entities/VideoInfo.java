package com.nexum.rpajavalab.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoInfo {
    private String nomeVideo;
    private String nomeCanal;
    private String quantidadeVisualizacoes;
    private String dataLancamento;
}
