package com.example.multimediafragmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.multimediafragmented.ui.main.AlbumFragment;
import com.example.multimediafragmented.ui.main.MainFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ListOfSongs listOfSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //On récupère les musiques
        final String musicOnly = MediaStore.Audio.Media.IS_MUSIC + " != 0 ";

        //AJOUTER PROTECTION

        listOfSongs = new ListOfSongs(this,musicOnly);
        ArrayAdapter<String> NamesAdapter = new ArrayAdapter<String>(this,R.layout.simplerow, listOfSongs.getAlbums());

        if (savedInstanceState == null) {

            Fragment newFrag = MainFragment.newInstance(NamesAdapter);
            //ListView listView = newFrag.getView().;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, newFrag)
                    .commitNow();
        }


        for(int j=0; j<listOfSongs.getAlbums().size(); j++) {
            Toast.makeText(this, "Album: " + listOfSongs.getAlbums().get(j), Toast.LENGTH_SHORT).show();
            ArrayList<String> result = listOfSongs.getSongsByAlbum(this, listOfSongs.getAlbums().get(j), musicOnly);
            for (int i = 0; i < result.size(); i++) {
                Toast.makeText(this, result.get(i), Toast.LENGTH_SHORT).show();
            }
        }
        /*
        int i;
        for(i=0; i<listOfSongs.getAlbums().size(); i++){
            Toast.makeText(this, listOfSongs.getAlbums().get(i), Toast.LENGTH_SHORT).show();
        }
        */


    }


    public void changeFragment(View view){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft
                .addToBackStack(null)
                .replace(R.id.container, AlbumFragment.newInstance())
                .commit();
    }
}
