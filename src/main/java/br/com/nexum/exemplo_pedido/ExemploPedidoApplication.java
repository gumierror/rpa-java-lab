package br.com.nexum.exemplo_pedido;

import br.com.nexum.exemplo_pedido.application.services.PedidoService;
import br.com.nexum.exemplo_pedido.domain.models.Pedido;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExemploPedidoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExemploPedidoApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(PedidoService service) {
		return args -> {
			service.listarPedidos().forEach(pedido ->
					System.out.println("ID: " + pedido.getId() + " | Descrição: " + pedido.getDescricao())
			);
		};
	}
}
