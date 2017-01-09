package com.example.wanglisheng.pulsedetection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by malloc on 1/6/2017.
 */

public class NeuralNetwork {

    int inputLayerN, midLayerN, outputLayerN;
    String pathToData;
    float[] biases,w2_3;
    float[][] w1_2;
    Vector<NN_Node> nn_graph;




    public NeuralNetwork(int inputLayerN, int midLayerN, int outputLayerN, String pathToData){

        this.inputLayerN = inputLayerN;
        this.midLayerN = midLayerN;
        this.outputLayerN = outputLayerN;
        this.pathToData = pathToData;
        nn_graph = new Vector(midLayerN+outputLayerN);

        File biasesF = new File(pathToData+"/bias.txt");
        File w1_2F = new File(pathToData+"/weights1-2.txt");
        File w2_3F = new File(pathToData+"/weights2-3.txt");



        try{

            BufferedReader br = new BufferedReader(new FileReader(biasesF));
            BufferedReader br12 = new BufferedReader(new FileReader(w1_2F));
            BufferedReader br23 = new BufferedReader(new FileReader(w2_3F));

            String line;
            String[] lineD = null;
            String[] lineD23 = null;
            int index = 0;

            biases = new float[midLayerN+outputLayerN];
            w2_3 = new float[midLayerN];
            w1_2 = new float[midLayerN][inputLayerN];

            while((line = br.readLine())!= null){
                 lineD = line.split(" ");
            }

            while((line = br23.readLine())!= null){
                 lineD23 = line.split(" ");
            }
            //read two dimensional file
            String[] lineD12 = null;
            while((line = br12.readLine())!= null){
                lineD12 = line.split(" ");
                for(int j = 0; j < inputLayerN; j++){
                    w1_2[index][j] = Float.parseFloat(lineD12[j]);
                }
                index++;
            }

            br.close();
            br12.close();
            br23.close();

            for(int i = 0; i < midLayerN+outputLayerN; i++){
                 biases[i] = Float.parseFloat(lineD[i]);
            }
            for(int i = 0; i < midLayerN; i++){
                 w2_3[i] = Float.parseFloat(lineD23[i]);
            }


        }catch (IOException e){e.printStackTrace();}

    }

    public void create(){


        int i;
        for(i = 0; i < midLayerN; i++){
            float[] tempWeights = new float[inputLayerN];
            for(int j = 0; j < inputLayerN; j++)tempWeights[j] = w1_2[i][j];


            nn_graph.add(i,new NN_Node(biases[i],tempWeights,false));
        }
            nn_graph.add(i,new NN_Node(biases[i],w2_3,true));
    }

    public float estimate(float[] inputData){
        float[] output1_2 = new float[midLayerN];

        for (int i=0 ; i < midLayerN; i++){
            output1_2[i] = nn_graph.get(i).calculate(inputData);
        }
        return nn_graph.get(midLayerN+outputLayerN-1).calculate(output1_2);
    }

    public Vector<NN_Node> testGetData(){
        return nn_graph;
    }
}
