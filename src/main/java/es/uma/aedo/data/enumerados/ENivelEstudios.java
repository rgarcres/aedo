package es.uma.aedo.data.enumerados;

public enum ENivelEstudios {
    DESCONOCIDO("Desconocido"),
    ESO("ESO"),
    BACHILLERATO("Bachillerato"),
    GRADO_UNIVERSITARIO("Grado Universitario"),
    FORMACION_PROFESIONAL("Formación profesional"),
    MASTER("Máster"),
    DOCTORADO("Doctorado"),
    OTRO("Otro");

    private final String text;

    private ENivelEstudios(String t){
        this.text = t;
    }

    @Override
    public String toString(){
        return text;
    }
}
