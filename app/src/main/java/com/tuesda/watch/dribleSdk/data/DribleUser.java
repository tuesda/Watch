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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhanglei on 15/7/26.
 */
public class DribleUser implements Parcelable {


    public static final String
            USER_ID = "id",
            USER_NAME = "name",
            USER_USERNAME = "username",
            USER_HTML_URL = "html_url",
            USER_AVATAR_URL = "avatar_url",

    USER_BIO = "bio",
            USER_LOCATION = "location",
            USER_LINKS = "links",
            USER_LINKS_KEYS = "links.keys",
            USER_LINKS_VALUES = "links.values",
            USER_BUCKETS_COUNT = "buckets_count",
            USER_COMMENTS_RECEIVED_COUNT = "comments_received_count",

    USER_FOLLOWERS_COUNT = "followers_count",
            USER_FOLLOWINGS_COUNT = "followings_count",
            USER_LIKES_COUNT = "likes_count",
            USER_LIKES_RECEIVED_COUNT = "likes_received_count",
            USER_PROJECTS_COUNT = "projects_count",

    USER_REBOUNDS_RECEIVED_COUNT = "rebounds_received_count",
            USER_SHOTS_COUNT = "shots_count",
            USER_TEAMS_COUNT = "teams_count",
            USER_CAN_UPLOAD_SHOT = "can_upload_shot",
            USER_TYPE = "type",

    USER_PRO = "pro",
            USER_BUCKETS_URL = "buckets_url",
            USER_FOLLOWERS_URL = "followers_url",
            USER_FOLLOWINGS_URL = "following_url",
            USER_LIKES_URL = "likes_url",

    USER_PROJECTS_URL = "projects_url",
            USER_SHOTS_URL = "shots_url",
            USER_TEAMS_URL = "teams_url",
            USER_CREATED_AT = "created_at",
            USER_UPDATED_AT = "updated_at";


    // for mem, totally 30 fields
    private int id;
    private String name;
    private String username;
    private String html_url;
    private String avatar_url;
    private String bio;
    private String location;
    private Map<String, String> links;
    private int buckets_count;
    private int comments_received_count;
    private int followers_count;
    private int followings_count;
    private int likes_count;
    private int likes_received_count;
    private int projects_count;
    private int rebounds_received_count;
    private int shots_count;
    private int teams_count;
    private boolean can_upload_shot;
    private String type;
    private boolean pro;
    private String buckets_url;
    private String followers_url;
    private String followings_url;
    private String likes_url;
    private String projects_url;
    private String shots_url;
    private String teams_url;
    private Calendar created_at;
    private Calendar updated_at;


    public DribleUser() {
    }

