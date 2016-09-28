package com.example.wanglisheng.pulsedetection;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by wanglisheng on 16/9/23.
 */
public class ImagePreviewAty extends Activity{


    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iv = new ImageView(this);
        setContentView(iv);

        String path = getIntent().getStringExtra("path");
        if (path!=null){
            iv.setImageURI(Uri.fromFile(new File(path)));
        }
    }
}
