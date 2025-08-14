### Explicação:

✅ Scheduler (é o trigger)
✅ Adapter In (implementação de código da requisição que será feita pra aplicação)
✅ Port In (é a interface que define o contrato de entrada do domain - o que ele oferece)
✅ UseCase (implementa o Port In e orquestra a requisição, chamando as entities)
✅ Entities (a lógica de negócio de fato)
✅ Port Out (interface dizendo pro adapter out o que o domain precisa)
✅ Adapter Out (implementação de código que vai resolver o que o domain pediu)

### Fluxo:

**Scheduler** → **Adapter In** → **Port In** → **UseCase** → **Entities** → **Port Out** → **Adapter Out**