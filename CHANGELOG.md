# Changelog

Todas as alterações notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto segue [Versionamento Semântico](https://semver.org/lang/pt-BR/).

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
