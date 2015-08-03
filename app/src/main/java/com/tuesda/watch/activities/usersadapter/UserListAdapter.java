package com.tuesda.watch.activities.usersadapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tuesda.watch.R;
import com.tuesda.watch.activities.UserInfoActivity;
import com.tuesda.watch.dribleSdk.data.DribleUser;

import java.util.ArrayList;

/**
 * Created by zhanglei on 15/8/2.
 */
public class UserListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DribleUser> mUsers;
    private LayoutInflater mInflater;

    public UserListAdapter(Context context, ArrayList<DribleUser> mUsers) {
        this.mContext = context;
        this.mUsers = mUsers;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mUsers.size();
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
            convertView = (RelativeLayout) mInflater.inflate(R.layout.user_item, parent, false);
            holder = new ViewHolder();
            holder.avatar = (SimpleDraweeView) convertView.findViewById(R.id.user_item_avater);
            holder.userName = (TextView) convertView.findViewById(R.id.user_item_name);
            holder.userDescrip = (TextView) convertView.findViewById(R.id.user_item_description);
            holder.detailText = (TextView) convertView.findViewById(R.id.user_item_detail_btn);
            holder.detailZone = (RelativeLayout) convertView.findViewById(R.id.user_item_detail_zone);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(mUsers.get(position).getAvatar_url())) {
            Uri avatarUri = Uri.parse(mUsers.get(position).getAvatar_url());
            holder.avatar.setImageURI(avatarUri);
            final int userId = mUsers.get(position).getId();
            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra(UserInfoActivity.USER_ID_EXTRA, userId);
                    mContext.startActivity(intent);
                }
            });
        } else {
            holder.avatar.setImageURI(null);
        }

        if (!TextUtils.isEmpty(mUsers.get(position).getName())) {
            holder.userName.setText(mUsers.get(position).getName());
        } else {
            holder.userName.setText("");
        }

        if (!TextUtils.isEmpty(mUsers.get(position).getBio())) {
            holder.userDescrip.setText(Html.fromHtml(mUsers.get(position).getBio()));
            holder.userDescrip.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            holder.userDescrip.setText("");
        }
        final int userId = mUsers.get(position).getId();

        if (userId != 0) {
            holder.detailText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra(UserInfoActivity.USER_ID_EXTRA, userId);
                    mContext.startActivity(intent);
                }
            });
        }


        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView avatar;
        TextView userName;
        TextView userDescrip;
        RelativeLayout detailZone;
        TextView detailText;
    }
}
