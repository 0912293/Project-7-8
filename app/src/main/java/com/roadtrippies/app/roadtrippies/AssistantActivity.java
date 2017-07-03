package com.roadtrippies.app.roadtrippies;
import android.content.Intent;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static android.R.id.empty;

public class AssistantActivity extends AppCompatActivity implements AIListener {

    private Button listenButton;
    private TextView resultTextView;
    private AIService aiService;
    private static final String TAG = "logMsg";
    private ArrayList list = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.RECORD_AUDIO},3);

        listenButton = (Button) findViewById(R.id.btn_listen);
        resultTextView = (TextView) findViewById(R.id.result_text);

        final AIConfiguration config = new AIConfiguration("88d81a5adb4a4a79811d201a001317d9",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        listenButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        aiService.startListening();
                        System.out.println("Hello");
                    }
                }
        );
    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();
        //Get parameters
        
        String paraString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                paraString += "(" + entry.getKey() + ", "  + entry.getValue() + " ) ";
            }
        }

        // Show results in TextView.
        resultTextView.setText(response.getResult().getFulfillment().getSpeech()+//.toString()+
                "\n\n\nQuery:" + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nParameters: " + paraString);

        final HashMap<String, JsonElement> params = result.getParameters();
        Intent intent = new Intent(getBaseContext(), ScrollingActivity.class);

        if (params != null && !params.isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                if(entry.getValue().toString().length()>3) {
                    if (entry.getKey().equals("genre")) {
                        Log.d("Debug", entry.getValue().toString());
                        String genre = entry.getValue().toString().toLowerCase();
                        if (!genre.contains(",")){
                            genre = genre.substring(2, genre.length() - 2); //makes a substring from the genre substring
                            Log.d("Debug", genre);                          //send genre string to where you need it
                            list.add(genre);
                        }else{
                            getGenre(genre);
                        }
                        for (int i=0;i<list.size();i++){
                            Log.d("Debug","List item "+i+": "+list.get(i).toString());
                        }
                        //#TODO Save list in globalclass and clear list


                    } else if (entry.getKey().equals("distance")) {
                        String amount = entry.getValue().toString();
                        amount = amount.substring(10, amount.indexOf(",")); //makes a substring from the amount part
                        Log.d("Debug", amount);                         //send amount string to where you need it
                        String unit = entry.getValue().toString().toLowerCase();
                        unit = unit.substring(unit.indexOf(",") + 9,unit.length()-2);   //makes a substring from the unit part
                        Log.d("Debug", unit);                           //send unit string to where you need it
                        if(!unit.isEmpty() && !list.isEmpty()){

                            intent.putStringArrayListExtra("list", list);

                            startActivity(intent);
                        }
                    }
                }
            }
        }
        Toast.makeText(getApplicationContext(),
                response.getResult().getFulfillment().getSpeech(),//.toString(),
                Toast.LENGTH_LONG).show();
        //System.out.println(paraString);
        //Parser.ParseResult(paraString);

    }

    public List getGenre(String entry){
        if(entry.contains(",")){
            String sub = entry;
            sub = sub.substring(2,entry.indexOf(",")-1);
            list.add(sub);
            entry = entry.substring(entry.indexOf(",")+2);
            Log.d("Debug","sub:"+sub);
            Log.d("Debug","entry:"+entry);
            getGenre(entry);
        }else{
            entry = entry.substring(0,entry.length()-2);
            list.add(entry);
        }
        Log.d("Debug",list.toString());
        return list;
    }

    @Override
    public void onError(AIError error) {
        Log.i(TAG, "Error");
        resultTextView.setText(error.toString());
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {
        Log.i(TAG, "Listening");
        resultTextView.setText("Listening");
    }

    @Override
    public void onListeningCanceled() {
        Log.i(TAG, "Listening cancelled.");
    }

    @Override
    public void onListeningFinished() {
        Log.i(TAG, "Listening finished");
        resultTextView.setText("Processing");
    }
}
