package com.example.demo;

/**
 * TOMMY OJO AND SAKHANA
 */

import com.example.demo.Controller.CalendarController;
import com.example.demo.Model.Reminders;
import com.example.demo.UserInput.ConfirmBox;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class CircleApplication extends Application {
    /**
     * Main stage for application
     **/
    private Stage window;

    private Scene calendar_scene;

    public static HostServices hostServices;


    public void start(Stage primarystage) throws IOException {
        // host Services needed for opening files
        hostServices=getHostServices();
        // setting main window
        window = primarystage;
        window.setTitle("Circle");

        Parent sidemenu = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("View/SideMenuView.fxml")));

        // adds confirmation to close
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        // making calendar scene
        calendar_scene = new Scene(sidemenu);

        // WELCOME PAGE, Sakhana
        VBox root = new VBox();
        root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        Label myText = new Label("StudyW/Me");

        myText.setFont(new Font("Elephant", 75));
        myText.setPadding(new Insets(0, 0, 130, 0));
        myText.setTextFill(Color.CORNFLOWERBLUE);

        double radius = 175;
        Button circleButton = new Button("GO");
        circleButton.setFont(new Font("Elephant", 45));
        circleButton.setTextFill(Color.CORNFLOWERBLUE);
        circleButton.setLayoutX(250);
        circleButton.setLayoutY(350);
        circleButton.setShape(new Circle(radius));
        circleButton.getStyleClass().add("button");
        circleButton.setPrefSize(radius, radius);
        circleButton.setOnAction(e -> window.setScene(calendar_scene));

        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("background");

        root.getChildren().addAll(myText, circleButton);
        window.setTitle("Circle");

        window.setScene(new Scene(root, 720, 560));
        window.show();

    }


    private void closeProgram() {
        if (ConfirmBox.display("Confirmation", "Are you sure you want to close?")) {
            for (Reminders reminder : CalendarController.reminders){ // this closes any running timer threads
                reminder.unschedule();
            }
            window.close();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}