package com.tuesda.watch.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tuesda.watch.R;
import com.tuesda.watch.activities.usersadapter.UserListAdapter;
import com.tuesda.watch.dribleSdk.AuthUtil;
import com.tuesda.watch.dribleSdk.DriRegInfo;
import com.tuesda.watch.dribleSdk.HttpUtils;
import com.tuesda.watch.dribleSdk.data.DribleUser;
import com.tuesda.watch.httpnetwork.NetworkHandler;
import com.tuesda.watch.log.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanglei on 15/8/2.
 */
public class UsersActivity extends Activity {

    public static final String USERS_TITLE = "com.tuesda.watch.title.extra";
    public static final String USERS_URL = "com.tuesda.watch.url.extra";

    private RelativeLayout mNavBack;
    private TextView mNavTitle;

    private String mUrl;
    private ListView mList;
    private View mHeader;
    private RelativeLayout mFooter;
    private TextView mShowMore;
    private ProgressBar mFootProgress;

    private ArrayList<DribleUser> mUsers = new ArrayList<>();
    private UserListAdapter mUsersAdapter;

    private ProgressBar mProgress;

    private LayoutInflater mInflater;


    private HashMap<String, String> mRelatedLinks = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_users);
        mInflater = LayoutInflater.from(this);
        initView();

    }

    private void initView() {
        mNavBack = (RelativeLayout) findViewById(R.id.nav_back);
        mNavTitle = (TextView) findViewById(R.id.users_nav_title);

        mList = (ListView) findViewById(R.id.users_list);

        mProgress = (ProgressBar) findViewById(R.id.users_progress);

        mHeader = new View(this);
        AbsListView.LayoutParams headParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (getResources().getDimension(R.dimen.toolbar_height)));
        mHeader.setLayoutParams(headParams);
        mList.addHeaderView(mHeader);


        mFooter = (RelativeLayout) mInflater.inflate(R.layout.users_foot, mList, false);
        mShowMore = (TextView) mFooter.findViewById(R.id.foot_load_more);

        mFootProgress = (ProgressBar) mFooter.findViewById(R.id.footer_progress);

        mList.addFooterView(mFooter);


        mUsersAdapter = new UserListAdapter(this, mUsers);
        mList.setAdapter(mUsersAdapter);
        mList.setDivider(null);


        mNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String title = getIntent().getStringExtra(USERS_TITLE);
        if (!TextUtils.isEmpty(title)) {
            mNavTitle.setText(title);
        }

        mUrl = getIntent().getStringExtra(USERS_URL);
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = DriRegInfo.REQUEST_USER_URL + 100 + "/followers";
        }

        requestUsers(mUrl);

        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!mList.canScrollVertically(1) && firstVisibleItem > 0 && mRelatedLinks.containsKey("next")) {
                    mShowMore.setVisibility(View.VISIBLE);
                    mFooter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mShowMore.setVisibility(View.INVISIBLE);
                            mFootProgress.setVisibility(View.VISIBLE);
                            requestUsers(mRelatedLinks.get("next"));
                        }
                    });
                }
            }
        });
    }


    private void requestUsers(String url) {
        final String accessToken = AuthUtil.getAccessToken(this);
        if (Log.DBG) {
//            Log.e("uses request: url: " + url);
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mFootProgress.setVisibility(View.INVISIBLE);
                        parseUsers(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mFootProgress.setVisibility(View.INVISIBLE);
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
                if (response.headers != null && Log.DBG) {
//                    Log.e("response: " + response.headers);
                }
                if (response.headers != null && response.headers.containsKey(DriRegInfo.RESPONSE_HEADER_LINK)) {
                    mRelatedLinks = HttpUtils.genNextUrl(response.headers.get(DriRegInfo.RESPONSE_HEADER_LINK));
                } else {
                    mRelatedLinks.clear();
                }
                return super.parseNetworkResponse(response);
            }
        };

        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetworkHandler.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void parseUsers(JSONArray jsonArray) {
        mProgress.setVisibility(View.INVISIBLE);
        if (jsonArray.length() <= 0) {
            return;
        }

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                String flag = Uri.parse(mUrl).getLastPathSegment();
//                Log.e("last str: " + flag);
                DribleUser user = new DribleUser();
                if (flag.equals(DriRegInfo.FOLLOWER_URL_FLAG)) {
                    user = new DribleUser((JSONObject) json.get(DriRegInfo.FOLLOWER_JSON_FLAG));
                } else if (flag.equals(DriRegInfo.FOLLOWING_URL_FLAG)) {
                    user = new DribleUser((JSONObject) json.get(DriRegInfo.FOLLOWING_JSON_FLAG));
                }

                mUsers.add(user);
            }
            mUsersAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
