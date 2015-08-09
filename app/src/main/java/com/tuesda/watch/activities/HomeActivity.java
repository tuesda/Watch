package com.tuesda.watch.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tuesda.watch.R;
import com.tuesda.watch.activities.homefragments.FragmentAdapter;
import com.tuesda.watch.activities.homefragments.HomeFragment;
import com.tuesda.watch.dribleSdk.AuthUtil;
import com.tuesda.watch.dribleSdk.DriRegInfo;
import com.tuesda.watch.dribleSdk.data.DribleUser;
import com.tuesda.watch.httpnetwork.NetworkHandler;
import com.tuesda.watch.log.Log;

import org.apache.http.auth.AUTH;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanglei on 15/7/25.
 */
public class HomeActivity extends FragmentActivity {


    private RelativeLayout mNavBar;
    private RelativeLayout mNavMenu;
    private RelativeLayout mNavSearch;

    private float mCurNavTrans;

    /**
     * nav bottom three buttons
     */
    private RelativeLayout mNavBtnLeft;
    private RelativeLayout mNavBtnMid;
    private RelativeLayout mNavBtnRig;

    private TextView mNavTextLeft, mNavTextMid, mNavTextRig;

    private ViewPager mContentPager;
    private View mIndicator;
    private List<HomeFragment> mFragmentList = new ArrayList<HomeFragment>();
    private FragmentAdapter mFragmentAdapter;
    private HomeFragment mLeftFrag, mMidFrag, mRigFrag;

    /**
     * current page
     * 0 -- left
     * 1 -- middle
     * 2 -- right
     */
    private int mCurIndex;

    private int mScreenWidth;

    /**
     * drawerlayout
     */
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mLeftDrawer;

    private LinearLayout mUserZone;
    private SimpleDraweeView mMyAvatar;
    private TextView mMyName;

