package com.app.yellodriver.util;

import android.util.Base64;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JWTUtils {

    //1000(millliseconds in a second)*60(seconds in a minute)*5(number of minutes)=300000
    private static final long GAPTIME = 300000; //5 minutes
    String hasuraUserId;
    String hasuraUserName;
    String hasuraUserRole;
    String hasuraUserEmail;

    public String getHasuraUserId() {
        return hasuraUserId;
    }

    public String getHasuraUserName() { return hasuraUserName;    }

    public String getHasuraUserRole() {
        return hasuraUserRole;
    }

    public String getHasuraUserEmail() {
        return hasuraUserEmail;
    }

    public String getHasuraUserPhone() {
        return hasuraUserPhone;
    }

    String hasuraUserPhone;

    public void decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            YLog.d("JWT_DECODED", "Header: " + getJson(split[0]));

            JSONObject bodyJson = new JSONObject(getJson(split[1]));
            YLog.d("JWT_DECODED", "Body: " + bodyJson);
            JSONObject claimObject = bodyJson.getJSONObject("https://hasura.io/jwt/claims");
            hasuraUserId = claimObject.getString("x-hasura-user-id");
            hasuraUserRole = claimObject.getString("x-hasura-default-role");
            hasuraUserEmail = bodyJson.getString("email");
            hasuraUserPhone = bodyJson.getString("phone_number");
            hasuraUserName = bodyJson.getString("name");

            long expireTime = bodyJson.getLong("exp");

            YLog.e("Token Expire time:",DateUtils.getDateFromUTCTimestamp(expireTime,"dd-MM-yyyy - hh:mm a"));

            long curTime = System.currentTimeMillis();
            long tokenExpTime = expireTime*1000;

            if(tokenExpTime >(curTime - GAPTIME)){
                YLog.e("Token is", "valid");
            }else {
                YLog.e("Token is", "expired/requires refresh");
            }


        } catch (UnsupportedEncodingException e) {
            //Error
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}