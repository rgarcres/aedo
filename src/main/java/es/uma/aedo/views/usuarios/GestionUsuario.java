package es.uma.aedo.views.usuarios;

import java.time.LocalDate;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;

import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.data.enumerados.EGenero;
import es.uma.aedo.data.enumerados.ENivelEstudios;
import es.uma.aedo.data.enumerados.ESituacionLaboral;
import es.uma.aedo.data.enumerados.ESituacionPersonal;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

public class GestionUsuario {

    public static VerticalLayout crearCamposLayout(UsuarioService usuarioService, Usuario usuario) {
        // ------------Layouts------------
        VerticalLayout layout = new VerticalLayout();
        FormLayout camposLayout = new FormLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();

        // ------------Componentes------------
        TextField idField = new TextField("ID");
        TextField aliasField = new TextField("Nombre");
        DatePicker nacimientoPicker = new DatePicker("Fecha Nacimiento");
        ComboBox<EGenero> generoBox = new ComboBox<>("Género");
        ComboBox<ENivelEstudios> estudiosBox = new ComboBox<>("Nivel de Estudios");
        ComboBox<ESituacionLaboral> laboralBox = new ComboBox<>("Situación Laboral");
        ComboBox<ESituacionPersonal> personalBox = new ComboBox<>("Situación Personal");

        // ------------Div para centrar la personalBox------------
        Div personalDiv = new Div();
        personalDiv.getStyle()
                .set("width", "100%")
                .set("display", "flex")
                .set("justify-content", "center");
        personalDiv.add(personalBox);

        // ------------Botones------------
        Button siguiente = BotonesConfig.crearBotonPrincipal("Siguiente");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "usuarios");

        // ------------Instanciar comboBox------------
        generoBox.setItems(EGenero.values());
        estudiosBox.setItems(ENivelEstudios.values());
        laboralBox.setItems(ESituacionLaboral.values());
        personalBox.setItems(ESituacionPersonal.values());

        // ------------Instanciar valores si hay que editar------------
        if (usuario != null) {
            idField.setValue(usuario.getId());
            aliasField.setValue(usuario.getAlias());
            nacimientoPicker.setValue(usuario.getFechaNacimiento());
            generoBox.setValue(usuario.getGenero());
            estudiosBox.setValue(usuario.getNivelEstudios());
            laboralBox.setValue(usuario.getSituacionLaboral());
            personalBox.setValue(usuario.getSituacionPersonal());
        }

        // ------------Comportamiento de botones------------
        siguiente.addClickListener(e -> {
            String id = idField.getValue();
            String alias = aliasField.getValue();
            LocalDate nacimiento = nacimientoPicker.getValue();
            EGenero genero = generoBox.getValue();
            ESituacionLaboral laboral = laboralBox.getValue();
            ESituacionPersonal personal = personalBox.getValue();

            Boolean exito = false;
            // ------------Crear/Editar usuario------------
            if (usuario == null) {
                exito = crear(usuarioService, id, alias, nacimiento, genero, laboral, personal);
            } else {
                exito = editar(usuario, usuarioService, id, alias, nacimiento, genero, laboral, personal);
            }

            // ------------Navegar------------
            if (exito) {
                siguiente.getUI().ifPresent(ui -> ui.navigate("usuarios/seleccionar-region/"+id));
            }
        });

        // ------------Añadir componentes al layout------------
        camposLayout.add(idField, aliasField, nacimientoPicker, generoBox, estudiosBox, laboralBox, personalDiv);
        camposLayout.setColspan(personalDiv, 2);
        botonesLayout.add(siguiente, cancelar);
        layout.setAlignItems(Alignment.CENTER);
        layout.add(camposLayout, botonesLayout);
        return layout;
    }

    private static boolean crear(UsuarioService usuarioService, String id, String alias, LocalDate nacimiento,
            EGenero genero, ESituacionLaboral laboral, ESituacionPersonal personal) {
        if (camposVacios(id, alias, nacimiento, genero, laboral, personal)) {
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Los campos no pueden estar vacíos");
            return false;
        } else if (comprobarFecha(nacimiento)) {
            NotificacionesConfig.crearNotificacionError("Fecha incorecta", "La fecha introducida no es válida");
            return false;
        } else if (comprobarId(id, usuarioService)) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            Usuario usuario = new Usuario();
            usuario.setId(id);
            usuario.setAlias(alias);
            usuario.setFechaNacimiento(nacimiento);
            usuario.setGenero(genero);
            usuario.setSituacionLaboral(laboral);
            usuario.setSituacionPersonal(personal);
            usuarioService.save(usuario);
            return true;
        }
    }

    private static boolean editar(Usuario usuario, UsuarioService usuarioService, String id, String alias,
            LocalDate nacimiento,
            EGenero genero, ESituacionLaboral laboral, ESituacionPersonal personal) {
        if (camposVacios(id, alias, nacimiento, genero, laboral, personal)) {
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Los campos no pueden estar vacíos");
            return false;
        } else if (comprobarFecha(nacimiento)) {
            NotificacionesConfig.crearNotificacionError("Fecha incorecta", "La fecha introducida no es válida");
            return false;
        } else if (comprobarId(id, usuarioService) && !id.equals(usuarioService.get(id).get().getId())) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            usuario.setId(id);
            usuario.setAlias(alias);
            usuario.setFechaNacimiento(nacimiento);
            usuario.setGenero(genero);
            usuario.setSituacionLaboral(laboral);
            usuario.setSituacionPersonal(personal);
            usuarioService.save(usuario);
            return true;
        }
    }

    private static boolean camposVacios(String id, String alias, LocalDate nacimiento,
            EGenero genero, ESituacionLaboral laboral, ESituacionPersonal personal) {
        return id.isBlank() || alias.isBlank() || nacimiento == null || genero == null || laboral == null
                || personal == null;
    }

    private static boolean comprobarFecha(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

    private static boolean comprobarId(String id, UsuarioService service) {
        return service.get(id).isPresent();
    }
}
