package com.example.wanglisheng.pulsedetection;

/**
 * Created by malloc on 1/6/2017.
 */

public class NN_Node {

    float[] weights;
    float bias;
    boolean outputN;

    public NN_Node(float bias, float[] weights, boolean outputN){
        this.bias = bias;
        this.weights = weights;
        this.outputN = outputN;
    }

    public float calculate(float[] input){

        float sum = 0;
        //calculate sum
        for(int i = 0; i < input.length; i++) sum += input[i]*weights[i];
        //subtract threshold
        sum += bias;
        //return output using sigmoid

        return (!outputN)?(float)(1/(1+Math.exp((-1)*sum))) : sum;
    }



}
