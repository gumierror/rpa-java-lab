package com.nexum.rpajavalab.domain.models;

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
