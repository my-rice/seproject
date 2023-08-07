module it.faint {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens it.faint.controller to javafx.fxml;
    exports it.faint;
}
