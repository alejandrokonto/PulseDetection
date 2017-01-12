package com.example.wanglisheng.pulsedetection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.media.ImageWriter;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Vector;

import static java.lang.System.out;

/**
 * Created by malloc on 1/10/2017.
 */

public class YUVtoBrightnessSignal {

    Vector<byte[]> data;
    String pathToWrite;
    int w,h;

    public YUVtoBrightnessSignal(Vector<byte[]> data, String pathToWrite, int w, int h){
        this.data = data;
        this.pathToWrite = pathToWrite+"/previewFrameData.txt";
        this.w = w;
        this.h = h;
    }

    public void parse() throws IOException{
        File dataFile = new File(pathToWrite);
        FileWriter dataWriter = new FileWriter(dataFile);
        Bitmap bitmap;
        Buffer buf;
        float brightness;

        //for each frame in vector, create bitmap and then take brightness, lastly, write it on file
        //here i know i am taking a 100 frames and selecting the 60 last ones, THIS HAS TO CHANGE
        for(int i = 20; i < 80; i++){

            byte[] frame = data.get(i);
            bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.RGB_565);
            buf = ByteBuffer.wrap(frame);
            bitmap.copyPixelsFromBuffer(buf);

            dataWriter.write(Float.toString(takeFrameBrightness(bitmap)) + " ");



        }
        dataWriter.close();

    }

    private float takeFrameBrightness(int[] grayScaleData){

        float brightnessSum = 0;

        for(int i = 0; i < grayScaleData.length; i++) brightnessSum += grayScaleData[i];

        return brightnessSum/grayScaleData.length;


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

    public static void applyGrayScale(int [] pixels, byte [] data, int width, int height) {
        int p;
        int size = width*height;
        for(int i = 0; i < size; i++) {
            p = data[i] & 0xFF;
            pixels[i] = 0xff000000 | p<<16 | p<<8 | p;
        }
    }

    void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {

        final int frameSize = width * height;


        for (int j = 0, yp = 0; j < height; j++) {       int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;

            for (int i = 0; i < width; i++, yp++) {

                int y = (0xff & ((int) yuv420sp[yp])) - 16;

                if (y < 0)

                y = 0;

                if ((i & 1) == 0) {

                    v = (0xff & yuv420sp[uvp++]) - 128;

                    u = (0xff & yuv420sp[uvp++]) - 128;

                }



                int y1192 = 1192 * y;

                int r = (y1192 + 1634 * v);

                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);



                if (r < 0)

                r = 0;

                else if (r > 262143)

                r = 262143;

                if (g < 0)

                g = 0;

                else if (g > 262143)

                g = 262143;

                if (b < 0)

                b = 0;

                else if (b > 262143)

                b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);

            }

        }

    }

}
