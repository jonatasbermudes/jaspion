# **Guia de Implementação: Back-end "AlertsFrom.tv"**

Este documento detalha a arquitetura e os componentes necessários para construir o back-end da aplicação "AlertsFrom.tv" usando Spring Boot (v2.3.12) e Java 11\.

## **1\. Visão Geral da Stack**

* **Framework:** Spring Boot 2.3.12 (com spring-web, spring-data-jpa, spring-security)  
* **Linguagem:** Java 11  
* **Banco de Dados:** PostgreSQL (Hospedado no Supabase)  
* **Autenticação:** Spring Security \+ JWT (JSON Web Tokens)  
* **API do Telegram:** java-telegram-bot-api  
* **Criptografia de Tokens:** jasypt-spring-boot-starter

## **2\. Dependências (Maven pom.xml)**

Você precisará adicionar estas dependências principais ao seu pom.xml:

\<\!-- ... outras dependências do spring ... \--\>

\<\!-- Para Endpoints REST e Servlets \--\>  
\<dependency\>  
    \<groupId\>org.springframework.boot\</groupId\>  
    \<artifactId\>spring-boot-starter-web\</artifactId\>  
\</dependency\>

\<\!-- Para persistência de dados com Supabase/Postgres \--\>  
\<dependency\>  
    \<groupId\>org.springframework.boot\</groupId\>  
    \<artifactId\>spring-boot-starter-data-jpa\</artifactId\>  
\</dependency\>  
\<dependency\>  
    \<groupId\>org.postgresql\</groupId\>  
    \<artifactId\>postgresql\</artifactId\>  
    \<scope\>runtime\</scope\>  
\</dependency\>

\<\!-- Para segurança (Autenticação e JWT) \--\>  
\<dependency\>  
    \<groupId\>org.springframework.boot\</groupId\>  
    \<artifactId\>spring-boot-starter-security\</artifactId\>  
\</dependency\>  
\<dependency\>  
    \<groupId\>io.jsonwebtoken\</groupId\>  
    \<artifactId\>jjwt\</artifactId\>  
    \<version\>0.9.1\</version\> \<\!-- Versão clássica compatível com Java 11 \--\>  
\</dependency\>  
\<dependency\>  
    \<groupId\>javax.xml.bind\</groupId\>  
    \<artifactId\>jaxb-api\</artifactId\>  
    \<version\>2.3.1\</version\> \<\!-- Necessário para JWT no Java 11+ \--\>  
\</dependency\>

\<\!-- Para criptografar o token do bot no DB \--\>  
\<dependency\>  
    \<groupId\>com.github.ulisesbocchio\</groupId\>  
    \<artifactId\>jasypt-spring-boot-starter\</artifactId\>  
    \<version\>3.0.3\</version\> \<\!-- Verifique a versão compatível com SB 2.3 \--\>  
\</dependency\>

\<\!-- Para integração com a API do Telegram \--\>  
\<dependency\>  
    \<groupId\>org.telegram\</groupId\>  
    \<artifactId\>telegrambots\</artifactId\>  
    \<version\>5.7.1\</version\> \<\!-- Verifique uma versão compatível com Java 11 \--\>  
\</dependency\>

\<\!-- Utilitário (Opcional, mas recomendado) \--\>  
\<dependency\>  
    \<groupId\>org.projectlombok\</groupId\>  
    \<artifactId\>lombok\</artifactId\>  
    \<optional\>true\</optional\>  
\</dependency\>

## **3\. Configuração (application.properties)**

Você precisará configurar a conexão com o Supabase e as chaves de segurança.

\# \--- Conexão com Supabase (PostgreSQL) \---  
\# Você encontra essa URL no painel do Supabase  
spring.datasource.url=jdbc:postgresql://\[HOST\_DO\_SUPABASE\]:5432/postgres  
spring.datasource.username=postgres  
spring.datasource.password=\[SUA\_SENHA\_DO\_SUPABASE\]  
spring.datasource.driver-class-name=org.postgresql.Driver

\# \--- Configuração do JPA \---  
\# "validate" verifica se as tabelas no DB correspondem às entidades.  
\# Não use "create" ou "update" em produção.  
spring.jpa.hibernate.ddl-auto=validate  
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

