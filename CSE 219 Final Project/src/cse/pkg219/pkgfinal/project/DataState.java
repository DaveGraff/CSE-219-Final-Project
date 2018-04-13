

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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import javafx.geometry.Insets;
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
public class DataState implements Serializable{
    private String data;
    private boolean isSaved;
    
    private File file;
    private int line;
    private ArrayList<String> lineNames;
    private boolean isWrong;
    private String[] loadedMetaData =  new String[4];//0 = # of instances, 1 = # of labels, 2 = label names, 3 = filePath
    private ArrayList<String> labelList;
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
    
    public boolean getIsWrong(){
        return isWrong;
    }
    
    /**
     * Attempts to save the data present in TextArea. 
     * Will alert the user if there is an error.
     * 
     * @param text The data from the TextArea to be saved.
     */
    public void handleSaveRequest(String text) {
        isWrong(text);
        if(!isWrong){
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
                if(file != null)
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
    public String[] isWrong(String text){
        isWrong = false;
        lineNames = new ArrayList<>();
        line = 1;
        labelList = new ArrayList<>();
        Stream.of(text.split("\n")).map(line -> Arrays.asList(line.split("\t"))).forEach(list -> {
            try {
                String name = checkedName(list.get(0));
                if(lineNames.contains(name)){
                    saveAlert("Line " + (lineNames.indexOf(name) +1) + ": Two lines cannot share a name");
                    isWrong = true;
                }
                lineNames.add(name);
                String   label = list.get(1);
                if(!labelList.contains(label))
                    labelList.add(label);
                String[] pair  = list.get(2).split(",");
                Point2D  point = new Point2D(Double.parseDouble(pair[0]), Double.parseDouble(pair[1]));
            } catch (Exception e) {
                isWrong = true;
                saveAlert("Line " + line + ": Not a valid format");
            }
            line++;
        });
        loadedMetaData[0] = Integer.toString(lineNames.size()) + " Instances";
        loadedMetaData[1] = Integer.toString(labelList.size()) + " Labels";
        loadedMetaData[2] = "Labels: ";
        for(String string : labelList){
            loadedMetaData[2] = loadedMetaData[2].concat(string + ", ");
        }
        loadedMetaData[2] = loadedMetaData[2].subSequence(0, loadedMetaData[2].length()-2).toString();
        if(!isWrong)
            isSaved = false;
        return loadedMetaData;
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
        alert.setTitle("Error");
        alert.setHeaderText("There is an error in your data");
        alert.setContentText(reason);
        alert.showAndWait();
    }
    
    /**
     * Exception to be thrown if a line
     * of data doesn't start with "@"
     */
    public static class InvalidDataNameException extends Exception {
        private static final String NAME_ERROR_MSG = "All data instance names must start with the @ character.";

        public InvalidDataNameException(String name) {
            super(String.format("Invalid name '%s'." + NAME_ERROR_MSG, name));
        }
    }
    
    /**
     * Checks if the result returned from loadingHelper is valid
     */
    public String[] handleLoadRequest(){
        String maybe = loadingHelper();
        if (!maybe.equals("")){
            isWrong(maybe);
            if(!isWrong)
                data = maybe;
        }
        return loadedMetaData;
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
            isWrong(total);
            if (!isWrong) {
                isSaved = true;
                bleh = total;
            }
            loadedMetaData[3] = file.toString();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        } catch (NullPointerException e) {
        }
        return bleh;
    }
    
    /**
     * Prompts the user to
     * save their data
     */
    public void checkForSave() {
        Stage checkStage = new Stage();
        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
        Button continueButton = new Button("Continue without saving");
        Button saveButton = new Button("Save");
        Label checkLabel = new Label("Your current data is unsaved, would you like to save it?");
        HBox buttons = new HBox(continueButton, saveButton, cancelButton);
        buttons.setSpacing(5);
        VBox container = new VBox(checkLabel, buttons);
        container.setPadding(new Insets(10));
        container.setSpacing(5);
        Scene scene = new Scene(container);
        checkStage.setScene(scene);

        saveButton.setOnAction(e -> {
            checkStage.close();
            handleSaveRequest(data);
        });

        cancelButton.setOnAction(e -> checkStage.close());

        continueButton.setOnAction(e -> {
            checkStage.close();
            data = "";
        });

        checkStage.showAndWait();
    }
    
    /**
     * Resets the currently stored data
     * 
     * @return Whether the action was
     * successful
     */
    public boolean handleNewRequest(){
        if(!isSaved){
            checkForSave();
        }
        if(isSaved){
            data = "";
        }
        return data.equals("");
    }
}
