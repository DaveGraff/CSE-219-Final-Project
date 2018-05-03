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
    private ArrayList<DataPoint> data;
    private int max;//Subtract from last run
    private MyChart chart;
    private double[] bounds = new double[4];//min x, max x, min y, max y
    private Button run;
    private HBox runLine;
    private int counter = 1;
    
    private volatile boolean stop = false;
    
    AlgorithmThread(Algorithm a, ArrayList<DataPoint> d, MyChart c, Button r, HBox h){
        algo = a;
        data = d;
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
    }
    
    public void addData() {
        Random RAND = new Random();
        int xCoefficient = new Long(-1 * Math.round((2 * RAND.nextDouble() - 1) * 10)).intValue();
        int yCoefficient = 10;
        int constant = RAND.nextInt(11);
        int thing = counter;
        data.add(new DataPoint((-yCoefficient * bounds[2] - constant) / xCoefficient, (-xCoefficient * bounds[0] - constant) / yCoefficient, "Random" + thing, "R" + (2 * counter)));
        data.add(new DataPoint((-yCoefficient * bounds[3] - constant) / xCoefficient, (-xCoefficient * bounds[1] - constant) / yCoefficient, "Random" + thing, "R" + (2 * counter + 1)));
    }
    
    public void stopper(){
        stop = true;
    }
    
    @Override
    public void run() {
        int interval = algo.getConfig().getUpdateInterval();
        if (algo.getConfig().getContinuous()) {
            int iter = algo.getConfig().getMaxIter();
            int i;
            for (i = 1; i < iter + 1; i++) {
                if (!stop) {
                    counter = i;
                    addData();
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
        } else {
            Button cont = new Button("Continue");
            Platform.runLater(() -> {
                runLine.getChildren().clear();
                runLine.getChildren().addAll(run, cont);
            });
            cont.setOnAction(q -> {
                Platform.runLater(() -> cont.setDisable(true));
                addData();
                counter++;
                if (counter * algo.getConfig().getUpdateInterval() >= algo.getConfig().getMaxIter()) {
                    Platform.runLater(() -> {
                        runLine.getChildren().remove(cont);
                        run.setDisable(false);
                    });
                    counter = 1;
                }
                Platform.runLater(() -> chart.processData(data));
                Platform.runLater(() -> cont.setDisable(false));
            });
            cont.fire();
        }
    }
}
