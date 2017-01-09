package com.example.wanglisheng.pulsedetection;

/**
 * Created by malloc on 1/7/2017.
 */
public class Normalization {

    float limitMax,limitMin,baseMin,baseMax;

    public Normalization(){

    }
    public Normalization(float baseMin, float baseMax){
        limitMax = 1;
        limitMin = -1;
        this.baseMin = baseMin;
        this.baseMax = baseMax;

    }

    public float normalize(float x ){

        return ((limitMax - limitMin) * (x - baseMin) / (baseMax - baseMin)) + limitMin;
    }

    public void init(float baseMin, float baseMax){
        this.baseMin = baseMin;
        this.baseMax = baseMax;
        limitMax = 1;
        limitMin = -1;

    }
}
