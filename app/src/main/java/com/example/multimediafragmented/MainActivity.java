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
import android.widget.TextView;
import android.widget.Toast;

import com.example.multimediafragmented.ui.main.AlbumFragment;
import com.example.multimediafragmented.ui.main.MainFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ListOfSongs listOfSongs;
    public ArrayList<MainFragment> listOfAlbumFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //On récupère les musiques
        final String musicOnly = MediaStore.Audio.Media.IS_MUSIC + " != 0 ";

        //AJOUTER PROTECTION
        listOfSongs = new ListOfSongs(this,musicOnly);
        ArrayAdapter<String> NamesAdapter = new ArrayAdapter<String>(this,R.layout.albumrow, listOfSongs.getAlbums());

        if (savedInstanceState == null) {

            Fragment newFrag = MainFragment.newInstance(NamesAdapter);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, newFrag)
                    .commitNow();
        }

        //On crée les différents fragments et crée la liste d'albums
        listOfAlbumFragments = new ArrayList<MainFragment>();
        ArrayList<String> listOfAlbums = listOfSongs.getAlbums();
        for(int i=0; i<listOfAlbums.size();i++){
            ArrayList<String> songInAlbum = listOfSongs.getSongsByAlbum(this,listOfAlbums.get(i),musicOnly);
            ArrayAdapter<String> albumFragmentAdapter = new ArrayAdapter<String>(this,R.layout.simplerow, songInAlbum);
            MainFragment currentAlbumFragment = MainFragment.newInstance(albumFragmentAdapter);
            listOfAlbumFragments.add(currentAlbumFragment);
        }
    }


    public void changeFragment(View view){
        TextView tv = (TextView) view;
        int index = listOfSongs.getAlbums().indexOf(tv.getText());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft
                .addToBackStack(null)
                .replace(R.id.container, listOfAlbumFragments.get(index) )
                .commit();
    }
}
