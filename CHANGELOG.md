# Changelog

Todas as alterações notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto segue [Versionamento Semântico](https://semver.org/lang/pt-BR/).

## [1.1.0] - 2026-03-23

### Added
- `CNPJ.equals(String, String)` — comparação ignorando formatação e case
- `CNPJ.isSameCompany(String, String)` — verifica se dois CNPJs têm a mesma raiz (mesma empresa)
- `CNPJ.mask(String)` — máscara LGPD no formato `12.***.345/01DE-**`
- `CNPJ.mask(String, int)` — máscara com N caracteres visíveis
- `CNPJ.validateBatch(Collection)` — validação em lote com resultado detalhado
- `CNPJBatchResult` — resultado de validação em lote com contagens por tipo e motivos de erro
- `CNPJ.requiresStorageMigration(String)` — detecta se CNPJ não cabe em BIGINT
- `CNPJ.getMinColumnType(String)` — retorna tipo SQL mínimo (`BIGINT` ou `CHAR(14)`)
- `CNPJ.getBarcodeEncoding(String)` — retorna encoding correto (CODE-128A ou CODE-128C)
- `BarcodeEncoding` — enum com tipos de código de barras
- `CNPJ.listBranches(String, Collection)` — gera CNPJs completos para lista de filiais
- `@CNPJ` annotation — Bean Validation (JSR 380) para validação em DTOs
- `CNPJValidator` (validation) — validador programático para @CNPJ
- `CNPJSerializer` — serialização de CNPJ (formata para JSON)
- `CNPJDeserializer` — deserialização de CNPJ (remove formatação do JSON)
- `CNPJModule` — módulo de integração Jackson
- 45 novos testes (total: 223)

## [1.0.0] - 2026-03-23

### Added
- `CNPJ` — classe fachada com API completa via métodos estáticos
- `CNPJValidator` — validação com algoritmo Módulo 11 (ASCII-48), retrocompatível
- `CNPJFormatter` — formatação (`XX.XXX.XXX/XXXX-XX`) e desformatação
- `CNPJGenerator` — geração de CNPJs válidos para testes (alfanumérico e numérico)
- `CNPJType` — enum para classificação: `NUMERIC` / `ALPHANUMERIC`
- `CNPJParts` — value object com decomposição: raiz, ordem, dígitos verificadores
- `CNPJException` — exceção customizada para erros de validação
- Suporte a Java 8, 11, 17 e 21
- Build com Maven e Gradle
- Suite de testes com JUnit 5 (validação, formatação, geração, retrocompatibilidade)
- README com documentação completa, API reference e guia de migração
