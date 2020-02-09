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

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    public ArrayAdapter<String> mAdapter;

    public static MainFragment newInstance(ArrayAdapter<String> adapter) {
        MainFragment MF = new MainFragment();
        MF.mAdapter = adapter;
        return MF;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        View view = getView();
        if(view !=null){
            /*
            if(mAdapter != null){
                int toto = mAdapter.getCount();
                String Toto = String.valueOf(toto);
                Toast.makeText(getContext(),Toto ,Toast.LENGTH_LONG).show();
            }*/
            Toast.makeText(getContext(),"coucou" ,Toast.LENGTH_SHORT).show();
            ListView listView = view.findViewById(R.id.listMusics);
            if(listView != null){
                Toast.makeText(getContext(),"ListView not null" ,Toast.LENGTH_SHORT).show();
                listView.setAdapter(mAdapter);
            }
        }
    }

}
