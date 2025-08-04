package br.com.nexum.exemplo_pedido.domain.models;

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