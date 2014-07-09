package com.example.graphsketcher.app;

import java.util.ArrayList;

public class PracticeModel{

    //instance variables that are needed for the equations to work
    private double diffusionCoefficient;
    private double domainSize; //these will be used at some point, possible for setting the values below
    private double integrationTime;

    private int numberOfGridPoints;
    private int numberOfTimeSteps;

    private double deltaX;
    private double deltaT;

    private double[] concentrationValues1; //initially set to the values passed from the main activity
    private double[] concentrationValues2; //used as a helper array during the solution() method
    private ArrayList<double[]> solutionValues; //stores the double[]'s that are needed for the animations
    private ArrayList<Coordinates> coordinatesArrayList; //passed from the activity, and used as input for the equations
    private int graphViewHeight; //used for scaling the values in the equations


    /* Constructor - to begin with the original values will be set
     *    Eventually - these will be set by the user
     */
    public PracticeModel(ArrayList<Coordinates> alc, int graphViewHeight){

        this.coordinatesArrayList = alc;
        diffusionCoefficient = 0.5;
        domainSize = 1.0;
        integrationTime = 0.1;
        this.graphViewHeight = graphViewHeight;

        numberOfGridPoints = this.coordinatesArrayList.size() + 2; //the plus two is for the boundary conditions
        numberOfTimeSteps = 25000;

        deltaX = 2.0/(numberOfGridPoints - 1); //check where this comes from
        deltaT = ((deltaX*deltaX)/(2*diffusionCoefficient))*0.9; //practice messing around with these values



        setInitialConcentrationValues(); //fill concentrationValues with values from the coordinate array list

        //call the method that generates doubles[] containing solution values
        solutionValues = new ArrayList<double[]>();
        solution();

    }//end of constructor


    /* The Getters/Setters that are needed by PracticeActivity */
    public ArrayList<double[]> getSolutionValues(){ return this.solutionValues; }

    public ArrayList<Coordinates> getCoordinatesArrayList() {return this.coordinatesArrayList;}

    public void setCoordinatesArrayList(ArrayList<Coordinates> alc) {this.coordinatesArrayList = alc;}


    public void setDiffusionValues(){

    }

    /* For now this sets them manually - think of a way of getting them set by user by the end of the day
     *    i.e. what will the inpt parameters be, and ho w will they convert it into what is needed
     */
    public void setInitialConcentrationValues(){

        /* Initialise the array and set preliminary-values */
        concentrationValues1 = new double[numberOfGridPoints]; //not sure if it should be plus or minus one
        concentrationValues2 = new double[numberOfGridPoints];

        //set boundary conditions (set to the height, which is the same as being zero at both ends)
        concentrationValues1[0] = graphViewHeight/2; concentrationValues1[numberOfGridPoints-1] = graphViewHeight/2;
        concentrationValues2[0] = graphViewHeight/2; concentrationValues2[numberOfGridPoints-1] = graphViewHeight/2;



        //find the maximum value in the coordinate array list, and pass the values into the concentrationValues array
        double maximumValue = 0.0;
        for(int i=1; i<coordinatesArrayList.size()-1; i++){

            double value =   coordinatesArrayList.get(i).getCurrentValue().y;
            concentrationValues1[i] = value;
            concentrationValues2[i] = value; //for now, this can be changed next time
            if(maximumValue<value) maximumValue = value; //this might be used in the future for scaling

        }//end of for loop

    } //end of setting the initial concentration values method

    /* Method that builds the array list containing the concentration values at each time step */
    public void solution(){

        for(int i=0; i<numberOfTimeSteps; i++){ //run the loop for the number of time steps

            for(int j=1; j<concentrationValues1.length-1; j++){ //run this loop across the array of values

                //half the time, take values from 1 and update 2

                    concentrationValues1[j] = (((diffusionCoefficient * deltaT) / deltaX) *
                            (concentrationValues1[j - 1] + concentrationValues1[j + 1] - 2 * concentrationValues1[j])) + concentrationValues1[j];

                    if(i%100==0) solutionValues.add(concentrationValues1.clone()); //mess around with changing this now



            }//end of inner for




            //print of the values here to check them, and to see the effect that changing the values has


        }//end of outer for

    }//end of solution method

}//end of class
