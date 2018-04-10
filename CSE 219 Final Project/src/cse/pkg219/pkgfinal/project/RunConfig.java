/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

/**
 *
 * @author HP
 */
public class RunConfig {
    int maxIter;
    int updateInterval;
    int clusterNum;
    boolean continuous;
    
    RunConfig(){
        maxIter = 100;
        updateInterval = 5;
        continuous = false;
        clusterNum = 2;
    }
    
    RunConfig(int m, int u, int cn, boolean c){
        maxIter = m;
        updateInterval = u;
        continuous = c;
        clusterNum = cn;
    }
}
