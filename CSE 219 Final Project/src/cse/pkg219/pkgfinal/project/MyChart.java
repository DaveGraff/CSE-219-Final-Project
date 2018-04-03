/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.VBox;

/**
 *
 * @author HP
 */
public class MyChart {
    private LineChart chart;
    private VBox container;
    
    MyChart(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new LineChart(xAxis, yAxis);
        container = new VBox(chart);
    }
    
    public void saveGraph(){
        
    }
    
    public LineChart getChart(){
        return chart;
    }
}
