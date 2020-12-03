package com.aditya.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    //editText is responsible for editing notes
    EditText editText;

    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        editText = findViewById(R.id.editText);

        //getting the note id from the listView in Main Activity
        Intent intent = getIntent();
         noteId = intent.getIntExtra("noteId",-1);
        //in a listView the list starts from 0 like in the array if noteId is -1 then that tells us that something went wrong
        if(noteId!=-1){
            //this will set the text in the editText in noteEditorActivity from the listView in the Main activity
            editText.setText(MainActivity.notes.get(noteId));
        }
        else{
            //if the noteId == -1 then add a new note in the list and that will be an empty string
            MainActivity.notes.add("");
            //picking a note Id since we added a new item onto there so it should be whatever the last Item is
            //size of an array is size -1
            noteId = MainActivity.notes.size() - 1;
        }

        //looking for changes in the editText's Text
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             //this will handle the changes in the text of the EditText
                //updating the Notes ArrayList from the Main Activity
                //String.valueOf(s) converting CharSequence s into the string
                MainActivity.notes.set(noteId,String.valueOf(s));
                //updating our arrayAdapter
                MainActivity.arrayAdapter.notifyDataSetChanged();

                //updating the notes shared preferences when we change anything in the notes Array
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.aditya.notes", Context.MODE_PRIVATE);
                //here we will use sets and not serializer because we don't want the notes to be in particular order because there is no way to change the order of things
                //converting this Arraylist notes into a hashset
                HashSet<String> hashSet = new HashSet<>(MainActivity.notes);
                //hashset is something that can be stored in the SharedPreferences
                sharedPreferences.edit().putStringSet("notes",hashSet).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}