/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author HP
 */
public class MyChart implements Serializable{
    private LineChart chart;
    private VBox container;
    
    /**
     * Creates an instance of MyChart to be used by the application
     */
    MyChart(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new LineChart(xAxis, yAxis);
        container = new VBox(chart);
    }
    
    /**
     * Prompts the user to save the current graph representation
     */
    public void saveGraph(){
        Stage pStage = new Stage();
        FileChooser saver = new FileChooser();
        saver.setInitialFileName("myCoolGraph");
        saver.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = saver.showSaveDialog(pStage);
        WritableImage image = container.snapshot(new SnapshotParameters(), null);
        try{
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch(IOException | IllegalArgumentException e){
        }
    }
    
    public LineChart getChart(){
        return chart;
    }
}
