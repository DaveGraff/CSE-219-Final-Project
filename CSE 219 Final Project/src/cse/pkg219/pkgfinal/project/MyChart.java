/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import java.io.Serializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.VBox;

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
        
    }
    
    public LineChart getChart(){
        return chart;
    }
}
