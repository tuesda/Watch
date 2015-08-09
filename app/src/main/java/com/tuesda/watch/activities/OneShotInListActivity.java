package com.tuesda.watch.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tuesda.watch.R;
import com.tuesda.watch.dribleSdk.DriRegInfo;
import com.tuesda.watch.dribleSdk.data.DribleShot;
import com.tuesda.watch.httpnetwork.NetworkHandler;
import com.tuesda.watch.log.Log;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanglei on 15/7/26.
 */
public class OneShotInListActivity extends Activity {
    private static final int RETRY_COUNT = 5;

    private RelativeLayout mNavBack;
    private String mAccess_token;


    private TextView mItemHeaderText;
    private SimpleDraweeView mItemImage;
    private TextView mItemTitle;
    private SimpleDraweeView mItemAvatar;
    private TextView mItemAuthorName;
    private TextView mItemCreate;
    private TextView mItemLikeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_one_shot);

        initView();
        setupRequest();
    }


    private void initView() {
        mNavBack = (RelativeLayout) findViewById(R.id.nav_back);
        mNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mItemHeaderText = (TextView) findViewById(R.id.item_header_text);
        mItemImage = (SimpleDraweeView) findViewById(R.id.item_image_view);
        mItemTitle = (TextView) findViewById(R.id.item_title);
        mItemAvatar = (SimpleDraweeView) findViewById(R.id.item_author_avatar);
        mItemAuthorName = (TextView) findViewById(R.id.item_author_name);
        mItemCreate = (TextView) findViewById(R.id.item_create_date);
        mItemLikeCount = (TextView) findViewById(R.id.item_likes_count);
    }


    private void setupRequest() {
        checkAuth();

        int shot_id = getIntent().getIntExtra(DriRegInfo.INTENT_SHOT_ID, 5001);
        String url = DriRegInfo.REQUEST_ONE_SHOT_URL + shot_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseShotResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OneShotInListActivity.this, "return error", Toast.LENGTH_LONG).show();
            }
        }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put(DriRegInfo.REQUEST_HEAD_AUTH_FIELD, DriRegInfo.REQUEST_HEAD_BEAR + mAccess_token);
                params.putAll(super.getHeaders());
                // Log.i(params.toString());
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                // Log.i(" status code: " + response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 0;
            }

            @Override
            public int getCurrentRetryCount() {
                return RETRY_COUNT;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        NetworkHandler.getInstance(getApplicationContext()).addToRequestQueue(request);


    }

    private void parseShotResponse(JSONObject reponse) {
        DribleShot dribleShot = new DribleShot(reponse);
        Log.i("shot: " + dribleShot);
        if (!TextUtils.isEmpty(dribleShot.getTags().get(0))) {
            mItemHeaderText.setText(dribleShot.getTags().get(0));
            mItemHeaderText.setVisibility(View.VISIBLE);
        } else {
            mItemHeaderText.setVisibility(View.INVISIBLE);
        }
        Uri imgUri = Uri.parse(dribleShot.getImages()[1]);
        mItemImage.setImageURI(imgUri);
        mItemTitle.setText(dribleShot.getTitle());
        Uri avatarUri = Uri.parse(dribleShot.getUser().getAvatar_url());
        mItemAvatar.setImageURI(avatarUri);
        mItemAuthorName.setText(dribleShot.getUser().getName());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        mItemCreate.setText(formatter.format(dribleShot.getCreated_at().getTime()));
        mItemLikeCount.setText("" + dribleShot.getLikes_count());
    }

    private void checkAuth() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.DRIBLE_MEM, Context.MODE_PRIVATE);
        mAccess_token = sharedPreferences.getString(LoginActivity.DRIBLE_TOKEN_FIELD, null);
        if (TextUtils.isEmpty(mAccess_token)) {
            Intent intent = new Intent(OneShotInListActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }


}