\# \--- Configuração do JWT \---  
\# Use um segredo forte e guarde-o em variáveis de ambiente (ex: no Heroku)  
jwt.secret=SEGREDO\_MUITO\_FORTE\_PARA\_ASSINAR\_O\_TOKEN  
jwt.expiration.ms=86400000 \# 24 horas

\# \--- Configuração do Jasypt (Criptografia de Tokens) \---  
\# Use uma senha forte e guarde-a em variáveis de ambiente  
jasypt.encryptor.password=SENHA\_FORTE\_PARA\_CRIPTOGRAFAR\_TOKENS\_NO\_DB

## **4\. Estrutura do Projeto (Pacotes)**

Recomenda-se organizar o projeto da seguinte forma:

com.alertsfromtv  
├── config/             \# Configurações (SecurityConfig, JasyptConfig)  
├── controller/         \# @RestControllers (APIs)  
├── dto/                \# Data Transfer Objects (LoginRequest, BotDTO, etc.)  
├── entity/             \# @Entities (Classes JPA que mapeiam o DB)  
├── exception/          \# Handlers de exceção (ResourceNotFoundException)  
├── repository/         \# Interfaces Spring Data (ex: UsuarioRepository)  
├── security/           \# Classes de segurança (JwtUtil, JwtRequestFilter)  
├── service/            \# Lógica de negócios (WebhookService, UsuarioService)  
└── util/               \# Classes utilitárias (ex: TokenCriptografiaConverter)

## **5\. Entidades JPA (/entity)**

Estas classes mapeiam o script SQL que criamos. Note o uso de javax.persistence.\* (padrão do Spring Boot 2.3).

**Usuario.java**

@Data  
@Entity  
@Table(name \= "usuarios")  
public class Usuario {  
    @Id  
    @GeneratedValue(strategy \= GenerationType.IDENTITY)  
    private Long id;

    @Column(unique \= true, nullable \= false)  
    private String email;

    @Column(nullable \= false)  
    private String senha; // Hash BCrypt

    // Relacionamentos (Opcional, mas útil para cascade)  
}

**BotTelegram.java**

@Data  
@Entity  
@Table(name \= "bots\_telegram")  
public class BotTelegram {  
    @Id  
    @GeneratedValue(strategy \= GenerationType.IDENTITY)  
    private Long id;

    @Column(nullable \= false)  
    private String nome;

    @Column(length \= 1024, nullable \= false)  
    @Convert(converter \= TokenCriptografiaConverter.class) // Veja Seção 7  
    private String tokenApi; // Token criptografado

    @ManyToOne(fetch \= FetchType.LAZY)  
    @JoinColumn(name \= "usuario\_id", nullable \= false)  
    private Usuario usuario;  
}

**DestinoTelegram.java**

@Data  
@Entity  
@Table(name \= "destinos\_telegram")  
public class DestinoTelegram {  
    // ... id, nome, chatId ...  
    @ManyToOne(fetch \= FetchType.LAZY)  
    @JoinColumn(name \= "usuario\_id", nullable \= false)  
    private Usuario usuario;  
}

**ConexaoWebhook.java**

@Data  
@Entity  
@Table(name \= "conexoes\_webhook")  
public class ConexaoWebhook {  
    @Id  
    @GeneratedValue(strategy \= GenerationType.AUTO) // UUID  
    private UUID id;

    @Column(nullable \= false)  
    private String nome;  
      
    private boolean ativo \= true;  
    private String templateMensagem;

    @ManyToOne(fetch \= FetchType.LAZY)  
    @JoinColumn(name \= "usuario\_id", nullable \= false)  
    private Usuario usuario;

    @ManyToOne(fetch \= FetchType.LAZY)  
    @JoinColumn(name \= "bot\_id", nullable \= false)  
    private BotTelegram bot;

    @ManyToOne(fetch \= FetchType.LAZY)  
    @JoinColumn(name \= "destino\_id", nullable \= false)  
    private DestinoTelegram destino;  
}

**LogExecucao.java**

@Data  
@Entity  
@Table(name \= "logs\_execucao")  
public class LogExecucao {  
    @Id  
    @GeneratedValue(strategy \= GenerationType.IDENTITY)  
    private Long id;

    private Instant timestamp;  
    private boolean sucesso;

