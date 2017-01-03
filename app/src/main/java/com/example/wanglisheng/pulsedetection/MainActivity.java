package com.example.wanglisheng.pulsedetection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.ImageWriter;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import static android.R.attr.button;
import static android.R.attr.path;
import static android.provider.MediaStore.ACTION_VIDEO_CAPTURE;

public class MainActivity extends Activity {

    Button takePhotos;
    TextView resultsV;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePhotos = (Button) findViewById(R.id.btnTakePic);
        //======================12.29.2016========================
        //从这里开始我们应该做下面的步骤：
        // 1.把电脑上存过的视频分成照片
        // 2.从照片取得个信号
        // 3.给信号应用滤波

        takePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resultsV = (TextView) findViewById(R.id.resultsView);
                //============= 1.把电脑上存过的视频分成照片=================================================
                String pathToVideo = "/storage/emulated/0/DCIM/Camera/";
                String pathToWrite = "/storage/emulated/0/DCIM/PulseDetectionApp";
                VideoToSignalParser videoToSignalParser = new VideoToSignalParser(pathToVideo,pathToWrite);

                //创建所有数据的目录
                File newDir = new File(pathToWrite);
                if(!newDir.exists()){
                    boolean success = newDir.mkdir();
                }

                //创建frameData.txt
                File frameData = new File(pathToWrite+"/frameData.txt");
                try {
                    FileWriter frameDataWriter = new FileWriter(frameData,true);
                    //计算起来每个视频的brightness矩阵,又把矩阵写在frameData.txt上
                    for(int i = 2; i < 3; i++ ){//到现在已经做好了两个对象
                        videoToSignalParser.parse(i,1);
                        videoToSignalParser.writeSignalToFile(frameDataWriter,i-1);
                        videoToSignalParser.parse(i,2);
                        videoToSignalParser.writeSignalToFile(frameDataWriter, i-1);
                        resultsV.setText("写完了");
                    }
                    frameDataWriter.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }






                /*dispatchTakeVideoIntent();  That was for using an intent to use the camera of the phone*/
            }
        });








        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    static final int REQUEST_VIDEO_CAPTURE = 1;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();

            resultsV.setText("Data received!");

        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
