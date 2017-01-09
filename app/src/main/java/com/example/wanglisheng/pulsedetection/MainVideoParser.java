package com.example.wanglisheng.pulsedetection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by malloc on 1/5/2017.
 */

public class MainVideoParser {
    String pathToVideo, pathToWrite;
    File frameData;
    VideoToSignalParser videoToSignalParser;

    public MainVideoParser(String pathToVideo, String pathToWrite){

        this.pathToVideo = pathToVideo;
        this.pathToWrite = pathToWrite;

        //创建所有数据的目录
        File newDir = new File(pathToWrite);
        if(!newDir.exists()){
            boolean success = newDir.mkdir();
        }

        //创建frameData.txt
        frameData = new File(pathToWrite+"/mingqiData.txt");

        //parser itself
        videoToSignalParser = new VideoToSignalParser(pathToVideo,pathToWrite);

    }

    //integer "s" stands for starting video index and "e" for ending one
    public void start(int s, int e) throws IOException {

        FileWriter frameDataWriter = new FileWriter(frameData,true);

        //计算起来每个视频的brightness矩阵,又把矩阵写在pathToWrite.txt上

        for(int i = s; i < e+1; i++ ){
            videoToSignalParser.parse(i,1);
            videoToSignalParser.writeSignalToFile(frameDataWriter,i-1);

        }
        frameDataWriter.close();

    }


}
