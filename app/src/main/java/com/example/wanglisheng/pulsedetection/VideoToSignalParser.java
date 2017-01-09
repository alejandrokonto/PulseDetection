package com.example.wanglisheng.pulsedetection;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by malloc on 1/3/2017.
 */

public class VideoToSignalParser {

    String pathToVideo, pathToWrite;
    final String baseFname = "sampleSub";
    AndroidFrameConverter frameConv;
    float [] brightness;
    int [] heartBeatRate;

    public VideoToSignalParser(String pathToVideo, String pathToWrite){

        //初始化参数
        this.pathToVideo = pathToVideo;
        this.pathToWrite = pathToWrite;

        //创建以后需要的frame converter
        this.frameConv = new AndroidFrameConverter();

        //每个视频分成90帧的五个组.那么,每个视频亮度/帧的矩阵是个450的矩阵
        brightness = new float[300];

        //采集过的心跳数据
        heartBeatRate = new int[]{71,70,69,82};
    }

    public void parse(int index, int subIndex){
        //每个样本的对...
                FFmpegFrameGrabber grab = new FFmpegFrameGrabber(pathToVideo + baseFname + index+".mp4");

                try {
                    Bitmap img;
                    grab.start();

                    for (int i = 1; i <= 300; i++) {

                        grab.setFrameNumber(i);
                        Frame frame = grab.grabImage();

                        img = frameConv.convert(frame);
                        brightness[i-1] = takeFrameBrightness(img);

                    }

                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }




    }

    private float takeFrameBrightness(Bitmap image){

        int pixel = 0;

        float brightnessSum = 0;
        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 0; j < image.getHeight(); j++){
                pixel = image.getPixel(i,j);
                int red = Color.red(pixel);
                int blue = Color.blue(pixel);
                int green = Color.green(pixel);
                brightnessSum += 0.299*red + 0.587*green + 0.114*blue;
            }
        }

        return (brightnessSum/(image.getHeight()*image.getWidth()));


    }

    public void writeSignalToFile(FileWriter fileWriter, int index){
        try {
            for (int i = 0; i < 300; i++) {
                fileWriter.write(Float.toString(brightness[i]) + " ");
            }
            fileWriter.write(Integer.toString(heartBeatRate[index]));
            fileWriter.write("\n");
        }catch (IOException e){e.printStackTrace();}
    }

    public float[] getBrightness(){
        return brightness;
    }


}
