# Napkin

## Corrections
| Date | Source | What Went Wrong | What To Do Instead |
|------|--------|----------------|-------------------|

## User Preferences
- User communicates in Portuguese (BR)
- Wants both Maven and Gradle build support
- Multi-Java version support: 8, 11, 17, 21
- Documentation style based on "j-obs" project (pending clarification on exact project)

## Patterns That Work
- (accumulate as we go)

## Patterns That Don't Work
- (accumulate as we go)

## Domain Notes
- Project: Java library for Brazilian alphanumeric CNPJ validation/migration
- CNPJ alfanumerico: first 12 chars become A-Z/0-9, last 2 remain numeric
- Check digit algorithm: Modulo 11 with ASCII-48 conversion
- Weights: [6,5,4,3,2,9,8,7,6,5,4,3,2]
- Timeline: homologation 2026-04-06, production 2026-07-06
- Backwards compatible with numeric-only CNPJs
