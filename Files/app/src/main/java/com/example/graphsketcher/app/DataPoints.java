package com.example.graphsketcher.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Class used for storing (x,y) coordinates from drawings, and whether an erase was taking palce at the time
 * Created by Sam on 25/06/2014.
 */
public class DataPoints implements Parcelable, Comparable<DataPoints> {

    private float xCoord, yCoord;


    /* Constructor */
    public DataPoints(float x, float y){
        this.xCoord = x; this.yCoord = y;

    }//end of constructor

    /* Getters and Setters for the instance variables */

    public void setX(float x){
        this.xCoord=x;
    }

    public float getX(){
        return this.xCoord;
    }

    public void setY(float y){
        this.yCoord=y;
    }

    public float getY(){
        return this.yCoord;
    }

    /* Methods from the Parcelable interface */

    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        //Log.v(TAG, "writeToParcel..." + flags);
        dest.writeFloat(xCoord);
        dest.writeFloat(yCoord);
    }//end of writeToParcel method

    public DataPoints(Parcel in){
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        xCoord = in.readFloat();
        yCoord = in.readFloat();
    }


    public static final Parcelable.Creator<DataPoints> CREATOR = new Creator<DataPoints>() {
        public DataPoints createFromParcel(Parcel source) {
            return new DataPoints(source);
        }
        public DataPoints[] newArray(int size) {
            return new DataPoints[size];
        }
    };

    /* May be used in future for sorting coordinates */
    public int compareTo(DataPoints dp){
        if(this.yCoord == dp.getY()){
            if(this.xCoord > dp.getX()) return 1;
            else if(this.xCoord < dp.getX()) return -1;
            else if(this.xCoord == this.getX()) return 0;
        }
        else if(this.yCoord > dp.getY()) return 1;
        return -1;
    }

}//end of class
