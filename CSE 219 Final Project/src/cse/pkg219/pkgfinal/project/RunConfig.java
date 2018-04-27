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
public class RunConfig implements Serializable{
    int maxIter;
    int updateInterval;
    int clusterNum;
    boolean continuous;
    /**
     * The default run configuration for any algorithm
     */
    RunConfig(){
        maxIter = 5;
        updateInterval = 1;
        continuous = false;
        clusterNum = 2;
    }
    /**
     * 
     * @param m The number of iterations the algorithm will run for
     * @param u The interval at which the GUI will update the display
     * @param cn The number of clusters for a Clustering algorithm
     * @param c If the algorithm is updating continuously
     */
    RunConfig(int m, int u, int cn, boolean c){
        maxIter = m;
        updateInterval = u;
        continuous = c;
        clusterNum = cn;
    }
    
    public int getMaxIter(){
        return maxIter;
    }
    
    public void setMaxIter(int i){
        maxIter = i;
    }
    
    public int getUpdateInterval(){
        return updateInterval;
    }
    
    public void setUpdateInterval(int i){
        updateInterval = i;
    }
    
    public boolean getContinuous(){
        return continuous;
    }
    
    public void setContinuous(boolean b){
        continuous = b;
    }
    
    public int getClusterNum(){
        return clusterNum;
    }
    
    public void setClusterNum(int a){
        clusterNum = a;
    }
}
