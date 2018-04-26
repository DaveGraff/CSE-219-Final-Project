/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cse.pkg219.pkgfinal.project;

/**
 *
 * @author David
 */
public class DataPoint {
    private int x;
    private int y;
    private String series;
    
    DataPoint(int xi, int yi, String seriesi){
        x = xi;
        y = yi;
        series = seriesi;
    }
    
    public void setSeries (String seriesi){
        series = seriesi;
    }
    
    @Override
    public String toString(){
        String s = series + "\t" + x + "," + y + "\n"; 
        return s;
    }
}
