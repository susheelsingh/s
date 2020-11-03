package com.qrms.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qrms.Common.ShareData;
import com.qrms.Common.Urls;
import com.qrms.Pozo.User;
import com.qrms.R;
import com.qrms.Threads.GetAuthToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText_Login,editText_Pass;
    ShareData mSharedData;
    Button button_sign;
    TextView text_FrogetPass,textSignup;
    ProgressDialog dialogProgress = null;
    boolean mStatus;
    private String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initObjects();
        if (!mSharedData.containsKey("Auth_token")) {
            new GetAuthToken(this).execute();
        }
        token = mSharedData.getString("Auth_token_type", "");
        token = token.concat(" ").concat(mSharedData.getString("Auth_token", ""));

        text_FrogetPass.setPaintFlags(text_FrogetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
         textSignup.setPaintFlags(textSignup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // ::::::::::::::::::  OnSetClickListNer  :::::::::::::::::::::::::
        button_sign.setOnClickListener(this);
        text_FrogetPass.setOnClickListener(this);

        // textSignup.setOnClickListener(this);
    }

    public void cancel(View view){
        finish();
    }
    private void  initObjects()
    {
        editText_Login=(EditText)findViewById(R.id.editText_Login);
        editText_Pass=(EditText)findViewById(R.id.editText_Pass);
        text_FrogetPass=(TextView) findViewById(R.id.text_FrogetPass);
         textSignup=(TextView) findViewById(R.id.textSignup);
        button_sign=(Button) findViewById(R.id.button_sign);
       // databaseHelper = new DatabaseHelper(LoginActivity.this);
        mSharedData=new ShareData(LoginActivity.this);


        dialogProgress = new ProgressDialog(LoginActivity.this);
        dialogProgress.setMessage("Please Wait...");
        dialogProgress.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button_sign:
                if (editText_Login.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                    Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                    editText_Login.startAnimation(shake);
                } else if (editText_Pass.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                    editText_Pass.startAnimation(shake);
                } else {

                    dialogProgress.show();
                    checkLoginThread();
                }

                break;

            case R.id.text_FrogetPass:
                Intent ForgetPassIntent = new Intent(LoginActivity.this,FogetPassActivity.class);
                startActivity(ForgetPassIntent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;

        }

    }

    private void checkLoginThread() {
        String url =Urls.Login;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username",editText_Login.getText().toString());
            jsonObject.put("Password",editText_Pass.getText().toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialogProgress.dismiss();
                    Log.e("loginResponse","loginResponse"+response);
                    try {
                        String str=response.getString("User");
                            JSONObject jsonObject1=new JSONObject(str);
                            mSharedData.putBoolean("is_user_logged_in", true);
                            mSharedData.putString("logged_UserName",jsonObject1.getString("UserName"));
                        mSharedData.putString("logged_userId",jsonObject1.getString("UserId"));
                        mSharedData.putString("LoginTypeId",jsonObject1.getString("LoginTypeId"));
                            mSharedData.putString("UserPhotoPath",jsonObject1.getString("UserPhotoPath"));
                            mSharedData.putString("CompanyLogoName",jsonObject1.getString("CompanyLogoName"));
                            mSharedData.putString("CompanyId",jsonObject1.getString("CompanyId"));
                            mSharedData.putString("Email",jsonObject1.getString("Email"));
                            mSharedData.putString("Contact",jsonObject1.getString("Contact"));
                        Intent ForgetPassIntent = new Intent(LoginActivity.this,DashboardActivity.class);
                            startActivity(ForgetPassIntent);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            Toast.makeText(LoginActivity.this,"Login Suscessfully !",Toast.LENGTH_SHORT).show();

                    }catch (Exception e)
                    {
                        e.getMessage();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("jsonResponse","JsonResponse"+error);
                    dialogProgress.dismiss();
                    if (error instanceof TimeoutError) {
                        Toast.makeText(getApplicationContext(), "Session Time out, Please SignUp Again...", Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getApplicationContext(), "Server Authorization Failed", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(getApplicationContext(), "Server Error, please try after some time later", Toast.LENGTH_LONG).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(getApplicationContext(), "Network not Available", Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(getApplicationContext(), "JSONArray Problem", Toast.LENGTH_LONG).show();

                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization",token);
                    return headers;
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}