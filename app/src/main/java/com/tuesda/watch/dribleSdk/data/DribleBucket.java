package com.tuesda.watch.dribleSdk.data;

import com.tuesda.watch.dribleSdk.DriRegInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by zhanglei on 15/8/5.
 */
public class DribleBucket {

    public static final String BUCKET_ID = "id",
    BUCKET_NAME  = "name",
    BUCKET_DESCRIPTION = "description",
    BUCKET_SHOTS_COUNT = "shots_count",
    BUCKET_CREATED_AT = "created_at",
    BUCKET_UPDATED_AT = "updated_at";


    private int id;
    private String name;
    private String description;
    private int shots_count;
    private Calendar created_at;
    private Calendar updated_at;

    public DribleBucket() {
    }

    public DribleBucket(int id, String name, String description, int shots_count, Calendar created_at, Calendar updated_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shots_count = shots_count;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public DribleBucket(JSONObject data) {
        try {
            if (data.has(BUCKET_ID)) {
                id = data.getInt(BUCKET_ID);
            }
            if (data.has(BUCKET_NAME)) {
                name = data.getString(BUCKET_NAME);
            }
            if (data.has(BUCKET_DESCRIPTION)) {
                description = data.getString(BUCKET_DESCRIPTION);
            }
            if (data.has(BUCKET_SHOTS_COUNT)) {
                shots_count = data.getInt(BUCKET_SHOTS_COUNT);
            }
            if (data.has(BUCKET_CREATED_AT)) {
                String createStr = data.getString(BUCKET_CREATED_AT);
                created_at = Calendar.getInstance();
                created_at.setTime(DriRegInfo.SIMPLE_DATE_FORMAT.parse(createStr));
            }
            if (data.has(BUCKET_UPDATED_AT)) {
                String updateStr = data.getString(BUCKET_UPDATED_AT);
                updated_at = Calendar.getInstance();
                updated_at.setTime(DriRegInfo.SIMPLE_DATE_FORMAT.parse(updateStr));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String getBucketId() {
        return BUCKET_ID;
    }

    public static String getBucketName() {
        return BUCKET_NAME;
    }

    public static String getBucketDescription() {
        return BUCKET_DESCRIPTION;
    }

    public static String getBucketShotsCount() {
        return BUCKET_SHOTS_COUNT;
    }

    public static String getBucketCreatedAt() {
        return BUCKET_CREATED_AT;
    }

    public static String getBucketUpdatedAt() {
        return BUCKET_UPDATED_AT;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getShots_count() {
        return shots_count;
    }

    public void setShots_count(int shots_count) {
        this.shots_count = shots_count;
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

    @Override
    public String toString() {
        return "DribleBucket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", shots_count=" + shots_count +
                ", created_at=" + DriRegInfo.SIMPLE_DATE_FORMAT.format(created_at.getTime()) +
                ", updated_at=" + DriRegInfo.SIMPLE_DATE_FORMAT.format(updated_at.getTime()) +
                '}';
    }
}
