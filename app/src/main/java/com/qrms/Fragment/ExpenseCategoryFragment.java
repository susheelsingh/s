package com.qrms.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qrms.R;

public class ExpenseCategoryFragment extends Fragment {

    View mView;
    public ExpenseCategoryFragment() {
        // Required empty public constructor
    }

    public static ExpenseCategoryFragment newInstance(String param1, String param2) {
        ExpenseCategoryFragment fragment = new ExpenseCategoryFragment();
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
        mView= inflater.inflate(R.layout.fragment_expense_category, container, false);
        return mView;
    }
}