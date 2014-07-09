package com.example.graphsketcher.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
//import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.TreeSet;


public class PracticeActivity extends Activity {

    private PracticeView mPracticeView;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private Button mPlayButton;
    private PracticeModel mPracticeModel;
    private ArrayList<Coordinates> coordinatesArrayList;
    private ArrayList<double[]> solutionValues;
    private int graphViewWidth;
    private int graphViewHeight;

    /* Practice code for cycling through images */
    private Handler mHandler;
    private boolean paused = false; //used for pausing and un-pausing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        mPracticeView = (PracticeView) findViewById(R.id.practice_view);
        mNextButton = (ImageButton) findViewById(R.id.next_btn);
        mPreviousButton = (ImageButton) findViewById(R.id.prev_btn);
        mPlayButton = (Button) findViewById(R.id.play_btn);
        //mPracticeModel.setCoordinatesArrayList(coordinatesArrayList);

        //receive the intent
        Intent intent = getIntent(); //move this to instance variable in future
        coordinatesArrayList =  intent.getParcelableArrayListExtra("main_activity_coordinate_arraylist");
        graphViewWidth = intent.getIntExtra("sketch_area_width",0);
        mPracticeView.getLayoutParams().width = graphViewWidth; //set the width to be the same
        graphViewHeight = intent.getIntExtra("sketch_area_height", 0);

        mPracticeModel = new PracticeModel(coordinatesArrayList, graphViewHeight); //these will have input values which are retrieved from the intent in the future
        mPracticeView.setGraphViewHeight(graphViewHeight);

        //set up the handler for practice - now used but might not be in the future
        mHandler = new Handler();


        setUpView();

    }//end of onCreate method



    /* Helper method for onCreate, passes values to the model, then redirects the responses to the view */
    public void setUpView(){

        //pass the arraylist of coordinates to the model

        //get the solution values array list from the model
        solutionValues = mPracticeModel.getSolutionValues();
      //  ArrayList<ArrayList<Double>> aad = mPracticeModel.getNewAttemptArrayList();
        //mPracticeView.setNewAttemptArrayList(aad);
        //get the values from the model and pass them to the view
        mPracticeView.setSolutionValues(solutionValues);
        mPracticeView.setCoordinatesArrayList(coordinatesArrayList);
    }//end of setUpView method

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.practice, menu);
        return true;
    }


    /* Handles the onClick events for the previous and next Image Buttons */
    public void imageCycle(View v){

        //implement cycling between images here - used for testing before implementing the animation
        if(v.getId()==R.id.next_btn){
            mPracticeView.displayNextPicture();
        }
        else if(v.getId()==R.id.prev_btn){
            mPracticeView.displayPreviousPicture();
        }

    }//end of imageCycle method

    private Object pauseLock = new Object();

    Runnable mUpdate = new Runnable(){
        public void run(){

                mPracticeView.displayNextPicture();
                mHandler.postDelayed(this, 10);
               synchronized (pauseLock) {
                   while (paused) {
                       try {
                           mUpdate.wait();
                       } catch (InterruptedException ie) {
                       }
                   }
               }
        }
    };
    /* On Click handler for the play button */
    public void animateGraphs(View v){

         if(v.getId()==R.id.play_btn)mUpdate.run();



    }//end of animateGraphics method

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }//end of onOptions... method


    /* Getters and Setters */
    public ArrayList<Coordinates> getCoordinatesArrayList(){return this.coordinatesArrayList;}

    public int getGraphViewHeight(){return graphViewHeight;}

}//end of Activity Class
