package es.uma.aedo.data.enumerados;

public enum ESituacionPersonal {
    DESCONOCIDA("Desconocida"),
    SOLTERO("Soltero"),
    CASADO("Casado"),
    SEPARADO("Separado"),
    DIVORCIADO("Divorciado"),
    VIUDO("Viudo"),
    OTRO("Otro");

    private final String text;

    private ESituacionPersonal(String t){
        this.text = t;
    }

    @Override
    public String toString() {
        return text;
    }
}
