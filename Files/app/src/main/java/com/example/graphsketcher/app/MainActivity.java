package com.example.graphsketcher.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener { //, OnLongClickListener

    private SketchAreaView mSketchAreaView;
    private Button mRefreshButton;
    private Button mDoneButton;
//    private ImageButton mUndoButton;
//    private ImageButton mRedoButton;
    private CharSequence[] numberOfPixels = {"1", "3", "5", "10"}; //used for undo and redo operations
    private Button mGridPositionsButton;
    private int sketchAreaWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate the instance variables
        mSketchAreaView = (SketchAreaView) findViewById(R.id.drawingView);
        mRefreshButton = (Button) findViewById(R.id.refresh_btn);
        mDoneButton = (Button) findViewById(R.id.done_btn);
//        mUndoButton = (ImageButton) findViewById(R.id.undo_btn);
//        mRedoButton = (ImageButton) findViewById(R.id.redo_btn);
        mGridPositionsButton = (Button) findViewById(R.id.gridPos_btn);

        //set the on click listeners for the button
        mDoneButton.setOnClickListener(this);
        mRefreshButton.setOnClickListener(this);
//        mRedoButton.setOnClickListener(this);
//        mUndoButton.setOnClickListener(this);
        mGridPositionsButton.setOnClickListener(this);

        //long click listeners for the undo and redo buttons
//        mRedoButton.setOnLongClickListener(this);
//        mUndoButton.setOnLongClickListener(this);

        //scale the SketchAreaView based on the number of grid points
        sketchAreaWidth = 900;

        mSketchAreaView.getLayoutParams().width = sketchAreaWidth;

    }//end of onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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
    }

    /* Handles the touch events for the buttons */
    @Override
    public void onClick(View v){

         if(v.getId()==R.id.refresh_btn){ //code below creates pop-up dialogue
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    mSketchAreaView.startNewDrawing(); //refreshes the canvas


                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();

        }//end of refresh button
        else if(v.getId()==R.id.done_btn) {

            //create an intent, attach the array list to it and pass to the next activity
            Intent intent = new Intent(this, PracticeActivity.class);

            ArrayList<Coordinates> alc = mSketchAreaView.getCoordinateArrayList();
            intent.putExtra("main_activity_coordinate_arraylist", alc);
            intent.putExtra("sketch_area_width", sketchAreaWidth);
            intent.putExtra("sketch_area_height", mSketchAreaView.getHeight()); //used for scaling the data points
            System.out.println("HEIGHT OF THE GRAPH VIEW IS: " + mSketchAreaView.getHeight());
            startActivity(intent);

        }//end of done button


        else if(v.getId()==R.id.gridPos_btn){ //open up a number picker dialog, and call appropriate method in view
             //open up a dialogue box
             AlertDialog.Builder alert = new AlertDialog.Builder(this);

             alert.setTitle("Grid Points");
             alert.setMessage("Select the number of grid points");

            // Set an EditText view to get user input
             final EditText input = new EditText(this);
             alert.setView(input);

             alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) {
                     String value = input.getText().toString();
                     try{
                         int numberOfGridPoints = Integer.parseInt(value);
                         setGridPosition(numberOfGridPoints); //call helper method to deal with the number
                     }catch(NumberFormatException e){//ignore when a number isnt input for now
                      }
                 }//end of on click method
             });

             alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) {
                     // Canceled.
                 }
             });

             alert.show();
         }

    }//end of onClick method

//    /* Used for setting the number of coordinates handled in undo and redo things */
//    @Override
//    public boolean onLongClick(View v){
//
//        if(v.getId()==R.id.undo_btn){
//            //open up a dialogue block
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Number of positions to change"); //add this to the string class in future
//            builder.setItems(numberOfPixels, new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface d, int which){
//                    //cast the number of selected pixels to an Integer and set in view
//                    mSketchAreaView.setUndoOperations(Integer.parseInt((numberOfPixels[which]).toString()));
//                }
//            }); builder.show();return true;
//        }
//        else if(v.getId()==R.id.redo_btn){
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Number of positions to change"); //add this to the string class in future
//            builder.setItems(numberOfPixels, new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface d, int which){
//                    //cast the number of selected pixels to an Integer and set in view
//                    mSketchAreaView.setRedoOperations(Integer.parseInt((numberOfPixels[which]).toString()));
//                }
//            }); builder.show();return true;
//        }
//        return false;
//    }//end of onLongClick method

    /* Sets the number of grid points on the screen and the array used to store the grid points */
    public void setGridPosition(int numberOfGridPoints){

       //scale the SketchAreaView based on the number of grid points
       sketchAreaWidth = (900/numberOfGridPoints)*numberOfGridPoints;

       mSketchAreaView.getLayoutParams().width = sketchAreaWidth;

       //pass this value to the sketch area
       mSketchAreaView.setNumberOfGridPoints(numberOfGridPoints);

    }//end of setGridPosition method

}//end of MainActivity class
