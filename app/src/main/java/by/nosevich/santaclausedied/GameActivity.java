package by.nosevich.santaclausedied;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import by.nosevich.santaclausedied.dto.PhraseDto;
import by.nosevich.santaclausedied.dto.PhraseTag;
import by.nosevich.santaclausedied.dto.Setting;
import by.nosevich.santaclausedied.easteregg.impl.SendingYourDataEasterEgg;
import by.nosevich.santaclausedied.service.SettingsService;
import by.nosevich.santaclausedied.util.PhrasesParsingUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameActivity extends AppCompatActivity {

    public static final String RESOURCES_SPLITTER = "\r\n@\r\n";

    private SettingsService settingsService;

    private List<String> phrases;
    private List<String> emotions;
    private int emotionCounter = 0;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);
        settingsService = new SettingsService(this);

        TextView phraseTextView = findViewById(R.id.phraseText);
        phraseTextView.setMovementMethod(new ScrollingMovementMethod());

        initGameData();
        updateEmotion();
    }

    public void onNextEmotionButtonClick(View view) {
        if (emotionCounter != emotions.size()) {
            updateEmotion();
        } else {
            SendingYourDataEasterEgg.getInstance().activate(this);
            emotionCounter = 0;
        }
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
            phrases = initPhrases();
            emotions = initEmotions();
            Collections.shuffle(phrases);
            Collections.shuffle(emotions);
        } catch (IOException e) {
            Toast.makeText(this, "Resources reading error", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
    }

    private List<String> initPhrases() throws IOException {
        InputStream inputStream = getResources().openRawResource(R.raw.phrases);
        String phrasesStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        Stream<PhraseDto> phrasesStream = PhrasesParsingUtil.parsePhrases(phrasesStr).stream();
        if (settingsService.isSettingEnabled(Setting.HIDE_DIALOG_PHRASES)) {
            phrasesStream = phrasesStream.filter(phrase -> !phrase.getTags().contains(PhraseTag.DIALOG));
        }
        return phrasesStream.map(PhraseDto::getText).collect(Collectors.toList());
    }

    private List<String> initEmotions() throws IOException {
        InputStream inputStream = getResources().openRawResource(R.raw.emotions);
        String[] itemsArray = IOUtils.toString(inputStream, StandardCharsets.UTF_8).split(RESOURCES_SPLITTER);
        return Arrays.stream(itemsArray).filter(StringUtils::isNoneBlank).collect(Collectors.toList());
    }
}