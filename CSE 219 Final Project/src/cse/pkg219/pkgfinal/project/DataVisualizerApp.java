/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author HP
 */
public class DataVisualizerApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button newButton = new Button("New");
        Button loadButton = new Button("Load");
        Button saveButton = new Button("Save");
        Button saveGraphButton = new Button("Save Graph");
        Button exitButton = new Button("Exit");
        ToolBar toolbar = new ToolBar(newButton, loadButton, saveButton, saveGraphButton, exitButton);
        
        MyChart chart = new MyChart();
        TextArea textbox = new TextArea();
        VBox leftSide = new VBox(textbox);
        HBox sides = new HBox(leftSide, chart.getChart());
        
        VBox root = new VBox(toolbar, sides);
        Scene scene = new Scene(root, 800, 500);
        
        primaryStage.setTitle("Data Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
