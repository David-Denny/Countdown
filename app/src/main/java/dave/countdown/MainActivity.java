package dave.countdown;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int charCount;
    TextView char1;
    TextView char2;
    TextView char3;
    TextView char4;
    TextView char5;
    TextView char6;
    TextView char7;
    TextView char8;
    TextView char9;
    String vowels;
    String consonants;
    Random rand;
    TextView[] textViews;
    boolean timerFlag;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vowels = "AEIOU";
        consonants = "BCDFGHJKLMNPQRSTVWXZ";

        char1 = findViewById(R.id.char1);
        char2 = findViewById(R.id.char2);
        char3 = findViewById(R.id.char3);
        char4 = findViewById(R.id.char4);
        char5 = findViewById(R.id.char5);
        char6 = findViewById(R.id.char6);
        char7 = findViewById(R.id.char7);
        char8 = findViewById(R.id.char8);
        char9 = findViewById(R.id.char9);

        textViews = new TextView[9];
        textViews[0] = char1;
        textViews[1] = char2;
        textViews[2] = char3;
        textViews[3] = char4;
        textViews[4] = char5;
        textViews[5] = char6;
        textViews[6] = char7;
        textViews[7] = char8;
        textViews[8] = char9;

        rand = new Random();

        charCount = 0;

        timerFlag = false;

    }


    public void getVowel (View view) {

        // generate pseudo random vowel
        char character = vowels.charAt(rand.nextInt(vowels.length()));

        // add one to the character count
        charCount ++;
        Log.d("Char", String.valueOf(character));

        addCharacter(character);

  }

    public void getConsonant (View view) {

        // generate pseudo random consonant
        char character = consonants.charAt(rand.nextInt(consonants.length()));

        // add one to the character count
        charCount ++;

        Log.d("Consonant", String.valueOf(character));

        addCharacter(character);
    }

    public void addCharacter(char character) {

        String stringChar = String.valueOf(character);
        if (charCount <= 9) {

            // switch.. case statement starts at 1 because the count has 1 added to it on the
            // first click
            switch (charCount) {

                case 1:
                    textViews[0].setText(stringChar);
                    break;

                case 2:
                    textViews[1].setText(stringChar);
                    break;

                case 3:
                    textViews[2].setText(stringChar);
                    break;

                case 4:
                    textViews[3].setText(stringChar);
                    break;
                case 5:
                    textViews[4].setText(stringChar);
                    break;
                case 6:
                    textViews[5].setText(stringChar);
                    break;
                case 7:
                    textViews[6].setText(stringChar);
                    break;
                case 8:
                    textViews[7].setText(stringChar);
                    break;
                case 9:
                    textViews[8].setText(stringChar);
                    break;
            }
        } else if (charCount > 9) {
            Toast.makeText(this, "You've picked all your characters!",
                    Toast.LENGTH_SHORT).show();


            // start timer only if it has not already started
            if (!timerFlag) {
                timer();
            }

            timerFlag = true;
        }
    }

    public void timer() {

        final TextView timerTextView = findViewById(R.id.timer);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                timerTextView.setText("Seconds remaining: " + millisUntilFinished / 1000);

            }

            public void onFinish() {
                timerTextView.setText("You're out of time!");


            }
        }.start();
    }
}
