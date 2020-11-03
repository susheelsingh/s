package com.qrms.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.qrms.Common.ShareData;
import com.qrms.Common.Urls;
import com.qrms.Communication.InputStreamVolleyRequest;
import com.qrms.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;


public class InternalStaffListingFragment extends Fragment implements View.OnClickListener {
    View mView;
    private ProgressDialog dialogProgress = null;
    private String token=null;
    private TableRow row;
    ShareData mSharedata;
    private int indexRow;
    private TableLayout display_Staff_layout;
    TextView text_VIntStaffPersonalDtlId,text_InternalStaff, text_IntFirstName, text_IntLastName,
            text_IntParentOtherName, text_IntMobile, text_IntEmailId,
            text_IntHomeAddress,text_IntCity,text_IntState,textPincode, text_IntAadharCard,text_IntPAN,text_IntBankAccount,text_IntIFSC,
    text_IntBranch,text_IntBankAddress,text_IntStatus,text_IntEdit,text_VisitorsExportToExcel;


    public InternalStaffListingFragment() {
        // Required empty public constructor
    }

    public static InternalStaffListingFragment newInstance(String param1, String param2) {
        InternalStaffListingFragment fragment = new InternalStaffListingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Internal Staff List");
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView=  inflater.inflate(R.layout.fragment_internal_staff_listing, container, false);
        initObjects();
        getVisitorListThread();
        // ::::::::::::::::::: OnclickListner::::::::::::::::

        text_VisitorsExportToExcel.setOnClickListener(this);
        return mView;
    }
    private void initObjects()
    {
        dialogProgress = new ProgressDialog(getActivity());
        text_VisitorsExportToExcel=(TextView)mView.findViewById(R.id.text_VisitorsExportToExcel);
        dialogProgress.setMessage("Loading...");
        dialogProgress.setCancelable(false);
        mSharedata=new ShareData(getActivity());
        token = mSharedata.getString("Auth_token_type", "");
        token = token.concat(" ").concat(mSharedata.getString("Auth_token", ""));
        display_Staff_layout=(TableLayout)mView.findViewById(R.id.display_Staff_layout);

    }
    private void getVisitorListThread() {
        dialogProgress.show();
        String url = Urls.InternalStaffList;
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                dialogProgress.dismiss();
                Log.e("InternalStaffResponse","InternalStaffResponse"+response);
                try {
                    indexRow = 1;
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject=response.getJSONObject(i);
                        row = new TableRow(getActivity());
                        row.setLayoutParams(lp);

                        text_VIntStaffPersonalDtlId = new TextView(getActivity());
                        text_VIntStaffPersonalDtlId.setText(jsonObject.getString("VIntStaffPersonalDtlId"));
                        text_VIntStaffPersonalDtlId.setPadding(20, 20, 20, 20);
                        row.setDividerDrawable(getResources().getDrawable(R.drawable.table_divider));
                        row.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                        row.addView(text_VIntStaffPersonalDtlId);

                        text_InternalStaff = new TextView(getActivity());
                        text_InternalStaff.setText(jsonObject.getString("InternalStaffId"));
                        text_InternalStaff.setGravity(Gravity.CENTER);
                        text_InternalStaff.setPadding(20, 20, 20, 20);
                        row.addView(text_InternalStaff);

                        text_IntFirstName = new TextView(getActivity());
                        text_IntFirstName.setText(jsonObject.getString("FirstName"));
                        text_IntFirstName.setGravity(Gravity.CENTER);
                        text_IntFirstName.setPadding(20, 20, 20, 20);
                        row.addView(text_IntFirstName);

                        text_IntLastName = new TextView(getActivity());
                        text_IntLastName.setText(jsonObject.getString("LastName"));
                        text_IntLastName.setGravity(Gravity.CENTER);
                        text_IntLastName.setPadding(20, 20, 20, 20);
                        row.addView(text_IntLastName);

                        text_IntParentOtherName = new TextView(getActivity());
                        text_IntParentOtherName.setText(jsonObject.getString("ParentOtherName"));
                        text_IntParentOtherName.setGravity(Gravity.CENTER);
                        text_IntParentOtherName.setPadding(20, 20, 20, 20);
                        row.addView(text_IntParentOtherName);

                        text_IntMobile = new TextView(getActivity());
                        text_IntMobile.setText(jsonObject.getString("MobileNo"));
                        text_IntMobile.setGravity(Gravity.CENTER);
                        text_IntMobile.setPadding(20, 20, 20, 20);
                        row.addView(text_IntMobile);

                        text_IntEmailId = new TextView(getActivity());
                        text_IntEmailId.setText(jsonObject.getString("Email"));
                        text_IntEmailId.setGravity(Gravity.CENTER);
                        text_IntEmailId.setPadding(20, 20, 20, 20);
                        row.addView(text_IntEmailId);

                        text_IntHomeAddress = new TextView(getActivity());
                        text_IntHomeAddress.setText(jsonObject.getString("PrimaryAddress"));
                        text_IntHomeAddress.setGravity(Gravity.CENTER);
                        text_IntHomeAddress.setPadding(20, 20, 20, 20);
                        row.addView(text_IntHomeAddress);

                        text_IntCity = new TextView(getActivity());
                        text_IntCity.setText(jsonObject.getString("CityName"));
                        text_IntCity.setGravity(Gravity.CENTER);
                        text_IntCity.setPadding(20, 20, 20, 20);
                        row.addView(text_IntCity);

                        text_IntState = new TextView(getActivity());
                        text_IntState.setText(jsonObject.getString("StateName"));
                        text_IntState.setGravity(Gravity.CENTER);
                        text_IntState.setPadding(20, 20, 20, 20);
                        row.addView(text_IntState);

                        textPincode = new TextView(getActivity());
                        textPincode.setText(jsonObject.getString("Pincode"));
                        textPincode.setGravity(Gravity.CENTER);
                        textPincode.setPadding(20, 20, 20, 20);
                        row.addView(textPincode);

                        text_IntAadharCard = new TextView(getActivity());
                        text_IntAadharCard.setText(jsonObject.getString("AadharCard"));
                        text_IntAadharCard.setGravity(Gravity.CENTER);
                        text_IntAadharCard.setPadding(20, 20, 20, 20);
                        row.addView(text_IntAadharCard);

                        text_IntPAN = new TextView(getActivity());
                        text_IntPAN.setText(jsonObject.getString("PanNo"));
                        text_IntPAN.setGravity(Gravity.CENTER);
                        text_IntPAN.setPadding(20, 20, 20, 20);
                        row.addView(text_IntPAN);


                        text_IntBankAccount = new TextView(getActivity());
                        text_IntBankAccount.setText(jsonObject.getString("BankAccountNo"));
                        text_IntBankAccount.setGravity(Gravity.CENTER);
                        text_IntBankAccount.setPadding(20, 20, 20, 20);
                        row.addView(text_IntBankAccount);

                        text_IntIFSC = new TextView(getActivity());
                        text_IntIFSC.setText(jsonObject.getString("IFSCCode"));
                        text_IntIFSC.setGravity(Gravity.CENTER);
                        text_IntIFSC.setPadding(20, 20, 20, 20);
                        row.addView(text_IntIFSC);

                        text_IntBranch = new TextView(getActivity());
                        text_IntBranch.setText(jsonObject.getString("BranchName"));
                        text_IntBranch.setGravity(Gravity.CENTER);
                        text_IntBranch.setPadding(20, 20, 20, 20);
                        row.addView(text_IntBranch);

                        text_IntBankAddress = new TextView(getActivity());
                        text_IntBankAddress.setText(jsonObject.getString("BranchAddress"));
                        text_IntBankAddress.setGravity(Gravity.CENTER);
                        text_IntBankAddress.setPadding(20, 20, 20, 20);
                        row.addView(text_IntBankAddress);

                        text_IntStatus = new TextView(getActivity());
                        text_IntStatus.setText(jsonObject.getString("IsActive"));
                        text_IntStatus.setGravity(Gravity.CENTER);
                        text_IntStatus.setPadding(20, 20, 20, 20);
                        row.addView(text_IntStatus);

                        text_IntEdit = new TextView(getActivity());
                        text_IntEdit.setText("Edit");
                        text_IntEdit.setGravity(Gravity.CENTER);
                        text_IntEdit.setPadding(20, 20, 20, 20);
                        row.addView(text_IntEdit);
                        display_Staff_layout.addView(row);

                    }

                }catch (Exception e)
                {
                    e.getMessage();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("InternalStaffResponse","InternalStaffResponse"+error);
                dialogProgress.dismiss();
                if (error instanceof TimeoutError) {
                    Toast.makeText(getActivity(), "Session Time out, Please SignUp Again...", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getActivity(), "Server Authorization Failed", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getActivity(), "Server Error, please try after some time later", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getActivity(), "Network not Available", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getActivity(), "JSONArray Problem", Toast.LENGTH_LONG).show();

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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_VisitorsExportToExcel:
                break;
        }
    }
    /*private void getFile(){
        //Change your url below
        String mUrl="http://yoururl.com";
        InputStreamVolleyRequest inputStreamVolleyRequest = new InputStreamVolleyRequest(Request.Method.GET, mUrl, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {

            }
        });
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity(),
                new HurlStack());
        mRequestQueue.add(inputStreamVolleyRequest);
    }*/
}