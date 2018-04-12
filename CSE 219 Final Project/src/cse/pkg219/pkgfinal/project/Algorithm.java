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
public class Algorithm {
    private String name;
    private AlgorithmType type;
    private RunConfig config;
    
    /**
     * Instantiates an Algorithm object with
     * all the necessary information to find
     * and run it.
     * 
     * @param n The name of the given algorithm
     * @param t The type of the algorithm 
     * as defined by AlgorithmType
     */
    Algorithm(String n, AlgorithmType t){
        name = n;
        type = t;
        config = new RunConfig();
    }
    
    public AlgorithmType getType(){
        return type;
    }
    
    public String getName(){
        return name;
    }
}
