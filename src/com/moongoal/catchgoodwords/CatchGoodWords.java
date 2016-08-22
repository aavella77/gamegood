/* 
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package com.moongoal.catchgoodwords;
 
import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;
import com.moongoal.catchgoodwords.R;

 
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.*;
 
/**
 * Sample code that invokes the speech recognition intent API.
 */
public class CatchGoodWords extends Activity implements OnClickListener {
 
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private static final String TAG = "CatchGoodWords";
    int score = 0;
    Button speakButton, skipButton;
    TextView goodWord, currentScore;
    
    
    String[][] myWords = { 
    		{"friendship", "1"},
    		{"love", "1"}, 
    		{"cheerful", "1"}, 
    		{"elated", "1"}, 
    		{"bitter", "0"},
    		{"empty", "0"}, 
    		{"anxious", "0"},
    		{"serene", "1"}, 
    		{"regret", "0"}, 
    		{"angry", "0"}, 
    		{"hope", "1"},
    		{"successful", "1"}, 
    		{"calm", "1"}, 
    		{"admiration", "1"}, 
    		{"bliss", "1"}, 
    		{"scared", "0"},
    		{"panic", "0"}, 
    		{"worried", "0"}, 
    		{"offended", "0"}, 
    		{"joyous", "1"}, 
    		{"upset", "0"},
    		{"desire", "1"}, 
    		{"jealous", "0"}, 
    		{"triumph", "1"},
    		{"tender", "1"}, 
    		{"jubilant", "1"},
    		{"hostile", "0"}, 
    		{"cheerful", "1"},
    		{"energetic", "1"},
    		{"eager", "1"}, 
    		{"excited", "1"},
    		{"hurt",  "0"}, 
    		{"joy", "1"}, 
    		{"enthusiastic", "1"},
    		{"playful", "1"}, 
    		{"proud", "1"}, 
    		{"delighted", "1"}, 
    		{"amused", "1"}, 
    		{"tearful", "0"},
    		{"pessimistic", "0"},
    		{"pleasure", "1"},
    		{"peace", "1"}, 
    		{"brave", "1"},
    		{"engaged", "1"}, 
    		{"envy", "0"},
    		{"hopeless", "0"}, 
    		{"hope", "1"}		
    		};
    
 
    private ListView mList;
 
    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.voice_recognition);
 
        // Get display items for later interaction
        speakButton = (Button) findViewById(R.id.btn_speak);
        skipButton = (Button) findViewById(R.id.skipButton);
     
 
        mList = (ListView) findViewById(R.id.list);
 
        AdManager.setTestDevices( new String[] {
        		AdManager.TEST_EMULATOR, // Android emulator
        		"E83D20734F72FB3108F104ABC0FFC738", // My T-Mobile G1 Test Phone
        		} );
        
        AdView adView = (AdView)findViewById(R.id.ad);
        adView.requestFreshAd();
        
        // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            speakButton.setOnClickListener(this);
            skipButton.setOnClickListener(this);
        } else {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }
        
        
        goodWord = (TextView) findViewById(R.id.goodWordText);
        goodWord.setText("Love");
        
        currentScore = (TextView) findViewById(R.id.currentScore);
        currentScore.setText("0");

    }
 
    /**
     * Handle the click on the start recognition button.
     */
    
    @Override
    public void onClick(View v) {
       
		switch(v.getId()){
		case R.id.btn_speak:
			    startVoiceRecognitionActivity();
			    break;
		case R.id.skipButton:
                Random generator = new Random();
                int randomIndex = generator.nextInt( 46 );
                goodWord.setText(myWords[randomIndex][0]);
			    break;
  	     }
        
    }
 
    /**
     * Fire an intent to start the speech recognition activity.
     */
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }
 
    /**
     * Handle the results from the recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
          
            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    matches));
            
            TextView goodWord = (TextView) findViewById(R.id.goodWordText);
            TextView currentScore = (TextView) findViewById(R.id.currentScore);
            
        
 
            for (int i=0; i < 46; i++){

            String firstResult = null != matches && !matches.isEmpty() ? 
            			matches.get(0) : null;
            
            Log.d(TAG,"i=" +i );
            Log.e(TAG, firstResult);
            Log.e(TAG, myWords[i][0] );
            			
                if ( (firstResult.equals(myWords[i][0]) ) && (myWords[i][1] == "1") ) {
                	score = score + 300;
                    Log.d(TAG,"My Score=" +score );
                }
            }   
            
            
                	
            
            currentScore.setText(String.valueOf(score));
            
            Random generator = new Random();
            int randomIndex = generator.nextInt( 46 );
            goodWord.setText(myWords[randomIndex][0]);
            
        }
 
        super.onActivityResult(requestCode, resultCode, data);
    }
    

}
