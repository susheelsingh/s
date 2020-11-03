package com.qrms.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.qrms.Common.ShareData;
import com.qrms.R;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private View mView;
    CircleImageView profile_image;
    EditText editUserId, editUserName,edtUserEmail,edtUserMobileNumber;
    Button button_profileSubmit;
    ShareData mSharedData;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("User Profile");
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_profile, container, false);
        initObjects();
        // ::::::::::::: OnClickListner:::::::::::::::::::::::
        button_profileSubmit.setOnClickListener(this);
        return mView;
    }
    private void initObjects()
    {
        profile_image=(CircleImageView)mView.findViewById(R.id.profile_image);
        editUserId=(EditText)mView.findViewById(R.id.editUserId);
        editUserName=(EditText)mView.findViewById(R.id.editUserName);
        edtUserEmail=(EditText)mView.findViewById(R.id.edtUserEmail);
        edtUserMobileNumber=(EditText)mView.findViewById(R.id.edtUserMobileNumber);
        mSharedData=new ShareData(getActivity());
        button_profileSubmit=(Button)mView. findViewById(R.id.button_profileSubmit);
       // Glide.with(this).load("https://in.pinterest.com/pin/825988387888992674/").centerCrop().into(profile_image);
        editUserId.setText(mSharedData.getString("logged_userId",""));
        editUserName.setText(mSharedData.getString("logged_UserName",""));
        edtUserEmail.setText(mSharedData.getString("Email",""));
        edtUserMobileNumber.setText(mSharedData.getString("Contact",""));

    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_profileSubmit:
                Toast.makeText(getActivity()," Working Progress....",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}