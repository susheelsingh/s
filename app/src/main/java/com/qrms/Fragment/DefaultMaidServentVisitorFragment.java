package com.qrms.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.android.volley.toolbox.Volley;
import com.qrms.Common.ShareData;
import com.qrms.Common.Urls;
import com.qrms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DefaultMaidServentVisitorFragment extends Fragment implements View.OnClickListener {
    View mView;
    Spinner spinner_visitorTypeUser;
    EditText edt_SocietyName1,edt_FlatNo1,edt_OwnerName1,edt_MobileNo1,edt_StaffType1,edt_ServantName1,
            edt_ServantMobileNo1,edt_ServantAdharNo1,edt_ServantAddress1;
    Button button_submit,button_Select;
    ShareData mSharedata;
    String[] inputArrayUser = {"--Select Visitor Type--","Default Visitor","Internal Staff","Delivery","Maid/Servent"};
    private String token=null;
    ProgressDialog dialogProgress = null;


    public DefaultMaidServentVisitorFragment() {
        // Required empty public constructor
    }

    public static DefaultMaidServentVisitorFragment newInstance(String param1, String param2) {
        DefaultMaidServentVisitorFragment fragment = new DefaultMaidServentVisitorFragment();
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
        mView= inflater.inflate(R.layout.fragment_default_maid_servent_visitor, container, false);
        initObjects();
        // ::::::::::::::::::: OnclickListner::::::::::::::
        button_submit.setOnClickListener(this);
        button_Select.setOnClickListener(this);
        return mView;
    }
    private void initObjects(){
        spinner_visitorTypeUser=(Spinner)mView.findViewById(R.id.spinner_visitorTypeUser);
        edt_SocietyName1=(EditText)mView.findViewById(R.id.edt_SocietyName1);
        edt_FlatNo1=(EditText)mView.findViewById(R.id.edt_FlatNo1);
        edt_OwnerName1=(EditText)mView.findViewById(R.id.edt_OwnerName1);
        edt_MobileNo1=(EditText)mView.findViewById(R.id.edt_MobileNo1);
        edt_StaffType1=(EditText)mView.findViewById(R.id.edt_StaffType1);
        edt_ServantName1=(EditText)mView.findViewById(R.id.edt_ServantName1);
        edt_ServantMobileNo1=(EditText)mView.findViewById(R.id.edt_ServantMobileNo1);
        edt_ServantAdharNo1=(EditText)mView.findViewById(R.id.edt_ServantAdharNo1);
        edt_ServantAddress1=(EditText)mView.findViewById(R.id.edt_ServantAddress1);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.button_submit:
                if(spinner_visitorTypeUser.getSelectedItem().toString().trim().equals("--Select Visitor Type--"))
                {
                    Toast.makeText(getActivity(), "Select Visitor is required.", Toast.LENGTH_SHORT).show();
                }
                 else if (edt_SocietyName1.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Visitor name is required.", Toast.LENGTH_SHORT).show();
                }else if(edt_FlatNo1.getText().toString().isEmpty()){
                     Toast.makeText(getActivity(), "Servant flatNo is required.", Toast.LENGTH_SHORT).show();
                 }else if(edt_OwnerName1.getText().toString().isEmpty()){
                     Toast.makeText(getActivity(), "Servant owner is required.", Toast.LENGTH_SHORT).show();

                 }else if(edt_MobileNo1.getText().toString().isEmpty()){
                     Toast.makeText(getActivity(), "Servant mobile is required.", Toast.LENGTH_SHORT).show();

                 }else if(edt_MobileNo1.getText().toString().length()<10){
                     Toast.makeText(getActivity(), "Servant mobile is required.", Toast.LENGTH_SHORT).show();

                 }else if(edt_StaffType1.getText().toString().isEmpty()){
                     Toast.makeText(getActivity(), "Servant type is required.", Toast.LENGTH_SHORT).show();

                 }else if(edt_ServantName1.getText().toString().isEmpty()){
                     Toast.makeText(getActivity(), "Servant name is required.", Toast.LENGTH_SHORT).show();

                 }else if(edt_ServantMobileNo1.getText().toString().isEmpty()){
                     Toast.makeText(getActivity(), "Servant mobile is required.", Toast.LENGTH_SHORT).show();

                 }else if(edt_ServantMobileNo1.getText().toString().isEmpty()){
                     Toast.makeText(getActivity(), "Servant mobile is required.", Toast.LENGTH_SHORT).show();

                 }else if(edt_ServantAdharNo1.getText().toString().isEmpty()){
                     Toast.makeText(getActivity(), "Servant adahar is required.", Toast.LENGTH_SHORT).show();

                 }else if(edt_ServantAddress1.getText().toString().isEmpty()){
                     Toast.makeText(getActivity(), "Servant address is required.", Toast.LENGTH_SHORT).show();

                 }
                 else{
                     Toast.makeText(getActivity(), "progress working...", Toast.LENGTH_SHORT).show();

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

        }

    }
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void insertMaidServantThread() {
        String url = Urls.entryDeliveryVisitor;
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("SocietName", edt_SocietyName1.getText().toString());
            jsonObject.put("FlatNo", edt_FlatNo1.getText().toString());
            jsonObject.put("OwnerName", edt_OwnerName1.getText().toString());
            jsonObject.put("MobileNo", edt_MobileNo1.getText().toString());
            jsonObject.put("StaffType", edt_StaffType1.getText().toString());
            jsonObject.put("ServantName", edt_ServantName1.getText().toString());
            jsonObject.put("ServentMobileNo", edt_ServantMobileNo1.getText().toString());
            jsonObject.put("ServantAdaharNo", edt_ServantAdharNo1.getText().toString());
            jsonObject.put("ServantAddress", edt_ServantAddress1.getText().toString());


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