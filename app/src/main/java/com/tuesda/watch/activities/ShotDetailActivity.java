package com.tuesda.watch.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tuesda.watch.R;
import com.tuesda.watch.activities.shotdetail.CommentAdapter;
import com.tuesda.watch.animator.AnimatorHelp;
import com.tuesda.watch.dribleSdk.AuthUtil;
import com.tuesda.watch.dribleSdk.DriRegInfo;
import com.tuesda.watch.dribleSdk.data.DribleComment;
import com.tuesda.watch.dribleSdk.data.DribleShot;
import com.tuesda.watch.frescohelp.ImageHelper;
import com.tuesda.watch.httpnetwork.NetworkHandler;
import com.tuesda.watch.log.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanglei on 15/7/30.
 */
public class ShotDetailActivity extends Activity {

    private static int RETRY_COUNT = 5;

    public static final String SHOT_ID_EXTRA_FIELD = "com.tuesda.watch.detail.shot.extra";

    private DribleShot mDribleShot;
    private int mShotId;

    private LayoutInflater inflater;

    private RelativeLayout mNavBack;
    private SimpleDraweeView mAvatar;
    private TextView mAuthorName;
    private RelativeLayout mNavShare;
    private RelativeLayout mNavLike;
    private ImageView mNavLikeImg;
    private boolean mLiked;
    private RelativeLayout mProgress;

    private View mHeader;

    private ListView mCommentsList;
    private CommentAdapter mCommentAdapter;

    private LinearLayout mCommentsHeader;
    private SimpleDraweeView mDetailImage;
    private TextView mShotTitle;
    private TextView mShotDescription;

    private ImageView mShotInfoLikeImg;
    private TextView mShotInfoLikeText;
    private ImageView mShotInfoCommentImg;
    private TextView mShotInfoCommentText;
    private ImageView mShotInfoViewImg;
    private TextView mShotInfoViewText;

    private LinearLayout mShotTag;
    private TextView mShotTagText;
    private RelativeLayout mShotComment;
    private ImageView mShotComImg;
    private RelativeLayout mShotBucket;
    private ImageView mShotBucketImg;

    private LinearLayout mShotScrollWall;

    private ArrayList<DribleComment> mComments = new ArrayList<DribleComment>();

