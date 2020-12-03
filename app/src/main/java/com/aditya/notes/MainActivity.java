package com.aditya.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //initializing out lisView that will hold our list of notes
    ListView listView;

    //ArrayList to hold the list of notes created by the user
    //static is used so that we access the ArrayList notes from the NoteEditorActivity
    static ArrayList<String> notes = new ArrayList<>();

    static ArrayAdapter arrayAdapter;

    //it is used to store data (small amount)
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up our shared Preferences
        sharedPreferences = getApplicationContext().getSharedPreferences("com.aditya.notes", Context.MODE_PRIVATE);
        //when the app opens up for the first time we need to tell the app that hey if there is some data stored already in the sharedPreferences go ahead and put that data into the lisView
        listView = findViewById(R.id.listView); //for holding list of notes created by the user

        //checking if we are getting anything out of the shared Preferences by using a hashSet
        HashSet<String> OutPutFromSharedPreferences =(HashSet<String>) sharedPreferences.getStringSet("notes",null);

        if(OutPutFromSharedPreferences == null)
        {

            //if OutPutFromSharedPreferences == null then that means we were'n able to pull something out of the shared Preferences
            //then that means there is no data stored inside the SharedPreferences
            //if that's the case then we should add an example note
            //adding an example note to our ArrayList notes
            notes.add("Example note");

        }
        else{
            //if the OutPutFromSharedPreferences != null then that means there are some data stored inside the SharedPreferences
            //in that case we want to take this OutPutFromSharedPreferences Hashset and add it to the notes array
            //the code below will create a new notes array list that will hold the information that is stored inside the OutPutFromSharedPreferences HashSet
            notes = new ArrayList(OutPutFromSharedPreferences);
        }

        //setting up our ArrayAdapter that we will use to display our notes array
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);
                intent.putExtra("noteId",position);
                startActivity(intent);
            }
        });

        //adding delete function on the listView when an item is long Pressed
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                //creating a new alert dialog box to ask the user weather they truely want to delete the note or not
                new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.danger).setTitle("Are you sure you want to delete the note?")
                        .setMessage("The deleted notes cant be recovered!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //here we will write the code to delete a note from the applications listView and from the memory
                                //deleting the note from the notes Arraylist
                                notes.remove(position);
                                //updating the ArrayAdapter
                                arrayAdapter.notifyDataSetChanged();

                                //now making changes to the shared Preferences when the notes are deleted
                                //updating the notes shared preferences when we change anything in the notes Array
                                //here we will use sets and not serializer because we don't want the notes to be in particular order because there is no way to change the order of things
                                //converting this Arraylist notes into a hashset
                                HashSet<String> hashSet = new HashSet<>(MainActivity.notes);
                                //hashset is something that can be stored in the SharedPreferences
                                sharedPreferences.edit().putStringSet("notes",hashSet).apply();
                            }
                        }).setNegativeButton("No",null).show();

                return true;
            }
        });

    }

    //the code below will handle our menue in the title bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //creating the menue in the title bar from the menue xml file
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menue,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //setting up the title menue options to respond to the user clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.addNote){
            //here we will write the code to add a note in our notes app
            //creating an intent to open NoteEditorActivity.class
            Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);
                    startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.DevInfo){
            Intent intent = new Intent(MainActivity.this,DevActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}