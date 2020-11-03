package com.qrms.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.qrms.Common.ShareData;
import com.qrms.Common.Urls;
import com.qrms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class VisitorDashboardFragment extends Fragment {
    View mView;
    TextView  text_date, text_date0, text_Date2, text_DefvisitorCount, text_date4,text_DefaultVisitor,text_DeliveryVisitor,
    text_InternalVisitor ;
    Button button_search;
    private ProgressDialog dialogProgress = null;
    private String token = null, mVistorCount0, mVisitorDate0, mVistorCount1, mVisitorDate1, mVistorCount2, mVisitorDate2,
            mVistorCount3, mVisitorDate3, mVistorCount4, mVisitorDate4, mVistorCount5, mVisitorDate5, mVistorCount6, mVisitorDate6;
    ShareData mSharedata;
    String mVisitorTypeDate, mVistorTypeCount, previousDate, previousCurrentDate;
    private Calendar calendar;
    BarChart Visitor_barChart, TypeVisitor_barChart;
    private int current_date, current_month, current_year;

    public VisitorDashboardFragment() {
        // Required empty public constructor
    }

    public static VisitorDashboardFragment newInstance(String param1, String param2) {
        VisitorDashboardFragment fragment = new VisitorDashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Visitor DashBoard");
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_visitor_dashboard, container, false);
        initObjects();
        // :::::::::::::::::::::::::  OnclicListner:::::::::::::::::
        return mView;
    }

    private void initObjects() {
        text_DefaultVisitor=(TextView)mView.findViewById(R.id.text_DefaultVisitor);
        text_DeliveryVisitor=(TextView)mView.findViewById(R.id.text_DeliveryVisitor);
        text_InternalVisitor=(TextView)mView.findViewById(R.id.text_InternalVisitor);
        Visitor_barChart = (BarChart) mView.findViewById(R.id.Visitor_barChart);
        TypeVisitor_barChart = (BarChart) mView.findViewById(R.id.TypeVisitor_barChart);
        text_date = (TextView) mView.findViewById(R.id.text_date);
        text_date0 = (TextView) mView.findViewById(R.id.text_date0);
        text_Date2 = (TextView) mView.findViewById(R.id.text_Date2);
        text_DefvisitorCount = (TextView) mView.findViewById(R.id.text_DefvisitorCount);
        text_date4 = (TextView) mView.findViewById(R.id.text_date4);
        dialogProgress = new ProgressDialog(getActivity());
        dialogProgress.setMessage("Loading...");
        dialogProgress.setCancelable(false);
        mSharedata = new ShareData(getActivity());
        token = mSharedata.getString("Auth_token_type", "");
        token = token.concat(" ").concat(mSharedata.getString("Auth_token", ""));

        calendar = Calendar.getInstance();
        current_date = calendar.get(Calendar.DAY_OF_MONTH);
        current_month = calendar.get(Calendar.MONTH);
        current_year = calendar.get(Calendar.YEAR);

        visitorGraphThread();
        visitorGraphTypeThread();
    }

    private void visitorGraphThread() {
        String url = Urls.VisitorGraph;

        previousDate=(current_year + "-" + (current_month - 1) + "-" + current_date);
        previousCurrentDate=(current_year + "-" + (current_month + 1) + "-" + current_date);

        JSONObject jsonObject = new JSONObject();
         JSONArray array=new JSONArray();
        try {

            jsonObject.put("StartDate", previousDate);
            jsonObject.put("EndDate", previousCurrentDate);
            array.put(jsonObject);
        }catch (JSONException e) {
        }

            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, url,array, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    dialogProgress.dismiss();
                  //  Log.e("VisitorResponse","VisitorResponse"+response);
                    if(response.length()>0)
                    {
                        callVisitorGraph(response);
                    }else{
                        Toast.makeText(getActivity(),"Currently No Visitor",Toast.LENGTH_SHORT).show();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialogProgress.dismiss();
                    if (error instanceof TimeoutError) {
                        Toast.makeText(getActivity(), "Session Time out, Please Login Again...", Toast.LENGTH_LONG).show();
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


    private void callVisitorGraph(JSONArray obj) {
        try {

            JSONObject jsonObject0 = obj.getJSONObject(0);
            text_date.setText(jsonObject0.getString("VisitorDate"));
            mVistorCount0 = jsonObject0.getString("VisitorCount");
            JSONObject jsonObject1 = obj.getJSONObject(1);
            text_date0.setText(jsonObject1.getString("VisitorDate"));
            mVistorCount1 = jsonObject1.getString("VisitorCount");
            text_Date2=(TextView)mView.findViewById(R.id.text_Date2);
            text_DefvisitorCount=(TextView)mView.findViewById(R.id.text_DefvisitorCount);
            text_date4=(TextView)mView.findViewById(R.id.text_date4);

            JSONObject jsonObject2 = obj.getJSONObject(2);
            text_Date2 .setText(jsonObject2.getString("VisitorDate"));
            mVistorCount2 = jsonObject2.getString("VisitorCount");

            JSONObject jsonObject3 = obj.getJSONObject(3);
            text_DefvisitorCount.setText(jsonObject3.getString("VisitorDate"));
            mVistorCount3 = jsonObject3.getString("VisitorCount");

            JSONObject jsonObject4 = obj.getJSONObject(4);
            text_date4.setText(jsonObject4.getString("VisitorDate"));
            mVistorCount4 = jsonObject4.getString("VisitorCount");
            getEntriesVisitor(mVistorCount0,mVistorCount1,mVistorCount2,mVistorCount3,mVistorCount4);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getEntriesVisitor(String mVistorCount0,String mVistorCount1,String mVistorCount2,String mVistorCount3 ,String mVistorCount4) {
       try {
           ArrayList<BarEntry> entries = new ArrayList<>();
           entries.add(new BarEntry(Integer.valueOf(mVistorCount0), 0));
           entries.add(new BarEntry(Integer.valueOf(mVistorCount1), 1));
           entries.add(new BarEntry(Integer.valueOf(mVistorCount2), 2));
           entries.add(new BarEntry(Integer.valueOf(mVistorCount3), 3));
           entries.add(new BarEntry(Integer.valueOf(mVistorCount4), 4));
           BarDataSet bardataset = new BarDataSet(entries, "Visitor");
           ArrayList<String> labels = new ArrayList<String>();
           labels.add("");
           labels.add("");
           labels.add("");
           labels.add("");
           labels.add("");
           BarData data = new BarData(labels, bardataset);
           Visitor_barChart.setData(data); // set the data and list of labels into chart
           bardataset.setColors(new int[] { R.color.yellow, R.color.increase, R.color.very_high, R.color.red,R.color.background}, getContext());
           Visitor_barChart.getLegend().setEnabled(false);
           Visitor_barChart.setDescription("Visitor");
           YAxis rightYAxis = Visitor_barChart.getAxisRight();
           rightYAxis.setDrawLabels(false);
           rightYAxis.setDrawGridLines(false);
           Visitor_barChart.animateY(5000);
       }catch (Exception e)
       {
           e.printStackTrace();
       }
    }

    private void visitorGraphTypeThread() {
        String url = Urls.GetVisitorTypeGraph;
      //  previousDate=(current_date-18 + "-" + (current_month + 1) + "-" + current_year);
      //  previousCurrentDate=(current_date + "-" + (current_month + 1) + "-" + current_year);
       // int t=current_date-18;
        previousDate=(current_year + "-" + (current_month - 1) + "-" + current_date);
        previousCurrentDate=(current_year + "-" + (current_month + 1) + "-" + current_date);



        JSONArray array=new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("StartDate", previousDate);
            jsonObject.put("EndDate", previousCurrentDate);
            array.put(jsonObject);
        }catch (JSONException e) {
            e.printStackTrace();
        }
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, url,array, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    dialogProgress.dismiss();
                   // Log.e("VisitorTypeResponse","VisitorTypeResponse"+response);
                    if(response.length()>0)
                    {
                        callVisitorTypeGraph1(response);
                    }else{
                        Toast.makeText(getActivity(),"Currently No Visitor",Toast.LENGTH_SHORT).show();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialogProgress.dismiss();
                    if (error instanceof TimeoutError) {
                        Toast.makeText(getActivity(), "Session Time out, Please Login Again...", Toast.LENGTH_LONG).show();
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

    private void callVisitorTypeGraph1(JSONArray obj) {
        try {
            JSONObject jsonObject0 = obj.getJSONObject(0);
            text_DefaultVisitor.setText(jsonObject0.getString("VisitorType"));
            String mVistorTypeCount0  = jsonObject0.getString("VisitorTypeCount");

            JSONObject jsonObject1 = obj.getJSONObject(1);
            text_DeliveryVisitor.setText(jsonObject1.getString("VisitorType"));
            String  mVistorTypeCount1  = jsonObject1.getString("VisitorTypeCount");

            JSONObject jsonObject2 = obj.getJSONObject(2);
            text_InternalVisitor.setText(jsonObject2.getString("VisitorType"));
            String mVistorTypeCount2  = jsonObject2.getString("VisitorTypeCount");
            getVisitorTypeGraph1(mVistorTypeCount0,mVistorTypeCount1,mVistorTypeCount2);



        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void getVisitorTypeGraph1(String mVistorTypeCount0, String mVistorTypeCount1,String mVistorTypeCount2) {
        try {
            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(Integer.valueOf(mVistorTypeCount0), 0));
            entries.add(new BarEntry(Integer.valueOf(mVistorTypeCount1), 1));
            entries.add(new BarEntry(Integer.valueOf(mVistorTypeCount2), 2));
            BarDataSet bardataset = new BarDataSet(entries, "Visitor Type");
            ArrayList<String> labels = new ArrayList<String>();
            labels.add("");
            labels.add("");
            labels.add("");
            BarData data = new BarData(labels, bardataset);
            TypeVisitor_barChart .setData(data); // set the data and list of labels into chart
            bardataset.setColors(new int[] { R.color.red, R.color.very_high, R.color.background}, getContext());
            TypeVisitor_barChart.getLegend().setEnabled(false);
            TypeVisitor_barChart.setDescription("Visitor Type");
            YAxis rightYAxis = TypeVisitor_barChart.getAxisRight();
            rightYAxis.setDrawLabels(false);
            rightYAxis.setDrawGridLines(false);
            TypeVisitor_barChart.animateY(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}