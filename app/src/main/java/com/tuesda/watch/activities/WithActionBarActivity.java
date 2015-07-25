package com.tuesda.watch.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tuesda.watch.R;

/**
 * Created by zhanglei on 15/7/25.
 */
public class WithActionBarActivity extends Activity {

    private RelativeLayout mNavBack;
    private RelativeLayout mMain;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_action);

        mNavBack = (RelativeLayout) findViewById(R.id.nav_back);
        mMain = (RelativeLayout) findViewById(R.id.action_main);
        mNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });






    }
}