    private boolean mFirstTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_shot_detail);

        mShotId = getIntent().getIntExtra(SHOT_ID_EXTRA_FIELD, -1);
        if (mShotId == -1) {
            if (Log.DBG) {
                Log.i("didn't deliver the shot ID");
            }
            //finish();
        }

        initView();

        String url = DriRegInfo.REQUEST_ONE_SHOT_URL + mShotId;
        requestShot(url);
        requestComments();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mFirstTime) {
//            Log.e("resume!");
            requestComments();
        } else {
            mFirstTime = false;
        }
    }

    private void initView() {
        inflater = LayoutInflater.from(this);

        mNavBack = (RelativeLayout) findViewById(R.id.nav_back);
        mAvatar = (SimpleDraweeView) findViewById(R.id.shot_detail_avatar);
        mAuthorName = (TextView) findViewById(R.id.shot_detail_author);
        mNavShare = (RelativeLayout) findViewById(R.id.nav_share);
        mNavLike = (RelativeLayout) findViewById(R.id.nav_like);
        mNavLikeImg = (ImageView) findViewById(R.id.nav_like_img);
        mNavLike.setVisibility(View.INVISIBLE);
        mProgress = (RelativeLayout) findViewById(R.id.shot_progress);

        mCommentsList = (ListView) findViewById(R.id.shot_detail_comments_list);

        mCommentsHeader = (LinearLayout) inflater.inflate(R.layout.shot_detail_comments_header, mCommentsList, false);

        mShotScrollWall = (LinearLayout) mCommentsHeader.findViewById(R.id.shot_detail_scroll_wall);

        //mHeader = findViewById(R.id.shot_detail_header);
        mDetailImage = (SimpleDraweeView) findViewById(R.id.shot_detail_img);
        mShotTitle = (TextView) mCommentsHeader.findViewById(R.id.shot_detail_title);
        mShotDescription = (TextView) mCommentsHeader.findViewById(R.id.shot_detail_description);

        mShotInfoLikeImg = (ImageView) mCommentsHeader.findViewById(R.id.shot_detail_info_like_img);
        mShotInfoLikeImg.setColorFilter(getResources().getColor(R.color.text_default_color));
        mShotInfoLikeText = (TextView) mCommentsHeader.findViewById(R.id.shot_detail_info_like_text);
        mShotInfoCommentImg = (ImageView) mCommentsHeader.findViewById(R.id.shot_detail_info_comment_img);
        mShotInfoCommentImg.setColorFilter(getResources().getColor(R.color.text_default_color));
        mShotInfoCommentText = (TextView) mCommentsHeader.findViewById(R.id.shot_detail_info_comment_text);
        mShotInfoViewImg = (ImageView) mCommentsHeader.findViewById(R.id.shot_detail_info_view_img);
        mShotInfoViewImg.setColorFilter(getResources().getColor(R.color.text_default_color));
        mShotInfoViewText = (TextView) mCommentsHeader.findViewById(R.id.shot_detail_info_view_text);

        mShotTag = (LinearLayout) mCommentsHeader.findViewById(R.id.shot_detail_tag_zone);
        mShotTagText = (TextView) mCommentsHeader.findViewById(R.id.shot_detail_tag_text);

        mShotComment = (RelativeLayout) mCommentsHeader.findViewById(R.id.shot_detail_comment_zone);
        mShotComImg = (ImageView) mCommentsHeader.findViewById(R.id.shot_detail_comment_zone_img);
        mShotComImg.setColorFilter(getResources().getColor(R.color.grey_text));

        mShotBucket = (RelativeLayout) mCommentsHeader.findViewById(R.id.shot_detail_bucket_zone);
        mShotBucketImg = (ImageView) mCommentsHeader.findViewById(R.id.shot_detail_bucket_zone_img);
        mShotBucketImg.setColorFilter(getResources().getColor(R.color.grey_text));

        mCommentsList.addHeaderView(mCommentsHeader);

        View footer = new View(this);
        AbsListView.LayoutParams footParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.comments_list_foot_height));
        footer.setLayoutParams(footParams);
        mCommentsList.addFooterView(footer);

        mCommentAdapter = new CommentAdapter(this, mComments);
        mCommentsList.setAdapter(mCommentAdapter);
        mCommentsList.setDivider(null);



        mNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //overridePendingTransition(0, R.anim.slide_out_right);
            }
        });

        mProgress.setVisibility(View.VISIBLE);





    }

    private int mLoadShotRetryCount = 5;

    private void requestShot(final String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (Log.DBG) {
            Log.i("Shot detail request url: " + url);
        }

        final String accessToken = AuthUtil.getAccessToken(this);
        if (TextUtils.isEmpty(accessToken)) { // because we examine the accesstoken, so will never in this switch
            return;
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (mLoadShotRetryCount > 0) {
                            mLoadShotRetryCount--;
                            requestShot(url);
                        } else {
                            Toast.makeText(ShotDetailActivity.this, "network errors", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(DriRegInfo.REQUEST_HEAD_AUTH_FIELD, DriRegInfo.REQUEST_HEAD_BEAR + accessToken);
                params.putAll(super.getHeaders());
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Log.i("shot detail: response headers: " + response.headers);
                return super.parseNetworkResponse(response);
            }
        };


        request.setRetryPolicy(new DefaultRetryPolicy(10000, RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);

        NetworkHandler.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    private void parseResponse(JSONObject response) {
        mProgress.setVisibility(View.INVISIBLE);
        DribleShot dribleShot = new DribleShot(response);
        fillData(dribleShot);
    }

    private void fillData(final DribleShot shot) {

        if (!TextUtils.isEmpty(shot.getUser().getAvatar_url())) {
            Uri uri = Uri.parse(shot.getUser().getAvatar_url());
            mAvatar.setImageURI(uri);
            mAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShotDetailActivity.this, UserInfoActivity.class);
                    intent.putExtra(UserInfoActivity.USER_ID_EXTRA, shot.getUser().getId());
                    startActivity(intent);
                }
            });
        }

        if (!TextUtils.isEmpty(shot.getUser().getName())) {
            mAuthorName.setText(shot.getUser().getName());
            mAuthorName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShotDetailActivity.this, UserInfoActivity.class);
                    intent.putExtra(UserInfoActivity.USER_ID_EXTRA, shot.getUser().getId());
                    startActivity(intent);
                }
            });
        }

        if (!TextUtils.isEmpty(shot.getHtml_url())) {
            mNavShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String shareUrl = shot.getHtml_url();
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);
                    startActivity(shareIntent);
                }
            });
        }
        checkIfLike();
        mNavLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCanChangeLike) {
                    if (!mLiked) {
                       requestLike(true);
                        AnimatorHelp.btnClick(mNavLikeImg, 300);
                       Toast.makeText(ShotDetailActivity.this, "requesting", Toast.LENGTH_SHORT).show();
                    } else {
                        requestLike(false);
                        AnimatorHelp.btnClick(mNavLikeImg, 300);
                        Toast.makeText(ShotDetailActivity.this, "requesting", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        for(int i=0; i<3; i++) {
            if (!TextUtils.isEmpty(shot.getImages()[i])) {
                String imgStr = shot.getImages()[i];
                Uri uri = Uri.parse(imgStr);
                Uri placeUri = null;
                if (!TextUtils.isEmpty(shot.getImages()[2])) {
                    placeUri = Uri.parse(shot.getImages()[2]);
                }

                ImageHelper.setupImage(getResources(), uri, placeUri, mDetailImage);
                break;
            }
        }

        if (!TextUtils.isEmpty(shot.getTitle())) {
            mShotTitle.setText(shot.getTitle());
        }

        if (!TextUtils.isEmpty(shot.getDescription()) && !shot.getDescription().equals("null")) {
            mShotDescription.setText(Html.fromHtml(shot.getDescription()));
            mShotDescription.setMovementMethod(LinkMovementMethod.getInstance());
        }

        mShotInfoLikeText.setText(String.valueOf(shot.getLikes_count()));
        mShotInfoCommentText.setText(String.valueOf(shot.getComments_count()));
        mShotInfoViewText.setText(String.valueOf(shot.getViews_count()));



        ArrayList<String> tags = shot.getTags();
        if (tags.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : tags) {
                stringBuilder.append(s + ", ");
            }

            mShotTagText.setText(stringBuilder.toString().substring(0, stringBuilder.length()-2));
        } else {
            mShotTagText.setText("No one tag");
        }


        mShotComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShotDetailActivity.this, InputActivity.class);
                intent.putExtra(InputActivity.INPUT_NAME, "Comment");
                intent.putExtra(InputActivity.SHOT_ID, mShotId);
                startActivity(intent);
            }
        });

        mShotBucket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShotDetailActivity.this, "developing...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void checkIfLike() {

        final String accessToken = AuthUtil.getAccessToken(this);
        if (TextUtils.isEmpty(accessToken)) {
            mNavLikeImg.clearColorFilter();
            mLiked = false;
            return;
        }

        String url = DriRegInfo.REQUEST_ONE_SHOT_URL + mShotId + "/" + "like";

