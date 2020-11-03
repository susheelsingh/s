package com.qrms.Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.qrms.Common.BitmapUtils;
import com.qrms.Common.ShareData;
import com.qrms.Common.Urls;
import com.qrms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class VisitorSecurityMangementFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private View mView;
    private Button button_chooseFile,button_StartCamera,button_submit;
    private TextView text_NoFileChosen;
    private String FilePath=null;
    private String token=null,VisitorInputType, IdentificationUser;
    ProgressDialog dialogProgress = null;
    private static final int PICKFILE_RESULT_CODE = 2;
    ImageView imageView;
    ShareData mSharedata;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private String mTempPhotoPath;
    private Bitmap mResultsBitmap;
    CheckBox checkBox;
    private Calendar calendar;
    private Spinner spinner_visitorTypeUser,SpiinerIdenttityType;
    String[] inputArrayUser = {"--Select Visitor Type--","Default Visitor","Internal Staff","Delivery"};
    String[] identityArrayUser = {" Identification Type","Adhar Card","Pan Card","Election Card"};

    private int current_date, current_month, current_year,mHour,mMinute;
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider";
    EditText editVistorname,editVistorMobile,editEmaildId,editHomeAddress,editOfficeAddress,editVistingPurpose,
            edittextVistordate,edittextVistrTime,editVistorDurationDate,editVistorDurationToDate1,editVistorExitDate,editVistorExitTime,
            editNameOfficialToMeet1,editBranchDesk1,editOfficialEmailId1,editOfficialMobileNumber1;

    public VisitorSecurityMangementFragment() {
        // Required empty public constructor
    }
    public static VisitorSecurityMangementFragment newInstance(String param1, String param2) {
        VisitorSecurityMangementFragment fragment = new VisitorSecurityMangementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Visitor Security");
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_visitor_mangement, container, false);
        initObjects();
        // :::::::::::::: OncLickListner ::::::::::::::::
        button_chooseFile.setOnClickListener(this);
        button_StartCamera.setOnClickListener(this);
        button_submit.setOnClickListener(this);
        edittextVistordate.setOnClickListener(this);
        edittextVistrTime.setOnClickListener(this);
        editVistorDurationDate.setOnClickListener(this);
        editVistorDurationToDate1.setOnClickListener(this);
        editVistorExitDate.setOnClickListener(this);
        editVistorExitTime.setOnClickListener(this);
        spinner_visitorTypeUser.setOnItemSelectedListener(this);
        SpiinerIdenttityType.setOnItemSelectedListener(this);
        return  mView;
    }
    private void initObjects()
    {
        button_chooseFile=(Button)mView.findViewById(R.id.button_chooseFile);
        button_StartCamera=(Button)mView.findViewById(R.id.button_StartCamera);
        text_NoFileChosen=(TextView)mView.findViewById(R.id.text_NoFileChosen);
        imageView=(ImageView)mView.findViewById(R.id.imageView);
        editVistorname=(EditText)mView.findViewById(R.id.editVistorname);
        editVistorMobile=(EditText)mView.findViewById(R.id.editVistorMobile);
        editEmaildId=(EditText)mView.findViewById(R.id.editEmaildId);
        editHomeAddress=(EditText)mView.findViewById(R.id.editHomeAddress);
        editOfficeAddress=(EditText)mView.findViewById(R.id.editOfficeAddress);
        editVistingPurpose=(EditText)mView.findViewById(R.id.editVistingPurpose);
        edittextVistordate=(EditText)mView.findViewById(R.id.edittextVistordate);
        edittextVistrTime=(EditText)mView.findViewById(R.id.edittextVistrTime);
        editVistorDurationDate=(EditText)mView.findViewById(R.id.editVistorDurationDate);
        editVistorDurationToDate1=(EditText)mView.findViewById(R.id.editVistorDurationToDate1);
        editVistorExitDate=(EditText)mView.findViewById(R.id.editVistorExitDate);
        editVistorExitTime=(EditText)mView.findViewById(R.id.editVistorExitTime);
        editNameOfficialToMeet1=(EditText)mView.findViewById(R.id.editNameOfficialToMeet1);
        editBranchDesk1=(EditText)mView.findViewById(R.id.editBranchDesk1);
        editOfficialEmailId1=(EditText)mView.findViewById(R.id.editOfficialEmailId1);
        editOfficialMobileNumber1=(EditText)mView.findViewById(R.id.editOfficialMobileNumber1);
        button_submit=(Button) mView.findViewById(R.id.button_submit);
        checkBox=(CheckBox) mView.findViewById(R.id.checkBox);
        spinner_visitorTypeUser=(Spinner)mView.findViewById(R.id.spinner_visitorTypeUser);
        SpiinerIdenttityType=(Spinner)mView.findViewById(R.id.SpiinerIdenttityType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, inputArrayUser);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_visitorTypeUser.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, identityArrayUser);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpiinerIdenttityType.setAdapter(adapter1);

        mSharedata=new ShareData(getActivity());
        token = mSharedata.getString("Auth_token_type", "");
        token = token.concat(" ").concat(mSharedata.getString("Auth_token", ""));
        Log.e("token is","Token Is"+token);

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
            case R.id.button_chooseFile:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
                break;
            case R.id.button_StartCamera:
                // Check for the external storage permission
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // If you do not have permission, request it
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_STORAGE_PERMISSION);
                } else {
                    // Launch the camera if the permission exists
                    launchCamera();
                }

                break;
            case R.id.button_submit:

                if (editVistorname.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Visitor name is required.", Toast.LENGTH_SHORT).show();
                } else if (editVistorMobile.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Mobile Number is required.", Toast.LENGTH_SHORT).show();
                } else if (editEmaildId.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Email Id is required.", Toast.LENGTH_SHORT).show();
                } else if (editHomeAddress.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Home Address is required.", Toast.LENGTH_SHORT).show();
                } else if (editOfficeAddress.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Office Address is required.", Toast.LENGTH_SHORT).show();
                } else if (editVistingPurpose.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Visitor Purpose is required.", Toast.LENGTH_SHORT).show();
                } else if (edittextVistordate.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "This date is required.", Toast.LENGTH_SHORT).show();
                } else if (edittextVistrTime.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Visitor time is required.", Toast.LENGTH_SHORT).show();
                }  else if (editNameOfficialToMeet1.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "this field is required.", Toast.LENGTH_SHORT).show();
                } else if (editOfficialEmailId1.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "This field is required.", Toast.LENGTH_SHORT).show();
                } else if (editBranchDesk1.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Address is required.", Toast.LENGTH_SHORT).show();
                } else {
                    if(VisitorInputType.equals("Default Visitor"))
                    {
                        visitorInsertThread();
                    }else if(VisitorInputType.equals("Internal Staff"))
                    {
                        insertVisitorInternalStaffThread();
                    }else
                    {
                        insertDeliveryVesitorThread();
                    }

                }
                break;
            case R.id.edittextVistordate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        edittextVistordate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, current_year, current_month, current_date);
                datePickerDialog.show();
                break;

            case R.id.edittextVistrTime:
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edittextVistrTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.editVistorDurationDate:
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        editVistorDurationDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, current_year, current_month, current_date);
                datePickerDialog1.show();
                break;
            case R.id.editVistorDurationToDate1:
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        editVistorDurationToDate1.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, current_year, current_month, current_date);
                datePickerDialog2.show();

                break;
            case R.id.editVistorExitDate:
                DatePickerDialog datePickerDialog3 = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        editVistorExitDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, current_year, current_month, current_date);
                datePickerDialog3.show();
                break;
            case R.id.editVistorExitTime:
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        editVistorExitTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
                timePickerDialog1.show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(getActivity(), "permission_denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode)
       {
           case REQUEST_IMAGE_CAPTURE:
               if(resultCode==RESULT_OK)
               {
                   processAndSetImage();
               }else
               {
                   // Otherwise, delete the temporary image file
                   BitmapUtils.deleteImageFile(getActivity(), mTempPhotoPath);
               }
               break;
           case PICKFILE_RESULT_CODE:
               if(resultCode==RESULT_OK){
                   String FilePath = data.getData().getPath();
                   text_NoFileChosen.setText(FilePath);
               }
               break;
       }

    }

    /**
     * Creates a temporary image file and captures a picture to store in it.
     */
    private void launchCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(getActivity());
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();
                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(getActivity(), FILE_PROVIDER_AUTHORITY, photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    /**
     * Method for processing the captured image and setting it to the TextView.
     */
    private void processAndSetImage() {
        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(getActivity(), mTempPhotoPath);

        // Set the new bitmap to the ImageView
        imageView.setImageBitmap(mResultsBitmap);
    }

    private void visitorInsertThread() {
        String url = Urls.InsertVisitor;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("VisitorName",editVistorname.getText().toString());
            jsonObject.put("MobileNo",editVistorMobile.getText().toString());
            jsonObject.put("EmailId",editEmaildId.getText().toString());
            jsonObject.put("HomeAddress",editHomeAddress.getText().toString());
            jsonObject.put("OfficeAddress",editOfficeAddress.getText().toString());
            jsonObject.put("VisitorPurpose",editVistingPurpose.getText().toString());
            jsonObject.put("VisitorDate",edittextVistordate.getText().toString());
            jsonObject.put("VisitorTime",edittextVistrTime.getText().toString());
            jsonObject.put("VisitorDurationFrom",editVistorDurationDate.getText().toString());
            jsonObject.put("VisitorDurationTo",editVistorDurationToDate1.getText().toString());
            jsonObject.put("VisitorExitDate",editVistorExitDate.getText().toString());
            jsonObject.put("VisitorExitTime",editVistorExitTime.getText().toString());
            jsonObject.put("VisitorMeetTo",editNameOfficialToMeet1.getText().toString());
            jsonObject.put("MeetToAddress",editBranchDesk1.getText().toString());
            jsonObject.put("MeetToMobileNo",editOfficialMobileNumber1.getText().toString());
            jsonObject.put("MeetToEmailId",editOfficialEmailId1.getText().toString());
            jsonObject.put("IsActive",checkBox.getText().toString());
            jsonObject.put("IdentityId",IdentificationUser);
            jsonObject.put("VisitorIdPath",FilePath);
            jsonObject.put("VisitorImage",mTempPhotoPath);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialogProgress.dismiss();
                    Log.e("loginResponse","loginResponse"+response);
                    try {
                        String str=response.getString("User");
                        JSONObject jsonObject1=new JSONObject(str);

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void insertDeliveryVesitorThread() {
        String url = Urls.InsertDeliveryVisitor;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("VisitorName",editVistorname.getText().toString());
            jsonObject.put("MobileNo",editVistorMobile.getText().toString());
            jsonObject.put("VisitorPurpose",editVistingPurpose.getText().toString());
            jsonObject.put("VisitorDate",edittextVistordate.getText().toString());
            jsonObject.put("VisitorTime",edittextVistrTime.getText().toString());
            jsonObject.put("company","Dunnock Services Pvt Ltd");
          JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialogProgress.dismiss();
                    Log.e("DeliveryVistorResponse","DeliveryVistorResponse"+response);
                    try {
                        String str=response.getString("User");
                        JSONObject jsonObject1=new JSONObject(str);

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void insertVisitorInternalStaffThread() {
        String url = Urls.InsertInternalStaff;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("VIntStaffPersonalDtlId",editVistorname.getText().toString());
            jsonObject.put("VisitorMeetTo",editVistorMobile.getText().toString());
            jsonObject.put("VisitorPurpose",editVistingPurpose.getText().toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dialogProgress.dismiss();
                    Log.e("VisitorInternalStaffRes","VisitorInternalStaffRes"+response);
                    try {
                        String str=response.getString("User");
                        JSONObject jsonObject1=new JSONObject(str);

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId())
        {
            case R.id.spinner_visitorTypeUser:
                VisitorInputType=  inputArrayUser[position];

                break;
            case R.id.SpiinerIdenttityType:
                 IdentificationUser=identityArrayUser[position];
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}