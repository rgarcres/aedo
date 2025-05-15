package es.uma.aedo.data.entidades;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@DiscriminatorValue("1")
public class RespuestaTipo1 extends Respuesta{
    //------------Atributos------------   
    public enum RespuestaSiNo {
        SI("Sí"),
        NO("No"),
        OCASIONALMENTE("Ocasionalmente");

        private final String text;

        private RespuestaSiNo(String txt){
            this.text = txt;
        }

        @Override
        public String toString(){
            return text;
        }

        public static String listToString(){
            StringBuilder sb = new StringBuilder("[");

            for(RespuestaSiNo r: RespuestaSiNo.values()){
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
    private RespuestaSiNo respuesta;

    //------------Constructor------------  
    public RespuestaTipo1(){}

    //------------Getters y Setters------------  
    public RespuestaSiNo getRespuesta() { return respuesta; }
    public void setRespuesta(RespuestaSiNo respuesta) { this.respuesta = respuesta; }
}
