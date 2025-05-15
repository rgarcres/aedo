package es.uma.aedo.data.entidades;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("2")
public class RespuestaTipo2 extends Respuesta{
    //------------Atributos------------
    public enum RespuestaSiNoIntensidad {
        SI("Sí"),
        NO("No"),
        NO_SE("No sé");

        private final String text;

        private RespuestaSiNoIntensidad(String t){
            this.text = t;
        }

        @Override
        public String toString(){
            return text;
        }

        public static String listToString(){
            StringBuilder sb = new StringBuilder("[");

            for(RespuestaSiNoIntensidad r: RespuestaSiNoIntensidad.values()){
                sb.append(r.toString() +", ");
            }
            //Quitar la última coma
            if(sb.length() > 1){
                sb.setLength(sb.length() - 2);
            }

            sb.append("]");
            return sb.toString();
        }
    }
    @Enumerated(EnumType.STRING)
    private RespuestaSiNoIntensidad respuesta;
    private Integer intensidad;
    
    //------------Constructor------------  
    public RespuestaTipo2() {}

    //------------Getters y Setters------------  
    public RespuestaSiNoIntensidad getRespuesta() { return respuesta; }
    public void setRespuesta(RespuestaSiNoIntensidad respuesta) { this.respuesta = respuesta; }

    public Integer getIntensidad() { return intensidad; }
    public void setIntensidad(Integer intensidad) { this.intensidad = intensidad; }
}
