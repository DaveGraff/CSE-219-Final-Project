/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    /**
     * Attempts to save the data present in TextArea. 
     * Will alert the user if there is an error.
     * 
     * @param text The data from the TextArea to be saved.
     */
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
    
    /**
     * Checks for errors in the data
     * 
     * @param text The data being checked
     * @return 
     */
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
    /**
     * Checks if a given line starts with the required "@" symbol
     * 
     * @param name The name of the line
     * @return
     * @throws cse.pkg219.pkgfinal.project.DataState.InvalidDataNameException 
     */
    private String checkedName(String name) throws InvalidDataNameException{
        if (!name.startsWith("@")){
            throw new InvalidDataNameException(name);
        }
        return name;
    }
    /**
     * Gives the user an Alert if there was an error in saving their data
     * 
     * @param reason The reason the alert was shown
     */
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
    /**
     * Checks if the result returned from loadingHelper is valid
     */
    public void handeLoadRequest(){
        String maybe = loadingHelper();
        if (!maybe.equals(""))
            data = maybe;
    }
    /**
     * Loads data from a user-selected TSD file
     * @return The data to be represented
     */
    private String loadingHelper() {
        String bleh = "";
        Stage pStage = new Stage();
        FileChooser loader = new FileChooser();
        loader.getExtensionFilters().add(new FileChooser.ExtensionFilter("TSD Files", "*.tsd"));
        file = loader.showOpenDialog(pStage);
        String fileName = "";
        try {
            fileName = file.toString();
            String line = null;
            String total = "";

            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                total = total.concat(line + "\n");
            }
            bufferedReader.close();
            if (!isWrong(total)) {
                isSaved = true;
                bleh = total;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        } catch (NullPointerException e) {
        }
        return bleh;
    }
    
    public boolean handleNewRequest(){
        if(!isSaved){
            Stage checkStage = new Stage();
            Button cancelButton = new Button("Cancel");cancelButton.setCancelButton(true);
            Button continueButton = new Button("Continue without saving");
            Button saveButton = new Button("Save");
            Label checkLabel = new Label("Your current data is unsaved, would you like to save it?");
            VBox container = new VBox(checkLabel, new HBox(continueButton, saveButton, cancelButton));
            Scene scene = new Scene(container);
            checkStage.setScene(scene);
            
            saveButton.setOnAction(e -> {
                checkStage.close();
                handleSaveRequest(data);
                if(isSaved = true)
                    data = "";
            });
            
            cancelButton.setOnAction(e -> checkStage.close());
            
            continueButton.setOnAction(e -> {
                data = "";
                checkStage.close();
            });
            
            checkStage.showAndWait();
            
            
        } else if(isSaved){
            data = "";
        }
        return data.equals("");
    }
}
