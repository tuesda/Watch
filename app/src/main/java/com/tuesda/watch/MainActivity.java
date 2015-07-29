package com.tuesda.watch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.tuesda.watch.activities.HomeActivity;
import com.tuesda.watch.activities.LoginActivity;
import com.tuesda.watch.activities.OneShotInListActivity;
import com.tuesda.watch.activities.UserInfoActivity;
import com.tuesda.watch.activities.WithActionBarActivity;
import com.tuesda.watch.dribleSdk.DriRegInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends Activity {
    private Button mLoginBtn;
    private Button mGetUser;
    private Button mGoAction;
    private Button mGoHome;
    private Button mGoOneShot;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        mGetUser = (Button) findViewById(R.id.get_user);
        mGoAction = (Button) findViewById(R.id.to_action);
        mGoHome = (Button) findViewById(R.id.to_home);
        mGoOneShot = (Button) findViewById(R.id.to_one_shot);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clearSharedPref();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mGetUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });
        mGoAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WithActionBarActivity.class);
                startActivity(intent);
            }
        });
        mGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        mGoOneShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OneShotInListActivity.class);
                startActivity(intent);
            }
        });

    }

    private void clearSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(DriRegInfo.DRIBLE_MEM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DriRegInfo.DRIBLE_CODE_FIELD, "");
        editor.putString(DriRegInfo.DRIBLE_TOKEN_FIELD, "");
        editor.commit();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
