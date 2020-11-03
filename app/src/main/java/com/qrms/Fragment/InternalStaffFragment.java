package com.qrms.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.android.volley.toolbox.Volley;
import com.qrms.Common.BitmapUtils;
import com.qrms.Common.ShareData;
import com.qrms.Common.Urls;
import com.qrms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class InternalStaffFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    View mView;
    String[] inputItemArray = {"--Internal Staff--","Security Person","Maintenance Manager","Chairman","Secretary","Committee Member","Other"};
    private Spinner edittext_InternalStaff;
    private String InternalStffInput,mTempPhotoPath,FilePath=null,token=null,emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";;
    ProgressDialog dialogProgress = null;
    ShareData mSharedata;
    CheckBox checkBox;
    TextView edit_joiningdate,text_browse;
    private static final int PICKFILE_RESULT_CODE = 2;
    private Calendar calendar;
    private int current_date, current_month, current_year,mHour,mMinute;
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider";
    Button button_submit;
    EditText edt_firstName,edit_MiddleName,edit_lastname,edit_IntParentOtherName,edit_IntMobile,edit_text_IntEmailId,
            edit_IntHomeAddress,edit_IntCity,edit_IntState,edit_CountryName,edit_IntPincode,
    edit_IntAadharCard,edit_text_IntPAN,edit_IntBankAccount,edit_IntIFSC,edit_IntBranch,edit_BankAddress,
            edit_BankPassoBookPath;



    public InternalStaffFragment() {
        // Required empty public constructor
    }

    public static InternalStaffFragment newInstance(String param1, String param2) {
        InternalStaffFragment fragment = new InternalStaffFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Internal Staff");
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_internal_staff, container, false);
        initObjects();
        // :::::::::::::::: OnclickListner::::::::::::::
        edittext_InternalStaff.setOnItemSelectedListener(this);
        edit_joiningdate.setOnClickListener(this);
        text_browse.setOnClickListener(this);
        button_submit.setOnClickListener(this);
        return mView;
    }

    private void initObjects()
    {
        edt_firstName=(EditText)mView.findViewById(R.id.edt_firstName);
        edit_MiddleName=(EditText)mView.findViewById(R.id.edit_MiddleName);
        edit_lastname=(EditText)mView.findViewById(R.id.edit_lastname);
        edit_IntParentOtherName=(EditText)mView.findViewById(R.id.edit_IntParentOtherName);
        edit_IntMobile=(EditText)mView.findViewById(R.id.edit_IntMobile);
        edit_text_IntEmailId=(EditText)mView.findViewById(R.id.edit_text_IntEmailId);
        edit_IntHomeAddress=(EditText)mView.findViewById(R.id.edit_IntHomeAddress);
        edit_IntCity=(EditText)mView.findViewById(R.id.edit_IntCity);
        edit_IntState=(EditText)mView.findViewById(R.id.edit_IntState);
        edit_CountryName=(EditText)mView.findViewById(R.id.edit_CountryName);
        edit_IntPincode=(EditText)mView.findViewById(R.id.edit_IntPincode);
        edit_joiningdate=(TextView)mView.findViewById(R.id.edit_joiningdate);
        edit_IntAadharCard=(EditText)mView.findViewById(R.id.edit_IntAadharCard);
        edit_text_IntPAN=(EditText)mView.findViewById(R.id.edit_text_IntPAN);
        edit_IntBankAccount=(EditText)mView.findViewById(R.id.edit_IntBankAccount);
        edit_IntIFSC=(EditText)mView.findViewById(R.id.edit_IntIFSC);
        edit_IntBranch=(EditText)mView.findViewById(R.id.edit_IntBranch);
        edit_BankAddress=(EditText)mView.findViewById(R.id.edit_BankAddress);
        edit_BankPassoBookPath=(EditText)mView.findViewById(R.id.edit_BankPassoBookPath);
        edittext_InternalStaff=(Spinner)mView.findViewById(R.id.edittext_InternalStaff);
        text_browse=(TextView)mView.findViewById(R.id.text_browse);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, inputItemArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edittext_InternalStaff.setAdapter(adapter);
        button_submit=(Button)mView.findViewById(R.id.button_submit);


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
        switch (v.getId()) {
            case R.id.button_submit:
                if(edittext_InternalStaff.getSelectedItem().toString().trim().equals("--Internal Staff--"))
                {
                    Toast.makeText(getActivity(), "Select Internal Staff is required.", Toast.LENGTH_SHORT).show();
                }
                 else if (edt_firstName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "First Name  is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_MiddleName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Middle Name is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_IntMobile.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "MobileNo is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_IntMobile.getText().toString().trim().length() < 10) {
                    Toast.makeText(getActivity(), "MobileNo is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_text_IntEmailId.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Email is required.", Toast.LENGTH_SHORT).show();
                }else if(!edit_text_IntEmailId.getText().toString().trim().matches(emailPattern))
                {
                    Toast.makeText(getActivity(), "Enter Valid Email.", Toast.LENGTH_SHORT).show();
                }
                 else if (edit_IntHomeAddress.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Address is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_IntCity.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "City is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_IntState.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "State is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_IntPincode.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "PinCode is required.", Toast.LENGTH_SHORT).show();
                }else if (edit_IntPincode.getText().toString().length()<6) {
                    Toast.makeText(getActivity(), "PinCode is six digit required.", Toast.LENGTH_SHORT).show();
                }
                 else if (edit_joiningdate.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Date is required.", Toast.LENGTH_SHORT).show();
                }else if(edit_IntAadharCard.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "AdharCard is required.", Toast.LENGTH_SHORT).show();
                }else if(edit_IntAadharCard.getText().toString().length()<12){
                    Toast.makeText(getActivity(), "AdharCard is twelve digit required.", Toast.LENGTH_SHORT).show();
                }
                 else if (edit_text_IntPAN.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "PanNo is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_IntBankAccount.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "BankAcNo is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_IntIFSC.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Ifsc is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_IntBranch.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Branch is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_BankAddress.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Bank Address is required.", Toast.LENGTH_SHORT).show();
                } else if (edit_BankPassoBookPath.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "File Path is required.", Toast.LENGTH_SHORT).show();
                } else {
                     dialogProgress.show();
                    insertVisitorInternalStaffThread();
                }
                break;
            case R.id.edit_joiningdate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        edit_joiningdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, current_year, current_month, current_date);
                datePickerDialog.show();
                break;
            case R.id.text_browse:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
                break;
        };
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.edittext_InternalStaff:
                InternalStffInput = inputItemArray[position];
                break;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case PICKFILE_RESULT_CODE:
                if(resultCode==RESULT_OK){
                    FilePath = data.getData().getPath();
                    edit_BankPassoBookPath.setText(FilePath); }
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
        private void insertVisitorInternalStaffThread() {
            String url = Urls.InsertInternalStaff;
            JSONObject jsonObject = new JSONObject();
            try {
                if (edittext_InternalStaff.getSelectedItem().toString().trim().equals("Security Person")) {
                    jsonObject.put("InternalStaffId", 1);
                } else if (edittext_InternalStaff.getSelectedItem().toString().trim().equals("Maintenance Manager")) {
                    jsonObject.put("InternalStaffId", 2);
                }else if (edittext_InternalStaff.getSelectedItem().toString().trim().equals("Chairman")) {
                    jsonObject.put("InternalStaffId", 3);
                }else if (edittext_InternalStaff.getSelectedItem().toString().trim().equals("Secretary")) {
                    jsonObject.put("InternalStaffId", 4);
                }else if (edittext_InternalStaff.getSelectedItem().toString().trim().equals("Committee Member")) {
                    jsonObject.put("InternalStaffId", 5);
                } else {
                    jsonObject.put("InternalStaffId", 6);
                }
                jsonObject.put("FirstName", edt_firstName.getText().toString());
                jsonObject.put("MiddleName", edit_MiddleName.getText().toString());
                jsonObject.put("LastName", edit_lastname.getText().toString());
                jsonObject.put("ParentOtherName", edit_IntParentOtherName.getText().toString());
                jsonObject.put("MobileNo", edit_IntMobile.getText().toString());
                jsonObject.put("Email", edit_text_IntEmailId.getText().toString());
                jsonObject.put("PrimaryAddress", edit_IntHomeAddress.getText().toString());
                jsonObject.put("Cityname", edit_IntCity.getText().toString());
                jsonObject.put("StateName", edit_IntState.getText().toString());
                jsonObject.put("countryName", edit_CountryName.getText().toString());
                jsonObject.put("Pincode", edit_IntPincode.getText().toString());
                jsonObject.put("JoiningDate", edit_joiningdate.getText().toString());
                jsonObject.put("AadharCard", edit_IntAadharCard.getText().toString());
                jsonObject.put("PanNo", edit_text_IntPAN.getText().toString());
                jsonObject.put("BankAccountNo", edit_IntBankAccount.getText().toString());
                jsonObject.put("IFSCCode", edit_IntIFSC.getText().toString());
                jsonObject.put("BranchName", edit_IntBranch.getText().toString());
                jsonObject.put("BranchAddress", edit_BankAddress.getText().toString());
                jsonObject.put("IsActive",true);
                jsonObject.put("BankPassoBookPath", edit_BankPassoBookPath.getText().toString());
                jsonObject.put("CreatedBy", "2020");
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


