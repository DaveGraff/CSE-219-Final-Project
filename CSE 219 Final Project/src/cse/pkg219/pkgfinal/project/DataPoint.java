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
    private double x;
    private double y;
    private String series;
    private String name;
    
    DataPoint(double xi, double yi, String seriesi, String namei){
        x = xi;
        y = yi;
        series = seriesi;
        name = namei;
    }
    
    public String getSeries(){
        return series;
    }
    
    public String getName(){
        return name;
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public void setSeries (String seriesi){
        series = seriesi;
    }
    
    @Override
    public String toString(){
        String s = "@" + name + "\t" + series + "\t" + x + "," + y + "\n"; 
        return s;
    }
}
