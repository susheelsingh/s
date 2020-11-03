package com.qrms.Communication;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.qrms.Common.ShareData;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

 public class InputStreamVolleyRequest extends Request<byte[]> {
     private final Response.Listener<byte[]> mListener;
     private Map<String, String> mParams;
     //create a static map for directly accessing headers
     public Map<String, String> responseHeaders;
     private String token=null;
     ShareData mSharedata;


     public InputStreamVolleyRequest(int post, String mUrl, Response.Listener<byte[]> listener,
                                     Response.ErrorListener errorListener, HashMap<String, String> params) {
         // TODO Auto-generated constructor stub

         super(post, mUrl, errorListener);
         // this request would never use cache.
         setShouldCache(false);
         mListener = listener;
         mParams = params;

        // mSharedata=new ShareData(this);
         token = mSharedata.getString("Auth_token_type", "");
         token = token.concat(" ").concat(mSharedata.getString("Auth_token", ""));
     }

     @Override
     public Map<String, String> getHeaders() throws AuthFailureError {
         final Map<String, String> headers = new HashMap<>();
         headers.put("Authorization",token);
         return headers;
     };

     @Override
     protected void deliverResponse(byte[] response) {
         mListener.onResponse(response);
     }

     @Override
     protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

         //Initialise local responseHeaders map with response headers received
         responseHeaders = response.headers;

         //Pass the response data here
         return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
     }
 }
