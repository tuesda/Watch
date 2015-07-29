package com.tuesda.watch.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tuesda.watch.R;
import com.tuesda.watch.activities.homefragments.FragmentAdapter;
import com.tuesda.watch.activities.homefragments.HomeFragment;
import com.tuesda.watch.log.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanglei on 15/7/25.
 */
public class HomeActivity extends FragmentActivity {


    private RelativeLayout mNavBar;
    private int mLastScrollY;
    private int mLastIndex;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_home);
        initView();
        initPager();
        initTabIndicatorWidth();
    }

    private void initView() {
        mNavBar = (RelativeLayout) findViewById(R.id.nav);


        mNavBtnLeft = (RelativeLayout) findViewById(R.id.nav_btn_l);
        mNavBtnMid = (RelativeLayout) findViewById(R.id.nav_btn_m);
        mNavBtnRig = (RelativeLayout) findViewById(R.id.nav_btn_r);

        mNavTextLeft = (TextView) findViewById(R.id.nav_bottom_l);
        mNavTextMid = (TextView) findViewById(R.id.nav_bottom_m);
        mNavTextRig = (TextView) findViewById(R.id.nav_bottom_r);

        mContentPager = (ViewPager) findViewById(R.id.home_content);
        mIndicator = findViewById(R.id.home_pager_indicator);
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
                break;
            case 1:
                mNavTextMid.setTextColor(getResources().getColor(R.color.default_font));
                mMidFrag.onSelected();
                break;
            case 2:
                mNavTextRig.setTextColor(getResources().getColor(R.color.default_font));
                mRigFrag.onSelected();
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


}
