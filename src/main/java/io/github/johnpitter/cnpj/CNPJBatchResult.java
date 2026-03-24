package io.github.johnpitter.cnpj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resultado da validação em lote de CNPJs.
 *
 * <p>Contém listas separadas de CNPJs válidos e inválidos,
 * contagens por tipo e detalhes dos erros encontrados.</p>
 *
 * @see CNPJ#validateBatch(java.util.Collection)
 */
public final class CNPJBatchResult {

    private final List<String> valid;
    private final Map<String, String> invalid;
    private int numericCount;
    private int alphanumericCount;

    CNPJBatchResult() {
        this.valid = new ArrayList<String>();
        this.invalid = new LinkedHashMap<String, String>();
        this.numericCount = 0;
        this.alphanumericCount = 0;
    }

    void addValid(String sanitizedCnpj) {
        valid.add(sanitizedCnpj);
        if (CNPJValidator.isNumericOnly(sanitizedCnpj)) {
            numericCount++;
        } else {
            alphanumericCount++;
        }
    }

    void addInvalid(String cnpj, String reason) {
        invalid.put(cnpj, reason);
    }

    /** Retorna a lista de CNPJs válidos (sem formatação, uppercase). */
    public List<String> getValid() {
        return Collections.unmodifiableList(valid);
    }

    /** Retorna mapa de CNPJs inválidos → motivo do erro. */
    public Map<String, String> getInvalid() {
        return Collections.unmodifiableMap(invalid);
    }

    /** Total de CNPJs processados. */
    public int getTotal() {
        return valid.size() + invalid.size();
    }

    /** Quantidade de CNPJs válidos. */
    public int getValidCount() {
        return valid.size();
    }

    /** Quantidade de CNPJs inválidos. */
    public int getInvalidCount() {
        return invalid.size();
    }

    /** Quantidade de CNPJs válidos que são numéricos (legado). */
    public int getNumericCount() {
        return numericCount;
    }

    /** Quantidade de CNPJs válidos que são alfanuméricos (novo formato). */
    public int getAlphanumericCount() {
        return alphanumericCount;
    }

    /** Retorna true se todos os CNPJs são válidos. */
    public boolean isAllValid() {
        return invalid.isEmpty();
    }

    /** Retorna true se há CNPJs alfanuméricos que requerem migração de storage. */
    public boolean hasAlphanumericEntries() {
        return alphanumericCount > 0;
    }

    @Override
    public String toString() {
        return "CNPJBatchResult{total=" + getTotal()
                + ", valid=" + valid.size()
                + ", invalid=" + invalid.size()
                + ", numeric=" + numericCount
                + ", alphanumeric=" + alphanumericCount + "}";
    }
}
