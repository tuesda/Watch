package com.tuesda.watch.activities.shotlistadapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tuesda.watch.R;
import com.tuesda.watch.activities.UserInfoActivity;
import com.tuesda.watch.dribleSdk.data.DribleShot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by zhanglei on 15/7/28.
 */
public class ShotListAdapter extends BaseAdapter {
    private ArrayList<DribleShot> mDribleShots = new ArrayList<DribleShot>();
    private LayoutInflater mInflater;
    private Context mContext;

    public ShotListAdapter(Context context, ArrayList<DribleShot> mDribleShots) {
        this.mDribleShots = mDribleShots;
        mContext = context;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return mDribleShots.size();
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
        final ShotViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.shot_list_item, parent, false);
            holder = new ShotViewHolder();
            holder.itemHeader = (TextView) convertView.findViewById(R.id.item_header_text);
            holder.itemImage = (SimpleDraweeView) convertView.findViewById(R.id.item_image_view);
            holder.itemTitle = (TextView) convertView.findViewById(R.id.item_title);
            holder.itemAuthor = (RelativeLayout) convertView.findViewById(R.id.author);
            holder.itemAvatar = (SimpleDraweeView) convertView.findViewById(R.id.item_author_avatar);
            holder.itemAuthName = (TextView) convertView.findViewById(R.id.item_author_name);
            holder.itemCreate = (TextView) convertView.findViewById(R.id.item_create_date);
            holder.itemLikesCount = (TextView) convertView.findViewById(R.id.item_likes_count);
            convertView.setTag(holder);
        } else {
            holder = (ShotViewHolder) convertView.getTag();
        }
        final DribleShot dribleShot = mDribleShots.get(position);
        if (dribleShot.getTags()!=null && dribleShot.getTags().size()>0 &&!TextUtils.isEmpty(dribleShot.getTags().get(0))) {
            holder.itemHeader.setText(dribleShot.getTags().get(0));
            holder.itemHeader.setVisibility(View.VISIBLE);
        } else {
            holder.itemHeader.setVisibility(View.INVISIBLE);
        }
        String imgStr = dribleShot.getImages()[1];
        Uri imgUri = Uri.parse(imgStr);
        if (imgStr.endsWith(".gif")) {
            setupGif(imgUri, holder.itemImage);
        } else {
            holder.itemImage.setImageURI(imgUri);
        }


        holder.itemTitle.setText(dribleShot.getTitle());
        Uri avatarUri = Uri.parse(dribleShot.getUser().getAvatar_url());
        holder.itemAvatar.setImageURI(avatarUri);
        holder.itemAuthName.setText(dribleShot.getUser().getName());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        holder.itemCreate.setText(formatter.format(dribleShot.getCreated_at().getTime()));
        holder.itemLikesCount.setText("" + dribleShot.getLikes_count());

//        holder.itemHeader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "will show list about this tag", Toast.LENGTH_LONG).show();
//            }
//        });

        holder.itemAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra(UserInfoActivity.USER_ID_EXTRA, dribleShot.getUser().getId());
                mContext.startActivity(intent);
            }
        });



        return convertView;
    }

    private void setupGif(Uri imgUri, SimpleDraweeView imageView) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imgUri)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();
        imageView.setController(controller);
    }



    class ShotViewHolder {
        TextView itemHeader;
        SimpleDraweeView itemImage;
        TextView itemTitle;
        RelativeLayout itemAuthor;
        SimpleDraweeView itemAvatar;
        TextView itemAuthName;
        TextView itemCreate;
        TextView itemLikesCount;
    }
}
