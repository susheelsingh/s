package com.qrms.Threads;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.qrms.Common.ShareData;
import com.qrms.Common.Urls;
import com.qrms.Communication.Http;
import com.qrms.Listner.OnGetNewTokenListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GetAuthToken extends AsyncTask<Void, Void, Integer> {
    private String jsonString;
    private int mSuccess;
    private String mAccessToken, mExpires, mTokenType;
      private String mainToken= "eniUbM7JHZu2i4VLk8MFBfwUTLqwk1_xlofhoNwm8soe23UQQWt5ZXI3WFFcZU2wYKL8L2HcPvFirWArgr586OWsn-fO-XSkdFrvl2dq-rbzGL0L79q2dsttuJPxnpL7AtEWTP2kJnNQXz7ZokZGMkY-RRlqCrqSFIDzWM3sU7HduNHbBxz6xEswz7cakB-d5qW4FRF5_o7kwG4nDSCPWUZVKkhR4VNklC66jqleX440StsBflrfdrm-iRAfsuQhKk5b5_YKZBPXyfblYAWGXZT7tD4X04Q97tGbcb6sKkdwN8JdQ28IRTGvCGDufujL";
    ShareData mSharedData;
    private OnGetNewTokenListener onGetNewTokenListener;

    public GetAuthToken(Context context){
        mSharedData = new ShareData(context);
        try{
            onGetNewTokenListener = (OnGetNewTokenListener) context;
        } catch (ClassCastException e){
            onGetNewTokenListener = null;
        }

    }
    private HashMap<String, String> getAuthMap(){
        HashMap<String, String> map = new HashMap<>();
        map.put("grant_type","password");
        map.put("username","test5@qrms.in");
        map.put("Password","dev@123");
        return map;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        Http http = new Http();
        jsonString = http.getAuthToken( Urls.Token,getAuthMap(),mainToken);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Log.e("Token Response is = ",jsonString);
            mSharedData.putString("Auth_token",jsonObject.getString("access_token"));
            mSharedData.putString("Auth_token_expires",jsonObject.getString("expires_in"));
            mSharedData.putString("Auth_token_type",jsonObject.getString("token_type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (onGetNewTokenListener != null)
            onGetNewTokenListener.onGetNewToken();
    }
}
