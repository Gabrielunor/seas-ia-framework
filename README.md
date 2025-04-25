# OpenAI Integration Framework

Framework reutilizável e configurável para integração com a API da OpenAI, desenvolvido com Java 21 e Spring Boot 3.4.4.

## Visão Geral

Este framework foi projetado para facilitar a integração com a API da OpenAI, permitindo que múltiplos clientes utilizem a mesma base de código com configurações personalizadas. O projeto segue os princípios de Domain-Driven Design (DDD) e está estruturado de forma modular para facilitar a manutenção e extensão.

## Estrutura do Projeto

O projeto está organizado nos seguintes módulos:

- **core**: Contém as classes de domínio e interfaces de serviço
- **infra**: Implementa a integração com serviços externos (OpenAI, banco de dados, etc.)
- **api**: Expõe os endpoints REST para interação com o framework
- **config**: Gerencia as configurações da aplicação
- **application**: Módulo principal que integra todos os outros módulos

## Funcionalidades

- Integração com a API da OpenAI via WebClient
- Suporte a contexto padrão carregado de arquivo externo YAML
- Endpoints REST para envio de perguntas e gerenciamento de conversas
- Cache com Redis para reduzir chamadas duplicadas à API
- Persistência de dados com PostgreSQL
- Suporte ao HashiCorp Vault para armazenamento seguro de secrets
- Configuração externalizada via application.yaml e variáveis de ambiente
- Suporte a Spring Profiles para diferentes ambientes (dev, prod, client-x)
- Documentação da API com Swagger/OpenAPI

## Requisitos

- Java 21
- Maven
- PostgreSQL
- Redis (opcional, para cache)
- HashiCorp Vault (opcional, para gerenciamento de secrets)

## Como Usar

### Configuração

1. Clone o repositório
2. Configure as propriedades no arquivo `application.yaml` ou use variáveis de ambiente
3. Execute a aplicação

### Exemplos de Configuração

O framework inclui exemplos de configuração para diferentes ambientes:

- `application-dev.yaml`: Configuração para ambiente de desenvolvimento
- `application-prod.yaml`: Configuração para ambiente de produção
- `application-client-a.yaml`: Exemplo de configuração específica para um cliente

### Contexto Personalizado

O contexto padrão para interações com a API da OpenAI pode ser personalizado através de arquivos YAML:

- `default-context.yaml`: Contexto padrão para todas as conversas
- `client-a-context.yaml`: Exemplo de contexto personalizado para um cliente específico

## Endpoints da API

### Enviar Pergunta

```
POST /api/v1/ask
```

Corpo da requisição:
```json
{
  "content": "Qual é a capital do Brasil?"
}
```

### Criar Conversa

```
POST /api/v1/conversations?clientId=client-a
```

### Enviar Mensagem em uma Conversa

```
POST /api/v1/conversations/{conversationId}/messages
```

Corpo da requisição:
```json
{
  "content": "Qual é a capital do Brasil?"
}
```

### Verificar Status do Serviço

```
GET /api/v1/health
```

## Documentação da API

A documentação completa da API está disponível através do Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

## Extensão do Framework

O framework foi projetado para ser facilmente extensível:

1. Para adicionar novos endpoints, crie novos controllers no módulo `api`
2. Para adicionar novas funcionalidades de domínio, estenda as interfaces e classes no módulo `core`
3. Para integrar com outros serviços externos, adicione implementações no módulo `infra`
4. Para personalizar a configuração, modifique os arquivos YAML ou crie novos perfis

## Segurança

O framework suporta o armazenamento seguro de secrets usando HashiCorp Vault:

- Chaves da API da OpenAI
- Credenciais de banco de dados
- Outras informações sensíveis

Para habilitar o Vault, configure as propriedades `spring.cloud.vault` no arquivo de configuração ou através de variáveis de ambiente.
