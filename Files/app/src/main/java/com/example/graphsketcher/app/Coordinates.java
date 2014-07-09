package com.example.graphsketcher.app;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Holds a min and max position - the x coordinates on the screen, and a current value on that screen
 *
 * Created by Sam on 04/07/2014.
 */
public class Coordinates implements Parcelable{ //implement parcelable in the future, and comparable?

    //private int deltaX; //the position on the screen that the coordinates belong to
    private float minX, maxX, midX; //the positions on the screen
    private float currentX, currentY; //current position on the screen
    private float oldX, oldY; //old position, used for undo operations (could turn this into an array list if need be

    /* Constructor */
    public Coordinates(float minX, float maxX){
        this.minX = minX; this.maxX = maxX;
        this.midX = (this.minX + this.maxX)/2.0f;
    }//end of constructor

    /* Getters/Setters for the min and max positions */
    public void setMinX(float minX){this.minX = minX;}

    public float getMinX(){return this.minX;}

    public void setMaxX(float maxX){this.maxX = maxX;}

    public float getMaxX(){return this.maxX;}

    public void setMidX(float midX){this.midX = midX;}

    public float getMidX(){return this.midX;}

    public void setCurrentValue(float x, float y){this.currentX = x; this.currentY = y;}

    public void setCurrentValue(PointF pf){this.currentX = pf.x; this.currentY = pf.y;}

    public PointF getCurrentValue(){return new PointF(this.currentX, this.currentY);}

    public void setOldValue(PointF pf){this.oldX = pf.x; this.oldY = pf.y;}

    public PointF getOldValue(){return new PointF(this.oldX, this.oldY);}

    /* Below is the code that allows the object to be passed through an intent */
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        //Log.v(TAG, "writeToParcel..." + flags);
        dest.writeFloat(minX);
        dest.writeFloat(maxX);
        dest.writeFloat(midX);
        dest.writeFloat(currentX);
        dest.writeFloat(currentY);
    }//end of writeToParcel method

    public Coordinates(Parcel in){
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        minX = in.readFloat();
        maxX = in.readFloat();
        midX = in.readFloat();
        currentX = in.readFloat();
        currentY = in.readFloat();
    }


    public static final Parcelable.Creator<Coordinates> CREATOR = new Creator<Coordinates>() {
        public Coordinates createFromParcel(Parcel source) {
            return new Coordinates(source);
        }
        public Coordinates[] newArray(int size) {
            return new Coordinates[size];
        }
    };


}//end of class
