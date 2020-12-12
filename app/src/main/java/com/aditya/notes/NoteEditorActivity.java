package com.aditya.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Locale;

public class NoteEditorActivity extends AppCompatActivity {

    //editText is responsible for editing notes
    EditText editText;

    int noteId;

    //setting up text to speech listener
    TextToSpeech mtts;
    int everythingIsOKmttsIsGoodToGo=0;

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

                //updating the notes shared preferences when we change anything in the notes Array
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.aditya.notes", Context.MODE_PRIVATE);
                //here we will use sets and not serializer because we don't want the notes to be in particular order because there is no way to change the order of things
                //converting this Arraylist notes into a hashset
                HashSet<String> hashSet = new HashSet<>(MainActivity.notes);
                //hashset is something that can be stored in the SharedPreferences
                sharedPreferences.edit().putStringSet("notes",hashSet).apply();
                MainActivity.arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //setting up text to speech engine
        mtts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    //checking if this set language method was successfull
                    int result = mtts.setLanguage(Locale.ENGLISH); //passing language to our text to speech engine if its initializaton is a success
                    if(result==TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        //if there is a missing data or language not supported by the device then we will show an error message
                        Toast.makeText(getApplicationContext(), "Either the language is not supported by your device or the input field is empty", Toast.LENGTH_LONG).show();
                    }
                    else{
                        //if there is no error and text to speech is successfully loaded then button is enabled
                        everythingIsOKmttsIsGoodToGo = 1;
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Initialization of text to speech engine failed!!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //the code below will handle our menue in the title bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //creating the menue2 in the title bar from the menue xml file
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menue2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //setting up the title menue options to respond to the user clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.SpeakNote){
           if(everythingIsOKmttsIsGoodToGo==1){
               String text = editText.getText().toString();
               mtts.setPitch(1.1f); //setting up the pitch and speed of the speech in text to speech engine
               mtts.setSpeechRate(1.1f);
               //making text to speech engine to speek our entered text
               //TextToSpeech.QUEUE_FLUSH = current txt is cancled to speak a new one
               //TextToSpeech.QUEUE_ADD the next text is spoken after the previous text is finished
               //mtts.speak(Passing the content of our editText, TextToSpeech.QUEUE_FLUSH,null);
               mtts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
           }
            return true;
        }
       return  false;
    }

    @Override
    protected void onDestroy() {
        //stopping mtts when the app is closed
        if(mtts!=null){
            mtts.stop();
            mtts.shutdown();
        }
        super.onDestroy();
    }
}