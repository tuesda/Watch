package com.tuesda.watch.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tuesda.watch.R;
import com.tuesda.watch.activities.shotlistadapter.ShotListAdapter;
import com.tuesda.watch.dribleSdk.AuthUtil;
import com.tuesda.watch.dribleSdk.DriRegInfo;
import com.tuesda.watch.dribleSdk.HttpUtils;
import com.tuesda.watch.dribleSdk.data.DribleShot;
import com.tuesda.watch.dribleSdk.data.DribleUser;
import com.tuesda.watch.httpnetwork.NetworkHandler;
import com.tuesda.watch.log.Log;

import org.apache.http.auth.AUTH;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhanglei on 15/7/24.
 */
public class UserInfoActivity extends Activity {

    private RelativeLayout mNavBack;
    private static final int RETRY_COUNT = 5;

    private RelativeLayout mFollowZone;
    private TextView mFollowText;
    private boolean mFollowed = false;
    private boolean mCanChangeFollow = false;


    private SwipeRefreshLayout mSwipeRefresh;
    private HashMap<String, String> mRelatedLinks;
    private boolean mCanLoadMore = true;
    private ListView mList;
    private RelativeLayout mHeader;
    private RelativeLayout mFooter;
    private ProgressBar mFootProgress;
    private ArrayList<DribleShot> mShots = new ArrayList<DribleShot>();
    private ShotListAdapter mShotAdapter;
    private LayoutInflater mInflater;

    private TextView mNavName;
    private SimpleDraweeView mUserAvatar;
    private TextView mUserName;
    private TextView mUserFollowerC;
    private RelativeLayout mUserFollowerZone;
    private TextView mUserFollowingC;
    private RelativeLayout mUserFollowingZone;
    private RelativeLayout mUserElseZone;

    private TextView mUserBio;

    private int mUserId;
    private DribleUser mDribleUser;
    public static final String USER_ID_EXTRA = "com.tuesda.watch.userId.extra";

