package com.example.test;

import static android.content.Context.MODE_PRIVATE;

import static com.example.test.LogIn.sharedpref;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link R_ProfileFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class R_ProfileFrag extends Fragment {
    TextView txtUsername,txtName,txtCompName;
    Button btnEdit,signout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public R_ProfileFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment R_ProfileFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static R_ProfileFrag newInstance(String param1, String param2) {
        R_ProfileFrag fragment = new R_ProfileFrag();
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
        View rootView = inflater.inflate(R.layout.fragment_r__profile, container, false);

        txtUsername = rootView.findViewById(R.id.txtUsername);
        txtName = rootView.findViewById(R.id.txtName);
        txtCompName= rootView.findViewById(R.id.txtCompName);
        btnEdit=rootView.findViewById(R.id.btnEdit);
        signout=rootView.findViewById(R.id.signoutR);
        String name=UserInfo.Uinfo.get(1)+" "+UserInfo.Uinfo.get(2);
        txtUsername.setText(UserInfo.Uinfo.get(0));
        txtName.setText(name);
        txtCompName.setText("company: "+UserInfo.Uinfo.get(3));
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),R_editProfile.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences= getActivity().getSharedPreferences(sharedpref, MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("token","");
                Intent i=new Intent(getActivity(),MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return  rootView;
    }
}