    private TextView mMenuHome;
//    private TextView mMenuNew;
    private TextView mMenuLiked;
    private TextView mMenuBuckets;
    private TextView mMenuAbout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_home);
        initView();

        initPager();
        initTabIndicatorWidth();
        showUserInfo();

    }

    private void initView() {
        mNavBar = (RelativeLayout) findViewById(R.id.nav);
        mNavMenu = (RelativeLayout) findViewById(R.id.nav_menu);
        mNavSearch = (RelativeLayout) findViewById(R.id.nav_search);


        mNavBtnLeft = (RelativeLayout) findViewById(R.id.nav_btn_l);
        mNavBtnMid = (RelativeLayout) findViewById(R.id.nav_btn_m);
        mNavBtnRig = (RelativeLayout) findViewById(R.id.nav_btn_r);

        mNavTextLeft = (TextView) findViewById(R.id.nav_bottom_l);
        mNavTextMid = (TextView) findViewById(R.id.nav_bottom_m);
        mNavTextRig = (TextView) findViewById(R.id.nav_bottom_r);

        mContentPager = (ViewPager) findViewById(R.id.home_content);
        mIndicator = findViewById(R.id.home_pager_indicator);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawer = (RelativeLayout) findViewById(R.id.left_drawer);


        mUserZone = (LinearLayout) findViewById(R.id.left_menu_user_zone);
        mMyAvatar = (SimpleDraweeView) findViewById(R.id.left_menu_avatar_img);
        mMyName = (TextView) findViewById(R.id.left_menu_name);

        mMenuHome = (TextView) findViewById(R.id.left_menu_home);
//        mMenuNew = (TextView) findViewById(R.id.left_menu_new);
        mMenuLiked = (TextView) findViewById(R.id.left_menu_like);
        mMenuBuckets = (TextView) findViewById(R.id.left_menu_buckets);
        mMenuAbout = (TextView) findViewById(R.id.left_menu_about);



        mNavMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
                    mDrawerLayout.openDrawer(mLeftDrawer);
                }
            }
        });

        mNavSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(Intent.ACTION_VIEW);
                searchIntent.setData(Uri.parse(DriRegInfo.DRIBLE_SEARCH_URL));
                startActivity(searchIntent);
            }
        });
    }



    private void initPager() {
        mLeftFrag = new HomeFragment();
        Bundle leftBundle = new Bundle();
        leftBundle.putInt(HomeFragment.TAB_INDEX_FIELD, 0);
        mLeftFrag.setArguments(leftBundle);
        mLeftFrag.setOnScrollListListener(new HomeFragment.OnScrollListListener() {
            @Override
            public void onListScroll(int scrollDisY) {
                transNavOnScroll(scrollDisY, 0);
            }
        });

        mMidFrag = new HomeFragment();
        Bundle midBundle = new Bundle();
        midBundle.putInt(HomeFragment.TAB_INDEX_FIELD, 1);
        mMidFrag.setArguments(midBundle);
        mMidFrag.setOnScrollListListener(new HomeFragment.OnScrollListListener() {
            @Override
            public void onListScroll(int scrollDisY) {
                transNavOnScroll(scrollDisY, 1);
            }
        });

        mRigFrag = new HomeFragment();
        Bundle rigBundle = new Bundle();
        rigBundle.putInt(HomeFragment.TAB_INDEX_FIELD, 2);
        mRigFrag.setArguments(rigBundle);
        mRigFrag.setOnScrollListListener(new HomeFragment.OnScrollListListener() {
            @Override
            public void onListScroll(int scrollDisY) {
                transNavOnScroll(scrollDisY, 2);
            }
        });

        mFragmentList.add(mLeftFrag);
        mFragmentList.add(mMidFrag);
        mFragmentList.add(mRigFrag);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList);

        mContentPager.setAdapter(mFragmentAdapter);
        mContentPager.setCurrentItem(0);
        onTabSelected(0);


        mContentPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scrollIndicator(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                onTabSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        addTabClickListener();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mLeftFrag!=null && getSupportFragmentManager().findFragmentById(mLeftFrag.getId())!=null) {
            getSupportFragmentManager().putFragment(outState, "left", mLeftFrag);
        }
        if (mMidFrag!=null && getSupportFragmentManager().findFragmentById(mMidFrag.getId())!=null) {
            getSupportFragmentManager().putFragment(outState, "mid", mMidFrag);
        }
        if (mRigFrag!=null && getSupportFragmentManager().findFragmentById(mRigFrag.getId())!=null) {
            getSupportFragmentManager().putFragment(outState, "right", mRigFrag);
        }
        outState.putFloat("navBarTranslation", mNavBar.getTranslationY());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (getSupportFragmentManager().getFragment(savedInstanceState, "left")!=null) {
            mLeftFrag = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "left");
            mLeftFrag.setOnScrollListListener(new HomeFragment.OnScrollListListener() {
                @Override
                public void onListScroll(int scrollDisY) {
                    transNavOnScroll(scrollDisY, 0);
                }
            });
        }
        if (getSupportFragmentManager().getFragment(savedInstanceState, "mid")!=null) {
            mMidFrag = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mid");
            mMidFrag.setOnScrollListListener(new HomeFragment.OnScrollListListener() {
                @Override
                public void onListScroll(int scrollDisY) {
                    transNavOnScroll(scrollDisY, 1);
                }
            });
        }
        if (getSupportFragmentManager().getFragment(savedInstanceState, "right")!=null) {
            mRigFrag = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "right");
            mRigFrag.setOnScrollListListener(new HomeFragment.OnScrollListListener() {
                @Override
                public void onListScroll(int scrollDisY) {
                    transNavOnScroll(scrollDisY, 2);
                }
            });
        }
        mNavBar.setTranslationY(savedInstanceState.getFloat("navBarTranslation"));
    }

    private void addTabClickListener() {
        mNavBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentPager.setCurrentItem(0);
            }
        });
        mNavBtnMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentPager.setCurrentItem(1);
            }
        });
        mNavBtnRig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentPager.setCurrentItem(2);
            }
        });
    }

    private void scrollIndicator(int position, float positionOffset, int positionOffsetPixels) {
        int translationX = (int) ((position + positionOffset) * mScreenWidth / 3);
        mIndicator.setTranslationX(translationX);
    }

    private void onTabSelected(int position) {
        resetTabs();
        switch (position) {
            case 0:
                mNavTextLeft.setTextColor(getResources().getColor(R.color.default_font));
                mLeftFrag.onSelected();
                mLeftFrag.setOnScrollListListener(new HomeFragment.OnScrollListListener() {
                    @Override
                    public void onListScroll(int scrollDisY) {
                        transNavOnScroll(scrollDisY, 0);
                    }
                });
                break;
            case 1:
                mNavTextMid.setTextColor(getResources().getColor(R.color.default_font));
                mMidFrag.onSelected();
                mMidFrag.setOnScrollListListener(new HomeFragment.OnScrollListListener() {
                    @Override
                    public void onListScroll(int scrollDisY) {
                        transNavOnScroll(scrollDisY, 1);
                    }
                });
                break;
            case 2:
                mNavTextRig.setTextColor(getResources().getColor(R.color.default_font));
                mRigFrag.onSelected();
                mRigFrag.setOnScrollListListener(new HomeFragment.OnScrollListListener() {
                    @Override
                    public void onListScroll(int scrollDisY) {
                        transNavOnScroll(scrollDisY, 2);
                    }
                });
                break;
        }
        mCurIndex = position;

    }

    private void resetTabs() {
        mNavTextLeft.setTextColor(getResources().getColor(R.color.text_default_color));
        mNavTextMid.setTextColor(getResources().getColor(R.color.text_default_color));
        mNavTextRig.setTextColor(getResources().getColor(R.color.text_default_color));
    }

    private void initTabIndicatorWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        mScreenWidth = dpMetrics.widthPixels;
        mIndicator.getLayoutParams().width = mScreenWidth/3;
        mIndicator.requestLayout();
    }







    private void transNavOnScroll(int disY, int index) {
        float curTranslation = mNavBar.getTranslationY();
        curTranslation -= disY;
        curTranslation = (int) Math.min(curTranslation, 0);
        curTranslation = (int) Math.max(curTranslation, -getResources().getDimension(R.dimen.toolbar_height));
        mNavBar.setTranslationY(curTranslation);
        mCurNavTrans = curTranslation;
    }

    public float getCurNavTrans() {
        return mCurNavTrans;
    }
    public RelativeLayout getNav() {return mNavBar; }



