# Guia de Instalação e Execução

Este guia fornece instruções detalhadas para instalar, configurar e executar o OpenAI Integration Framework.

## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- Java 21 (JDK)
- Maven 3.8+
- PostgreSQL 14+
- Redis 6+ (opcional, para cache)
- HashiCorp Vault (opcional, para gerenciamento de secrets)

## Instalação

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/openai-integration-framework.git
cd openai-integration-framework
```

### 2. Compile o Projeto

```bash
mvn clean install
```

### 3. Configure o Banco de Dados

Crie um banco de dados PostgreSQL:

```sql
CREATE DATABASE openai_framework;
```

Ou para ambiente de desenvolvimento:

```sql
CREATE DATABASE openai_framework_dev;
```

### 4. Configure o Redis (Opcional)

Se você estiver usando Redis para cache, certifique-se de que o servidor Redis esteja em execução:

```bash
redis-server
```

### 5. Configure o Vault (Opcional)

Se você estiver usando HashiCorp Vault para gerenciamento de secrets:

```bash
# Inicie o servidor Vault em modo de desenvolvimento
vault server -dev

# Em outro terminal, configure as variáveis de ambiente
export VAULT_ADDR='http://127.0.0.1:8200'
export VAULT_TOKEN='seu-token'

# Adicione seus secrets ao Vault
vault kv put secret/application openai.api.key=sk-sua-chave-da-openai
vault kv put secret/application spring.datasource.password=sua-senha-do-banco
```

## Configuração

### Configuração Básica

Edite o arquivo `application.yaml` na pasta `application/src/main/resources` para configurar as propriedades básicas:

```yaml
spring:
  profiles:
    active: dev  # Use 'dev', 'prod' ou um perfil específico de cliente
```

### Configuração da API da OpenAI

Você pode configurar a API da OpenAI de duas maneiras:

1. **Via arquivo de configuração**:

   Edite o arquivo `application-dev.yaml` ou `application-prod.yaml`:

   ```yaml
   openai:
     api:
       url: https://api.openai.com/v1
       key: sk-sua-chave-da-openai
       model: gpt-4
       timeout: 30000
   ```

2. **Via variáveis de ambiente**:

   ```bash
   export OPENAI_API_URL=https://api.openai.com/v1
   export OPENAI_API_KEY=sk-sua-chave-da-openai
   export OPENAI_API_MODEL=gpt-4
   export OPENAI_API_TIMEOUT=30000
   ```

### Configuração do Contexto

O contexto padrão para interações com a API da OpenAI é definido no arquivo `default-context.yaml`. Você pode personalizar este arquivo ou criar novos arquivos de contexto para diferentes clientes.

Para usar um arquivo de contexto personalizado, configure a propriedade `openai.context.file-path`:

```yaml
openai:
  context:
    file-path: classpath:context/seu-contexto-personalizado.yaml
```

## Execução

### Executar com Maven

```bash
mvn spring-boot:run -pl application
```

Para usar um perfil específico:

```bash
mvn spring-boot:run -pl application -Dspring-boot.run.profiles=dev
```

### Executar o JAR

```bash
java -jar application/target/application-0.0.1-SNAPSHOT.jar
```

Para usar um perfil específico:

```bash
java -jar application/target/application-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

## Verificação da Instalação

Após iniciar a aplicação, verifique se ela está funcionando corretamente:

1. Acesse a documentação da API:
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. Verifique o status do serviço:
   ```
   curl http://localhost:8080/api/v1/health
   ```

3. Envie uma pergunta de teste:
   ```
   curl -X POST http://localhost:8080/api/v1/ask \
     -H "Content-Type: application/json" \
     -d '{"content":"Olá, como você está?"}'
   ```

## Solução de Problemas

### Problemas de Conexão com o Banco de Dados

Se você encontrar problemas de conexão com o banco de dados, verifique:

- Se o PostgreSQL está em execução
- Se as credenciais estão corretas
- Se o banco de dados foi criado

### Problemas com a API da OpenAI

Se você encontrar problemas com a API da OpenAI, verifique:

- Se a chave da API está correta
- Se o modelo especificado está disponível
- Se você tem créditos suficientes na sua conta da OpenAI

### Logs

Os logs da aplicação podem ser encontrados no console ou no arquivo de log, dependendo da configuração. Para aumentar o nível de log, adicione a seguinte configuração:

```yaml
logging:
  level:
    com.example: DEBUG
    org.springframework.web: DEBUG
```
