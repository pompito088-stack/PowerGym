package com.ilerna.PowerGym.model;

public enum Entrenador {
    CARLOS_MARTIN("Carlos Martín"),
    ELENA_VIDAL("Elena Vidal"),
    JAVIER_ROJAS("Javier Rojas");

    private final String valorBd;

    Entrenador(String valorBd) {
        this.valorBd = valorBd;
    }

    public String getValorBd() {
        return valorBd;
    }

    public static Entrenador desdeValorBd(String valorBd) {
        for (Entrenador entrenador : values()) {
            if (entrenador.valorBd.equals(valorBd)) {
                return entrenador;
            }
        }
        throw new IllegalArgumentException("Entrenador desconocido: " + valorBd);
    }
}
