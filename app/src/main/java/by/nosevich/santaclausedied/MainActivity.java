package by.nosevich.santaclausedied;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String VERSION = "v0.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVersion();
    }

    public void onStartButtonClick(View view) {
        Intent myIntent = new Intent(this, GameActivity.class);
        startActivity(myIntent);
    }

    private void setVersion() {
        TextView versionTextView = findViewById(R.id.versionTextView);
        versionTextView.setText(VERSION);
    }
}