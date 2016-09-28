package com.example.wanglisheng.pulsedetection;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {
    private SurfaceView cameraPreview;
    private android.hardware.Camera camera = null;
    private SurfaceHolder.Callback cameraPreviewHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            startPreview();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stopPreview();

        }
    };

    private String saveTempFile(byte[] bytes){
        try {
            //File f = File.createTempFile("img", "");
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File f = new File(directory, "test.jpg");

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bytes);
            fos.flush();
            fos.close();


            return f.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void startPreview(){            //预览拍完照的画面
        camera = android.hardware.Camera.open();
        try {
            camera.setPreviewDisplay(cameraPreview.getHolder());
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPreview(){
        camera.stopPreview();
        camera.release();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);

        cameraPreview.getHolder().addCallback(cameraPreviewHolderCallback);

        findViewById(R.id.btnTakePic).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        String path = null;
                        if ((path=saveTempFile(data))!=null){

                            Intent i = new Intent(MainActivity.this,ImagePreviewAty.class);
                            i.putExtra("path",path);
                            startActivity(i);
                        }else{
                            Toast.makeText(MainActivity.this,"保存照片失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


}
