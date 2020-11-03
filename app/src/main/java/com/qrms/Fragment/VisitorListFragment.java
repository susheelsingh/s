package com.qrms.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.qrms.Common.ShareData;
import com.qrms.Common.Urls;
import com.qrms.R;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VisitorListFragment extends Fragment implements View.OnClickListener {
    View mView;
    private ProgressDialog dialogProgress = null;
    public final int WRITE_PERMISSON_REQUEST_CODE = 1;
    private String token=null, csv=null ;
    private TableRow row;
    private int indexRow;
    ShareData mSharedata;
    private TableLayout display_Vistor_layout;
      TextView text_VisitorsId,text_VisitorName, text_VisitorsMobile, text_VisitorsEmailId,
    text_VisitorsHomeAddress, text_VisitorsOfficeAddress, text_VisitorsVisitorPurpose,
    text_VisitorsVisitingDate,text_VisitorsStatus,text_VisitorsIdentityType, text_VisitorsEdit,
              text_VisitorsExportToExcel;


    public VisitorListFragment() {
        // Required empty public constructor
    }

    public static VisitorListFragment newInstance(String param1, String param2) {
        VisitorListFragment fragment = new VisitorListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Visitor List");
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_visitor_list, container, false);
        initObjects();
        // ::::::::::::::::: Onclicck Listner::::::::::::
        text_VisitorsExportToExcel.setOnClickListener(this);

        getVisitorListThread();

        return  mView;
    }


    private void initObjects()
    {
        dialogProgress = new ProgressDialog(getActivity());
        // csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyCsvFile.csv"); // Here csv file name is MyCsvFile.csv
        dialogProgress.setMessage("Loading...");
        dialogProgress.setCancelable(false);
        mSharedata=new ShareData(getActivity());
        token = mSharedata.getString("Auth_token_type", "");
        token = token.concat(" ").concat(mSharedata.getString("Auth_token", ""));
        display_Vistor_layout=(TableLayout)mView.findViewById(R.id.display_Vistor_layout);
        text_VisitorsExportToExcel=(TextView)mView.findViewById(R.id.text_VisitorsExportToExcel);

    }
    private void getVisitorListThread() {
        dialogProgress.show();
        String url = Urls.GetVisitorList;
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                dialogProgress.dismiss();
                Log.e("VisitorResponse","VisitorResponse"+response);
                try {
                    indexRow = 1;
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject=response.getJSONObject(i);
                        row = new TableRow(getActivity());
                        row.setLayoutParams(lp);
                        text_VisitorsId = new TextView(getActivity());
                        text_VisitorsId.setText(jsonObject.getString("VisitorId"));
                        text_VisitorsId.setPadding(20, 20, 20, 20);
                        row.setDividerDrawable(getResources().getDrawable(R.drawable.table_divider));
                        row.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                        row.addView(text_VisitorsId);

                        text_VisitorName = new TextView(getActivity());
                        text_VisitorName.setText(jsonObject.getString("VisitorName"));
                        text_VisitorName.setGravity(Gravity.CENTER);
                        text_VisitorName.setPadding(20, 20, 20, 20);
                        row.addView(text_VisitorName);

                        text_VisitorsMobile = new TextView(getActivity());
                        text_VisitorsMobile.setText(jsonObject.getString("MobileNo"));
                        text_VisitorsMobile.setGravity(Gravity.CENTER);
                        text_VisitorsMobile.setPadding(20, 20, 20, 20);
                        row.addView(text_VisitorsMobile);

                        text_VisitorsEmailId = new TextView(getActivity());
                        text_VisitorsEmailId.setText(jsonObject.getString("EmailId"));
                        text_VisitorsEmailId.setGravity(Gravity.CENTER);
                        text_VisitorsEmailId.setPadding(20, 20, 20, 20);
                        row.addView(text_VisitorsEmailId);

                        text_VisitorsHomeAddress = new TextView(getActivity());
                        text_VisitorsHomeAddress.setText(jsonObject.getString("HomeAddress"));
                        text_VisitorsHomeAddress.setGravity(Gravity.CENTER);
                        text_VisitorsHomeAddress.setPadding(20, 20, 20, 20);
                        row.addView(  text_VisitorsHomeAddress);

                        text_VisitorsOfficeAddress = new TextView(getActivity());
                        text_VisitorsOfficeAddress.setText(jsonObject.getString("OfficeAddress"));
                        text_VisitorsOfficeAddress.setGravity(Gravity.CENTER);
                        text_VisitorsOfficeAddress.setPadding(20, 20, 20, 20);
                        row.addView(text_VisitorsOfficeAddress);

                        text_VisitorsVisitorPurpose = new TextView(getActivity());
                        text_VisitorsVisitorPurpose.setText(jsonObject.getString("VisitorPurpose"));
                        text_VisitorsVisitorPurpose.setGravity(Gravity.CENTER);
                        text_VisitorsVisitorPurpose.setPadding(20, 20, 20, 20);
                        row.addView(text_VisitorsVisitorPurpose);

                        text_VisitorsVisitingDate = new TextView(getActivity());
                        text_VisitorsVisitingDate.setText(jsonObject.getString("VisitorDate"));
                        text_VisitorsVisitingDate.setGravity(Gravity.CENTER);
                        text_VisitorsVisitingDate.setPadding(20, 20, 20, 20);
                        row.addView(text_VisitorsVisitingDate);

                        text_VisitorsStatus = new TextView(getActivity());
                        text_VisitorsStatus.setText(jsonObject.getString("IsActive"));
                        text_VisitorsStatus.setGravity(Gravity.CENTER);
                        text_VisitorsStatus.setPadding(20, 20, 20, 20);
                        row.addView(text_VisitorsStatus);

                        text_VisitorsIdentityType = new TextView(getActivity());
                        text_VisitorsIdentityType.setText(jsonObject.getString("IdentityType"));
                        text_VisitorsIdentityType.setGravity(Gravity.CENTER);
                        text_VisitorsIdentityType.setPadding(20, 20, 20, 20);
                        row.addView(text_VisitorsIdentityType);


                        text_VisitorsEdit = new TextView(getActivity());
                        text_VisitorsEdit.setText("Edit");
                        text_VisitorsEdit.setGravity(Gravity.CENTER);
                        text_VisitorsEdit.setPadding(20, 20, 20, 20);
                        row.addView(text_VisitorsEdit);
                        display_Vistor_layout.addView(row);

                    }

                }catch (Exception e)
                {
                    e.getMessage();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VisitorResponse","VisitorResponse"+error);
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
        switch (v.getId()) {
            case R.id.text_VisitorsExportToExcel:
                break;
        }
    }

}