package com.dotcreek.takenote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    //private static final int VR_REQUEST = 999;

    private GestureDetector mGestureDetector;

    private static final int REQUEST_CODE = 0;
    private static final int RESULT_CODE = 0;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private ListView resultList;
    private TextView result;

    private String x = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        result = (TextView)findViewById(R.id.result);

        PackageManager packManager = getPackageManager();

        List<ResolveInfo> intActivities = packManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (intActivities.size() != 0) {
            //speech recognition is supported - detect user button clicks
            listenToSpeech();
        }
    }

    private void listenToSpeech() {

        //start the speech recognition intent passing required data
        Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        //set speech model
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        //indicate package
        //listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());

        //message to display while listening
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Guardar nota");

        //specify number of results to retrieve
        //listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);

        //start listening
        startActivityForResult(listenIntent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            Object[] mStringArray = matches.toArray();

            for(int i = 0; i < mStringArray.length ; i++){
                x += mStringArray[i].toString();
            }
            result.setText(x);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        switch (keycode){
            case KeyEvent.KEYCODE_DPAD_CENTER:
                Toast.makeText(this, "entra a tab", 50000).show();
                break;
            case KeyEvent.KEYCODE_BACK:
                Toast.makeText(this, "salir!!", 50000).show();
                this.finish();
                break;
        }
        return false;
    }

    private void saveNotes(String note) {
        try{
            FileOutputStream fos = openFileOutput("TakeNote", Context.MODE_PRIVATE);
            fos.write(note.getBytes());
            fos.close();
        }
        catch (FileNotFoundException a) {}
        catch (IOException b){}
    }
}
