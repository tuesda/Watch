package com.tuesda.watch.dribleSdk;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by zhanglei on 15/7/23.
 */
public class DriRegInfo {



    public static final String DRIBLE_AUTH_BASE = "https://dribbble.com/oauth/authorize";
    public static final String DRIBLE_TOKEN_URL = "https://dribbble.com/oauth/token";

    public static final String DRIBLE_CALL_BACK = "walker://www.tuesda.watch";
    public static final String DRIBLE_CLIENT_ID = "75349c965ebf2921cf1aebb3e3e442692441f49df73136f3483b2e0fcd55410d";
    public static final String DRIBLE_SECRET = "33528ad1f9a36832eda52ab7d19e3c382c5d6c4ff993e079a5a4f8aca09ab388";
    public static Random mGen = new Random(89);
    public static String mState;
    static {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i<10; i++) {
            str.append(mGen.nextInt(26) + 'a');
        }
    }

    public static final String DRIBLE_LOGIN_URL = DRIBLE_AUTH_BASE + "?" +
                            "client_id=" + DRIBLE_CLIENT_ID +
                            "&redirect_uri=" + DRIBLE_CALL_BACK +
                            "&scope=" + "public write comment upload" +
                            "&state=" + mState;

    public static final String REQUEST_HEAD_AUTH_FIELD = "Authorization";
    public static final String REQUEST_HEAD_BEAR = " Bearer ";

    public static final String REQUEST_USER_URL = "https://api.dribbble.com/v1/users/";
    public static final String REQUEST_MY_INFO = "https://api.dribbble.com/v1/user";

    public static final String REQUEST_ONE_SHOT_URL = "https://api.dribbble.com/v1/shots/";

    public static final String INTENT_USER_ID = "com.tuesda.watch.intent.user.id";
    public static final String INTENT_SHOT_ID = "com.tuesda.watch.intent.shot.id";

    public static final String DRIBLE_DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DRIBLE_DATE_FORMAT_PATTERN);


    /**
     * Following is about shots set
     */
    public static final String REQUEST_SHOTS_FIELD_LIST = "list",
                                REQUEST_LIST_ANIMATED = "animated",
                                REQEUST_LIST_ATTACHMENTS = "attachments",
                                REQUEST_LIST_DEBUTS = "debuts",
                                REQUEST_LIST_PLAYOFFS = "playoffs",
                                REQUEST_LIST_REBOUNDS = "rebounds",
                                REQUEST_LIST_TEAMS = "teams";
    public static final String REQUEST_SHOTS_FIELD_TIMEFRAME = "timeframe",
                                REQUEST_TIMEFRAME_WEEK = "week",
                                REQUEST_TIMEFRAME_MONTH = "month",
                                REQUEST_TIMEFRAME_YEAR = "year",
                                REQUEST_TIMEFRAME_EVER = "ever";

    // Limit the timeframe to a specific date, week, month, or year. Must be in the format of YYYY-MM-DD.
    public static final String REQUEST_SHOTS_FIELD_DATE = "date";

    public static final String REQUEST_SHOTS_FIELD_SORT = "sort",
                                REQUEST_SORT_COMMENTS = "comments",
                                REQUEST_SORT_RECENT = "recent",
                                REQUEST_SORT_VIEWS = "views";


    /**
     * Response header info
     */
    public static final String RESPONSE_HEADER_LINK = "Link";


    public static final String CHECK_IF_ME_FOLLOW_URL = "https://api.dribbble.com/v1/user/following/";






    public static final String FOLLOWER_URL_FLAG = "followers";
    public static final String FOLLOWING_URL_FLAG = "following";

    public static final String FOLLOWER_JSON_FLAG = "follower";
    public static final String FOLLOWING_JSON_FLAG = "followee";

    public static final String REQUEST_BUCKETS_URL = "https://api.dribbble.com/v1/buckets/";

    public static final String DRIBLE_SEARCH_URL = "https://dribbble.com/search/";


}