//        Log.e("check like url: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        updateLikeView(true);
                        mNavLike.setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                updateLikeView(false);
                mNavLike.setVisibility(View.VISIBLE);
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
        NetworkHandler.getInstance(getApplicationContext()).addToRequestQueue(request);


    }



    private boolean mCanChangeLike = true;
    private void requestLike(boolean like) {

        String url = DriRegInfo.REQUEST_ONE_SHOT_URL + mShotId + "/" + "like";

        final String accessToken = AuthUtil.getAccessToken(this);
//        Log.e("change like: " + url);

        JsonObjectRequest request = new JsonObjectRequest(like ? Request.Method.POST : Request.Method.DELETE, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        updateLikeView(true);
                        mCanChangeLike = true;
                        Toast.makeText(ShotDetailActivity.this, "Liked", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                updateLikeView(false);
                mCanChangeLike = true;
                Toast.makeText(ShotDetailActivity.this, "Unliked", Toast.LENGTH_SHORT).show();
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
        NetworkHandler.getInstance(getApplicationContext()).addToRequestQueue(request);
        mCanChangeLike = false;



    }

    private void updateLikeView(boolean like) {
        if (like) {
            mNavLikeImg.setColorFilter(getResources().getColor(R.color.pretty_red));
            mLiked = true;
        } else {
            mNavLikeImg.clearColorFilter();
            mLiked = false;
        }
    }



    private void requestComments() {
        final String accessToken = AuthUtil.getAccessToken(this);
        String url = DriRegInfo.REQUEST_ONE_SHOT_URL + mShotId + "/comments";

        Log.e("request comments: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseComments(response);
//                        Toast.makeText(ShotDetailActivity.this, "comments coming", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ShotDetailActivity.this, "error when get comments", Toast.LENGTH_SHORT).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetworkHandler.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    private void parseComments(JSONArray jsonArray) {
        if (jsonArray!=null && jsonArray.length() <= 0) {
            return;
        }
        mComments.clear();

        try {
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                mComments.add(new DribleComment(json));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCommentAdapter.notifyDataSetChanged();
    }

}
