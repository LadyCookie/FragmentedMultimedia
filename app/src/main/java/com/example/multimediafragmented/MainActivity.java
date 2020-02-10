package com.example.multimediafragmented;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;

import com.example.multimediafragmented.ui.main.AlbumFragment;
import com.example.multimediafragmented.ui.main.MainFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ListOfSongs listOfSongs;
    public ArrayList<MainFragment> listOfAlbumFragments;

    private Messenger mService = null;
    boolean bound;
    boolean sent=false;
    IntentFilter filter1;
    IntentFilter filter2;
    IntentFilter filter3;
    Button playButton;
    SeekBar seekBar;

    private String currentAlbum = "All Albums";
    private int currentAlbumSize;
    private ArrayList<String> currentSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //On récupère les musiques
        final String musicOnly = MediaStore.Audio.Media.IS_MUSIC + " != 0 ";

        //On récupère les chansons
        listOfSongs = new ListOfSongs(this,musicOnly);
        currentAlbumSize = listOfSongs.getPaths().size()-1;
        currentSongs = listOfSongs.getNames();
        //On affiche les albums
        ArrayAdapter<String> NamesAdapter = new ArrayAdapter<String>(this,R.layout.albumrow, listOfSongs.getAlbums());

        if (savedInstanceState == null) {

            Fragment newFrag = MainFragment.newInstance(NamesAdapter,"All Albums");
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
            MainFragment currentAlbumFragment = MainFragment.newInstance(albumFragmentAdapter,listOfAlbums.get(i));
            listOfAlbumFragments.add(currentAlbumFragment);
        }

        //On crée les intent filter
        filter1 = new IntentFilter();
        filter1.addAction("isPlaying");
        filter2 = new IntentFilter();
        filter2.addAction("StartMusic");
        filter3 = new IntentFilter();
        filter3.addAction("updateSeekBar");

        //Setup du broadcast receiver
        registerReceiver(mMBroadcastReceiver, filter1);
        registerReceiver(mMBroadcastReceiver,filter2);
        registerReceiver(mMBroadcastReceiver,filter3);

        playButton=(Button) findViewById(R.id.playButton);
        seekBar=(SeekBar) findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                if(fromUser){
                    Message message = Message.obtain(null,MessengerService.MSG_SEEKBAR, progress * 1000,0);
                    try {
                        mService.send(message);
                    }catch(RemoteException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private ServiceConnection mConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            mService = new Messenger(service);
            bound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name){
            mService=null;
            bound=false;
        }
    };

    @Override
    public void onStart(){
        super.onStart();
        bindService(new Intent(this,MessengerService.class), mConnection,Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mMBroadcastReceiver);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(bound){
            unbindService(mConnection);
            bound=false;
        }
    }

    public void playThis(View v){
        sent=false;
        TextView tv = (TextView) v;
        final String musicOnly = MediaStore.Audio.Media.IS_MUSIC + " != 0 ";
        currentSongs = listOfSongs.getSongsByAlbum(this,currentAlbum,musicOnly);
        int index = currentSongs.indexOf(tv.getText());
        sendMessagePlay(index);
    }

    public void play(View v){
        if(!sent){
            sendMessagePlay(0);
        }
        else{
            Message message;
            message = Message.obtain(null,MessengerService.MSG_PAUSE,0,0);
            try{
                mService.send(message);
            } catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }

    public void next(View v){
        if(!sent){
            sendMessagePlay(1);
        }
        else{
            Message message;
            message = Message.obtain(null,MessengerService.MSG_NEXT,0,0);
            try{
                mService.send(message);
            }catch(RemoteException e){
                e.printStackTrace();
            }
        }
    }

    public void prev(View v){
        if(!sent){
            sendMessagePlay(currentAlbumSize);
        }
        else{
            Message message;
            message = Message.obtain(null,MessengerService.MSG_PREV,0,0);
            try{
                mService.send(message);
            }catch(RemoteException e){
                e.printStackTrace();
            }
        }
    }

    public void sendMessagePlay(int index){
        Message message;
        if(!bound){
            return;
        }
        if(!sent){
            Bundle bundle = new Bundle();
            final String musicOnly = MediaStore.Audio.Media.IS_MUSIC + " != 0 ";
            ArrayList<String> mPaths =listOfSongs.getSongsPathsByAlbum(this,currentAlbum,musicOnly);
            currentSongs = listOfSongs.getSongsByAlbum(this,currentAlbum,musicOnly);
            currentAlbumSize = mPaths.size()-1;
            bundle.putStringArrayList("paths",mPaths);
            message = Message.obtain(null,MessengerService.MSG_SEND_PATHS,index,0);
            message.setData(bundle);
            sent = true;
        }
        else{
            message = Message.obtain(null,MessengerService.MSG_PLAY,index,0);
        }
        try{
            mService.send(message);
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    public void changeFragment(View view){
        TextView tv = (TextView) view;
        int index = listOfSongs.getAlbums().indexOf(tv.getText());
        currentAlbum = listOfSongs.getAlbums().get(index);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft
                .addToBackStack(null)
                .replace(R.id.container, listOfAlbumFragments.get(index) )
                .commit();
    }


    public void Debug(View v){
        Toast.makeText(this,"Debug time",Toast.LENGTH_SHORT).show();
    }

    private BroadcastReceiver mMBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().compareTo("isPlaying")==0){
                if(intent.getBooleanExtra("Status",false))
                {
                    playButton.setText("PAUSE");
                }
                else
                {
                    playButton.setText("PLAY");
                }
            }else if(intent.getAction().compareTo("startMusic")==0)
            {
                ((TextView)findViewById(R.id.textTitle)).setText(listOfSongs.getNames().get(intent.getIntExtra("Index",0)));
                seekBar.setMax(intent.getIntExtra("MaxDuration",0));
                seekBar.setProgress(0);
            }else if(intent.getAction().compareTo("updateSeekbar")==0)
            {
                seekBar.setProgress(intent.getIntExtra("Seek",0));
            }
        }
    };
}
