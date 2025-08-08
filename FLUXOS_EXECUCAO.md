# 🔄 **Fluxos de Execução do RPA Java Lab**

## 📊 **Visão Geral da Arquitetura**

```
┌─────────────────────────────────────────────────────┐
│                   CLEAN ARCHITECTURE                │
├─────────────────────────────────────────────────────┤
│  🌐 INFRASTRUCTURE │  📱 APPLICATION │  🎯 DOMAIN   │
│     (Adapters)     │    (Use Cases)  │   (Business) │
├─────────────────────────────────────────────────────┤
│  • BrowserService  │  • Scheduler    │  • Ports     │
│  • Email Config    │  • Use Cases    │  • Models    │
└─────────────────────────────────────────────────────┘
```

---

## 🚀 **Fluxo Principal de Execução**

### **1. INICIALIZAÇÃO DA APLICAÇÃO**
```java
RpajavalabApplication.main() 
    ↓
Spring Boot Context carrega todos os beans
    ↓ 
@PostConstruct BotJobScheduler.inicializarNavegador()
    ↓
BrowserPort.abrirChrome() → Abre Chrome via Selenium
```

### **2. AUTOMAÇÃO AGENDADA (5s após inicialização)**
```java
@Scheduled BotJobScheduler.executarAutomacaoYoutube()
    ↓
┌─────────────────────────────────────────────────────┐
│  FLUXO COMPLETO DE AUTOMAÇÃO RPA                    │
└─────────────────────────────────────────────────────┘
```

---

## 🎬 **Fluxo Detalhado: Automação YouTube**

### **Etapa 1: Busca e Navegação**
```java
YoutubeSearchUseCase.executarBuscaEClicarPrimeiroVideo("movements daylily")
    ↓
1️⃣ abrirYoutube() 
   → driver.get("https://www.youtube.com")
    ↓
2️⃣ buscarVideo("movements daylily")
   → Localiza campo search_query
   → Digita termo + ENTER
    ↓
3️⃣ clicarPrimeiroVideo()
   → Aguarda resultados carregar
   → Clica no primeiro vídeo da lista
    ↓
4️⃣ pularAnuncio()
   → Tenta pular anúncio (se existir)
   → Timeout de 10s se não houver
```

### **Etapa 2: Extração de Dados**
```java
5️⃣ coletarInformacoesVideo()
   ↓
   📊 COLETA OS DADOS:
   • Título do vídeo (h1.style-scope.ytd-watch-metadata)
   • Nome do canal (ytd-channel-name#channel-name)  
   • Visualizações (yt-formatted-string#info span.bold[0])
   • Data lançamento (yt-formatted-string#info span.bold[2])
   ↓
   📝 Cria objeto VideoInfo com os dados
```

### **Etapa 3: Geração de Arquivo**
```java
6️⃣ gerarArquivoTxt(VideoInfo)
   ↓
   📄 GERA ARQUIVO:
   • Nome: "video_info_YYYY-MM-DD_HH-mm-ss.txt"
   • Conteúdo formatado com dados do vídeo
   • Timestamp de geração
```

---

## 📊 **Fluxo: Conversão TXT → Excel**

```java
TxtParaExcelUseCase.converterUltimoTxtParaExcel()
    ↓
1️⃣ encontrarUltimoArquivoTxt()
   → Busca arquivos "video_info_*.txt"
   → Seleciona o mais recente (lastModified)
    ↓
2️⃣ lerArquivoTxt(arquivo)
   → Lê conteúdo completo do arquivo
    ↓
3️⃣ gerarArquivoExcel(conteudo)
   → Cria planilha Excel (.xlsx)
   → Header com atributos (Nome, Canal, Views, Data)
   → Linha com valores extraídos
   → AutoSize das colunas
   → Salva: "video_info_YYYY-MM-DD_HH-mm-ss.xlsx"
```

---

## 📧 **Fluxo: Envio por Email**

