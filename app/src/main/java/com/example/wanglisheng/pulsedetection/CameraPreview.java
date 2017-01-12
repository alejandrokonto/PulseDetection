package com.example.wanglisheng.pulsedetection;

/**
 * Created by malloc on 1/9/2017.
 */

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;


import static android.hardware.Camera.Parameters.FLASH_MODE_TORCH;


public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback{
    private SurfaceHolder mHolder;
    private Camera mCamera;
    public Vector<byte[]> dataToAnalyze;
    TextView textView;

    public CameraPreview(Context context, Camera camera, TextView textView) {
        super(context);

        this.textView = textView;

        dataToAnalyze = new Vector<>();

        camera.setDisplayOrientation(90);
        Camera.Parameters params = camera.getParameters();
        params.setFlashMode(FLASH_MODE_TORCH);
        params.setPreviewFormat(ImageFormat.RGB_565);
        camera.setParameters(params);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {

            mCamera.setPreviewCallback(this);

            mCamera.setPreviewDisplay(holder);

            mCamera.startPreview();



        } catch (IOException e) {
            Log.d("setpreview", "Error setting camera preview: " + e.getMessage());
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Take care of releasing the Camera preview
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Preview changes or rotates, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();

        } catch (Exception e){
            //do smth on stop preview exception
        }



        // set preview size and make any resize, rotate or
        // reformatting changes here


        // start preview with new settings
        try {


            mCamera.setPreviewCallback(this);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();




        } catch (Exception e){
            Log.d("setpreview", "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

        dataToAnalyze.add(bytes);
        Log.d("OnPreviewFrameVector", "My data has:"+bytes.length+" bytes");

        if (dataToAnalyze.size() == 100) {
            textView.setText("It's Time!");
            mCamera.setPreviewCallback(null);
        }

    }
}