package br.com.nexum.exemplo_pedido.infrastructure.persistence.repositories;

import br.com.nexum.exemplo_pedido.domain.models.Pedido;
import br.com.nexum.exemplo_pedido.domain.repositories.PedidoRepository;
import br.com.nexum.exemplo_pedido.infrastructure.persistence.entities.PedidoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PedidoRepositoryImpl implements PedidoRepository {

    private final PedidoJpaRepository jpaRepository;

    @Override
    public List<Pedido> buscarTodos() {
        return jpaRepository.findAll().stream()
                .map(e -> new Pedido(e.getId(), e.getDescricao()))
                .collect(Collectors.toList());
    }
}
