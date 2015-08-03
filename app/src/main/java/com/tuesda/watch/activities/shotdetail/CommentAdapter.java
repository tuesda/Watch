package com.tuesda.watch.activities.shotdetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tuesda.watch.R;
import com.tuesda.watch.activities.UserInfoActivity;
import com.tuesda.watch.dribleSdk.data.DribleComment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhanglei on 15/8/2.
 */
public class CommentAdapter extends BaseAdapter {

    private ArrayList<DribleComment> mComments;
    private Context mContext;
    private LayoutInflater mInflater;

    public CommentAdapter(Context context, ArrayList<DribleComment> mComments) {
        this.mComments = mComments;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mComments.size();
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
        final HolderView holder;
        if (convertView==null) {
            convertView = mInflater.inflate(R.layout.comment_item, parent, false);
            holder = new HolderView();
            holder.commentBody = (TextView) convertView.findViewById(R.id.comment_item_body);
            holder.avatar = (SimpleDraweeView) convertView.findViewById(R.id.comment_item_avatar);
            holder.authorName = (TextView) convertView.findViewById(R.id.comment_author_name);
            holder.created_at = (TextView) convertView.findViewById(R.id.comment_created_at);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }
        final DribleComment comment = mComments.get(position);
        holder.commentBody.setText(Html.fromHtml(comment.getBody()));
        holder.commentBody.setMovementMethod(LinkMovementMethod.getInstance());

        Uri uri = Uri.parse(comment.getUser().getAvatar_url());
        holder.avatar.setImageURI(uri);
        holder.authorName.setText(comment.getUser().getName());
        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra(UserInfoActivity.USER_ID_EXTRA, comment.getUser().getId());
                mContext.startActivity(intent);
            }
        });

        holder.authorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra(UserInfoActivity.USER_ID_EXTRA, comment.getUser().getId());
                mContext.startActivity(intent);
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        holder.created_at.setText(formatter.format(comment.getCreated_at().getTime()));
        return convertView;
    }

    class HolderView {
        TextView commentBody;
        SimpleDraweeView avatar;
        TextView authorName;
        TextView created_at;
    }
}
