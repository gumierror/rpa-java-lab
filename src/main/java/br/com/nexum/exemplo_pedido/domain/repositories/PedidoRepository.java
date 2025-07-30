package br.com.nexum.exemplo_pedido.domain.repositories;

import br.com.nexum.exemplo_pedido.domain.models.Pedido;
import java.util.List;

public interface PedidoRepository {
    List<Pedido> buscarTodos();
}
