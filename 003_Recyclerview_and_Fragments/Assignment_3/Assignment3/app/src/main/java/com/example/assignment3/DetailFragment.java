package com.example.assignment3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class DetailFragment extends Fragment {

    student mStudent;
    Button submit_button;
    Button reset_button;
    TextView roll;
    EditText name;
    EditText dept;
    EditText email;

    public static DetailFragment newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("student_id", id);

        //set Fragmentclass Arguments
        DetailFragment fragobj = new DetailFragment();
        fragobj.setArguments(bundle);
        return fragobj;

    }

    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String st_id = getArguments().getString("student_id");

        mStudent = studentLab.get(getActivity()).getStudent(st_id);
//        System.out.println("D-----------------------------" + mStudent.getId());

    }

    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);


        submit_button = v.findViewById(R.id.button_submit);

        reset_button = v.findViewById(R.id.button_reset);

        roll = v.findViewById(R.id.et_roll);
        name = v.findViewById(R.id.et_name);
        dept = v.findViewById(R.id.et_dept);
        email = v.findViewById(R.id.et_email);

        roll.setText(mStudent.getId());
        name.setText(mStudent.getName());
        dept.setText(mStudent.getDepartment());
        email.setText(mStudent.getEmail());


        // Implementing the download of song
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String new_dept = dept.getText().toString();
                String new_name = name.getText().toString();
                String new_email = email.getText().toString();

                if(new_name.length() > 0)
                    mStudent.setName(new_name);
                else
                    Toast.makeText(getActivity(),
                            " Name Empyty- Not set"  , Toast.LENGTH_SHORT)
                            .show();


                if(new_email.length() > 0)
                    mStudent.setEmail(new_email);
                else
                    Toast.makeText(getActivity(),
                            " Email Empty - Not set"  , Toast.LENGTH_SHORT)
                            .show();

                if (new_dept.length() > 0)
                    mStudent.setDepartment(new_dept);
                else
                    Toast.makeText(getActivity(),
                            " Department Empty - Not set"  , Toast.LENGTH_SHORT)
                            .show();


                getActivity().finish();
            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                name.getText().clear();
                dept.getText().clear();
                email.getText().clear();


                name.setText(mStudent.getName());
                dept.setText(mStudent.getDepartment());
                email.setText(mStudent.getEmail());

            }
        });


        return v;
    }

}




