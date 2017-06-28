package com.roadtrippies.app.roadtrippies;

import android.os.Bundle;
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

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class AssistantActivity extends AppCompatActivity implements AIListener {

    private Button listenButton;
    private TextView resultTextView;
    private AIService aiService;
    private static final String TAG = "logMsg";


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

        Toast.makeText(getApplicationContext(),
                response.getResult().getFulfillment().getSpeech(),//.toString(),
                Toast.LENGTH_LONG).show();
        System.out.println(paraString);
        Parser.ParseResult(paraString);

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
