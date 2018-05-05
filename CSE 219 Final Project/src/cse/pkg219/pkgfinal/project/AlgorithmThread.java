/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 *
 * @author HP
 */
public class AlgorithmThread extends Thread implements Serializable{
    private Algorithm algo;
    private ArrayList<DataPoint> data = new ArrayList<>();
    private int max;//Subtract from last run
    private MyChart chart;
    private double[] bounds = new double[4];//min x, max x, min y, max y
    private Button run;
    private HBox runLine;
    private int counter = 1;
    private Button saveGraph;
    
    private volatile boolean stop = false;
    
    AlgorithmThread(Algorithm a, ArrayList<DataPoint> d, MyChart c, Button r, HBox h, Button s){
        algo = a;
        d.forEach(e -> data.add(e));
        max = a.getConfig().getMaxIter();
        chart = c;
        run = r;
        runLine = h;
        data.forEach(f -> {
            if (f.getX() < bounds[0]) {
                bounds[0] = f.getX();
            } if (f.getX() > bounds[1]) {
                bounds[1] = f.getX();
            } if (f.getY() < bounds[2]) {
                bounds[2] = f.getY();
            } if (f.getY() > bounds[3]) {
                bounds[3] = f.getY();
            }
        });
        saveGraph = s;
    }
    
    public void randomClassifier() {
        Random RAND = new Random();
        int xCoefficient = new Long(-1 * Math.round((2 * RAND.nextDouble() - 1) * 10)).intValue();
        int yCoefficient = 10;
        int constant = RAND.nextInt(11);
        int thing = counter;
        data.add(new DataPoint((-yCoefficient * bounds[2] - constant) / xCoefficient, (-xCoefficient * bounds[0] - constant) / yCoefficient, "Random" + thing, "R" + (2 * counter)));
        data.add(new DataPoint((-yCoefficient * bounds[3] - constant) / xCoefficient, (-xCoefficient * bounds[1] - constant) / yCoefficient, "Random" + thing, "R" + (2 * counter + 1)));
    }
    
    public void algoPicker(){
        switch(algo.getName()){
            case "Random Classifier": randomClassifier();
            break;
            case "Random Clusterer": randomClusterer();
            break;
            case "K Means Clusterer": System.out.println("K means");
        }
    }
    
    public void randomClusterer(){
        ArrayList<String> forClustering = new ArrayList<>();
        for(int i = 0; i < algo.getConfig().getClusterNum(); i++){
            forClustering.add("Random"+i);
        }
        data.forEach(e -> {
            int index = (int)(Math.random() * forClustering.size());
            e.setSeries(forClustering.get(index));
        });
    }
    
    /**
     * Sets the value of stop to false,
     * making the thread stop after it
     * finishes its immediate task
     */
    public void stopper(){
        stop = true;
    }
    
    @Override
    public void run() {
        int interval = algo.getConfig().getUpdateInterval();
        if (algo.getConfig().getContinuous()) {
            Platform.runLater(() -> saveGraph.setDisable(true));
            int iter = algo.getConfig().getMaxIter();
            int i;
            for (i = 1; i < iter + 1; i++) {
                if (!stop) {
                    counter = i;
                    algoPicker();
                    if (i % interval == 0) {
                        try {
                            Platform.runLater(() -> chart.processData(data));
                            Thread.sleep(1200);
                        } catch (InterruptedException ex) {
                        }//should *never* happen
                    }
                } else {
                    i = iter + 1;
                }
            }
            if (i * interval < iter && !stop) {
                Platform.runLater(() -> chart.processData(data));
            }
            Platform.runLater(() -> saveGraph.setDisable(false));
        } else {
            Button cont = new Button("Continue");
            Platform.runLater(() -> {
                runLine.getChildren().clear();
                runLine.getChildren().addAll(run, cont);
            });
            cont.setOnAction(q -> {
                Platform.runLater(() -> cont.setDisable(true));
                Platform.runLater(() -> saveGraph.setDisable(true));
                algoPicker();
                counter++;
                if (counter * algo.getConfig().getUpdateInterval() > algo.getConfig().getMaxIter()) {
                    Platform.runLater(() -> {
                        runLine.getChildren().remove(cont);
                        run.setDisable(false);
                    });
                    counter = 1;
                }
                Platform.runLater(() -> chart.processData(data));
                Platform.runLater(() -> cont.setDisable(false));
                Platform.runLater(() -> saveGraph.setDisable(false));
            });
            cont.fire();
        }
    }
}
