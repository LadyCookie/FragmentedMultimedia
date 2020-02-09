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
        ArrayAdapter<String> NamesAdapter = new ArrayAdapter<String>(this,R.layout.simplerow, listOfSongs.getNames());

        if (savedInstanceState == null) {

            Fragment newFrag = MainFragment.newInstance(NamesAdapter);
            //ListView listView = newFrag.getView().;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, newFrag)
                    .commitNow();
        }

        Toast.makeText(this, "Albums:", Toast.LENGTH_SHORT).show();

        int i;
        for(i=0; i<listOfSongs.getAlbums().size(); i++){
            Toast.makeText(this, listOfSongs.getAlbums().get(i), Toast.LENGTH_SHORT).show();
        }

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
