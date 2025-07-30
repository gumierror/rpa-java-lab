package br.com.nexum.exemplo_pedido.infrastructure.persistence.repositories;

import br.com.nexum.exemplo_pedido.infrastructure.persistence.entities.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {
}
