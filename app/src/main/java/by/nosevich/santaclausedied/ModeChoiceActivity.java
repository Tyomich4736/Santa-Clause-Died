package by.nosevich.santaclausedied;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ModeChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_choice);
    }

    public void onSingleModeButtonClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void onPairModeButtonClick(View view) {
        Intent intent = new Intent(this, PairGameActivity.class);
        startActivity(intent);
    }
}