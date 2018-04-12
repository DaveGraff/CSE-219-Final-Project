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
public class AlgorithmThread implements Serializable{
    private Algorithm algo;
    private DataState data;
    private MyChart chart;
    
    AlgorithmThread(Algorithm a, DataState d, MyChart c){
        algo = a;
        data = d;
        chart = c;
    }
}
