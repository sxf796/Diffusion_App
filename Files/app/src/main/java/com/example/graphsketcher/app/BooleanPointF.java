package com.example.graphsketcher.app;

import android.graphics.PointF;

/**
 * Created by Sam on 05/07/2014.
 */
public class BooleanPointF {

    private boolean toBeRemoved;
    private PointF oldPF;
    private PointF newPF;

    /* Constructor called when a coordinate is to be removed */
    public BooleanPointF(boolean b, PointF oldPF, PointF newPF){
        this.toBeRemoved = b; this.oldPF = oldPF; this.newPF = newPF;
    }

    /* Constructor called when a coordinate needs to be added */
    public BooleanPointF(boolean b, PointF newPF){
        this.toBeRemoved = b; this.newPF = newPF; this.oldPF = null;
    }

    /* Blank constructor */
    public BooleanPointF(){}

    /* Getters for the instance variables */
    public boolean getRemoved(){return this.toBeRemoved;}

    public PointF getOldPF(){return this.oldPF;}

    public PointF getNewPF(){return this.newPF;}


}//end of class