```java
EnviarPorEmailUseCase.enviarUltimaPlanilhaPorEmail("gui.nobrega@hotmail.com")
    ↓
1️⃣ encontrarUltimaPlanilhaExcel()
   → Busca arquivos "video_info_*.xlsx"
   → Seleciona o mais recente
    ↓
2️⃣ enviarEmailComAnexo(email, arquivo)
   → Cria MimeMessage
   → Assunto: "Relatório de Informações do Vídeo"
   → Corpo: Informações do arquivo + timestamp
   → Anexa a planilha Excel
   → Envia via JavaMailSender
```

---

## ⚙️ **Fluxo de Dependências (Clean Architecture)**

```java
┌─────────────────────────────────────────────────────┐
│                  DEPENDENCY FLOW                    │
├─────────────────────────────────────────────────────┤
│                                                     │
│  BotJobScheduler                                    │
│       ↓ usa                                         │
│  YoutubeSearchUseCase (PORT/Interface)              │
│       ↓ implementado por                            │
│  YoutubeSearchUseCaseImpl                           │
│       ↓ usa                                         │
│  BrowserPort (PORT/Interface)                       │
│       ↓ implementado por                            │
│  BrowserService (Infrastructure/Selenium)           │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## 🕐 **Timeline de Execução**

```
⏰ T+0s:     Aplicação inicia
⏰ T+0s:     Chrome abre (@PostConstruct)
⏰ T+5s:     Automação inicia (@Scheduled)
⏰ T+5-15s:  YouTube busca + clique vídeo
⏰ T+15-20s: Coleta dados + gera TXT
⏰ T+20-22s: Converte TXT → Excel  
⏰ T+22-25s: Envia email com anexo
⏰ T+25s:    Chrome fecha + fim
```

---

## 🎯 **Pontos Importantes**

- **🔄 Execução única**: `fixedDelay = Long.MAX_VALUE`
- **⚡ Selenium Waits**: WebDriverWait com timeout 10s
- **📁 Gestão arquivos**: Sempre busca o mais recente
- **🛡️ Error Handling**: Try-catch em cada etapa
- **📧 Email configurado**: SMTP Gmail nas properties
- **🎨 Clean Architecture**: Ports & Adapters implementado

O fluxo é linear e sequencial, cada etapa depende do sucesso da anterior! 🚀

---

## 📁 **Estrutura de Arquivos Gerados**

### **Arquivos de Saída:**
- `video_info_2025-08-08_13-45-30.txt` - Dados extraídos do YouTube
- `video_info_2025-08-08_13-45-32.xlsx` - Planilha Excel formatada
- Email enviado para `gui.nobrega@hotmail.com` com anexo

### **Logs da Aplicação:**
- Informações detalhadas de cada etapa
- Tratamento de erros e exceções
- Status de sucesso/falha de cada operação

---

## 🔧 **Configurações Necessárias**

### **Banco de Dados (MySQL):**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_estudo
spring.datasource.username=root
spring.datasource.password=root
```

### **Email (Gmail SMTP):**
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=guilhermegumiero300@gmail.com
spring.mail.password=${EMAIL_PASSWORD}
```

### **Selenium WebDriver:**
- ChromeDriver automaticamente gerenciado pelo WebDriverManager
- Timeout padrão: 10 segundos para elementos
- Modo headless disponível para execução em servidor

---

## 🎯 **Casos de Uso Implementados**

1. **YouTube Search Use Case**
   - Busca automática por termo específico
   - Clique no primeiro resultado
   - Extração de metadados do vídeo

2. **TXT Para Excel Use Case**
   - Conversão automática de dados
   - Formatação em planilha Excel
   - Auto-dimensionamento de colunas

3. **Enviar Por Email Use Case**
   - Anexo automático da planilha
   - Template de email profissional
   - Confirmação de envio

Cada caso de uso é independente e pode ser executado separadamente através de suas interfaces (ports) definidas na camada de domínio.