    @Lob  
    private String bodyRecebido;  
      
    @Lob  
    private String respostaTelegram;

    @ManyToOne(fetch \= FetchType.LAZY)  
    @JoinColumn(name \= "conexao\_id", nullable \= false)  
    private ConexaoWebhook conexao;  
}

## **6\. Repositórios Spring Data (/repository)**

Crie interfaces que estendem JpaRepository para cada entidade. O Spring Data criará as queries automaticamente.

public interface UsuarioRepository extends JpaRepository\<Usuario, Long\> {  
    Optional\<Usuario\> findByEmail(String email);  
}

public interface BotTelegramRepository extends JpaRepository\<BotTelegram, Long\> {  
    List\<BotTelegram\> findByUsuarioId(Long usuarioId);  
}

public interface DestinoTelegramRepository extends JpaRepository\<DestinoTelegram, Long\> {  
    List\<DestinoTelegram\> findByUsuarioId(Long usuarioId);  
}

public interface ConexaoWebhookRepository extends JpaRepository\<ConexaoWebhook, UUID\> {  
    List\<ConexaoWebhook\> findByUsuarioId(Long usuarioId);  
}

public interface LogExecucaoRepository extends JpaRepository\<LogExecucao, Long\> {  
    List\<LogExecucao\> findByConexaoIdOrderByTimestampDesc(UUID conexaoId, Pageable pageable);  
}

## **7\. Criptografia do Token (/util)**

Esta é uma etapa de segurança **CRUCIAL**. Crie um AttributeConverter para que o token\_api seja salvo criptografado no banco de dados automaticamente.

**TokenCriptografiaConverter.java**

@Converter  
public class TokenCriptografiaConverter implements AttributeConverter\<String, String\> {

    // Injete o "StringEncryptor" do Jasypt  
    private final StringEncryptor encryptor;

    @Autowired  
    public TokenCriptografiaConverter(StringEncryptor encryptor) {  
        this.encryptor \= encryptor;  
    }

    @Override  
    public String convertToDatabaseColumn(String attribute) {  
        // Criptografa o token ANTES de salvar no DB  
        try {  
            return encryptor.encrypt(attribute);  
        } catch (Exception e) {  
            throw new RuntimeException("Erro ao criptografar token", e);  
        }  
    }

    @Override  
    public String convertToEntityAttribute(String dbData) {  
        // Descriptografa o token DEPOIS de ler do DB  
        try {  
            return encryptor.decrypt(dbData);  
        } catch (Exception e) {  
            throw new RuntimeException("Erro ao descriptografar token", e);  
        }  
    }  
}

*Não se esqueça de adicionar @EnableEncryptableProperties na sua classe Application.*

## **8\. Segurança (Spring Security \+ JWT)**

Esta é a parte mais complexa. Você precisará de 4 componentes principais (no pacote /security):

1. **MyUserDetailsService.java**: Implementa UserDetailsService. Tem um método loadUserByUsername(String email) que busca o Usuario no UsuarioRepository e o converte para um UserDetails do Spring.  
2. **JwtUtil.java**: Classe utilitária com métodos para:  
   * generateToken(UserDetails userDetails)  
   * validateToken(String token, UserDetails userDetails)  
   * extractUsername(String token)  
3. **JwtRequestFilter.java**: Estende OncePerRequestFilter. Intercepta **todas** as requisições.  
   * Pega o cabeçalho Authorization: Bearer ...  
   * Extrai e valida o token.  
   * Se o token for válido, busca o UserDetails e o coloca no SecurityContextHolder.  
4. **SecurityConfig.java**: Estende WebSecurityConfigurerAdapter (padrão do Spring Boot 2.3).  
   * Configura o PasswordEncoder (use BCryptPasswordEncoder).  
   * Configura o AuthenticationManager.  
   * No método configure(HttpSecurity http), você define as regras:  
     * Desabilitar CSRF (.csrf().disable())  
     * Tornar a sessão STATELESS (essencial para API/JWT).  
     * Permitir acesso público (.permitAll()) aos endpoints:  
       * /auth/\*\* (login e registro)  
       * /webhook/\*\* (O endpoint público do TradingView\!)  
     * Exigir autenticação (.authenticated()) para todos os outros (/api/\*\*).  
     * Adicionar o JwtRequestFilter **antes** do filtro padrão de usuário/senha.

