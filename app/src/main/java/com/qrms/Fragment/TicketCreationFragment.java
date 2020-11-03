package com.qrms.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qrms.R;

public class TicketCreationFragment extends Fragment {

    View mView;
    public TicketCreationFragment() {
        // Required empty public constructor
    }

       // TODO: Rename and change types and number of parameters
    public static TicketCreationFragment newInstance(String param1, String param2) {
        TicketCreationFragment fragment = new TicketCreationFragment();
        Bundle args = new Bundle();
                fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView= inflater.inflate(R.layout.fragment_ticket_creation, container, false);
        return mView;
    }
}