//    private void getUserInfo() {
//        final String accessToken = AuthUtil.getAccessToken(this);
//
//        String url = DriRegInfo.REQUEST_MY_INFO;
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        parseMyInfo(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(HomeActivity.this, "errors", Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<>();
//                params.put(DriRegInfo.REQUEST_HEAD_AUTH_FIELD, DriRegInfo.REQUEST_HEAD_BEAR + accessToken);
//                params.putAll(super.getHeaders());
//                return params;
//            }
//        };
//
//        request.setShouldCache(false);
//        request.setRetryPolicy(new DefaultRetryPolicy(20000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        NetworkHandler.getInstance(this).addToRequestQueue(request);
//    }

//    private void parseMyInfo(JSONObject data) {
//        final DribleUser user = new DribleUser(data);
//
//        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.ACCOUNT_INFO_MEM, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt(LoginActivity.ACCOUNT_USER_ID, user.getId());
//        editor.commit();
//
//        if (!TextUtils.isEmpty(user.getAvatar_url())) {
//            Uri avatarUri = Uri.parse(user.getAvatar_url());
//            mMyAvatar.setImageURI(avatarUri);
//
//            mUserZone.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mDrawerLayout.closeDrawer(mLeftDrawer);
//                    Intent intent = new Intent(HomeActivity.this, UserInfoActivity.class);
//                    intent.putExtra(UserInfoActivity.USER_ID_EXTRA, user.getId());
//                    startActivity(intent);
//                }
//            });
//
//            mMenuHome.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mDrawerLayout.closeDrawer(mLeftDrawer);
//                }
//            });
////            mMenuNew.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    // to create a shot
////                }
////            });
////            mMenuNew.setVisibility(View.INVISIBLE);
//            mMenuLiked.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(HomeActivity.this, "to like list", Toast.LENGTH_SHORT).show();
//                }
//            });
//            mMenuBuckets.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(HomeActivity.this, "to buckets list", Toast.LENGTH_SHORT).show();
//                }
//            });
//            mMenuAbout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(HomeActivity.this, "to about me page", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//
//        if (!TextUtils.isEmpty(user.getName())) {
//            mMyName.setText(user.getName());
//
//        }
//
//    }


    private void showUserInfo() {
        final DribleUser user = AuthUtil.getMe(this);



        if (user!=null && !TextUtils.isEmpty(user.getAvatar_url())) {
            Uri avatarUri = Uri.parse(user.getAvatar_url());
            mMyAvatar.setImageURI(avatarUri);

            mUserZone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.closeDrawer(mLeftDrawer);
                    Intent intent = new Intent(HomeActivity.this, UserInfoActivity.class);
                    intent.putExtra(UserInfoActivity.USER_ID_EXTRA, user.getId());
                    startActivity(intent);
                }
            });

            mMenuHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.closeDrawer(mLeftDrawer);
                }
            });
//            mMenuNew.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // to create a shot
//                }
//            });
//            mMenuNew.setVisibility(View.INVISIBLE);
            mMenuLiked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(HomeActivity.this, "to like list", Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(mLeftDrawer);
                    SharedPreferences shared = getSharedPreferences(LoginActivity.ACCOUNT_INFO_MEM, Context.MODE_PRIVATE);
                    String like_url = shared.getString(LoginActivity.ACCOUNT_USER_LIKE_URL, null);
                    Intent intent = new Intent(HomeActivity.this, ShotsActivity.class);
                    intent.putExtra(ShotsActivity.SHOTS_URL, like_url);
                    intent.putExtra(ShotsActivity.SHOTS_TITLE_EXTRA, "My liked");
                    intent.putExtra(ShotsActivity.CALL_FROM, "like");
                    startActivity(intent);
                }
            });
            mMenuBuckets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(HomeActivity.this, "to buckets list", Toast.LENGTH_SHORT).show();
                    mDrawerLayout.closeDrawer(mLeftDrawer);
                    SharedPreferences shared = getSharedPreferences(LoginActivity.ACCOUNT_INFO_MEM, Context.MODE_PRIVATE);
                    String buckets_url = shared.getString(LoginActivity.ACCOUNT_USER_BUCKETS_URL, null);
                    Intent intent = new Intent(HomeActivity.this, BucketsActivity.class);
                    intent.putExtra(BucketsActivity.BUCKET_URL, buckets_url);
                    intent.putExtra(BucketsActivity.BUCKET_TITLE, "My buckets");
                    startActivity(intent);
                }
            });
            mMenuAbout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(HomeActivity.this, "to about me page", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                    startActivity(intent);
                }
            });

        }

        if (user!=null && !TextUtils.isEmpty(user.getName())) {
            mMyName.setText(user.getName());

        }


    }


}
