package by.nosevich.santaclausedied.easteregg.impl;

import android.content.Context;
import android.widget.Toast;
import by.nosevich.santaclausedied.easteregg.EasterEgg;

public class SendingYourDataEasterEgg implements EasterEgg {

    private static final String MESSAGE = "Sending your personal data and photos to my PC.\nDon't close the app...";

    private static final SendingYourDataEasterEgg instance = new SendingYourDataEasterEgg();

    private SendingYourDataEasterEgg() {}

    public static SendingYourDataEasterEgg getInstance() {
        return instance;
    }

    @Override
    public void activate(Context context) {
        Toast.makeText(context, MESSAGE, Toast.LENGTH_LONG).show();
    }
}