    private RelativeLayout mProgressZone;


    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_user_info);
        mInflater = LayoutInflater.from(this);
        mUserId = getIntent().getIntExtra(USER_ID_EXTRA, 69311);
        initView();

        requestUserInfo();
        if (AuthUtil.getMe(this).getId()!=mUserId) {
            checkIfFollowing();
        }
    }

    private void initView() {
        mNavBack = (RelativeLayout) findViewById(R.id.nav_back);
        mNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mNavName = (TextView) findViewById(R.id.user_info_nav_name);
        mNavName.setAlpha(0x0);

        mFollowZone = (RelativeLayout) findViewById(R.id.nav_follow);
        mFollowText = (TextView) findViewById(R.id.nav_follow_text);
        mFollowZone.setVisibility(View.INVISIBLE);


        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.user_info_swipe);
        mList = (ListView) findViewById(R.id.user_info_list);

        mHeader = (RelativeLayout) mInflater.inflate(R.layout.user_info_header, mList, false);
        mList.addHeaderView(mHeader);
        mList.setDivider(null);

        mFooter = (RelativeLayout) mInflater.inflate(R.layout.home_list_footer, mList, false);
        mFootProgress = (ProgressBar) mFooter.findViewById(R.id.footer_progress);
        AbsListView.LayoutParams footParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        mFooter.setLayoutParams(footParams);
        mList.addFooterView(mFooter);

        mShotAdapter = new ShotListAdapter(this, mShots);
        mList.setAdapter(mShotAdapter);

        if (AuthUtil.getMe(this).getId() != mUserId) {
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(UserInfoActivity.this, ShotDetailActivity.class);
                    intent.putExtra(ShotDetailActivity.SHOT_ID_EXTRA_FIELD, mShots.get(position - 1).getId());
                    startActivity(intent);
                }
            });
        }



        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestUserShots(null, true);
            }
        });
        mSwipeRefresh.setProgressViewOffset(true, (int) getResources().getDimension(R.dimen.toolbar_height),
                (int) (getResources().getDimension(R.dimen.toolbar_height) + 150));

        mSwipeRefresh.setColorSchemeResources(R.color.pretty_blue,
                R.color.pretty_green);

        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                handleNavNameAlpha(firstVisibleItem);


                if (!mList.canScrollVertically(1) && (firstVisibleItem + visibleItemCount == totalItemCount)
                        && firstVisibleItem>0 && mCanLoadMore) {
                    mCanLoadMore = false;
                    if (mRelatedLinks==null||
                            TextUtils.isEmpty(mRelatedLinks.get("next"))) {
                        requestUserShots(null, true);
                    } else {
                        requestUserShots(mRelatedLinks.get("next"), false);
                    }
                }
            }
        });




        mUserAvatar = (SimpleDraweeView) findViewById(R.id.user_info_avatar);
        mUserName = (TextView) findViewById(R.id.user_info_name);
        mUserFollowerZone = (RelativeLayout) findViewById(R.id.user_info_follower_zone);
        mUserFollowerC = (TextView) findViewById(R.id.follower_count);
        mUserFollowingZone = (RelativeLayout) findViewById(R.id.user_info_following_zone);
        mUserFollowingC = (TextView) findViewById(R.id.following_count);
        mUserElseZone = (RelativeLayout) findViewById(R.id.user_info_else);
        mUserBio = (TextView) findViewById(R.id.user_info_bio_text);

        mProgressZone = (RelativeLayout) findViewById(R.id.progress_zone);
        mProgressZone.setVisibility(View.VISIBLE);

        mFollowZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCanChangeFollow) {
                    if (mFollowed) {
                        requestChangeFollow(false);
                        updateFollowView(false);
                    } else {
                        requestChangeFollow(true);
                        updateFollowView(true);
                    }
                }
            }
        });
    }

    private void handleNavNameAlpha(int firstVisibleItem) {
        if (firstVisibleItem > 0)  {
            mNavBack.setAlpha(0xff);
            return;
        }
        int height = mHeader.getHeight();
        int delta = height - mHeader.getBottom();
        if (delta >= height/2 && delta < height*3/4) {
            float ratio = (delta - height/2)/(float) (height/4);
            ratio = Math.min(ratio, 1);
            mNavName.setAlpha(ratio);
        } else if (delta < height/2) {
            mNavName.setAlpha(0);
        } else {
            mNavName.setAlpha(1);
        }

    }



    private void requestUserInfo() {

        final String accessToken = AuthUtil.getAccessToken(this);
        String url = DriRegInfo.REQUEST_USER_URL + mUserId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseUserInfo(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (Log.DBG) {
                    Toast.makeText(UserInfoActivity.this, "userinfo error code: " + (error.networkResponse==null ? "" : error.networkResponse.statusCode), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put(DriRegInfo.REQUEST_HEAD_AUTH_FIELD, DriRegInfo.REQUEST_HEAD_BEAR + accessToken);
                params.putAll(super.getHeaders());
                return params;
            }
        };

        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetworkHandler.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void parseUserInfo(JSONObject response) {
        final DribleUser user = new DribleUser(response);
        mDribleUser = user;
        // Log.e("userinfo: " + user);
        if (!TextUtils.isEmpty(user.getName())) {
            mNavName.setText(user.getName());
            mUserName.setText(user.getName());
        }

        if (!TextUtils.isEmpty(user.getAvatar_url())) {
            Uri avatarUri = Uri.parse(user.getAvatar_url());
            mUserAvatar.setImageURI(avatarUri);
        }

        int followerCount = user.getFollowers_count();
        String followerCStr = followerCount > 1000 ? (String.valueOf(followerCount/1000) + "K") : String.valueOf(followerCount);
        mUserFollowerC.setText(followerCStr);
        int followingCount = user.getFollowings_count();
        String followingCStr = followingCount > 1000 ? (String.valueOf(followingCount/1000) + "K") : String.valueOf(followingCount);
        mUserFollowingC.setText(followingCStr);

        mUserFollowerZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, UsersActivity.class);
                intent.putExtra(UsersActivity.USERS_URL, DriRegInfo.REQUEST_USER_URL + mUserId + "/followers");
                intent.putExtra(UsersActivity.USERS_TITLE, "Followers");
                startActivity(intent);
            }
        });
        mUserFollowingZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, UsersActivity.class);
                intent.putExtra(UsersActivity.USERS_URL, DriRegInfo.REQUEST_USER_URL + mUserId + "/following");
                intent.putExtra(UsersActivity.USERS_TITLE, "Following");
                startActivity(intent);
            }
        });

        mUserElseZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Map.Entry entry : user.getLinks().entrySet()) {
                    String url = (String) entry.getValue();
                    if (!TextUtils.isEmpty(url) && !url.equals("null")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        break;
                    }
                }
            }
        });

        if (!TextUtils.isEmpty(user.getBio())) {
            mUserBio.setText(Html.fromHtml(user.getBio()));
            mUserBio.setMovementMethod(LinkMovementMethod.getInstance());
        }

        mProgressZone.setVisibility(View.INVISIBLE);
        requestUserShots(null, true);
        mSwipeRefresh.setRefreshing(true);

    }

    private void checkIfFollowing() {
        final String accessToke = AuthUtil.getAccessToken(this);
        String url = DriRegInfo.CHECK_IF_ME_FOLLOW_URL + mUserId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCanChangeFollow = true;
                if (error.networkResponse!=null && error.networkResponse.statusCode == 404) { // unfollow
                    updateFollowView(false);
                }
                mFollowZone.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put(DriRegInfo.REQUEST_HEAD_AUTH_FIELD, DriRegInfo.REQUEST_HEAD_BEAR + accessToke);
                params.putAll(super.getHeaders());
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                Log.e(response.headers.toString());
                if (response.headers!=null && response.headers.get("Status").startsWith("204")) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCanChangeFollow = true;
                            updateFollowView(true);
                            mFollowZone.setVisibility(View.VISIBLE);
                        }
                    });
                }


                return super.parseNetworkResponse(response);
            }
        };

        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetworkHandler.getInstance(getApplicationContext()).addToRequestQueue(request);
    }


    private void updateFollowView(boolean followed) {
        if (followed) {
            mFollowed = true;
            mFollowText.setText("Following");
            mFollowText.setTextColor(getResources().getColor(R.color.content_back));
            mFollowText.setBackgroundResource(R.drawable.following_btn_back);
        } else {
            mFollowed = false;
            mFollowText.setText("Follow");
            mFollowText.setTextColor(getResources().getColor(R.color.pretty_green));
            mFollowText.setBackgroundResource(R.drawable.unfollow_btn_back);
        }
    }


    private void requestChangeFollow(final boolean follow) {

        final String accessToken = AuthUtil.getAccessToken(this);
        String url = DriRegInfo.REQUEST_USER_URL + mUserId + "/follow";
        // Log.e("change follow url " + url);

        JsonObjectRequest request = new JsonObjectRequest(follow ? Request.Method.PUT : Request.Method.DELETE, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                mCanChangeFollow = true;
                final boolean success = response.headers.get("Status").startsWith("204");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (success) { // sucesss
                            Toast.makeText(UserInfoActivity.this, follow ? "follow success" : "unfollow success", Toast.LENGTH_SHORT).show();
                            if (follow) {
                                updateFollowView(true);
                            } else {
                                updateFollowView(false);
                            }
                        } else { // fail
                            Toast.makeText(UserInfoActivity.this, "reqeust fail", Toast.LENGTH_SHORT).show();
                            mCanChangeFollow = true;
                            if (follow) {
                                updateFollowView(false);
                            } else {
                                updateFollowView(true);
                            }
                        }
                    }
                });
                return super.parseNetworkResponse(response);
            }
        };

        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetworkHandler.getInstance(getApplicationContext()).addToRequestQueue(request);
        mCanChangeFollow = false;

    }

    private void requestUserShots(String url, final boolean first) {
        final String accessToken = AuthUtil.getAccessToken(this);
        if (first) {
            url = DriRegInfo.REQUEST_USER_URL + mUserId + "/shots";
        }

//        Log.e("user info shots: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseUserShots(response, first);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
//                Log.i("response headers: " + response.headers);
                mRelatedLinks = HttpUtils.genNextUrl(response.headers.get(DriRegInfo.RESPONSE_HEADER_LINK));
                return super.parseNetworkResponse(response);
            }
        };

        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(20000, RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        NetworkHandler.getInstance(getApplicationContext()).addToRequestQueue(request);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(false);
            }
        }, 30000);

        if (!first) {
            mFootProgress.setVisibility(View.VISIBLE);
        }
    }


    private void parseUserShots(JSONArray jsonArray, boolean first) {
        mSwipeRefresh.setRefreshing(false);
        if (jsonArray.length() <= 0) {
//            Log.e("userID:" + mUserId + " user info shots is empty!");
            mCanLoadMore = false;
            mList.setOnItemClickListener(null);
            TextView noShots = new TextView(this);
            noShots.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            noShots.setText("No shots yet");
            noShots.setTextColor(getResources().getColor(R.color.grey_text));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            noShots.setLayoutParams(params);
            mFooter.addView(noShots);
            return;
        }

        if (first) {
            mShots.clear();
        }
        try {
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                DribleShot shot = new DribleShot(json);
                shot.setUser(mDribleUser);
                mShots.add(shot);
            }

            mShotAdapter.notifyDataSetChanged();
            mCanLoadMore = true;
            mFootProgress.setVisibility(View.INVISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }








}
