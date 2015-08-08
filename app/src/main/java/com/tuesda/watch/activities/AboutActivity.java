package com.tuesda.watch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.tuesda.watch.R;

/**
 * Created by zhanglei on 15/8/7.
 */
public class AboutActivity extends Activity {


    private Button mTitle, mLine1, mLine2, mLine3, mLine4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
