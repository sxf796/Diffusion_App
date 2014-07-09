package com.example.graphsketcher.app;

import android.content.Context;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Class for the view that will animate the evolution of the concentration profile over time
 *
 * Created by Sam on 25/06/2014.
 */
public class PracticeView extends View {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial colour
    private int paintColor = Color.BLUE;
    //canvas
    private Canvas drawCanvas, mainCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;


    private float brushSize;

    private ArrayList<double[]> solutionValues;
    private int positionInSolutionValues; //gets the correct set of doubles to display
    private double[] currentValues; //the set of doubles that are currently being displayed


    private ArrayList<Coordinates> coordinatesArrayList; //move away from using this now
    private int graphViewHeight;

    /* Practice on the new thing, before moving over to the graphics */
    private ArrayList<ArrayList<Double>> newAttemptArrayList;
    public void setNewAttemptArrayList(ArrayList<ArrayList<Double>> a){this.newAttemptArrayList = a;}

    /* Constructor for the view class */
    public PracticeView(Context context, AttributeSet attrs){
        super(context, attrs);
        setUpView();
    }//end of constructor

    /* Used to instantiate variables and set the view up */
    public void setUpView(){

        brushSize = 5;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);

        //set initial path properties
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        positionInSolutionValues = 0;

    }//end of setUpView


     /* Code for handling setting up drawing surface */
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){ //find out when this method gets called when a view gets created
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
       // drawCanvas = new Canvas(canvasBitmap);

    }//end of onSizeChanged method

    @Override
    public void onDraw(Canvas canvas){


        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
       // canvas.drawColor(Color.WHITE); //repaint the canvas to be white (for refresh testing)

        canvas.drawPath(drawPath, drawPaint);



        //draw the current set of doubles
        float y = canvas.getHeight();
        float x = canvas.getWidth();

     //   drawPath.moveTo(0, canvas.getHeight()); // this will have to be set in the future to mirror the chosen boundary conditions


        //TODO need to think about what to plot on the y axis - pass the original, and use the y values
        for(int i=1; i<coordinatesArrayList.size()-1; i++){
            //canvas.drawPoint((float)solutionValues.get(positionInSolutionValues)[i], coordinatesArrayList.get(i).getCurrentValue().y, drawPaint);


            //canvas.drawPoint(coordinatesArrayList.get(i).getCurrentValue().x, (float)(solutionValues.get(positionInSolutionValues)[i] ) , drawPaint);
            if(i>1) canvas.drawLine(coordinatesArrayList.get(i-1).getCurrentValue().x, (float)(solutionValues.get(positionInSolutionValues)[i-1] ),
                    coordinatesArrayList.get(i).getCurrentValue().x, (float)(solutionValues.get(positionInSolutionValues)[i] ), drawPaint);
            else{

            }

        }//end of for loop

        /* Attempt at drawing from the new array list - should take a bit of time to get completely correct i think */
        //display the


//        for(int i=1; i<currentValues.length-1; i++){
//
//            //8 and 98 are place holders for now - will be replaced by max a and y values in the future
//            drawPath.lineTo((float) ((i/50.0)*canvas.getWidth()), (float) (canvas.getHeight()-((currentValues[i]/98)*canvas.getHeight())));
//          //  drawCanvas.drawLine((float)((i-1/50.0)*canvas.getWidth()), (float) (canvas.getHeight()-((currentValues[i-1]/98)*canvas.getHeight())),(float)((i/50.0)*canvas.getWidth()), (float) (canvas.getHeight()-((currentValues[i]/98)*canvas.getHeight())), drawPaint);
//        }

        //plot the first point from the coordinates
//        drawCanvas.drawPoint(coordinatesArrayList.get(0).getMidX(), coordinatesArrayList.get(0).getCurrentValue().y, drawPaint);
//        //loop for the coordinate array list, and plot the values
//        for(int i=1; i<coordinatesArrayList.size(); i++){
//            //drawCanvas.drawPoint(coordinatesArrayList.get(i).getMidX(), coordinatesArrayList.get(i).getCurrentValue().y, drawPaint);
//            drawCanvas.drawLine(coordinatesArrayList.get(i-1).getMidX(), coordinatesArrayList.get(i-1).getCurrentValue().y,
//                    coordinatesArrayList.get(i).getMidX(), coordinatesArrayList.get(i).getCurrentValue().y, drawPaint);
//        }
//
//        drawCanvas.drawPath(drawPath, drawPaint);

    }//end of onDraw method

    /* Called from the solution() method in the PracticeActivity class */
    public void setSolutionValues(ArrayList<double[]> a){
        this.solutionValues = a;
        this.currentValues = this.solutionValues.get(0);
        this.positionInSolutionValues = 0;
    }

    /* Cycles through the graphs that have been generated */
    public void cycleGraphs(){




    }//end of cycleGraphs


    /* Called by the next_btn */
    public boolean displayNextPicture(){

        if(this.positionInSolutionValues<solutionValues.size()-1){
            this.positionInSolutionValues++;
            this.currentValues = this.solutionValues.get(this.positionInSolutionValues);
            invalidate();
            return true;
        }
        else{ //loop back to the first image (temporary solution)
            this.positionInSolutionValues = 0;
            this.currentValues = this.solutionValues.get(this.positionInSolutionValues);
            invalidate();
            return true;
        }


    }//end of next picture method

    /* Called by the prev_btn */
    public boolean displayPreviousPicture(){

        if(this.positionInSolutionValues>0){
            this.positionInSolutionValues--;
            currentValues = this.solutionValues.get(this.positionInSolutionValues);
            invalidate();
            return true;
        }
        else{
            this.positionInSolutionValues = this.solutionValues.size()-1;
            this.currentValues = this.solutionValues.get(this.positionInSolutionValues);
            invalidate();
            return true;
        }

    }//end of previous picture method

    /* resets the canvas and calls invalidate */
    public void canvasRedraw(){

       // mainCanvas.drawColor(Color.WHITE); //clears the canvas
        invalidate();

    }//end of canvasRedraw method

    //setter for the coordinate array list
    public void setCoordinatesArrayList(ArrayList<Coordinates> alc){
        this.coordinatesArrayList = alc;
    }

    public void setGraphViewHeight(int height){
        this.graphViewHeight = height;
    }

}//end of class
