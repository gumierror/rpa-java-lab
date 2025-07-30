package br.com.nexum.exemplo_pedido.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pedido {
    private Long id;
    private String descricao;
}
