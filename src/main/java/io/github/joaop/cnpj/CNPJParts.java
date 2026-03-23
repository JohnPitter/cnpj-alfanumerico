package io.github.joaop.cnpj;

/**
 * Representa as partes decompostas de um CNPJ.
 *
 * <ul>
 *   <li><b>root</b> (posições 1-8): identificador único da empresa</li>
 *   <li><b>branch</b> (posições 9-12): ordem do estabelecimento (0001 = matriz)</li>
 *   <li><b>checkDigits</b> (posições 13-14): dígitos verificadores</li>
 * </ul>
 */
public final class CNPJParts {

    private final String root;
    private final String branch;
    private final String checkDigits;

    CNPJParts(String root, String branch, String checkDigits) {
        this.root = root;
        this.branch = branch;
        this.checkDigits = checkDigits;
    }

    /** Raiz do CNPJ (8 caracteres) — identifica a empresa. */
    public String getRoot() {
        return root;
    }

    /** Ordem do estabelecimento (4 caracteres) — 0001 para matriz. */
    public String getBranch() {
        return branch;
    }

    /** Dígitos verificadores (2 dígitos numéricos). */
    public String getCheckDigits() {
        return checkDigits;
    }

    /** Retorna true se este CNPJ é de uma matriz (branch == "0001"). */
    public boolean isHeadquarters() {
        return "0001".equals(branch);
    }

    /** Retorna o CNPJ completo sem formatação. */
    public String toUnformatted() {
        return root + branch + checkDigits;
    }

    /** Retorna o CNPJ completo com formatação (XX.XXX.XXX/XXXX-XX). */
    public String toFormatted() {
        return CNPJFormatter.format(toUnformatted());
    }

    /** Retorna o tipo do CNPJ (NUMERIC ou ALPHANUMERIC). */
    public CNPJType getType() {
        String full = toUnformatted();
        return CNPJValidator.isNumericOnly(full) ? CNPJType.NUMERIC : CNPJType.ALPHANUMERIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CNPJParts)) return false;
        CNPJParts that = (CNPJParts) o;
        return root.equals(that.root) && branch.equals(that.branch) && checkDigits.equals(that.checkDigits);
    }

    @Override
    public int hashCode() {
        int result = root.hashCode();
        result = 31 * result + branch.hashCode();
        result = 31 * result + checkDigits.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CNPJParts{root='" + root + "', branch='" + branch + "', checkDigits='" + checkDigits + "'}";
    }
}
