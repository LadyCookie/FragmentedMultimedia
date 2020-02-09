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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.multimediafragmented.ListOfSongs;
import com.example.multimediafragmented.R;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private ArrayAdapter<String> mAdapter;
    private ListView saveListView;

    public static MainFragment newInstance(ArrayAdapter<String> adapter) {
        MainFragment MF = new MainFragment();
        MF.mAdapter = adapter;
        return MF;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        //saveListView = (ListView) rootView.findViewById(R.id.listMusics);
        //saveListView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel


        View view = getView();
        if(view !=null){
            ListView listView = view.findViewById(R.id.listMusics);
            if(listView != null){
                listView.setAdapter(mAdapter);
            }
        }
    }

}
