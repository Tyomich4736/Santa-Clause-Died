package by.nosevich.santaclausedied;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameActivity extends AppCompatActivity {

    public static final String RESOURCES_SPLITTER = "\r\n\\@\r\n";
    private List<String> phrases;
    private List<String> emotions;
    private int emotionCounter = 0;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);

        TextView phraseTextView = findViewById(R.id.phraseText);
        phraseTextView.setMovementMethod(new ScrollingMovementMethod());

        initGameData();
        updateEmotion();
    }

    public void onNextEmotionButtonClick(View view) {
        updateEmotion();
    }


    public void onNextPhraseButtonClick(View view) {
        Collections.shuffle(phrases);
        Collections.shuffle(emotions);
        emotionCounter = 0;
        TextView phraseTextView = findViewById(R.id.phraseText);
        String phrase = phrases.get(0);
        phraseTextView.setText(phrase);
        updateEmotion();
    }

    private void updateEmotion() {
        TextView emotionTextView = findViewById(R.id.emotionText);
        emotionTextView.setText(emotions.get(emotionCounter++));
        TextView emotionNumberText = findViewById(R.id.emotionNumberText);
        emotionNumberText.setText(Integer.toString(emotionCounter));
    }

    private void initGameData() {
        try {
            phrases = initRawResource(R.raw.phrases);
            emotions = initRawResource(R.raw.emotions);
            Collections.shuffle(phrases);
            Collections.shuffle(emotions);
        } catch (IOException e) {
            Toast.makeText(this, "Resources reading error", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
    }

    private List<String> initRawResource(int id) throws IOException {
        InputStream inputStream = getResources().openRawResource(id);
        String[] itemsArray = IOUtils.toString(inputStream, StandardCharsets.UTF_8).split(RESOURCES_SPLITTER);
        return Arrays.stream(itemsArray).filter(StringUtils::isNoneBlank).collect(Collectors.toList());
    }
}