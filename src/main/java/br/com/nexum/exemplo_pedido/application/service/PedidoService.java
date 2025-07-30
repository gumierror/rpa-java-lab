package br.com.nexum.exemplo_pedido.application.services;

import br.com.nexum.exemplo_pedido.domain.models.Pedido;
import br.com.nexum.exemplo_pedido.domain.repositories.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;

    public List<Pedido> listarPedidos() {
        return repository.buscarTodos();
    }
}
