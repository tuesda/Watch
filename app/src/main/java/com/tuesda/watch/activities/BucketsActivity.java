package com.tuesda.watch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tuesda.watch.R;
import com.tuesda.watch.activities.bucketadapter.BucketListAdapter;
import com.tuesda.watch.dribleSdk.AuthUtil;
import com.tuesda.watch.dribleSdk.DriRegInfo;
import com.tuesda.watch.dribleSdk.data.DribleBucket;
import com.tuesda.watch.httpnetwork.NetworkHandler;
import com.tuesda.watch.log.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanglei on 15/8/5.
 */
public class BucketsActivity extends Activity {

    public static final String BUCKET_URL = "com.tuesda.watch.buckets.url.extra";
    public static final String BUCKET_TITLE = "com.tuesda.watch.buckets.title.extra";

    private RelativeLayout mNavBack;
    private ListView mList;
    private View mHeader;

    private BucketListAdapter mBucketsAdapter;
    private ArrayList<DribleBucket> mBuckets = new ArrayList<>();

    private TextView mTitle;
    private String mTitleTxt;
    private String mUrl;

    private ProgressBar mProgress;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_bucket);
        mTitleTxt = getIntent().getStringExtra(BUCKET_TITLE);
        mUrl = getIntent().getStringExtra(BUCKET_URL);
        if (TextUtils.isEmpty(mTitleTxt) || TextUtils.isEmpty(mUrl)) {
            finish();
        }

        initView();
        requestForBuckets();
    }


    private void initView() {
        mNavBack = (RelativeLayout) findViewById(R.id.nav_back);
        mNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mList = (ListView) findViewById(R.id.buckets_list);

        mHeader = new View(this);
        AbsListView.LayoutParams headParam = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.toolbar_height));
        mHeader.setLayoutParams(headParam);
        mList.addHeaderView(mHeader);


        mBucketsAdapter = new BucketListAdapter(this, mBuckets);
        mList.setAdapter(mBucketsAdapter);
        mList.setDivider(null);



        mTitle = (TextView) findViewById(R.id.buckets_nav_title);
        mTitle.setText(mTitleTxt);

        mProgress = (ProgressBar) findViewById(R.id.buckets_progress);
        mProgress.setVisibility(View.VISIBLE);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent intent = new Intent(BucketsActivity.this, ShotsActivity.class);
                    intent.putExtra(ShotsActivity.SHOTS_TITLE_EXTRA, "Shots of " + mBuckets.get(position-1).getName());
                    intent.putExtra(ShotsActivity.SHOTS_URL, DriRegInfo.REQUEST_BUCKETS_URL + mBuckets.get(position-1).getId() + "/shots");
                    intent.putExtra(ShotsActivity.CALL_FROM, "bucket");
                    startActivity(intent);
                }
            }
        });


    }


    private void requestForBuckets() {
        final String accessToken = AuthUtil.getAccessToken(this);
        String url = mUrl;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mProgress.setVisibility(View.INVISIBLE);
                        parseBuckets(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.setVisibility(View.INVISIBLE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put(DriRegInfo.REQUEST_HEAD_AUTH_FIELD, DriRegInfo.REQUEST_HEAD_BEAR + accessToken);
                params.putAll(super.getHeaders());
                return params;
            }

            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                Log.e("buckets response headers" + (response.headers == null ? "" : response.headers));
                return super.parseNetworkResponse(response);
            }
        };

        NetworkHandler.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void parseBuckets(JSONArray jsonArray) {
        if (jsonArray.length() <= 0) {

            return;
        }

        try {
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                DribleBucket bucket = new DribleBucket(json);
                mBuckets.add(bucket);
            }
            mBucketsAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
