/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
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
    private boolean disabledText = true;
    private final ArrayList<Algorithm> algorithms = loadAlgorithms();
    
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
        TextArea textbox = new TextArea();textbox.setDisable(true);
        
        final ToggleGroup group = new ToggleGroup();//All available algorithms
        VBox bigAlgoBox = new VBox();
        
        algoOptions.valueProperty().addListener(e ->{
            group.getToggles().clear();
            bigAlgoBox.getChildren().clear();
            for(Algorithm algo : algorithms){
                if(algo.getType().toString().equals(algoOptions.getValue())){
                    RadioButton temp = new RadioButton(algo.getName());
                    Button settings = new Button("Settings");
                    HBox algoBox = new HBox(temp, settings);
                    bigAlgoBox.getChildren().add(algoBox);
                }
            }
        });
        
        CheckBox disableText = new CheckBox("Edit Text");
        disableText.selectedProperty().addListener(e -> {
            disabledText = !disabledText;
            textbox.setDisable(disabledText);
            boolean goodData = false;
            if(disabledText){
                goodData = data.isWrong(data.getData());
            }
            if(goodData){
                //Show run button
            }
        });
        
        VBox leftSide = new VBox(textbox, disableText, algoOptions, bigAlgoBox); 
        leftSide.setPadding(new Insets(10));
        leftSide.setSpacing(5);
        HBox sides = new HBox(leftSide, chart.getChart());
        sides.setSpacing(5);
        
        VBox root = new VBox(toolbar, sides);
        Scene scene = new Scene(root, 800, 500);
        
        saveButton.setOnAction(e -> data.handleSaveRequest(textbox.getText()));
        loadButton.setOnAction(e -> {
            data.handeLoadRequest(); 
            textbox.setText(data.getData());
            saveButton.setDisable(true);
            data.setIsSaved(true);
        });
        
        textbox.textProperty().addListener(e -> {
            data.setData(textbox.getText());
            data.setIsSaved(false);
            saveButton.setDisable(false);
        });
        
        newButton.setOnAction(e -> {
            boolean worked = data.handleNewRequest();
            if (worked){
                textbox.setText(data.getData());
                saveButton.setDisable(true);
                data.setIsSaved(true);
                disabledText = false;
                textbox.setDisable(disabledText);
            }
        });
        
        primaryStage.setTitle("Data Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Shows the user an Alert for some given error
     * 
     * @param title The title of the alert
     * @param header The header of the alert
     * @param reason The reason or body of the alert
     */
    private void alert(String title, String header, String reason){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(reason);
        alert.showAndWait();
    }
    /**Used to load config data for algorithms
     * 
     * @return Either the saved runConfigs for the algorithms, or
     * the default configs if none have been set
     */
    private ArrayList<Algorithm> loadAlgorithms(){
        ArrayList<Algorithm> algos= new ArrayList<>();
        
        try{   
            FileInputStream file = new FileInputStream("CSE219FinalAlgos.ser");
            ObjectInputStream in = new ObjectInputStream(file);
             
            algos = (ArrayList<Algorithm>) in.readObject();
            
            in.close();
            file.close();
        } catch(IOException ex){//There is no saved info on runConfigs
            algos.add(new Algorithm("Classification A", AlgorithmType.Classification));
            algos.add(new Algorithm("Classification B", AlgorithmType.Classification));
            algos.add(new Algorithm("Clustering A", AlgorithmType.Clustering));
            algos.add(new Algorithm("Clustering B", AlgorithmType.Clustering));
        } catch(ClassNotFoundException m){System.out.println("This should never be visible");}
        
        return algos;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
