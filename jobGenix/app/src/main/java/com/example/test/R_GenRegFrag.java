package com.example.test;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link R_GenRegFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class R_GenRegFrag extends Fragment {

    Button RbtnOnDemand,RbtnFullTime,RbtnPartTime;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public R_GenRegFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment R_GenRegFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static R_GenRegFrag newInstance(String param1, String param2) {
        R_GenRegFrag fragment = new R_GenRegFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View rootview=inflater.inflate(R.layout.fragment_r__gen_reg, container, false);
        RbtnOnDemand=rootview.findViewById(R.id.RbtnOnDemand);
        RbtnFullTime=rootview.findViewById(R.id.RbtnFullTime);
        RbtnPartTime=rootview.findViewById(R.id.RbtnPartTime);
        RbtnOnDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),RonDemand.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        RbtnFullTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),RfullTime.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        RbtnPartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),Rpart_time.class);
                startActivity(i);
                getActivity().finish();
            }
        });

         return rootview;
    }
}