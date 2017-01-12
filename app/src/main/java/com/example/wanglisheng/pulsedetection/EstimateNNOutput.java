package com.example.wanglisheng.pulsedetection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by malloc on 1/8/2017.
 */

public class EstimateNNOutput {

    String pathToData, pathToModel;
    Normalization normalizer;

    public EstimateNNOutput(String path, String filename){
        pathToData = path + "/" + filename;
        normalizer = new Normalization();
        pathToModel = path;
    }

    public float estimateOutput(){

        File testInputFile = new File(pathToData);
        float[] testInput = new float[60];
        float min = 200;
        float max = -200;
        try{

            BufferedReader br = new BufferedReader(new FileReader(testInputFile));


            String line;
            String[] lineD = null;

            line = br.readLine();

            lineD = line.split(" ");

            for(int i = 0; i < 60; i++){
                int j = i;
                float temp = Float.parseFloat(lineD[j]);
                max = temp > max ? temp : max;
                min = temp < min ? temp : min;
                testInput[i] = temp;
            }
            br.close();
            normalizer.init(min,max);
            for (int i = 0; i < 60; i++) testInput[i] = normalizer.normalize(testInput[i]);

        }catch (IOException e){e.printStackTrace();}


        NeuralNetwork neuralNetwork = new NeuralNetwork(60,10,1,pathToModel);
        neuralNetwork.create();

        return neuralNetwork.estimate(testInput);


    }
}

