/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
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
public class DataVisualizerApp extends Application implements Serializable{
    private DataState data = new DataState("");
    private boolean disabledText = true;
    private ArrayList<Algorithm> algorithms = loadAlgorithms();
    
    boolean areYouSure;
    AlgorithmThread runner = null;
    Algorithm selected = null;
    private String selectedAlgoName = "";
    ArrayList<DataPoint> newData = null;
    @Override
    public void start(Stage primaryStage) {
        Button newButton = new Button("New");
        Button loadButton = new Button("Load");
        Button saveButton = new Button("Save");saveButton.setDisable(true);
        Button saveGraphButton = new Button("Save Graph"); saveGraphButton.setDisable(true);
        Button exitButton = new Button("Exit");
        ToolBar toolbar = new ToolBar(newButton, loadButton, saveButton, saveGraphButton, exitButton);
        Button cont = new Button("Continue");//For running the algo
        
        MyChart chart = new MyChart();
        Button runButton = new Button("Run");runButton.setDefaultButton(true);
        HBox runLine = new HBox(runButton);
        
        saveGraphButton.setOnAction(e -> chart.saveGraph());
        
        ComboBox algoOptions = new ComboBox(); 
        algoOptions.setDisable(true);algoOptions.setOpacity(0);//disabled until later
        algoOptions.getItems().addAll("Select an Algorithm", "Clustering");
        algoOptions.getSelectionModel().selectFirst();
        
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
        
        Label l1 = new Label();
        Label l2 = new Label();
        Label l3 = new Label();
        Label l4 = new Label();
        VBox loadedMetaData = new VBox(l1, l2, l3, l4);
        
        CheckBox disableText = new CheckBox("Edit Text");
        
        VBox leftSide = new VBox(textbox, disableText, loadedMetaData, algoOptions, bigAlgoBox); 
        leftSide.setPadding(new Insets(10));
        leftSide.setSpacing(5);
        HBox sides = new HBox(leftSide, chart.getChart());
        sides.setSpacing(5);
        
        group.selectedToggleProperty().addListener(e -> {
            if(group.getSelectedToggle() != null){
                if(!leftSide.getChildren().contains(runLine))
                    leftSide.getChildren().add(runLine);
                selectedAlgoName = group.getSelectedToggle().toString();
                selectedAlgoName = selectedAlgoName.substring(selectedAlgoName.indexOf(39)+1, selectedAlgoName.length()-1);
            } else {
                leftSide.getChildren().remove(runLine);
            }
        });
        
        VBox root = new VBox(toolbar, sides);
        Scene scene = new Scene(root, 800, 450);
        
        disableText.selectedProperty().addListener(e -> {
            algoOptions.getItems().remove("Classification");
            disabledText = !disabledText;
            textbox.setDisable(disabledText);
            boolean goodData = false;
            algoOptions.getSelectionModel().selectFirst();
            leftSide.getChildren().remove(runLine);
            String[] metaData = new String[4];
            l1.setText("");l2.setText("");l3.setText("");l4.setText("");
            if(disabledText){
                metaData = data.isWrong(data.getData());
                goodData = !data.getIsWrong();
            }
            if(goodData){
                l1.setText(metaData[0]);l2.setText(metaData[1]);l3.setText(metaData[2]);
                algoOptions.setDisable(false);
                algoOptions.setOpacity(100);
                if(supportsClassification())
                    algoOptions.getItems().add("Classification");
                newData = interpretData(data.getData());
                chart.processData(newData);
            } else{
                algoOptions.setDisable(true);
                algoOptions.setOpacity(0);
                chart.processData(null);
            }
        });
        
        exitButton.setOnAction(e -> {
            boolean checked = true;
            if(runner != null)
                if(runner.isAlive())
                    checked = areYouSure("Warning!","There is an algorithm currently running. \n Any generated data will be lost");
            
            if(checked){
                if(runner != null)
                    runner.stopper();
                leftSide.getChildren().remove(cont);
                if(!data.getIsSaved()){
                    data.checkForSave();
                }
                if(data.getIsSaved() || data.getData().equals(""))
                    System.exit(0);
            }
        });
        
        saveButton.setOnAction(e -> data.handleSaveRequest(textbox.getText()));
        loadButton.setOnAction(e -> {
            boolean checked = true;
            if(runner != null)
                if(runner.isAlive())
                    checked = areYouSure("Warning!","There is an algorithm currently running. \n Any generated data will be lost");
            
            if(checked){
                if(runner != null)
                    runner.stopper();
                leftSide.getChildren().remove(cont);
                chart.processData(null);
                try{
                    algoOptions.getItems().remove("Classification");
                    String [] temp = data.handleLoadRequest();
                    l1.setText(temp[0]);l2.setText(temp[1]);
                    l3.setText(temp[2]);l4.setText(temp[3]);
                    textbox.setText(data.getData());
                    saveButton.setDisable(true);
                    data.setIsSaved(true);algoOptions.getSelectionModel().selectFirst();
                    leftSide.getChildren().remove(runLine);
                    algoOptions.setDisable(false);
                    algoOptions.setOpacity(100);
                    if(supportsClassification())
                        algoOptions.getItems().add("Classification");
                    newData = interpretData(data.getData());
                    chart.processData(newData);
                } catch (IndexOutOfBoundsException f){}
            }
        });
        
        textbox.textProperty().addListener(e -> {
            data.setData(textbox.getText());
            data.setIsSaved(false);
            saveButton.setDisable(false);
        });
        
        newButton.setOnAction(e -> {
            boolean checked = true;
            if(runner != null)
                if(runner.isAlive() || runner.isInterrupted())
                    checked = areYouSure("Warning!","There is an algorithm currently running. \n Any generated data will be lost");
            
            if(checked){
                if(runner != null)
                    runner.stopper();
                leftSide.getChildren().remove(cont);
                chart.processData(null);
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
                    algoOptions.getSelectionModel().selectFirst();
                    leftSide.getChildren().remove(runLine);
                }
            }
        });
        
