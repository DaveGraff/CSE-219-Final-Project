/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import java.io.Serializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author HP
 */
public class Algorithm implements Serializable{
    private String name;
    private AlgorithmType type;
    private RunConfig config;
    
    private TextField iter;
    private TextField clusterNum;
    private TextField updateInter;
    /**
     * Instantiates an Algorithm object with
     * all the necessary information to find
     * and run it.
     * 
     * @param n The name of the given algorithm
     * @param t The type of the algorithm 
     * as defined by AlgorithmType
     */
    Algorithm(String n, AlgorithmType t){
        name = n;
        type = t;
        config = new RunConfig();
    }
    
    public AlgorithmType getType(){
        return type;
    }
    
    public String getName(){
        return name;
    }
    /**
     * Creates a screen for the user to edit the 
     * default configuration of an algorithm
     */
    public void editConfig(){
        Stage newStage = new Stage();
        newStage.setTitle(name + "Settings");
        VBox columns = new VBox();
        Scene scene = new Scene(columns);
        
        Label iterL = new Label("# of Iterations:\t ");
        iter = new TextField(Integer.toString(config.getMaxIter()));
        HBox iters = new HBox(iterL, iter);
        Label updateInterL = new Label("Update Interval:");
        updateInter = new TextField(Integer.toString(config.getUpdateInterval()));
        HBox updaters = new HBox(updateInterL, updateInter);
      
        columns.getChildren().addAll(iters, updaters);
        clusterNum = null;
        if(type == AlgorithmType.Clustering){
            Label clusterNumL = new Label("# of Clusters:\t ");
            clusterNum = new TextField(Integer.toString(config.getClusterNum()));
            columns.getChildren().add(new HBox(clusterNumL, clusterNum));
        }
        
        RadioButton cont = new RadioButton("Continuous?");
        
        columns.setSpacing(5);
        columns.setPadding(new Insets(10));
        
        Button save = new Button("Save"); save.setDefaultButton(true);
        Button cancel = new Button("Cancel"); cancel.setCancelButton(true);
        columns.getChildren().addAll(cont, new HBox(save, new Label("  "),cancel));
        
        cancel.setOnAction(e -> newStage.close());
        save.setOnAction(e -> {
            try{
                if(clusterNum != null){
                    config.setClusterNum(Integer.parseInt(clusterNum.getText()));
                }
                config.setContinuous(cont.isSelected());
                config.setMaxIter(Integer.parseInt(iter.getText()));
                config.setUpdateInterval(Integer.parseInt(updateInter.getText()));
                newStage.close();
            } catch(NumberFormatException n){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Formatting Error");
                alert.setContentText("Only integer values are allowed");
                alert.showAndWait();
            }
        });
        
        newStage.setScene(scene);
        newStage.showAndWait();
    }
}
