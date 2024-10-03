module org.example.inscripcionmaterias {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens Entidades to javafx.fxml;
    exports Entidades;
    exports Controladores;
    opens Controladores to javafx.fxml;
}