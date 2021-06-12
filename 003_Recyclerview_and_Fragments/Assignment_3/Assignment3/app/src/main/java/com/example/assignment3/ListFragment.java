package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ListFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private studentAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = view
                .findViewById(R.id.student_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        updateUI();

        return view;
    }

    public void onResume() { super.onResume(); updateUI();
    }

    private void updateUI() {
        studentLab StLab = studentLab.get(getActivity());
        List<student> students = StLab.getstudents();


        if (mAdapter == null) {
            mAdapter = new studentAdapter(students);
            mRecyclerView.setAdapter(mAdapter);

        }
        else{
            mAdapter.notifyDataSetChanged();
        }
    }


    private class CrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {


        private TextView mNameView;

        private TextView mRollView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.student_item, parent, false));
            itemView.setOnClickListener(this);

            mRollView = (TextView) itemView.findViewById(R.id.st_rollNo);

            mNameView = (TextView) itemView.findViewById(R.id.st_name);

        }

        public void bind(student s) {
            student stObj = s;
            mRollView.setText(stObj.getId());
            mNameView.setText(stObj.getName());
        }

        @Override
        public void onClick(View view) {
            TextView t = (TextView) view.findViewById(R.id.st_rollNo) ;

            Toast.makeText(getActivity(),
                    " clicked! on roll " + t.getText().toString() , Toast.LENGTH_SHORT)
                    .show();

            Intent int_var = new Intent(getActivity(),DetailActivity.class);
            int_var.putExtra("student_id", t.getText().toString());
//            System.out.println("D---------------------kkkk--------" + t.toString());
            startActivity(int_var);

        }
    }


    private class studentAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<student> mstudents;

        public studentAdapter(List<student> students) {
            mstudents = students;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            student c = mstudents.get(position);
            holder.bind(c);
        }

        @Override
        public int getItemCount() {
            return mstudents.size();
        }
    }
}




