package com.tuesda.watch.activities.bucketadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tuesda.watch.R;
import com.tuesda.watch.dribleSdk.data.DribleBucket;

import java.util.ArrayList;

/**
 * Created by zhanglei on 15/8/5.
 */
public class BucketListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<DribleBucket> mBuckets;

    public BucketListAdapter(Context context, ArrayList<DribleBucket> mBuckets) {
        this.mContext = context;
        this.mBuckets = mBuckets;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mBuckets.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.bucket_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.bucket_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(mBuckets.get(position).getName());


        return convertView;
    }

    static class ViewHolder {
        TextView name;
    }
}
