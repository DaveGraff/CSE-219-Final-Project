/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author HP
 */
public class AlgorithmThread implements Serializable, Runnable{
    private Algorithm algo;
    private ArrayList<DataPoint> data;
    private int max;//Subtract from last run
    private MyChart chart;
    private double[] bounds = new double[4];//min x, max x, min y, max y
    
    AlgorithmThread(Algorithm a, ArrayList<DataPoint> d, MyChart c){
        algo = a;
        data = d;
        max = a.getConfig().getMaxIter();
        chart = c;
        data.forEach(e ->{
            if(e.getX() < bounds[0])
                bounds[0] = e.getX();
            if(e.getX() > bounds[1])
                bounds[1] = e.getX();
            if(e.getY() < bounds[2])
                bounds[2] = e.getY();
            if(e.getY() > bounds[3])
                bounds[3] = e.getY();
        });
    }

    @Override
    public void run() {
        int interval = algo.getConfig().getUpdateInterval();
        if(algo.getConfig().getContinuous()){
            int iter = algo.getConfig().getMaxIter();
            for(int i = 1; i < iter + 1; i++){
                Random RAND = new Random();
                int xCoefficient =  new Long(-1 * Math.round((2 * RAND.nextDouble() - 1) * 10)).intValue();
                int yCoefficient = 10;
                int constant     = RAND.nextInt(11);
                
                data.add(new DataPoint((-yCoefficient*bounds[2] - constant)/xCoefficient, (-xCoefficient * bounds[0] - constant)/yCoefficient, "Random"+i, "R"+(2*i)));
                data.add(new DataPoint((-yCoefficient*bounds[3] - constant)/xCoefficient, (-xCoefficient * bounds[1] - constant)/yCoefficient, "Random"+i, "R"+(2*i + 1)));
                if(interval % i == 0){
                    chart.processData(data);
                    System.out.println("I fired");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {}//should *never* happen
                }
            }
            chart.processData(data);
            //update all the time fam
        } else {
            
        }
    }
}