## **9\. Design da API (/controller)**

Defina seus endpoints REST.

### **AuthController.java**

* POST /auth/register: Recebe DTO de registro, salva o usuário (com senha em hash).  
* POST /auth/login: Recebe DTO de login, autentica o usuário, retorna um DTO com o JWT.

### **BotController.java (Protegido \- /api/bots)**

* GET /: Lista todos os bots do usuário autenticado.  
* GET /{id}: Busca um bot.  
* POST /: Cria um novo bot.  
* PUT /{id}: Atualiza um bot.  
* DELETE /{id}: Deleta um bot.

### **DestinoController.java (Protegido \- /api/destinos)**

* (Similar ao BotController: GET, POST, PUT, DELETE)

### **ConexaoController.java (Protegido \- /api/conexoes)**

* GET /: Lista todas as conexões do usuário.  
* POST /: Cria uma nova conexão (associando um Bot e um Destino).  
* PUT /{id}: Atualiza uma conexão (ex: trocar template, (des)ativar).  
* DELETE /{id}: Deleta uma conexão.

### **LogController.java (Protegido \- /api/logs)**

* GET /conexao/{conexaoId}: Busca os últimos 20 logs de uma conexão (use Pageable).

### **WebhookController.java (Público \- /webhook)**

Este é o endpoint mais importante.

* POST /{conexaoId}  
  * Recebe o UUID pela URL (@PathVariable).  
  * Recebe o alerta do TradingView (@RequestBody String body).  
  * Este endpoint **NÃO PODE** ter lógica pesada. Ele deve apenas:  
    1. Validar o conexaoId.  
    2. Salvar o body e o conexaoId (talvez em uma fila/tópico, ou chamar um @Async service).  
    3. Retornar 200 OK imediatamente para o TradingView.

## **10\. Lógica de Negócios (/service)**

### **WebhookService.java**

Este serviço fará o trabalho pesado (possivelmente de forma assíncrona \- @Async).

**Função processarWebhook(UUID conexaoId, String body):**

1. **Buscar Conexão:** conexaoRepository.findById(conexaoId). Se não achar, loga o erro e encerra (não precisa salvar log no DB).  
2. **Verificar Status:** Se conexao.isAtivo() for false, encerra.  
3. **Buscar Entidades:** Pega conexao.getBot() e conexao.getDestino().  
4. **Criar Log (Início):** Cria um LogExecucao (novo). Seta conexaoId, timestamp, bodyRecebido.  
5. **Tentar Enviar:**  
   * Inicia um bloco try-catch.  
   * String token \= conexao.getBot().getTokenApi(); (O token já virá descriptografado pelo AttributeConverter).  
   * String chatId \= conexao.getDestino().getChatId();  
   * **Lógica do Template (V2):** Por enquanto, String mensagem \= body;.  
   * **API Telegram:**  
     * Instancie o bot (ex: new TelegramBotsApi(DefaultBotSession.class)).  
     * Crie o objeto SendMessage.  
     * Sete chatId e text (mensagem).  
     * Execute o envio.  
   * **Sucesso:** Seta log.setSucesso(true) e log.setRespostaTelegram("OK").  
6. **catch (Exception e):**  
   * **Falha:** Seta log.setSucesso(false) e log.setRespostaTelegram(e.getMessage()).  
7. **finally:**  
   * logExecucaoRepository.save(log); (Salva o log no DB, com sucesso ou falha).

## **11\. Implantação (Heroku)**

1. **Java Version:** Como seu projeto base é Java 11, certifique-se que seu system.properties no Heroku está como java.runtime.version=11.  
2. **Procfile:** Crie um arquivo Procfile na raiz do projeto:  
   web: java \-jar target/seu-arquivo-SNAPSHOT.jar

3. **Variáveis de Ambiente (Config Vars):** No painel do Heroku, configure as variáveis de ambiente que definimos no application.properties:  
   * SPRING\_DATASOURCE\_URL  
   * SPRING\_DATASOURCE\_USERNAME  
   * SPRING\_DATASOURCE\_PASSWORD  
   * JWT\_SECRET  
   * JASYPT\_ENCRYPTOR\_PASSWORD
