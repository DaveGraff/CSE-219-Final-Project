/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author HP
 */
public class DataVisualizerApp extends Application {
    private DataState data = new DataState("");
    
    @Override
    public void start(Stage primaryStage) {
        Button newButton = new Button("New");
        Button loadButton = new Button("Load");
        Button saveButton = new Button("Save");saveButton.setDisable(true);
        Button saveGraphButton = new Button("Save Graph");
        Button exitButton = new Button("Exit");
        ToolBar toolbar = new ToolBar(newButton, loadButton, saveButton, saveGraphButton, exitButton);
        
        ComboBox algoOptions = new ComboBox();
        algoOptions.getItems().addAll("Select an Algorithm", "Classification", "Clustering");
        algoOptions.getSelectionModel().selectFirst();
        MyChart chart = new MyChart();
        TextArea textbox = new TextArea();
        
        VBox leftSide = new VBox(textbox, algoOptions);
        HBox sides = new HBox(leftSide, chart.getChart());
        sides.setSpacing(5);
        
        VBox root = new VBox(toolbar, sides);
        Scene scene = new Scene(root, 800, 500);
        
        saveButton.setOnAction(e -> data.handleSaveRequest(textbox.getText()));
        
        textbox.textProperty().addListener(e -> {
            data.setIsSaved(false);
            saveButton.setDisable(false);
        });
        
        primaryStage.setTitle("Data Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void alert(String title, String header, String reason){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(reason);
        alert.showAndWait();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
