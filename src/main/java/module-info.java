module org.example.inscripcionmaterias {
    requires javafx.controls;
    requires javafx.fxml;


    opens Entidades to javafx.fxml;
    exports Entidades;
    exports Controladores;
    opens Controladores to javafx.fxml;
}