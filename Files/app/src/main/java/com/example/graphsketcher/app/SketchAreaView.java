package com.example.graphsketcher.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * View for the area on which users can sketch an initial concentration profile
 *
 * Created by Sam on 23/06/2014.
 */
public class SketchAreaView extends View {

    /* Instance Variables */

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial colour
    private int paintColor = Color.BLUE;

    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    private float brushSize;

    /* The below variables are used for the undo/redo operations on the graph */
    private int lastGridPoint;
    private int[] lastGridPointArray;
    private float[] lastYPosition;
    private int undoOperations, redoOperations;


    /* Testing drawing faint lines on the canvas */
    private Paint linePaint;
    private int lineColor = -7829368; //grey
    private float lineSize;
    private int numberOfGridPoints; //controls the number of lines that appear on the screen
    private float spaceBetweenLines;

    /* Variables for storing coordinates, and allowing only one value per x position to be had */
    private ArrayList<Coordinates> mCoordinateArrayList;
    private boolean arrayListInitialised = false;

    /* Constructor */
    public SketchAreaView(Context context, AttributeSet attrs){

        super(context, attrs);

        //call helper method for rest of set-up
        setUpDrawing();

    }//end of constructor

    /* Helper method for the constructor - sets up the needed instance variables */
    private void setUpDrawing(){

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


        undoOperations = 1; redoOperations = 1;

        //set up variables for adding lines to the canvas
        linePaint = new Paint();
        linePaint.setColor(lineColor);
        lineSize = 2f;
        linePaint.setStrokeWidth(lineSize);
        linePaint.setAlpha(100);
        numberOfGridPoints = 100; //will be manually set in final version
        mCoordinateArrayList = new ArrayList<Coordinates>();


    }//end of setUpDrawing method

    /* Getters/Setters for instance variables */

    public void setUndoOperations(int i){
        this.undoOperations = i;
    }

    public void setRedoOperations(int i){
        this.redoOperations = i;
    }

    public ArrayList<Coordinates> getCoordinateArrayList(){return this.mCoordinateArrayList;}

    /* Code for handling setting up drawing surface */
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){ //find out when this method gets called when a view gets created
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }//end of onSizeChanged method

    @Override
    public void onDraw(Canvas canvas){

        //draw the canvas and drawing path - called when a touch event is registered
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
        //for loop plots the gray lines onto the graph area
        for(int i=0; i<canvas.getWidth(); i+=(canvas.getWidth()/numberOfGridPoints)){ //change this to delta x at some point in the future
            //not sure if this works 100 percent just yet
            canvas.drawLine(i, 0, i, canvas.getHeight(), linePaint);

        }//end of for  loop

        //call helper method for setting up coordinates
        if(!arrayListInitialised)initialiseCoordinateArrayList();

        //TRY RESETTING THE DRAWCANVAS HERE???? - need to mess around mroe with draw canvas ad things like that -

    }//end of onDraw method


    /* Sets up the coordinate array list
    *   adds a Coordinate object for each of the positions that will appear on the screen
    */
    public void initialiseCoordinateArrayList(){

        spaceBetweenLines = drawCanvas.getWidth()/numberOfGridPoints; //this might not work with draw canvas - test it out
        //if it doesn't work, throw a paddy and give up for the weekend, until monday anyway
        float runningTotal = 0;

        for(int i=0; i<numberOfGridPoints; i++){

            mCoordinateArrayList.add(new Coordinates(runningTotal, runningTotal+spaceBetweenLines)); //TODO this is what needs testing out
            runningTotal += spaceBetweenLines;
        }//end of for loop
    arrayListInitialised = true;
    }//end of method

    /* Method for handling drawing to the screen */
    @Override
    public boolean onTouchEvent(MotionEvent event){

        final int action = event.getAction();

        //retrieve the x and y positions of the users touch
        float touchX = event.getX();
        float touchY = event.getY();

        //determine the type of touch
        switch(action){

            case MotionEvent.ACTION_DOWN: //finger touching the screen

                drawPath.moveTo(touchX, touchY);

            case MotionEvent.ACTION_MOVE: //finger moving across the screen

                 addToScreen(touchX, touchY);
                //use historical coordinates to fill in gaps
                for(int j = 0; j < event.getHistorySize(); j++)
                {
                    for(int i = 0; i < event.getPointerCount(); i++)
                    {
                        float x = event.getHistoricalX(i, j);
                        float y = event.getHistoricalY(i, j);

                           // drawCanvas.drawPoint(x, y, drawPaint);
                              addToScreen(x,y);
                    }
                }//end of for loop for historical coordinates

                break;

            case MotionEvent.ACTION_UP: //finger being lifted from the screen

                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;

            default:

                return false; //return false for an unrecognised action

        }//end of switch statement

        invalidate();
        return true;

    }//end of onTouchEvent method


    public void addToScreen(float xPos, float yPos){

        Coordinates c;
        //find out which section of the screen the point belongs to
        for(int i=0; i<mCoordinateArrayList.size(); i++) {

            c = mCoordinateArrayList.get(i);

            //if the touched x position lies between the min and max for the coordinate object
            if (xPos >= c.getMinX() && xPos <= c.getMaxX()) {

                try{ //try erasing the old position if it exists
                    PointF oldPf = c.getCurrentValue();
                    drawPaint.setColor(Color.WHITE);
                    drawPaint.setStrokeWidth(6);
                    drawCanvas.drawPoint(c.getMidX(), oldPf.y, drawPaint); //paint over the old value

                    drawPaint.setColor(paintColor);
                    drawPaint.setStrokeWidth(brushSize);

                    c.setOldValue(oldPf);
                }catch (NullPointerException e){}

                drawCanvas.drawPoint(c.getMidX(), yPos, drawPaint);

                c.setCurrentValue(new PointF(c.getMidX(), yPos));

            }//end of if

        }//end of for loop

    }//end of addToScreen method

    /* Method used to start a new drawing */
    public void startNewDrawing(){

        //include in here code that restarts the array lists containing values
        this.mCoordinateArrayList.clear(); this.arrayListInitialised = false;
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR); //clears the canvas
        invalidate();
    }