        runButton.setOnAction(e -> {
            for(Algorithm algo : algorithms){
                if (algo.getName().equals(selectedAlgoName))
                    selected = algo;
            }//Selected should never be null
            
            if(selected.getConfig().getMaxIter() < 1 || selected.getConfig().getUpdateInterval() < 1
                    || (selected.getConfig().getClusterNum() < 1 && selected.getType() == AlgorithmType.Clustering)){
                alert("Error", "Configuration Error", "Please set a run configuration");
            } else {
                runButton.setDisable(true);
                runner = new AlgorithmThread(selected, newData, chart, runButton, runLine, saveGraphButton);
                runner.start();
                runButton.setDisable(false);
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
            algos.add(new Algorithm("Random Classifier", AlgorithmType.Classification));
            algos.add(new Algorithm("Random Clusterer", AlgorithmType.Clustering));
            algos.add(new Algorithm("K Means Clusterer", AlgorithmType.Clustering));
        } catch(ClassNotFoundException m){System.out.println("This should never be visible");}
        
        return algos;
    }
    
    public ArrayList<DataPoint> interpretData(String data){
        ArrayList<DataPoint> interpreted = new ArrayList<>();
        Stream.of(data.split("\n")).map(line -> Arrays.asList(line.split("\t"))).forEach(list -> {
            try {
                String   name  = list.get(0);
                String   label = list.get(1);
                String[] pair  = list.get(2).split(",");
                DataPoint temp = new DataPoint(Double.parseDouble(pair[0]), Double.parseDouble(pair[1]), label, name);
                interpreted.add(temp);
            } catch (Exception e) {}
        });
        return interpreted;
    }
    /**
     * Discerns how the user wants to deal with
     * a set situation
     * 
     * @param title The title of the check
     * @param explanation The reason for the check
     * @return 
     */
    public boolean areYouSure(String title, String explanation){
        Stage newStage =  new Stage();
        newStage.setTitle(title);
        Label explanationL = new Label(explanation);
        Button cont = new Button("Continue"); cont.setDefaultButton(true);
        Button cancel = new Button("Cancel"); cancel.setCancelButton(true);
        HBox buttons = new HBox(cont, cancel); buttons.setSpacing(5);
        VBox main = new VBox(explanationL, buttons);
        
        main.setSpacing(5);
        main.setPadding(new Insets(10));
                
        cont.setOnAction(e -> {
            areYouSure = true;
            newStage.close();
        });
        cancel.setOnAction(e -> {
            areYouSure = false;
            newStage.close();
        });
        
        Scene scene = new Scene(main);
        newStage.setScene(scene);
        newStage.showAndWait();
        return areYouSure;
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
    /**
     * Saves the information to run an
     * algorithm after the configuration
     * has been updated
     */
    public void saveAlgorithms(){
        PrintWriter pw;
        try {
            pw = new PrintWriter("CSE219FinalAlgos.ser");
            pw.close();
        } catch (FileNotFoundException ex) {
            System.out.println("This text should never be visible.");
        } try{   
            FileOutputStream file = new FileOutputStream("CSE219FinalAlgos.ser");
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput out = new ObjectOutputStream(buffer);
            
            out.writeObject(algorithms);
             
            out.close();
            file.close();     
        } catch(IOException ex){
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
