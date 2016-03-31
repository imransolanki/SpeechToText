package imran.com.speechtotext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView speechOutput;
    private ImageButton speakNowButton;
    private final int REQ_CODE_GOOGLE_SPEECH_ACTIVITY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechOutput = (TextView) findViewById(R.id.textSpeechOutput);
        speakNowButton = (ImageButton) findViewById(R.id.btnSpeak);

        speakNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));

        if (intent.resolveActivity(getPackageManager()) != null) {
            speechOutput.setText(getString(R.string.please_wait));
            startActivityForResult(intent, REQ_CODE_GOOGLE_SPEECH_ACTIVITY);
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_GOOGLE_SPEECH_ACTIVITY:
                if (resultCode == RESULT_OK && data != null) {
                    final ArrayList<String> result =
                            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechOutput.setText(result.get(0));
                    speechOutput.setSelected(true);
                }
                break;
        }
    }
}
