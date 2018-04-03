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
    
    Algorithm(String n, AlgorithmType t, RunConfig r){
        name = n;
        type = t;
        config = r;
    }
}
