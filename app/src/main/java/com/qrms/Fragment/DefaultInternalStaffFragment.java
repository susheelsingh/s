package com.qrms.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.Volley;
import com.qrms.Common.ShareData;
import com.qrms.Common.Urls;
import com.qrms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DefaultInternalStaffFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    View mView;
    Spinner spinner_visitorTypeUser, spinner_searchToVisit,spinner_staffName;
    EditText edt_VisitorPurpose;
    Button button_submit,button_Select;
    private String token=null,mStaffName,mStaffId,mFlatOwnerName;
    ProgressDialog dialogProgress = null;
    ShareData mSharedata;
    private String[] mStaffNameArray,mIdStaffArray,mFlatOwnerNameArray,mFlatOwnerIdArray;
    private ArrayAdapter<String> SpinerAdapter,FlatSpinerAdapter;
    String[] inputArrayUser = {"--Select Visitor Type--","Default Visitor","Internal Staff","Delivery","Maid/Servent"};

    public DefaultInternalStaffFragment() {
        // Required empty public constructor
    }

    public static DefaultInternalStaffFragment newInstance(String param1, String param2) {
        DefaultInternalStaffFragment fragment = new DefaultInternalStaffFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        mView= inflater.inflate(R.layout.fragment_default_internal_staff, container, false);
        initObjects();
        getInternalStaffListThread();
        getFlatOwnerListThread();
        // :::::::::::::: Onclick:Listner :::::::::::::::::::
        button_submit.setOnClickListener(this);
        button_Select.setOnClickListener(this);
        spinner_visitorTypeUser.setOnItemSelectedListener(this);
        spinner_searchToVisit.setOnItemSelectedListener(this);
        spinner_staffName.setOnItemSelectedListener(this);
        return mView;
    }

    private void initObjects(){
        spinner_visitorTypeUser=(Spinner)mView.findViewById(R.id.spinner_visitorTypeUser);
        spinner_searchToVisit=(Spinner)mView.findViewById(R.id.spinner_searchToVisit);
        spinner_staffName=(Spinner)mView.findViewById(R.id.spinner_staffName);
        edt_VisitorPurpose=(EditText)mView.findViewById(R.id.edt_VisitorPurpose);
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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_submit:
                if (edt_VisitorPurpose.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Visitor Purpose is required.", Toast.LENGTH_SHORT).show();
            }else{
                    dialogProgress.show();
                    insertInternalStaffThread();

                }
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
            case R.id.spinner_searchToVisit:
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId())
        {
            case R.id.spinner_staffName:
                mStaffName=  mStaffNameArray[position];
                mStaffId=mIdStaffArray[position];
               // Toast.makeText(getActivity(),mStaffName,Toast.LENGTH_SHORT).show();
                break;
            case R.id.spinner_searchToVisit:
                mFlatOwnerName=mFlatOwnerNameArray[position];
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void getInternalStaffListThread() {
        dialogProgress.show();
        String url = Urls.InternalStaffList;
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                dialogProgress.dismiss();
                try {
                    mIdStaffArray=new String[response.length()];
                    mStaffNameArray = new String[response.length()];
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        mIdStaffArray[i]=jsonObject.getString("VIntStaffPersonalDtlId");
                        mStaffNameArray[i] = jsonObject.getString("FirstName");
                         SpinerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
                            SpinerAdapter.addAll(mStaffNameArray);
                             spinner_staffName.setAdapter(SpinerAdapter);
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
    private void getFlatOwnerListThread() {
        dialogProgress.show();
        String url ="http://apis.qrms.in/api/GetFlatOwnerList";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                dialogProgress.dismiss();
                Log.e("flatOwnerList","flatOwnerList"+response);
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


    private void insertInternalStaffThread() {
        int count=1;
        int count1=1;
        String url = Urls.entryVistiorInternalStaff;

        JSONObject jsonObject = new JSONObject();
        try {
            count=count+ SpinerAdapter.getPosition(spinner_staffName.getSelectedItem().toString());
            count1=count1+FlatSpinerAdapter.getPosition(spinner_searchToVisit.getSelectedItem().toString());


            jsonObject.put("VIntStaffPersonalDtlId", count);
            jsonObject.put("VisitorMeetTo",spinner_staffName.getSelectedItem().toString());
            jsonObject.put("VisitorPurpose", edt_VisitorPurpose.getText().toString());
            jsonObject.put("FlatOwnerId",count1);


        }catch(JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialogProgress.dismiss();
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