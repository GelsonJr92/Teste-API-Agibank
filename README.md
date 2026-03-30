# Agibank - Dog API Automation Tests

![Java](https://img.shields.io/badge/Java-17-orange)
![Maven](https://img.shields.io/badge/Maven-3.9+-blue)
![RestAssured](https://img.shields.io/badge/RestAssured-5.4.0-green)
![JUnit](https://img.shields.io/badge/JUnit-5.10.1-red)
![Allure](https://img.shields.io/badge/Allure-2.25.0-yellow)

## Sobre o Projeto

Este projeto foi desenvolvido como parte do teste técnico para o **Agibank**, implementando uma suite completa de automação de testes para a **Dog CEO API** (https://dog.ceo/api).

## Objetivos

- Automatizar testes para os principais endpoints da Dog API
- Implementar cobertura de testes abrangente e robusta
- Seguir padrões de qualidade e boas práticas
- Gerar relatórios detalhados e visuais
- Configurar pipeline de CI/CD
- Documentar o projeto de forma clara e completa

## Stack Tecnológica

### Core Technologies
- **Java 17**: Linguagem de programação principal
- **Maven 3.9+**: Gerenciamento de dependências e build
- **RestAssured 5.4.0**: Framework para testes de API REST
- **JUnit 5.10.1**: Framework de testes unitários
- **Allure Reports 2.25.0**: Geração de relatórios HTML visuais

### Ferramentas Auxiliares
- **Lombok 1.18.30**: Redução de boilerplate code
- **Jackson 2.16.1**: Serialização/deserialização JSON
- **GitHub Actions**: Pipeline de CI/CD
- **Maven Surefire**: Execução dos testes
- **Maven AntRun**: Automação de tarefas

## Arquitetura do Projeto

```
src/
└── test/java/com/agibank/
    ├── config/
    │   └── ConfiguracaoApi.java          # Configurações da API
    ├── models/
    │   ├── RespostaListaRacas.java       # Modelo para lista de raças
    │   ├── RespostaImagensRaca.java      # Modelo para imagens por raça
    │   └── RespostaImagemAleatoria.java  # Modelo para imagem aleatória
    ├── services/
    │   └── ServicoDogApi.java            # Service Object Pattern
    └── tests/
        ├── TesteBase.java                # Classe base para testes
        ├── TesteListagemRacas.java       # Testes de listagem de raças
        ├── TesteImagensPorRaca.java      # Testes de imagens por raça
        ├── TesteImagensAleatorias.java   # Testes de imagens aleatórias
        └── TesteIntegracao.java          # Testes de integração
```

### Padrões Implementados

1. **Service Object Pattern**: Encapsulamento da lógica de negócio nos services
2. **Builder Pattern**: Construção fluente de requisições com RestAssured
3. **Data Transfer Objects (DTOs)**: Modelos para mapeamento de respostas JSON

## Cobertura de Testes

### Endpoints Testados

| Endpoint | Método | Descrição | Testes |
|----------|--------|-----------|---------|
| `/breeds/list/all` | GET | Lista todas as raças | 8 testes |
| `/breed/{breed}/images` | GET | Imagens por raça específica | 6 testes |
| `/breeds/image/random` | GET | Imagem aleatória geral | 10 testes |
| **Integração** | - | Testes end-to-end | 6 testes |

### Total: **30 testes automatizados**

### Cenários de Teste

#### Listagem de Raças (`TesteListagemRacas`)
- Busca completa de raças com sucesso
- Validação da estrutura JSON da resposta
- Verificação de tipos de dados e campos obrigatórios
- Validação de sub-raças quando disponíveis
- Teste de performance (tempo de resposta)
- Validação de headers HTTP
- Verificação de idempotência
- Formato dos nomes das raças

#### Imagens por Raça (`TesteImagensPorRaca`)
- Busca de imagens por raça específica
- Validação da estrutura da resposta JSON
- Verificação de URLs válidas das imagens
- Testes com diferentes raças válidas
- Tratamento de raças inexistentes
- Testes de performance

#### Imagens Aleatórias (`TesteImagensAleatorias`)
- Busca de imagem aleatória geral
- Validação da estrutura da resposta
- Verificação de URLs válidas
- Teste de aleatoriedade (múltiplas chamadas)
- Busca de múltiplas imagens aleatórias
- Imagens aleatórias por raça específica
- Testes de performance e headers
- Validação de limites e parâmetros

#### Integração (`TesteIntegracao`)
- Fluxo completo: buscar raças → selecionar → buscar imagens
- Validação de diferentes tipos de imagens aleatórias
- Consistência entre endpoints
- Teste de robustez com diferentes cenários
- Estabilidade da API
- Validação de tipos de conteúdo

## Como Executar

### Pré-requisitos

```bash
# Java 17 ou superior
java -version

# Maven 3.9 ou superior
mvn -version
```

### Executando os Testes

```bash
# Executar todos os testes
mvn clean test

# Executar classe específica
mvn test -Dtest=TesteListagemRacas

# Executar teste específico
mvn test -Dtest=TesteListagemRacas#deveRetornarListaCompletaDeRacasComSucesso

# Executar com debug
mvn test -X
```

### Executando por Tags

```bash
# Executar suite completa (runner único)
mvn test -Dtest=SuiteTestesCompleta

# Executar apenas testes críticos (smoke)
mvn test -Dgroups="smoke"

# Executar apenas testes de performance
mvn test -Dgroups="performance"

# Executar apenas testes negativos (cenários de erro)
mvn test -Dgroups="negativo"

# Executar por feature
mvn test -Dgroups="listagem-racas"
mvn test -Dgroups="imagens-por-raca"
mvn test -Dgroups="imagens-aleatorias"
mvn test -Dgroups="integracao"

# Combinar tags (OR)
mvn test -Dgroups="smoke | integracao"
```

### Tags Disponíveis

| Tag | Tipo | Descrição | Testes |
|-----|------|-----------|--------|
| `smoke` | Cenário | Testes críticos — caminho feliz principal de cada endpoint | 4 |
| `positivo` | Cenário | Cenário principal de sucesso (mesmo testes que smoke) | 4 |
| `negativo` | Cenário | Cenários de erro e entrada inválida | 3 |
| `contrato` | Cenário | Validação da estrutura e formato do JSON retornado | 6 |
| `dados` | Cenário | Validação do conteúdo e dados específicos retornados | 6 |
| `url` | Cenário | Validação do formato e conteúdo das URLs das imagens | 2 |
| `performance` | Cenário | Testes de tempo de resposta | 4 |
| `headers` | Cenário | Validação dos headers HTTP da resposta | 2 |
| `consistencia` | Cenário | Verificação de consistência entre chamadas ou endpoints | 2 |
| `aleatoriedade` | Cenário | Verificação do comportamento aleatório da API | 1 |
| `limite` | Cenário | Testes de comportamento em valores-limite (boundary) | 1 |
| `comparacao` | Cenário | Comparação de comportamento entre endpoints distintos | 1 |
| `robustez` | Cenário | Estabilidade da API em diferentes cenários consecutivos | 1 |
| `listagem-racas` | Feature | Todos os testes de listagem de raças | 8 |
| `imagens-por-raca` | Feature | Todos os testes de imagens por raça | 6 |
| `imagens-aleatorias` | Feature | Todos os testes de imagens aleatórias | 10 |
| `integracao` | Feature | Todos os testes de integração | 6 |

### Visualizando Relatórios

```bash
# Gerar e abrir relatório Allure
mvn allure:serve

# Apenas gerar relatório
mvn allure:report

# Localização do relatório
target/allure-results/allure-report/index.html
```

## Relatórios e Evidências

### Allure Reports
O projeto gera relatórios HTML interativos com:

- **Dashboard**: Visão geral dos resultados
- **Suites**: Organização por features e stories
- **Gráficos**: Distribuição de resultados e trends
- **Timeline**: Linha temporal da execução
- **Categories**: Categorização de falhas
- **Environment**: Informações do ambiente de teste

### Configurações do Allure
- Épicos, Features e Stories organizados por funcionalidade
- Steps detalhados para cada ação
- Categorização de severidade (Critical, Normal, Minor)
- Descrições detalhadas para cada teste

## CI/CD Pipeline

### GitHub Actions
```yaml
# Workflow configurado para:
- Execução automática em push/PR
- Cache de dependências Maven
- Geração automática de relatórios
- Deploy do relatório no GitHub Pages
```

### Triggers
- **Push** para branch main/develop
- **Pull Requests**
- **Manual** via workflow dispatch

## Configurações

### Environment Properties
```properties
# src/test/resources/environment.properties
java.version=17
test.framework=JUnit 5
api.framework=RestAssured
report.framework=Allure Reports
build.tool=Maven
execution.date=${maven.build.timestamp}
project.version=1.0.0
```

### Configurações Maven
- **Java Target**: 17
- **Encoding**: UTF-8
- **Allure Integration**: Automática

## Troubleshooting

### Problemas Comuns

1. **Erro de conexão com a API**:
```bash
# Verificar conectividade
curl https://dog.ceo/api/breeds/list/all
```

2. **Falhas de timeout**:
```java
// Aumentar timeout no RestAssured
given().timeout(Duration.ofSeconds(30))
```

3. **Problemas com Java/Maven**:
```bash
# Limpar cache Maven
mvn dependency:purge-local-repository

# Recompilar
mvn clean compile
```

## Suporte

Para dúvidas ou problemas:

1. **Verificar logs**: `target/surefire-reports/`
2. **Consultar relatório Allure**: Detalhes completos das execuções
3. **Validar ambiente**: Java 17+ e Maven 3.9+
4. **Revisar configurações**: Verificar `ConfiguracaoApi.java`

## Resultados

- **30 testes** implementados e funcionais
- **100%** de cobertura dos endpoints solicitados
- **Relatórios Allure** com evidências visuais
- **CI/CD Pipeline** configurado
- **Documentação completa** e profissional
- **Boas práticas** aplicadas

*Desenvolvido para Agibank*