    public DribleUser(JSONObject data) {
        try {
            if (data.has(USER_ID)) id = data.getInt(USER_ID);
            if (data.has(USER_NAME)) name = data.getString(USER_NAME);
            if (data.has(USER_USERNAME)) username = data.getString(USER_USERNAME);
            if (data.has(USER_HTML_URL)) html_url = data.getString(USER_HTML_URL);
            if (data.has(USER_AVATAR_URL)) avatar_url = data.getString(USER_AVATAR_URL);
            if (data.has(USER_BIO)) bio = data.getString(USER_BIO);
            if (data.has(USER_LOCATION)) location = data.getString(USER_LOCATION);
            if (data.has(USER_LINKS)) {
                JSONObject linkJson = (JSONObject) data.get(USER_LINKS);

                HashMap<String, String> linksFromD = new HashMap<>();
                Iterator iter = linkJson.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    String value = (String) linkJson.get(key);
                    linksFromD.put(key, value);
                }
                links = linksFromD;
            }

            if (data.has(USER_BUCKETS_COUNT)) buckets_count = data.getInt(USER_BUCKETS_COUNT);
            if (data.has(USER_COMMENTS_RECEIVED_COUNT)) comments_received_count = data.getInt(USER_COMMENTS_RECEIVED_COUNT);
            if (data.has(USER_FOLLOWERS_COUNT)) followers_count = data.getInt(USER_FOLLOWERS_COUNT);
            if (data.has(USER_FOLLOWINGS_COUNT)) followings_count = data.getInt(USER_FOLLOWINGS_COUNT);
            if (data.has(USER_LIKES_COUNT)) likes_count = data.getInt(USER_LIKES_COUNT);
            if (data.has(USER_LIKES_RECEIVED_COUNT)) likes_received_count = data.getInt(USER_LIKES_RECEIVED_COUNT);
            if (data.has(USER_PROJECTS_COUNT)) projects_count = data.getInt(USER_PROJECTS_COUNT);
            if (data.has(USER_REBOUNDS_RECEIVED_COUNT)) rebounds_received_count = data.getInt(USER_REBOUNDS_RECEIVED_COUNT);
            if (data.has(USER_SHOTS_COUNT)) shots_count = data.getInt(USER_SHOTS_COUNT);
            if (data.has(USER_TEAMS_COUNT)) teams_count = data.getInt(USER_TEAMS_COUNT);
            if (data.has(USER_CAN_UPLOAD_SHOT)) can_upload_shot = data.getBoolean(USER_CAN_UPLOAD_SHOT);
            if (data.has(USER_TYPE)) type = data.getString(USER_TYPE);
            if (data.has(USER_PRO)) pro = data.getBoolean(USER_PRO);
            if (data.has(USER_BUCKETS_URL)) buckets_url = data.getString(USER_BUCKETS_URL);
            if (data.has(USER_FOLLOWERS_URL)) followers_url = data.getString(USER_FOLLOWERS_URL);
            if (data.has(USER_FOLLOWINGS_URL)) followings_url = data.getString(USER_FOLLOWINGS_URL);
            if (data.has(USER_LIKES_URL)) likes_url = data.getString(USER_LIKES_URL);
            if (data.has(USER_PROJECTS_URL)) projects_url = data.getString(USER_PROJECTS_URL);
            if (data.has(USER_SHOTS_URL)) shots_url = data.getString(USER_SHOTS_URL);
            if (data.has(USER_TEAMS_URL)) teams_url = data.getString(USER_TEAMS_URL);

            if (data.has(USER_CREATED_AT)) {
                created_at = Calendar.getInstance();
                Date create_date = DriRegInfo.SIMPLE_DATE_FORMAT.parse(data.getString(USER_CREATED_AT));
                created_at.setTime(create_date);
            }
            if (data.has(USER_UPDATED_AT)) {
                updated_at = Calendar.getInstance();
                Date updated_date = DriRegInfo.SIMPLE_DATE_FORMAT.parse(data.getString(USER_UPDATED_AT));
                updated_at.setTime(updated_date);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json parse error userId:" + id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public DribleUser(int comments_received_count, int id, String name, String username, String html_url,
                      String avatar_url, String bio, String location, Map<String, String> links, int buckets_count,
                      int followers_count, int followings_count, int likes_count, int likes_received_count,
                      int projects_count, int rebounds_received_count, int shots_count, int teams_count,
                      boolean can_upload_shot, String type, boolean pro, String buckets_url, String followers_url,
                      String followings_url, String likes_url, String projects_url, String shots_url, String teams_url,
                      Calendar created_at, Calendar updated_at) {
        this.comments_received_count = comments_received_count;
        this.id = id;
        this.name = name;
        this.username = username;
        this.html_url = html_url;
        this.avatar_url = avatar_url;
        this.bio = bio;
        this.location = location;
        this.links = links;
        this.buckets_count = buckets_count;
        this.followers_count = followers_count;
        this.followings_count = followings_count;
        this.likes_count = likes_count;
        this.likes_received_count = likes_received_count;
        this.projects_count = projects_count;
        this.rebounds_received_count = rebounds_received_count;
        this.shots_count = shots_count;
        this.teams_count = teams_count;
        this.can_upload_shot = can_upload_shot;
        this.type = type;
        this.pro = pro;
        this.buckets_url = buckets_url;
        this.followers_url = followers_url;
        this.followings_url = followings_url;
        this.likes_url = likes_url;
        this.projects_url = projects_url;
        this.shots_url = shots_url;
        this.teams_url = teams_url;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public DribleUser(Parcel in) {
        Bundle bundle = in.readBundle();
        this.comments_received_count = bundle.getInt(USER_COMMENTS_RECEIVED_COUNT);
        this.id = bundle.getInt(USER_ID);
        this.name = bundle.getString(USER_NAME);
        this.username = bundle.getString(USER_USERNAME);
        this.html_url = bundle.getString(USER_HTML_URL);
        this.avatar_url = bundle.getString(USER_AVATAR_URL);
        this.bio = bundle.getString(USER_BIO);
        this.location = bundle.getString(USER_LOCATION);
        ArrayList<String> links_keys = bundle.getStringArrayList(USER_LINKS_KEYS);
        ArrayList<String> links_values = bundle.getStringArrayList(USER_LINKS_VALUES);
        HashMap<String, String> linksFromP = new HashMap<>();
        for (int i = 0; i < links_keys.size(); i++) {
            linksFromP.put(links_keys.get(i), links_values.get(i));
        }

        this.links = linksFromP;
        this.buckets_count = bundle.getInt(USER_BUCKETS_COUNT);
        this.followers_count = bundle.getInt(USER_FOLLOWERS_COUNT);
        this.followings_count = bundle.getInt(USER_FOLLOWINGS_COUNT);
        this.likes_count = bundle.getInt(USER_LIKES_COUNT);
        this.likes_received_count = bundle.getInt(USER_LIKES_RECEIVED_COUNT);
        this.projects_count = bundle.getInt(USER_PROJECTS_COUNT);
        this.rebounds_received_count = bundle.getInt(USER_REBOUNDS_RECEIVED_COUNT);
        this.shots_count = bundle.getInt(USER_SHOTS_COUNT);
        this.teams_count = bundle.getInt(USER_TEAMS_COUNT);
        this.can_upload_shot = bundle.getBoolean(USER_CAN_UPLOAD_SHOT);
        this.type = bundle.getString(USER_TYPE);
        this.pro = bundle.getBoolean(USER_PRO);
        this.buckets_url = bundle.getString(USER_BUCKETS_URL);
        this.followers_url = bundle.getString(USER_FOLLOWERS_URL);
        this.followings_url = bundle.getString(USER_FOLLOWINGS_URL);
        this.likes_url = bundle.getString(USER_LIKES_URL);
        this.projects_url = bundle.getString(USER_PROJECTS_URL);
        this.shots_url = bundle.getString(USER_SHOTS_URL);
        this.teams_url = bundle.getString(USER_TEAMS_URL);
        this.created_at = (Calendar) bundle.get(USER_CREATED_AT);
        this.updated_at = (Calendar) bundle.get(USER_UPDATED_AT);
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getBio() {
        return bio;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public int getBuckets_count() {
        return buckets_count;
    }

    public int getComments_received_count() {
        return comments_received_count;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public int getFollowings_count() {
        return followings_count;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public int getLikes_received_count() {
        return likes_received_count;
    }

    public int getProjects_count() {
        return projects_count;
    }

    public int getRebounds_received_count() {
        return rebounds_received_count;
    }

    public int getShots_count() {
        return shots_count;
    }

    public int getTeams_count() {
        return teams_count;
    }

    public boolean isCan_upload_shot() {
        return can_upload_shot;
    }

    public String getType() {
        return type;
    }

    public boolean isPro() {
        return pro;
    }

    public String getBuckets_url() {
        return buckets_url;
    }

    public String getFollowers_url() {
        return followers_url;
    }

    public String getFollowings_url() {
        return followings_url;
    }

    public String getLikes_url() {
        return likes_url;
    }

    public String getProjects_url() {
        return projects_url;
    }

    public String getShots_url() {
        return shots_url;
    }

    public String getTeams_url() {
        return teams_url;
    }

    public Calendar getCreated_at() {
        return created_at;
    }

    public Calendar getUpdated_at() {
        return updated_at;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public void setBuckets_count(int buckets_count) {
        this.buckets_count = buckets_count;
    }

    public void setComments_received_count(int comments_received_count) {
        this.comments_received_count = comments_received_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public void setFollowings_count(int followings_count) {
        this.followings_count = followings_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public void setLikes_received_count(int likes_received_count) {
        this.likes_received_count = likes_received_count;
    }

    public void setProjects_count(int projects_count) {
        this.projects_count = projects_count;
    }

    public void setRebounds_received_count(int rebounds_received_count) {
        this.rebounds_received_count = rebounds_received_count;
    }

    public void setShots_count(int shots_count) {
        this.shots_count = shots_count;
    }

    public void setTeams_count(int teams_count) {
        this.teams_count = teams_count;
    }

    public void setCan_upload_shot(boolean can_upload_shot) {
        this.can_upload_shot = can_upload_shot;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPro(boolean pro) {
        this.pro = pro;
    }

    public void setBuckets_url(String buckets_url) {
        this.buckets_url = buckets_url;
    }

    public void setFollowers_url(String followers_url) {
        this.followers_url = followers_url;
    }

    public void setFollowings_url(String followings_url) {
        this.followings_url = followings_url;
    }

    public void setLikes_url(String likes_url) {
        this.likes_url = likes_url;
    }

    public void setProjects_url(String projects_url) {
        this.projects_url = projects_url;
    }

    public void setShots_url(String shots_url) {
        this.shots_url = shots_url;
    }

    public void setTeams_url(String teams_url) {
        this.teams_url = teams_url;
    }

    public void setCreated_at(Calendar created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(Calendar updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt(USER_COMMENTS_RECEIVED_COUNT, comments_received_count);
        bundle.putInt(USER_ID, id);
        this.id = bundle.getInt(USER_ID);
        bundle.putString(USER_NAME, name);
        bundle.putString(USER_USERNAME, username);
        bundle.putString(USER_HTML_URL, html_url);
        bundle.putString(USER_AVATAR_URL, avatar_url);
        bundle.putString(USER_BIO, bio);
        bundle.putString(USER_LOCATION, location);

        bundle.putStringArrayList(USER_LINKS_KEYS, new ArrayList<>(links.keySet()));
        bundle.putStringArrayList(USER_LINKS_VALUES, new ArrayList<>(links.values()));


        bundle.putInt(USER_BUCKETS_COUNT, buckets_count);
        bundle.putInt(USER_FOLLOWERS_COUNT, followers_count);
        bundle.putInt(USER_FOLLOWINGS_COUNT, followings_count);
        bundle.putInt(USER_LIKES_COUNT, likes_count);
        bundle.putInt(USER_LIKES_RECEIVED_COUNT, likes_received_count);
        bundle.putInt(USER_PROJECTS_COUNT, projects_count);
        bundle.putInt(USER_REBOUNDS_RECEIVED_COUNT, rebounds_received_count);
        bundle.putInt(USER_SHOTS_COUNT, shots_count);
        bundle.putInt(USER_TEAMS_COUNT, teams_count);
        bundle.putBoolean(USER_CAN_UPLOAD_SHOT, can_upload_shot);
        bundle.putString(USER_TYPE, type);
        bundle.putBoolean(USER_PRO, pro);
        bundle.putString(USER_BUCKETS_URL, buckets_url);
        bundle.putString(USER_FOLLOWERS_URL, followers_url);
        bundle.putString(USER_FOLLOWINGS_URL, followings_url);
        bundle.putString(USER_LIKES_URL, likes_url);
        bundle.putString(USER_PROJECTS_URL, projects_url);
        bundle.putString(USER_SHOTS_URL, shots_url);
        bundle.putString(USER_TEAMS_URL, teams_url);
        this.teams_url = bundle.getString(USER_TEAMS_URL);
        bundle.putSerializable(USER_CREATED_AT, created_at);
        bundle.putSerializable(USER_UPDATED_AT, updated_at);
        dest.writeBundle(bundle);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<DribleUser>() {
        @Override
        public DribleUser createFromParcel(Parcel source) {
            return new DribleUser(source);
        }

        @Override
        public DribleUser[] newArray(int size) {
            return new DribleUser[size];
        }
    };


    @Override
    public String toString() {
        return "DribleUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", html_url='" + html_url + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", bio='" + bio + '\'' +
                ", location='" + location + '\'' +
                ", links=" + links +
                ", buckets_count=" + buckets_count +
                ", comments_received_count=" + comments_received_count +
                ", followers_count=" + followers_count +
                ", followings_count=" + followings_count +
                ", likes_count=" + likes_count +
                ", likes_received_count=" + likes_received_count +
                ", projects_count=" + projects_count +
                ", rebounds_received_count=" + rebounds_received_count +
                ", shots_count=" + shots_count +
                ", teams_count=" + teams_count +
                ", can_upload_shot=" + can_upload_shot +
                ", type='" + type + '\'' +
                ", pro=" + pro +
                ", buckets_url='" + buckets_url + '\'' +
                ", followers_url='" + followers_url + '\'' +
                ", followings_url='" + followings_url + '\'' +
                ", likes_url='" + likes_url + '\'' +
                ", projects_url='" + projects_url + '\'' +
                ", shots_url='" + shots_url + '\'' +
                ", teams_url='" + teams_url + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }


}
