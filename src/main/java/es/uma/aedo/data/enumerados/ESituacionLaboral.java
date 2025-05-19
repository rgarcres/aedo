package es.uma.aedo.data.enumerados;

public enum ESituacionLaboral {
    DESCONOCIDO("Desconocido"),
    PARO("Paro"),
    ASALARIADO("Asalariado"),
    AUTONOMO("Autónomo"),
    TIEMPO_PARCIAL("Tiempo parcial"),
    TIEMPO_TOTAL("Tiempo total"),
    OTRO("Otro");

    private final String text;

    private ESituacionLaboral(String t){
        this.text = t;
    }

    @Override
    public String toString(){
        return text;
    }
}
