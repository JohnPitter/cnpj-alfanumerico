<div align="center">

# CNPJ Alfanumérico

**Validação, formatação e geração de CNPJ alfanumérico — uma dependência, tudo pronto.**

[![Java](https://img.shields.io/badge/Java-8+-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.joaop/cnpj-alfanumerico?style=flat-square&color=blue)](https://central.sonatype.com/artifact/io.github.joaop/cnpj-alfanumerico)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue?style=flat-square)](LICENSE)
[![Build](https://img.shields.io/github/actions/workflow/status/JohnPitter/cnpj-alfanumerico/ci.yml?style=flat-square)](https://github.com/JohnPitter/cnpj-alfanumerico/actions)

[Funcionalidades](#-funcionalidades) · [Quick Start](#-quick-start) · [API](#-api-reference) · [Migração](#-guia-de-migração) · [Compatibilidade](#-compatibilidade)

</div>

---

## O que é?

Biblioteca Java **zero dependências** para trabalhar com o novo CNPJ alfanumérico da Receita Federal do Brasil ([IN RFB nº 2.229/2024](http://normas.receita.fazenda.gov.br/sijut2consulta/link.action?idAto=141102)).

A partir de **julho de 2026**, novos CNPJs poderão conter letras (A-Z) nas 12 primeiras posições. Esta biblioteca implementa o algoritmo oficial de validação (Módulo 11 com conversão ASCII-48) e é **100% retrocompatível** com CNPJs numéricos existentes.

Adicione ao seu projeto e substitua sua validação atual — funciona tanto com CNPJs legados quanto com o novo formato.

---

## ✨ Funcionalidades

| Categoria | O que você ganha |
|---|---|
| **Validação** | Valida CNPJ numérico (legado) e alfanumérico (novo) com algoritmo oficial Módulo 11 |
| **Formatação** | Formata (`12ABC34501DE35` → `12.ABC.345/01DE-35`) e desformata |
| **Cálculo de DVs** | Calcula dígitos verificadores a partir de 12 caracteres base |
| **Classificação** | Detecta se é numérico ou alfanumérico, retorna tipo via enum |
| **Extração** | Decompõe em raiz (8), ordem (4) e DVs (2); verifica se é matriz |
| **Geração** | Gera CNPJs válidos aleatórios para testes (alfanumérico e numérico) |
| **Regex Patterns** | Patterns compilados para validação em formulários e schemas |
| **Zero Dependências** | Apenas Java puro — sem libs externas em runtime |

---

## 🏗️ Arquitetura

```mermaid
graph TB
    subgraph Lib["cnpj-alfanumerico"]
        FACADE["CNPJ<br/>(Fachada)"]

        FACADE --> VALIDATOR["CNPJValidator<br/>Validação + Cálculo DVs"]
        FACADE --> FORMATTER["CNPJFormatter<br/>Format / Unformat"]
        FACADE --> GENERATOR["CNPJGenerator<br/>Geração para testes"]
        FACADE --> PARTS["CNPJParts<br/>Decomposição"]
        FACADE --> TYPE["CNPJType<br/>NUMERIC | ALPHANUMERIC"]
    end

    APP["Sua Aplicação"] --> FACADE

    style FACADE fill:#F97316,color:#fff,stroke:none
    style VALIDATOR fill:#3B82F6,color:#fff,stroke:none
    style FORMATTER fill:#8B5CF6,color:#fff,stroke:none
    style GENERATOR fill:#10B981,color:#fff,stroke:none
    style PARTS fill:#6366F1,color:#fff,stroke:none
    style TYPE fill:#EC4899,color:#fff,stroke:none
```

### Como funciona o algoritmo

```mermaid
sequenceDiagram
    participant App as Aplicação
    participant V as CNPJValidator

    App->>V: isValid("12.ABC.345/01DE-35")
    V->>V: Sanitiza → "12ABC34501DE35"
    V->>V: Regex check [A-Z0-9]{12}[0-9]{2}
    V->>V: Rejeita se todos iguais
    V->>V: Extrai base 12: "12ABC34501DE"

    Note over V: Conversão ASCII-48<br/>1→1, 2→2, A→17, B→18...

    V->>V: DV1 = Σ(valor × peso) mod 11
    V->>V: DV2 = Σ(valor × peso + DV1) mod 11
    V->>V: Compara DVs calculados vs recebidos
    V-->>App: true ✓
```

---

## 🚀 Quick Start

### 1. Adicione a dependência

**Maven:**

```xml
<dependency>
    <groupId>io.github.joaop</groupId>
    <artifactId>cnpj-alfanumerico</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Gradle:**

```groovy
implementation 'io.github.joaop:cnpj-alfanumerico:1.0.0'
```

**Gradle (Kotlin DSL):**

```kotlin
implementation("io.github.joaop:cnpj-alfanumerico:1.0.0")
```

### 2. Use

```java
import io.github.joaop.cnpj.CNPJ;

// Validação — funciona com numérico e alfanumérico
CNPJ.isValid("12.ABC.345/01DE-35");  // true
CNPJ.isValid("11.222.333/0001-81");  // true (legado)
CNPJ.isValid("12.ABC.345/01DE-99");  // false

// Validação com exceção
CNPJ.validate("12ABC34501DE35");  // OK
CNPJ.validate("INVALIDO");        // → CNPJException

// Formatação
CNPJ.format("12ABC34501DE35");     // "12.ABC.345/01DE-35"
CNPJ.unformat("12.ABC.345/01DE-35"); // "12ABC34501DE35"

// Classificação
CNPJ.isAlphanumeric("12ABC34501DE35");  // true
CNPJ.isNumeric("11222333000181");       // true
CNPJ.getType("12ABC34501DE35");         // CNPJType.ALPHANUMERIC

// Extração de partes
CNPJ.getRoot("12ABC34501DE35");         // "12ABC345"
CNPJ.getBranch("12ABC34501DE35");       // "01DE"
CNPJ.getCheckDigits("12ABC34501DE35");  // "35"
CNPJ.isHeadquarters("11222333000181");  // true

// Geração para testes
CNPJ.generate();                          // CNPJ alfanumérico aleatório
CNPJ.generateNumeric();                   // CNPJ numérico aleatório
CNPJ.generateFromParts("12ABC345", "0001"); // com raiz específica
CNPJ.generateHeadquarters("12ABC345");   // matriz

// Cálculo de dígitos verificadores
CNPJ.calculateCheckDigits("12ABC34501DE"); // "35"
```

**Pronto.** Sem configuração, sem dependências externas.

---

## 📖 API Reference

### Validação

| Método | Retorno | Descrição |
|---|---|---|
| `CNPJ.isValid(String)` | `boolean` | Valida CNPJ (com ou sem formatação) |
| `CNPJ.validate(String)` | `void` | Valida e lança `CNPJException` se inválido |
| `CNPJ.calculateCheckDigits(String)` | `String` | Calcula os 2 DVs a partir de 12 caracteres base |

### Formatação

| Método | Retorno | Descrição |
|---|---|---|
| `CNPJ.format(String)` | `String` | Formata: `XX.XXX.XXX/XXXX-XX` |
| `CNPJ.unformat(String)` | `String` | Remove máscara (14 chars, uppercase) |
| `CNPJ.isFormatted(String)` | `boolean` | Verifica se está com máscara |
| `CNPJ.isUnformatted(String)` | `boolean` | Verifica se está sem máscara |

### Classificação

| Método | Retorno | Descrição |
|---|---|---|
| `CNPJ.isAlphanumeric(String)` | `boolean` | Contém letras (novo formato) |
| `CNPJ.isNumeric(String)` | `boolean` | Apenas números (legado) |
| `CNPJ.getType(String)` | `CNPJType` | `NUMERIC` ou `ALPHANUMERIC` |

### Extração de Partes

| Método | Retorno | Descrição |
|---|---|---|
| `CNPJ.parse(String)` | `CNPJParts` | Decompõe em root + branch + checkDigits |
| `CNPJ.getRoot(String)` | `String` | Raiz — 8 primeiros caracteres |
| `CNPJ.getBranch(String)` | `String` | Ordem do estabelecimento (4 chars) |
| `CNPJ.getCheckDigits(String)` | `String` | Dígitos verificadores (2 chars) |
| `CNPJ.isHeadquarters(String)` | `boolean` | `true` se branch == "0001" (matriz) |

### Geração (para testes)

| Método | Retorno | Descrição |
|---|---|---|
| `CNPJ.generate()` | `String` | CNPJ alfanumérico aleatório válido |
| `CNPJ.generateFormatted()` | `String` | Alfanumérico aleatório formatado |
| `CNPJ.generateNumeric()` | `String` | CNPJ numérico aleatório válido |
| `CNPJ.generateNumericFormatted()` | `String` | Numérico aleatório formatado |
| `CNPJ.generateFromParts(root, branch)` | `String` | Com raiz e ordem específicas |
| `CNPJ.generateFromBase(base12)` | `String` | A partir dos 12 chars base |
| `CNPJ.generateHeadquarters(root)` | `String` | Matriz (branch=0001) |

### Regex Patterns

| Método | Pattern | Descrição |
|---|---|---|
| `CNPJ.getPattern()` | `[A-Z0-9]{12}[0-9]{2}` | CNPJ sem formatação |
| `CNPJ.getFormattedPattern()` | `XX.XXX.XXX/XXXX-XX` | CNPJ com formatação |
| `CNPJ.getLegacyPattern()` | `[0-9]{14}` | CNPJ numérico (legado) |
| `CNPJ.getLegacyFormattedPattern()` | `XX.XXX.XXX/XXXX-XX` | Numérico formatado |

---

## 🔄 Guia de Migração

### Para sistemas que já validam CNPJ numérico

**Antes (validação numérica):**
```java
// ❌ Vai quebrar com CNPJs alfanuméricos
boolean valid = cnpj.matches("\\d{14}");
```

**Depois (com a biblioteca):**
```java
// ✅ Funciona com numérico E alfanumérico
boolean valid = CNPJ.isValid(cnpj);
```

### Checklist de migração

| Item | Ação |
|---|---|
| **Colunas no banco** | Alterar de `NUMERIC`/`BIGINT` para `CHAR(14)` ou `VARCHAR(14)` |
| **Validação de input** | Substituir regex `\d{14}` por `CNPJ.isValid()` |
| **Máscaras de input** | Aceitar letras A-Z nas 12 primeiras posições |
| **Índices no banco** | Recriar índices em colunas CNPJ após alteração de tipo |
| **Integrações** | Verificar se APIs parceiras aceitam formato alfanumérico |
| **Relatórios** | Atualizar formatação para suportar letras |
| **Código de barras** | Migrar de CODE-128C para CODE-128A |
| **NF-e / NFC-e** | Atualizar schemas XML conforme NT 2025.001 |

---

## 🧮 O Algoritmo

O algoritmo de dígitos verificadores segue a **Instrução Normativa RFB nº 2.229/2024**:

### Conversão de caracteres

Cada caractere é convertido para valor numérico: **código ASCII - 48**

| Caractere | ASCII | Valor |
|:---------:|:-----:|:-----:|
| 0 | 48 | 0 |
| 1 | 49 | 1 |
| ... | ... | ... |
| 9 | 57 | 9 |
| A | 65 | 17 |
| B | 66 | 18 |
| ... | ... | ... |
| Z | 90 | 42 |

### Pesos

```
Posição:  1   2   3   4   5   6   7   8   9  10  11  12  13
Peso:     6   5   4   3   2   9   8   7   6   5   4   3   2
```

- **DV1**: usa pesos das posições 2-13 sobre os 12 caracteres base
- **DV2**: usa pesos das posições 1-13 sobre os 12 chars base + DV1

### Regra do Módulo 11

```
resto = soma % 11
DV = (resto < 2) ? 0 : 11 - resto
```

### Exemplo completo

```
CNPJ: 12.ABC.345/01DE-??
Base: 1  2  A  B  C  3  4  5  0  1  D  E

Valores (ASCII-48):
      1  2  17 18 19  3  4  5  0  1  20 21

DV1: (1×5)+(2×4)+(17×3)+(18×2)+(19×9)+(3×8)+(4×7)+(5×6)+(0×5)+(1×4)+(20×3)+(21×2) = 459
     459 % 11 = 8 → DV1 = 11-8 = 3

DV2: (1×6)+(2×5)+(17×4)+(18×3)+(19×2)+(3×9)+(4×8)+(5×7)+(0×6)+(1×5)+(20×4)+(21×3)+(3×2) = 424
     424 % 11 = 6 → DV2 = 11-6 = 5

CNPJ completo: 12.ABC.345/01DE-35 ✓
```

---

## 📁 Estrutura do Projeto

```
cnpj-alfanumerico/
  pom.xml                                    # Build Maven
  build.gradle                               # Build Gradle
  settings.gradle

  src/main/java/io/github/joaop/cnpj/
    CNPJ.java                                # Fachada principal (API pública)
    CNPJValidator.java                       # Validação + cálculo de DVs
    CNPJFormatter.java                       # Formatação e desformatação
    CNPJGenerator.java                       # Geração de CNPJs para testes
    CNPJType.java                            # Enum: NUMERIC, ALPHANUMERIC
    CNPJParts.java                           # Value object com partes do CNPJ
    CNPJException.java                       # Exceção customizada

  src/test/java/io/github/joaop/cnpj/
    CNPJTest.java                            # Testes da fachada
    CNPJValidatorTest.java                   # Testes de validação
    CNPJFormatterTest.java                   # Testes de formatação
    CNPJGeneratorTest.java                   # Testes de geração
```

---

## ✅ Compatibilidade

| Java | Status |
|:----:|:------:|
| 8 (LTS) | ✅ Compatível |
| 11 (LTS) | ✅ Compatível |
| 17 (LTS) | ✅ Compatível |
| 21 (LTS) | ✅ Compatível |

> A biblioteca é compilada com source/target Java 8, garantindo compatibilidade com todas as versões LTS.
> Para compilar com uma versão específica via Maven, use profiles: `mvn compile -P java-17`

**Sem dependências externas em runtime** — apenas JUnit 5 para testes.

---

## Timeline da Receita Federal

| Data | Evento |
|---|---|
| 15/10/2024 | Instrução Normativa RFB nº 2.229 publicada |
| 05/11/2024 | Documentação técnica e código de referência publicados |
| **06/04/2026** | Ambiente de homologação aceita CNPJs alfanuméricos |
| **06/07/2026** | Produção — primeiros CNPJs alfanuméricos emitidos |

---

## Documentação

| Documento | Descrição |
|---|---|
| [CHANGELOG.md](CHANGELOG.md) | Histórico de versões |
| [LICENSE](LICENSE) | Licença Apache 2.0 |
| [Receita Federal - Docs Técnicos](https://www.gov.br/receitafederal/pt-br/centrais-de-conteudo/publicacoes/documentos-tecnicos/cnpj) | Documentação oficial |
| [IN RFB nº 2.229/2024](http://normas.receita.fazenda.gov.br/sijut2consulta/link.action?idAto=141102) | Instrução Normativa |
| [Serpro - Cálculo DV](https://www.serpro.gov.br/menu/noticias/videos/calculodvcnpjalfanaumerico.pdf) | PDF com algoritmo oficial |

---

## Contribuindo

Contribuições são bem-vindas! Por favor:

1. Fork o repositório
2. Crie uma branch para sua feature (`git checkout -b feature/minha-feature`)
3. Commit suas alterações (`git commit -m 'Adiciona minha feature'`)
4. Push para a branch (`git push origin feature/minha-feature`)
5. Abra um Pull Request

---

<div align="center">

**Construído com ☕ Java por [@JohnPitter](https://github.com/JohnPitter)**

Baseado na [Instrução Normativa RFB nº 2.229/2024](http://normas.receita.fazenda.gov.br/sijut2consulta/link.action?idAto=141102)

</div>
