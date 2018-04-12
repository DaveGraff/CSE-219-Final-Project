/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
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
public class DataVisualizerApp extends Application implements Serializable{
    private DataState data = new DataState("");
    private boolean disabledText = true;
    private ArrayList<Algorithm> algorithms = loadAlgorithms();
    
    @Override
    public void start(Stage primaryStage) {
        Button newButton = new Button("New");
        Button loadButton = new Button("Load");
        Button saveButton = new Button("Save");saveButton.setDisable(true);
        Button saveGraphButton = new Button("Save Graph"); saveGraphButton.setDisable(true);
        Button exitButton = new Button("Exit");
        ToolBar toolbar = new ToolBar(newButton, loadButton, saveButton, saveGraphButton, exitButton);
        
        exitButton.setOnAction(e -> {
            if(!data.getIsSaved()){
                data.checkForSave();
            }
            if(data.getIsSaved())
                System.exit(0);
        });
        
        ComboBox algoOptions = new ComboBox(); 
        algoOptions.setDisable(true);algoOptions.setOpacity(0);//disabled until later
        algoOptions.getItems().addAll("Select an Algorithm", "Clustering");
        algoOptions.getSelectionModel().selectFirst();
        MyChart chart = new MyChart();
        TextArea textbox = new TextArea();textbox.setDisable(true);
        
        final ToggleGroup group = new ToggleGroup();//All available algorithms
        VBox bigAlgoBox = new VBox(); bigAlgoBox.setSpacing(5);
        
        algoOptions.valueProperty().addListener(e ->{
            group.getToggles().clear();
            bigAlgoBox.getChildren().clear();
            for(Algorithm algo : algorithms){
                if(algo.getType().toString().equals(algoOptions.getValue())){
                    RadioButton temp = new RadioButton(algo.getName());
                    group.getToggles().add(temp);
                    Button settings = new Button("Settings");
                    Label forSpace = new Label("\t");
                    settings.setOnAction(x -> {
                        algo.editConfig();
                        saveAlgorithms();
                    });
                    HBox algoBox = new HBox(temp, forSpace, settings);
                    bigAlgoBox.getChildren().add(algoBox);
                }
            }
        });
        
        CheckBox disableText = new CheckBox("Edit Text");
        disableText.selectedProperty().addListener(e -> {
            algoOptions.getItems().remove("Classification");
            disabledText = !disabledText;
            textbox.setDisable(disabledText);
            boolean goodData = false;
            if(disabledText){
                goodData = !data.isWrong(data.getData());
            }
            if(goodData){
                algoOptions.setDisable(false);
                algoOptions.setOpacity(100);
                if(supportsClassification())
                    algoOptions.getItems().add("Classification");
            } else{
                algoOptions.setDisable(true);
                algoOptions.setOpacity(0);
            }
        });
        
        Label l1 = new Label();
        Label l2 = new Label();
        Label l3 = new Label();
        Label l4 = new Label();
        VBox loadedMetaData = new VBox(l1, l2, l3, l4);
        
        Button runButton = new Button("Run");runButton.setDefaultButton(true);
        runButton.setDisable(true);runButton.setOpacity(0);
        runButton.setOnAction(e -> alert("Error" , "", "This function isn't supported yet :("));
        
        group.selectedToggleProperty().addListener(e -> {
            if(group.getSelectedToggle() != null){
                runButton.setDisable(false);runButton.setOpacity(100);
            } else {
                runButton.setDisable(true);runButton.setOpacity(0);
            }
        });
        
        VBox leftSide = new VBox(textbox, disableText, loadedMetaData, algoOptions, bigAlgoBox, runButton); 
        leftSide.setPadding(new Insets(10));
        leftSide.setSpacing(5);
        HBox sides = new HBox(leftSide, chart.getChart());
        sides.setSpacing(5);
        
        VBox root = new VBox(toolbar, sides);
        Scene scene = new Scene(root, 800, 450);
        
        saveButton.setOnAction(e -> data.handleSaveRequest(textbox.getText()));
        loadButton.setOnAction(e -> {
            try{
                algoOptions.getItems().remove("Classification");
                String [] temp = data.handleLoadRequest();
                l1.setText(temp[0]);l2.setText(temp[1]);
                l3.setText(temp[2]);l4.setText(temp[3]);
                textbox.setText(data.getData());
                saveButton.setDisable(true);
                data.setIsSaved(true);
                algoOptions.setDisable(false);
                algoOptions.setOpacity(100);
                if(supportsClassification())
                    algoOptions.getItems().add("Classification");
            } catch (IndexOutOfBoundsException f){}
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
                l1.setText("");l2.setText("");
                l3.setText("");l4.setText("");
                algoOptions.setDisable(true);
                algoOptions.setOpacity(0);
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
    
    /**Checks if the current data meets the requirements
     * to use a Classification algorithm
     * 
     * @return T/F
     */
    public boolean supportsClassification(){
        ArrayList<String> labelList = new ArrayList<>();
        Stream.of(data.getData().split("\n")).map(line -> Arrays.asList(line.split("\t"))).forEach(list -> {
            String label = list.get(1);
            if(!labelList.contains(label))
                labelList.add(label);
        });
        return labelList.size() ==  2;
    }
    
    public void saveAlgorithms(){
        PrintWriter pw;
        try {
            pw = new PrintWriter("CSE219FinalAlgos.json");
            pw.close();
        } catch (FileNotFoundException ex) {
            System.out.println("This text should never be visible.");
        } try{   
            FileOutputStream file = new FileOutputStream("CSE219FinalAlgos.json");
            ObjectOutputStream out = new ObjectOutputStream(file);
            
            out.writeObject(algorithms);
             
            out.close();
            file.close();     
        } catch(IOException ex){
            System.out.println("IOException is caught");
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