//    public void undoOperation(){
//        //take the last 3 things out of the arraylist, and add to the new one, and fill in the gap in white, and increment the
//        //numbers
//        drawPaint.setColor(Color.WHITE);
//        drawPaint.setStrokeWidth(11);
//        ForLoop:
//        for (int i = 0; i < undoOperations; i++) {
//
//            if(positionInCurrent>=0) {
//                PointF pf = pointFArrayListCurrent.remove(positionInCurrent);
//                positionInCurrent--;
//                drawCanvas.drawPoint(pf.x, pf.y, drawPaint);
//               // drawPath.lineTo(pf.x, pf.y);
//
//                pointFArrayListRedo.add(pf);
//                positionInRedo++;
//            } else break ForLoop;
//
//        }//end of for loop
//        invalidate(); //call to redraw the screen
//        //reset the colour back to original
//        drawPaint.setColor(paintColor);
//        drawPaint.setStrokeWidth(brushSize);
//
//    }//end of undo method
//
//    //Similar to the above undoOperation (but reversed)
//    public void redoOperation(){
//
//        ForLoop:
//        for(int i=0; i<redoOperations; i++){
//
//            if(positionInRedo>=0){//this conditional might need testing - do this tomorrow
//                PointF pf = pointFArrayListRedo.remove(positionInRedo); positionInRedo--;
//                drawCanvas.drawPoint(pf.x, pf.y, drawPaint);
//               // drawPath.lineTo(pf.x, pf.y);
//                pointFArrayListCurrent.add(pf); positionInCurrent++;
//            } else break ForLoop;
//
//        }//end of for loop
//        invalidate();
//    }//end of redo method
//

    /* Called from activity, and resets the screen to change the number of grid points */
    public void setNumberOfGridPoints(int n){
        System.out.println("NUMBER OF GRID POINTS: " + n);
        this.numberOfGridPoints = n; //need to change the name of this variable to numberOfGridPoints, and have deltaX as something else
        mCoordinateArrayList.clear();
        drawCanvas.drawColor(Color.WHITE); //this is new quick fix, might end up sticking
        arrayListInitialised = false;
        invalidate();
    }//end of method

}//end of Class
