package com.example.wanglisheng.pulsedetection;

/**
 * Created by malloc on 1/2/2017.
 */

import android.graphics.Bitmap;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class VideoToPhotosParser {

    String pathToVideo, pathToWrite;
    final String baseFname = "sampleSub";
    AndroidFrameConverter frameConv;


    public VideoToPhotosParser(String pathToVideo, String pathToWrite){

        //初始化参数
        this.pathToVideo = pathToVideo;
        this.pathToWrite = pathToWrite;

        //创建所有数据的目录
        File newDir = new File(pathToWrite);
        if(!newDir.exists()){
            boolean success = newDir.mkdir();
        }

        //创建以后需要的frame converter
        this.frameConv = new AndroidFrameConverter();
    }

    public void parse(int start, int end){
        //每个样本的对象有两个视频.Index指的是对象的指标.subIndex指的是视频的指标.
        for(int index = start; index <= end; index++) {
            for (int subIndex = 1; subIndex <= 2; subIndex++) {
                //每个对象有自己的目录
                File newFileDir = new File(pathToWrite + "/" + baseFname + index);
                if (!newFileDir.exists()) {
                    boolean success = newFileDir.mkdir();
                }

                FFmpegFrameGrabber grab = new FFmpegFrameGrabber(pathToVideo + baseFname + index + "-" + subIndex + ".mp4");
                FileOutputStream fOut = null;

                try {
                    Bitmap img;
                    grab.start();

                    for (int i = 1; i < 12; i++) {
                        grab.setFrameNumber(i);
                        Frame frame = grab.grabImage();
                        img = frameConv.convert(frame);
                        File file = new File(pathToWrite + "/" + baseFname + index, "parsed"+ i +"("+ subIndex+")"+".jpg");

                        fOut = new FileOutputStream(file);
                        img.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        fOut.flush();

                    }

                    fOut.close();


                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}