package com.example.demo.Controller;

import com.example.demo.CircleApplication;
import com.example.demo.Launcher;
import com.example.demo.Model.SaveState;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SideMenuController implements Initializable {

    // SAKHANA

    @FXML
    private Button btn_cal, btn_fil, btn_sea, btn_menu, settingsButton;

    @FXML
    private Pane myMenu;

    boolean extended = true;

    private Pane calendar, files, search, setting;

    @FXML
    private Pane myArea;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        // slides side menu in and out
        if (event.getSource() == btn_menu){
            myMenu.setTranslateX(-150);
            TranslateTransition transition = new TranslateTransition(Duration.millis(1000), myMenu);
            transition.setFromX(-150);
            transition.setToX(0);
            if (extended){
                transition.setRate(-1);
                transition.play();
                extended = false;
            }
            else{
                transition.setRate(1);
                transition.play();
                extended = true;
            }
        }
        // switch to calendar page
        else if (event.getSource() == btn_cal) {
            myArea.getChildren().removeAll();
            myArea.getChildren().setAll(calendar);
            // switch to files page
        } else if (event.getSource() == btn_fil) {
            myArea.getChildren().removeAll();
            myArea.getChildren().setAll(files);
        // switch to search page
        } else if (event.getSource() == btn_sea) {
            myArea.getChildren().removeAll();
            myArea.getChildren().setAll(search);
        }
        // switch to settings page
        else if (event.getSource() == settingsButton){
            myArea.getChildren().removeAll();
            myArea.getChildren().setAll(setting);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            calendar = FXMLLoader.load(Launcher.class.getResource("View/CalendarView.fxml"));
            FXMLLoader fileLoader = new FXMLLoader(Launcher.class.getResource("View/TabsView.fxml"));
            files = fileLoader.load();
            search = FXMLLoader.load(Launcher.class.getResource("View/SearchView.fxml"));
            setting = FXMLLoader.load(Launcher.class.getResource("View/SettingsView.fxml"));

            TabsController tabsController = fileLoader.getController();
            tabsController.myHostServices(CircleApplication.hostServices); // setting up hostservices
            myArea.getChildren().setAll(calendar); // sets initial view to calendar
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        // Loads darkmode if it was saved as the user
        SettingsController temp = SaveState.LoadObject(SaveState.devFolder + "/Settings.json", SettingsController.class);
        if(temp != null && temp.getDarkMode()){
            temp.changeMode(myArea);
            temp.changeMode(myMenu);
        }

        SettingsController.darkMode.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                SettingsController settingsController = new SettingsController();
                settingsController.changeMode(myArea);
                settingsController.changeMode(myMenu);
            }
        });
    }

}




