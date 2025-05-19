package es.uma.aedo.data.enumerados;

public enum EGenero {
    DESCONOCIDO("Desconocido"),
    MUJER("Mujer"),
    HOMBRE("Hombre"),
    NO_BINARIO("No binario"),
    OTRO("Otro");

    private final String text;

    private EGenero(String t){
        this.text = t;
    }

    @Override
    public String toString(){
        return text;
    }
}
