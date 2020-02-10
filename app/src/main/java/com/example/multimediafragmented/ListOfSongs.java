package com.example.multimediafragmented;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListOfSongs {

    private ArrayList<String> paths = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> albums = new ArrayList<>();

    ListOfSongs(Context cxt, String criteria) {
        ContentResolver cr = cxt.getContentResolver();
        Uri songsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = cr.query(songsUri, new String[]{MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME}, criteria, null, sortOrder);

        //Querying song names and paths
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(0) != null) {
                    paths.add(cursor.getString(0));
                    names.add(cursor.getString(1));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();


        //Querying albums
        Cursor albumCursor = cr.query(songsUri, new String[] {"DISTINCT(" + MediaStore.Audio.Media.ALBUM +")"}, criteria, null, sortOrder);

        if (albumCursor.moveToFirst()) {
            do {
                if (albumCursor.getString(0) != null) {
                    albums.add(albumCursor.getString(0));
                }
            }while (albumCursor.moveToNext());
        }
        albumCursor.close();
    }

    public ArrayList<String> getSongsByAlbum(Context cxt, String album, String criteria){
        ArrayList<String> result = new ArrayList<>();
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Uri songsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = cxt.getContentResolver().query(songsUri, new String[] {MediaStore.Audio.Media.DISPLAY_NAME},criteria + " AND " + MediaStore.Audio.Media.ALBUM + " = " + '"' + album + '"',null,sortOrder);

        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(0)!=null){
                    result.add(cursor.getString(0));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public ArrayList<String> getPaths(){ return paths;}
    public ArrayList<String> getNames(){ return names;}
    public ArrayList<String> getAlbums() { return albums;}
}
