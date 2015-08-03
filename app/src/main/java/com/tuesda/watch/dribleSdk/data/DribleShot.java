package com.tuesda.watch.dribleSdk.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.tuesda.watch.dribleSdk.DriRegInfo;
import com.tuesda.watch.log.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhanglei on 15/7/26.
 */
public class DribleShot implements Parcelable {

    public static final String
    SHOT_ID = "id",
    SHOT_TITLE = "title",
    SHOT_DESCRIPTION = "description",
    SHOT_WIDTH = "width",
    SHOT_HEIGHT = "height",
    SHOT_IMAGES = "images",
        SHOT_IMAGES_HIDPI = "hidpi",
        SHOT_IMAGES_NORMAL = "normal",
        SHOT_IMAGES_TEASER = "teaser",
    SHOT_VIEWS_COUNT = "views_count",
    SHOT_LIKES_COUNT = "likes_count",
    SHOT_COMMENTS_COUNT = "comments_count",
    SHOT_ATTACHMENTS_COUNT = "attachments_count",
    SHOT_REBOUNDS_COUNT = "rebounds_count",
    SHOT_BUCKETS_COUNT = "buckets_count",
    SHOT_CREATED_AT = "created_at",
    SHOT_UPDATED_AT = "updated_at",
    SHOT_HTML_URL = "html_url",
    SHOT_ATTACHMENTS_URL = "attachments_url",
    SHOT_BUCKETS_URL = "buckets_url",
    SHOT_COMMENTS_URL = "comments_url",
    SHOT_LIKES_URL = "likes_url",
    SHOT_PROJECTS_URL = "projects_url",
    SHOT_REBOUNDS_URL = "rebounds_url",
    SHOT_TAGS = "tags",
    SHOT_USER = "user",
    SHOT_TEAM = "team";

    ;


    private int id;
    private String title;
    private String description;
    private int width;
    private int height;
    /**
     * There are three images:
     * 1. hidpi aka large
     * 2. normal aka middle
     * 3. teaser aka small
     */
    private String[] images = new String[3];
    private int views_count;
    private int likes_count;
    private int comments_count;
    private int attachments_count;
    private int rebounds_count;
    private int buckets_count;
    private Calendar created_at;
    private Calendar updated_at;
    private String html_url;
    private String attachments_url;
    private String buckets_url;
    private String comments_url;
    private String likes_url;
    private String projects_url;
    private String rebounds_url;
    private ArrayList<String> tags;
    private DribleUser user;
    private String team;


    public DribleShot() {
    }

