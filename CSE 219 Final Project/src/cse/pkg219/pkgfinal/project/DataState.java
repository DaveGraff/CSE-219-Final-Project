/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author HP
 */
public class DataState {
    private String data;
    private boolean isSaved;
    
    private File file;
    private int line;
    private ArrayList<String> lineNames;
    private boolean isWrong;
    DataState(String input){
        isSaved = true;
        data = input;
    }
    
    public boolean getIsSaved(){
        return isSaved;
    }
    
    public void setIsSaved(boolean b){
        isSaved = b;
    }
    
    public String getData(){
        return data;
    }
    
    public void setData(String d){
        data = d;
    }
    
    public void handleSaveRequest(String text) {
        if(!isWrong(text)){
            data = text;
            Stage pStage = new Stage();
            FileChooser saver = new FileChooser();
            saver.setInitialFileName("myCoolGraph");
            saver.getExtensionFilters().add(new FileChooser.ExtensionFilter("TSD Files", "*.tsd"));
            try {
                if(!isSaved){
                    file = saver.showSaveDialog(pStage);
                }
                FileWriter fileWriter;

                fileWriter = new FileWriter(file);
                fileWriter.write(data);
                fileWriter.close();
                isSaved = true;
            } catch(NullPointerException e){//I should probably make this do something...
            } catch (IOException ex) {}
        }
    }
    
    public boolean isWrong(String text){
        isWrong = false;
        lineNames = new ArrayList<>();
        line = 1;
        Stream.of(text.split("\n")).map(line -> Arrays.asList(line.split("\t"))).forEach(list -> {
            try {
                String name = checkedName(list.get(0));
                if(lineNames.contains(name)){
                    saveAlert("Line " + (lineNames.indexOf(name) +1) + ": Two lines cannot share a name");
                    isWrong = true;
                }
                lineNames.add(name);
                String   label = list.get(1);
                String[] pair  = list.get(2).split(",");
                Point2D  point = new Point2D(Double.parseDouble(pair[0]), Double.parseDouble(pair[1]));
            } catch (Exception e) {
                isWrong = true;
                saveAlert("Line " + line + ": Not a valid format");
            }
            line++;
            });
        return isWrong;
    }
    
    private String checkedName(String name) throws InvalidDataNameException{
        if (!name.startsWith("@")){
            throw new InvalidDataNameException(name);
        }
        return name;
    }
    
    public void saveAlert(String reason){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save Error");
        alert.setHeaderText("There was an error in saving your data");
        alert.setContentText(reason);
        alert.showAndWait();
    }
    
    public static class InvalidDataNameException extends Exception {
        private static final String NAME_ERROR_MSG = "All data instance names must start with the @ character.";

        public InvalidDataNameException(String name) {
            super(String.format("Invalid name '%s'." + NAME_ERROR_MSG, name));
        }
    }
}
