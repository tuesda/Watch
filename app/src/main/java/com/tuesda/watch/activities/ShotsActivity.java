package com.tuesda.watch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.JsonArray;
import com.tuesda.watch.R;
import com.tuesda.watch.activities.shotlistadapter.ShotListAdapter;
import com.tuesda.watch.dribleSdk.AuthUtil;
import com.tuesda.watch.dribleSdk.DriRegInfo;
import com.tuesda.watch.dribleSdk.HttpUtils;
import com.tuesda.watch.dribleSdk.data.DribleShot;
import com.tuesda.watch.httpnetwork.NetworkHandler;
import com.tuesda.watch.log.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanglei on 15/8/5.
 */
public class ShotsActivity extends Activity {

    public static final String SHOTS_TITLE_EXTRA = "com.tuesda.watch.shots.title.extra";
    public static final String SHOTS_URL = "com.tuesda.watch.shots.url.extra";
    public static final String CALL_FROM = "com.tuesda.watch.shot.call.from";


    private RelativeLayout mNavBack;
    private TextView mTitle;

    private String mTitleTxt;
    private String mUrl;

    private SwipeRefreshLayout mSwipeRefresh;
    private ListView mList;
    private ShotListAdapter mShotsAdapter;
    private ArrayList<DribleShot> mShots = new ArrayList<>();

    private View mHeader;
    private RelativeLayout mFooter;
    private ProgressBar mFootProgress;

    private ProgressBar mProgress;
    private LayoutInflater mInflater;

    private HashMap<String, String> mRelatedLinks = new HashMap<>();

    private boolean mCanLoadMore = true;

    private String mFrom;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_shots);
        initView();

        mTitleTxt = getIntent().getStringExtra(SHOTS_TITLE_EXTRA);
        mUrl = getIntent().getStringExtra(SHOTS_URL);
        mFrom = getIntent().getStringExtra(CALL_FROM);
        if (TextUtils.isEmpty(mTitleTxt) || TextUtils.isEmpty(mUrl)){
            finish();
        } else {
            mTitle.setText(mTitleTxt);
        }
//        Toast.makeText(this, mUrl, Toast.LENGTH_SHORT).show();

        requestForShots(true);

    }

    private void initView() {
        mInflater = LayoutInflater.from(this);
        mNavBack = (RelativeLayout) findViewById(R.id.nav_back);
        mTitle = (TextView) findViewById(R.id.shots_nav_title);

        mNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.shots_swipe);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestForShots(true);
            }
        });
        int toolbarH = (int) getResources().getDimension(R.dimen.toolbar_height);
        mSwipeRefresh.setProgressViewOffset(true, toolbarH, toolbarH + 200);

        mList = (ListView) findViewById(R.id.shots_list);



        mHeader = new View(this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.toolbar_height));
        mHeader.setLayoutParams(params);
        mList.addHeaderView(mHeader);

        mFooter = (RelativeLayout) mInflater.inflate(R.layout.home_list_footer, null, false);
        AbsListView.LayoutParams footParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.home_footer_height));
        mFooter.setLayoutParams(footParams);
        mList.addFooterView(mFooter);
        mFootProgress = (ProgressBar) mFooter.findViewById(R.id.footer_progress);

        mProgress = (ProgressBar) findViewById(R.id.shots_progress);
        mProgress.setVisibility(View.VISIBLE);

        mShotsAdapter = new ShotListAdapter(this, mShots);
        mList.setAdapter(mShotsAdapter);
        mList.setDivider(null);


        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (!mList.canScrollVertically(1) && firstVisibleItem > 0 &&
                        (firstVisibleItem + visibleItemCount == totalItemCount
                                && mCanLoadMore && mRelatedLinks!=null && mRelatedLinks.containsKey("next"))) {
                    mCanLoadMore = false;
                    mFootProgress.setVisibility(View.VISIBLE);
                    requestForShots(false);

                }
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShotsActivity.this, ShotDetailActivity.class);
                intent.putExtra(ShotDetailActivity.SHOT_ID_EXTRA_FIELD, mShots.get(position-1).getId());
                startActivity(intent);
            }
        });



    }


    private void requestForShots(final boolean isFirst) {
        final String accessToken = AuthUtil.getAccessToken(this);
        String url = isFirst? mUrl : mRelatedLinks.get("next");
        Log.i("shots url: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        whenReuestDone();

                        parseShots(response, isFirst);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                whenReuestDone();
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
                Log.e("shots response: " + (response.headers != null ? response.headers : ""));
                mRelatedLinks = HttpUtils.genNextUrl(response.headers.get(DriRegInfo.RESPONSE_HEADER_LINK));
                return super.parseNetworkResponse(response);
            }
        };

        NetworkHandler.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    private void whenReuestDone() {
        mProgress.setVisibility(View.INVISIBLE);
        mSwipeRefresh.setRefreshing(false);
        mCanLoadMore = true;
        mFootProgress.setVisibility(View.INVISIBLE);
    }

    private void parseShots(JSONArray jsonArray, boolean isFirst) {
        if (jsonArray.length() <= 0) {
            mList.setOnItemClickListener(null);
            TextView noShots = new TextView(this);
            noShots.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            noShots.setText("No shots yet");
            noShots.setTextColor(getResources().getColor(R.color.grey_text));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            noShots.setLayoutParams(params);
            mFooter.addView(noShots);

        }

        if (isFirst) {
            mShots.clear();
        }

        try {
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                DribleShot shot = mFrom.equals("like") ? (new DribleShot((JSONObject) json.get("shot")))
                        : (new DribleShot(json));
                mShots.add(shot);
            }

            mShotsAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
