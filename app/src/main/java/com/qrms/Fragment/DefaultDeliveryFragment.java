package com.qrms.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.android.volley.toolbox.Volley;
import com.qrms.Common.ShareData;
import com.qrms.Common.Urls;
import com.qrms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DefaultDeliveryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    View mView;
    Spinner spinner_visitorTypeUser,spinner_searchToVisit;
    EditText editVistorname,edit_mobilenumber,edt_Company2,edt_VisitingPurpose;
    TextView edit_Vistordate,edit_Time1;
    Button button_submit,button_Select;
    ShareData mSharedata;
    private String[] mFlatOwnerNameArray;
    private ArrayAdapter<String>FlatSpinerAdapter;
    private int current_date, current_month, current_year,mHour,mMinute;
    String[] inputArrayUser = {"--Select Visitor Type--","Default Visitor","Internal Staff","Delivery","Maid/Servent"};
    private String token=null,VisitorInputType, getFlatOwnerListThread,mFlateOwnerName;
    ProgressDialog dialogProgress = null;
    private Calendar calendar;


    public DefaultDeliveryFragment() {
        // Required empty public constructor
    }

    public static DefaultDeliveryFragment newInstance(String param1, String param2) {
        DefaultDeliveryFragment fragment = new DefaultDeliveryFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_default_delivery, container, false);
        initObjects();
        getFlatOwnerListThread();
        // ::::::::::::::::::: OnclickListner :::::::::::::::::::::::
        button_submit.setOnClickListener(this);
        edit_Vistordate.setOnClickListener(this);
        edit_Time1.setOnClickListener(this);
        button_Select.setOnClickListener(this);
        return mView;
    }

    private void initObjects(){
        spinner_visitorTypeUser=(Spinner)mView.findViewById(R.id.spinner_visitorTypeUser);
        spinner_searchToVisit=(Spinner)mView.findViewById(R.id.spinner_searchToVisit);
        editVistorname=(EditText)mView.findViewById(R.id.editVistorname);
        edit_mobilenumber=(EditText)mView.findViewById(R.id.edit_mobilenumber);
        edt_Company2=(EditText)mView.findViewById(R.id.edt_Company2);
        edt_VisitingPurpose=(EditText)mView.findViewById(R.id.edt_VisitingPurpose);
        edit_Vistordate=(TextView)mView.findViewById(R.id.edit_Vistordate);
        edit_Time1=(TextView)mView.findViewById(R.id.edit_Time1);
        button_submit=(Button) mView.findViewById(R.id.button_submit);
        button_Select=(Button)mView.findViewById(R.id.button_Select);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, inputArrayUser);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_visitorTypeUser.setAdapter(adapter);

        mSharedata=new ShareData(getActivity());
        token = mSharedata.getString("Auth_token_type", "");
        token = token.concat(" ").concat(mSharedata.getString("Auth_token", ""));
        dialogProgress = new ProgressDialog(getActivity());
        dialogProgress.setMessage("Please Wait...");
        dialogProgress.setCancelable(false);
        calendar = Calendar.getInstance();
        current_date = calendar.get(Calendar.DAY_OF_MONTH);
        current_month = calendar.get(Calendar.MONTH);
        current_year = calendar.get(Calendar.YEAR);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_submit:
                if(spinner_visitorTypeUser.getSelectedItem().toString().trim().equals("--Select Visitor Type--"))
                {
                    Toast.makeText(getActivity(), "Select Visitor is required.", Toast.LENGTH_SHORT).show();
                }
                else if (editVistorname.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Visitor name is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_mobilenumber.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Mobile Number is required.", Toast.LENGTH_SHORT).show();
                } else if(edit_mobilenumber.getText().toString().trim().length()<10)
                {
                    Toast.makeText(getActivity(), "Enter Valid MobileNo.", Toast.LENGTH_SHORT).show();
                } else if (edit_Vistordate.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "This date is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_Time1.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Visitor time is required.", Toast.LENGTH_SHORT).show();
                }
                 else if (edt_Company2.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Company is required.", Toast.LENGTH_SHORT).show();
                }  else if (edt_VisitingPurpose.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Visitor Purpose is required.", Toast.LENGTH_SHORT).show();
                }
                else{
                    dialogProgress.show();
                    insertDeliveryDeafultThread();
                }
                break;
            case R.id.edit_Vistordate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        edit_Vistordate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, current_year, current_month, current_date);
                datePickerDialog.show();
                break;

            case R.id.edit_Time1:
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        edit_Time1.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.button_Select:
                Fragment fragment = null;
                if(spinner_visitorTypeUser.getSelectedItem().toString().trim().equals("--Select Visitor Type--"))
                {
                    Toast.makeText(getActivity(), "Select Visitor is required.", Toast.LENGTH_SHORT).show();

                }else{
                    if(spinner_visitorTypeUser.getSelectedItem().toString().trim().equals("Default Visitor")){
                        fragment=new VisitorMangementFragment();
                        replaceFragment(fragment);

                    }else if(spinner_visitorTypeUser.getSelectedItem().toString().trim().equals("Internal Staff")){
                        fragment=new DefaultInternalStaffFragment();
                        replaceFragment(fragment);

                    }else if(spinner_visitorTypeUser.getSelectedItem().toString().trim().equals("Delivery")){
                        fragment=new DefaultDeliveryFragment();
                        replaceFragment(fragment);

                    }else{
                        fragment=new DefaultMaidServentVisitorFragment();
                        replaceFragment(fragment);
                    }
                }

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.spinner_visitorTypeUser:
                VisitorInputType=  inputArrayUser[position];
                break;
            case R.id.spinner_searchToVisit:
                mFlateOwnerName=mFlatOwnerNameArray[position];
                break;
        }

    }
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void getFlatOwnerListThread() {
        dialogProgress.show();
        String url ="http://apis.qrms.in/api/GetFlatOwnerList";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                dialogProgress.dismiss();
              //  Log.e("flatOwnerList","flatOwnerList"+response);
                try {
                    mFlatOwnerNameArray = new String[response.length()];
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        // mFlatOwnerIdArray[i]=jsonObject.getString("FlatownerId");
                        mFlatOwnerNameArray[i] = jsonObject.getString("FlatownerName");
                        FlatSpinerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
                        FlatSpinerAdapter.addAll(mFlatOwnerNameArray);
                        spinner_searchToVisit.setAdapter(FlatSpinerAdapter);
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

    private void insertDeliveryDeafultThread() {
        String url = Urls.entryDeliveryVisitor;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("VisitorName", editVistorname.getText().toString());
            jsonObject.put("MobileNo", edit_mobilenumber.getText().toString());
            jsonObject.put("VisitorPurpose", edt_VisitingPurpose.getText().toString());
            jsonObject.put("VisitorDate", edit_Vistordate.getText().toString());
            jsonObject.put("VisitorTime", edit_Time1.getText().toString());
            jsonObject.put("company", edt_Company2.getText().toString());
            jsonObject.put("FlatOwnerId", 1);

        }catch(JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialogProgress.dismiss();
                Log.e("VisitorInternalStaffRes","VisitorInternalStaffRes"+response);
                try {
                    boolean status=response.getBoolean("Status");
                    if(status)
                    {
                        Toast.makeText(getActivity(),response.getString("Message"),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(),response.getString("Message"),Toast.LENGTH_SHORT).show();

                    }

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
}