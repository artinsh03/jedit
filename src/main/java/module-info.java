module com.noone.jedit {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.noone.jedit to javafx.fxml;
    opens com.noone.jedit.controller to javafx.fxml;
    exports com.noone.jedit;
    exports com.noone.jedit.controller;
}
