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
 * Use the {@link E_ProfileFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class E_ProfileFrag extends Fragment {
    TextView txtUsernameE,txtNameE;
    Button btnEditE,signoutE;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public E_ProfileFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment E_ProfileFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static E_ProfileFrag newInstance(String param1, String param2) {
        E_ProfileFrag fragment = new E_ProfileFrag();
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
        View rootView = inflater.inflate(R.layout.fragment_e__profile, container, false);


        txtUsernameE=rootView.findViewById(R.id.txtUsernameE);
        txtNameE=rootView.findViewById(R.id.txtNameE);
        btnEditE=rootView.findViewById(R.id.btnEditE);
        signoutE=rootView.findViewById(R.id.signoutE);


        String name=UserInfo.Uinfo.get(1)+" "+UserInfo.Uinfo.get(2);
        txtUsernameE.setText(UserInfo.Uinfo.get(0));
        txtNameE.setText(name);

        btnEditE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),E_editProfile.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        signoutE.setOnClickListener(new View.OnClickListener() {
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


        return rootView;
    }
}