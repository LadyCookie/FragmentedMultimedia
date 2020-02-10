package com.example.multimediafragmented.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multimediafragmented.ListOfSongs;
import com.example.multimediafragmented.R;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private ArrayAdapter<String> mAdapter;
    private String mTitle;

    public static MainFragment newInstance(ArrayAdapter<String> adapter, String title) {
        MainFragment MF = new MainFragment();
        MF.mAdapter = adapter;
        MF.mTitle = title;
        return MF;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        //Setting the view
        View view = getView();
        if(view !=null){
            TextView textView = view.findViewById(R.id.myTitle);
            textView.setText(mTitle);

            ListView listView = view.findViewById(R.id.listMusics);
            if(listView != null){
                listView.setAdapter(mAdapter);
            }
            else{
                Toast.makeText(getContext(),"Didn't find list view",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
