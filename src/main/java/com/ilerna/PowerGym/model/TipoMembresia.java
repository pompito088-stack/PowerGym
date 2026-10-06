package com.ilerna.PowerGym.model;

public enum TipoMembresia {
    MANANA("mañana"),
    TARDE("tarde"),
    COMPLETO("completo");

    private final String valorBd;

    TipoMembresia(String valorBd) {
        this.valorBd = valorBd;
    }

    public String getValorBd() {
        return valorBd;
    }

    public static TipoMembresia desdeValorBd(String valorBd) {
        for (TipoMembresia tipo : values()) {
            if (tipo.valorBd.equals(valorBd)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de membresia desconocido: " + valorBd);
    }
}