    public DribleShot(JSONObject data) {
        try {
            id = data.getInt(SHOT_ID);
            title = data.getString(SHOT_TITLE);
            description = data.getString(SHOT_DESCRIPTION);
            width = data.getInt(SHOT_WIDTH);
            height = data.getInt(SHOT_HEIGHT);
            JSONObject imageJson = (JSONObject) data.get(SHOT_IMAGES);
            images[0] = imageJson.getString(SHOT_IMAGES_HIDPI);
            images[1] = imageJson.getString(SHOT_IMAGES_NORMAL);
            images[2] = imageJson.getString(SHOT_IMAGES_TEASER);
            for (int i=0; i<3; i++) {
                if (images[i].equals("null")) {
                    images[i] = null;
                }
            }
            views_count = data.getInt(SHOT_VIEWS_COUNT);
            likes_count = data.getInt(SHOT_LIKES_COUNT);
            comments_count = data.getInt(SHOT_COMMENTS_COUNT);
            attachments_count = data.getInt(SHOT_ATTACHMENTS_COUNT);
            rebounds_count = data.getInt(SHOT_REBOUNDS_COUNT);
            buckets_count = data.getInt(SHOT_BUCKETS_COUNT);

            created_at = Calendar.getInstance();
            String created_str = data.getString(SHOT_CREATED_AT);
            Date created_date = DriRegInfo.SIMPLE_DATE_FORMAT.parse(created_str);
            created_at.setTime(created_date);

            updated_at = Calendar.getInstance();
            String updated_str = data.getString(SHOT_UPDATED_AT);
            Date updated_date = DriRegInfo.SIMPLE_DATE_FORMAT.parse(updated_str);
            updated_at.setTime(updated_date);

            html_url = data.getString(SHOT_HTML_URL);
            attachments_url = data.getString(SHOT_ATTACHMENTS_URL);
            buckets_url = data.getString(SHOT_BUCKETS_URL);
            comments_url = data.getString(SHOT_COMMENTS_URL);
            likes_url = data.getString(SHOT_LIKES_URL);
            projects_url = data.getString(SHOT_PROJECTS_URL);
            rebounds_url = data.getString(SHOT_REBOUNDS_URL);
            JSONArray jsonArray = data.getJSONArray(SHOT_TAGS);
            tags = new ArrayList<>();
            for (int i=0; i<jsonArray.length(); i++) {
                tags.add(jsonArray.getString(i));
            }
            if (data.has(SHOT_USER)) {
                JSONObject userJson = data.getJSONObject(SHOT_USER);
                user = new DribleUser(userJson);
            }
            team = data.getString(SHOT_TEAM);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String getShotImagesNormal() {
        return SHOT_IMAGES_NORMAL;
    }

    public static String getShotId() {
        return SHOT_ID;
    }

    public static String getShotTitle() {
        return SHOT_TITLE;
    }

    public static String getShotDescription() {
        return SHOT_DESCRIPTION;
    }

    public static String getShotWidth() {
        return SHOT_WIDTH;
    }

    public static String getShotHeight() {
        return SHOT_HEIGHT;
    }

    public static String getShotImages() {
        return SHOT_IMAGES;
    }

    public static String getShotImagesHidpi() {
        return SHOT_IMAGES_HIDPI;
    }

    public static String getShotImagesTeaser() {
        return SHOT_IMAGES_TEASER;
    }

    public static String getShotViewsCount() {
        return SHOT_VIEWS_COUNT;
    }

    public static String getShotLikesCount() {
        return SHOT_LIKES_COUNT;
    }

    public static String getShotCommentsCount() {
        return SHOT_COMMENTS_COUNT;
    }

    public static String getShotAttachmentsCount() {
        return SHOT_ATTACHMENTS_COUNT;
    }

    public static String getShotReboundsCount() {
        return SHOT_REBOUNDS_COUNT;
    }

    public static String getShotBucketsCount() {
        return SHOT_BUCKETS_COUNT;
    }

    public static String getShotCreatedAt() {
        return SHOT_CREATED_AT;
    }

    public static String getShotUpdatedAt() {
        return SHOT_UPDATED_AT;
    }

    public static String getShotHtmlUrl() {
        return SHOT_HTML_URL;
    }

    public static String getShotAttachmentsUrl() {
        return SHOT_ATTACHMENTS_URL;
    }

    public static String getShotBucketsUrl() {
        return SHOT_BUCKETS_URL;
    }

    public static String getShotCommentsUrl() {
        return SHOT_COMMENTS_URL;
    }

    public static String getShotLikesUrl() {
        return SHOT_LIKES_URL;
    }

    public static String getShotProjectsUrl() {
        return SHOT_PROJECTS_URL;
    }

    public static String getShotReboundsUrl() {
        return SHOT_REBOUNDS_URL;
    }

    public static String getShotTags() {
        return SHOT_TAGS;
    }

    public static String getShotUser() {
        return SHOT_USER;
    }

    public static String getShotTeam() {
        return SHOT_TEAM;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String[] getImages() {
        return images;
    }

    public int getViews_count() {
        return views_count;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public int getAttachments_count() {
        return attachments_count;
    }

    public int getRebounds_count() {
        return rebounds_count;
    }

    public int getBuckets_count() {
        return buckets_count;
    }

    public Calendar getCreated_at() {
        return created_at;
    }

    public Calendar getUpdated_at() {
        return updated_at;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getAttachments_url() {
        return attachments_url;
    }

    public String getBuckets_url() {
        return buckets_url;
    }

    public String getComments_url() {
        return comments_url;
    }

    public String getLikes_url() {
        return likes_url;
    }

    public String getProjects_url() {
        return projects_url;
    }

    public String getRebounds_url() {
        return rebounds_url;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public DribleUser getUser() {
        return user;
    }

    public String getTeam() {
        return team;
    }

    public void setUpdated_at(Calendar updated_at) {
        this.updated_at = updated_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public void setAttachments_count(int attachments_count) {
        this.attachments_count = attachments_count;
    }

    public void setRebounds_count(int rebounds_count) {
        this.rebounds_count = rebounds_count;
    }

    public void setBuckets_count(int buckets_count) {
        this.buckets_count = buckets_count;
    }

    public void setCreated_at(Calendar created_at) {
        this.created_at = created_at;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public void setAttachments_url(String attachments_url) {
        this.attachments_url = attachments_url;
    }

    public void setBuckets_url(String buckets_url) {
        this.buckets_url = buckets_url;
    }

    public void setComments_url(String comments_url) {
        this.comments_url = comments_url;
    }

    public void setLikes_url(String likes_url) {
        this.likes_url = likes_url;
    }

    public void setProjects_url(String projects_url) {
        this.projects_url = projects_url;
    }

    public void setRebounds_url(String rebounds_url) {
        this.rebounds_url = rebounds_url;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setUser(DribleUser user) {
        this.user = user;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public DribleShot(Parcel in) {
        Bundle bundle = in.readBundle();
        id = bundle.getInt(SHOT_ID);
        title = bundle.getString(SHOT_TITLE);
        description = bundle.getString(SHOT_DESCRIPTION);
        width = bundle.getInt(SHOT_WIDTH);
        height = bundle.getInt(SHOT_HEIGHT);
        ArrayList<String> imagesList = bundle.getStringArrayList(SHOT_IMAGES);
        for (int i=0; i<imagesList.size(); i++) {
            if (i>2) break; // there should only 3 images
            images[i] = imagesList.get(i);
        }
        views_count = bundle.getInt(SHOT_VIEWS_COUNT);
        likes_count = bundle.getInt(SHOT_LIKES_COUNT);
        comments_count = bundle.getInt(SHOT_COMMENTS_COUNT);
        attachments_count = bundle.getInt(SHOT_ATTACHMENTS_COUNT);
        rebounds_count = bundle.getInt(SHOT_ATTACHMENTS_COUNT);
        buckets_count = bundle.getInt(SHOT_BUCKETS_COUNT);
        created_at = (Calendar) bundle.getSerializable(SHOT_CREATED_AT);
        updated_at = (Calendar) bundle.getSerializable(SHOT_UPDATED_AT);
        html_url = bundle.getString(SHOT_HTML_URL);
        attachments_url = bundle.getString(SHOT_ATTACHMENTS_URL);
        buckets_url = bundle.getString(SHOT_BUCKETS_URL);
        comments_url = bundle.getString(SHOT_COMMENTS_URL);
        likes_url = bundle.getString(SHOT_LIKES_URL);
        projects_url = bundle.getString(SHOT_PROJECTS_URL);
        rebounds_url = bundle.getString(SHOT_REBOUNDS_URL);
        tags = bundle.getStringArrayList(SHOT_TAGS);
        int userId = bundle.getInt(SHOT_USER);
        user = new DribleUser();
        user.setId(userId);

        team = bundle.getString(SHOT_TEAM);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt(SHOT_ID, id);
        bundle.putString(SHOT_TITLE, title);
        bundle.putString(SHOT_DESCRIPTION, description);
        bundle.putInt(SHOT_WIDTH, width);
        bundle.putInt(SHOT_HEIGHT, height);
        ArrayList<String> imagesList = new ArrayList<>();
        for (int i=0; i<images.length; i++) {
            imagesList.add(images[i]);
        }
        bundle.putStringArrayList(SHOT_IMAGES, imagesList);
        bundle.putInt(SHOT_VIEWS_COUNT, views_count);
        bundle.putInt(SHOT_LIKES_COUNT, likes_count);
        bundle.putInt(SHOT_COMMENTS_COUNT, comments_count);
        bundle.putInt(SHOT_ATTACHMENTS_COUNT, attachments_count);
        bundle.putInt(SHOT_REBOUNDS_COUNT, rebounds_count);
        bundle.putInt(SHOT_BUCKETS_COUNT, buckets_count);
        bundle.putSerializable(SHOT_CREATED_AT, created_at);
        bundle.putSerializable(SHOT_UPDATED_AT, updated_at);
        bundle.putString(SHOT_HTML_URL, html_url);
        bundle.putString(SHOT_ATTACHMENTS_URL, attachments_url);
        bundle.putString(SHOT_BUCKETS_URL, buckets_url);
        bundle.putString(SHOT_COMMENTS_URL, comments_url);
        bundle.putString(SHOT_LIKES_URL, likes_url);
        bundle.putString(SHOT_PROJECTS_URL, projects_url);
        bundle.putString(SHOT_REBOUNDS_URL, rebounds_url);
        bundle.putStringArrayList(SHOT_TAGS, tags);
        bundle.putInt(SHOT_USER, user.getId());

        bundle.putString(SHOT_TEAM, team);
        dest.writeBundle(bundle);

    }

    public static final Parcelable.Creator<DribleShot> CREATOR = new Creator<DribleShot>() {
        @Override
        public DribleShot createFromParcel(Parcel source) {
            return new DribleShot(source);
        }

        @Override
        public DribleShot[] newArray(int size) {
            return new DribleShot[size];
        }
    };

    @Override
    public String toString() {
        return "DribleShot{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", images=" + Arrays.toString(images) +
                ", views_count=" + views_count +
                ", likes_count=" + likes_count +
                ", comments_count=" + comments_count +
                ", attachments_count=" + attachments_count +
                ", rebounds_count=" + rebounds_count +
                ", buckets_count=" + buckets_count +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", html_url='" + html_url + '\'' +
                ", attachments_url='" + attachments_url + '\'' +
                ", buckets_url='" + buckets_url + '\'' +
                ", comments_url='" + comments_url + '\'' +
                ", likes_url='" + likes_url + '\'' +
                ", projects_url='" + projects_url + '\'' +
                ", rebounds_url='" + rebounds_url + '\'' +
                ", tags=" + tags +
                ", user=" + user +
                ", team='" + team + '\'' +
                '}';
    }
}
