/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

import java.io.Serializable;

/**
 *
 * @author HP
 */
public class AlgorithmThread implements Serializable, Runnable{
    private Algorithm algo;
    private DataState data;
    private int max;//Subtract from last run
    //private MyChart chart;
    
    AlgorithmThread(Algorithm a, DataState d/*, MyChart c*/){
        algo = a;
        data = d;
        max = a.getConfig().getMaxIter();
        //chart = c;
    }

    @Override
    public void run() {
        if(algo.getConfig().getContinuous()){
            //update all the time fam
        } else {
            int interval = algo.getConfig().getUpdateInterval();
        }
    }
}
