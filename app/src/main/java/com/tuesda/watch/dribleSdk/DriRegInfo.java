package com.tuesda.watch.dribleSdk;

import java.util.Random;

/**
 * Created by zhanglei on 15/7/23.
 */
public class DriRegInfo {

    public static final String DRIBLE_TOKEN_FIELD = "com.tuesda.watch.access.taken";

    public static final String DRIBLE_CODE_FIELD = "com.tuesda.watch.dribbble.code";

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


}
