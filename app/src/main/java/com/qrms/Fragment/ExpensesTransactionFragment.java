package com.qrms.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qrms.R;

public class ExpensesTransactionFragment extends Fragment {
    View mView;

       public ExpensesTransactionFragment() {
        // Required empty public constructor
    }

        public static ExpensesTransactionFragment newInstance(String param1, String param2) {
        ExpensesTransactionFragment fragment = new ExpensesTransactionFragment();
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
        mView= inflater.inflate(R.layout.fragment_expenses_transaction, container, false);
        return mView;
    }
}