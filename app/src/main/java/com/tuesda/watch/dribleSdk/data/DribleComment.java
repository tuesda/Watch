package com.tuesda.watch.dribleSdk.data;

import com.tuesda.watch.dribleSdk.DriRegInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by zhanglei on 15/8/1.
 */
public class DribleComment {
    private int id;
    private String body;
    private int likes_count;
    private String likes_url;
    private Calendar created_at;
    private Calendar updated_at;
    private DribleUser user;


    private static final String COMMENT_ID = "id",
    COMMENT_BODY = "body",
    COMMENT_LIKES_COUNT = "likes_count",
    COMMENT_LIKES_URL = "likes_url",
    COMMENT_CREATED_AT = "created_at",
    COMMENT_UPDATED_AT = "updated_at",
    COMMENT_USER = "user";



    public DribleComment() {
    }

    public DribleComment(JSONObject data) {
        try {
            if (data.has(COMMENT_ID)) {
                id = data.getInt(COMMENT_ID);
            }
            if (data.has(COMMENT_BODY)) {
                body = data.getString(COMMENT_BODY);
            }
            if (data.has(COMMENT_LIKES_COUNT)) {
                likes_count = data.getInt(COMMENT_LIKES_COUNT);
            }
            if (data.has(COMMENT_LIKES_URL)) {
                likes_url = data.getString(COMMENT_LIKES_URL);
            }
            if (data.has(COMMENT_CREATED_AT)) {
                String str = data.getString(COMMENT_CREATED_AT);
                created_at = Calendar.getInstance();
                created_at.setTime(DriRegInfo.SIMPLE_DATE_FORMAT.parse(str));
            }
            if (data.has(COMMENT_UPDATED_AT)) {
                String str = data.getString(COMMENT_UPDATED_AT);
                updated_at = Calendar.getInstance();
                updated_at.setTime(DriRegInfo.SIMPLE_DATE_FORMAT.parse(str));
            }
            if (data.has(COMMENT_USER)) {
                JSONObject userJson = data.getJSONObject(COMMENT_USER);
                user = new DribleUser(userJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public String getLikes_url() {
        return likes_url;
    }

    public void setLikes_url(String likes_url) {
        this.likes_url = likes_url;
    }

    public Calendar getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Calendar created_at) {
        this.created_at = created_at;
    }

    public Calendar getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Calendar updated_at) {
        this.updated_at = updated_at;
    }

    public DribleUser getUser() {
        return user;
    }

    public void setUser(DribleUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "DribleComment{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", likes_count=" + likes_count +
                ", likes_url='" + likes_url + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", user=" + user.toString() +
                '}';
    }
}
