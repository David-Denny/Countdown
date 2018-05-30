package dave.countdown;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

/**
 * This class displays the user's personal statistics that they've gained while playing.
 *
 * This includes the number of times they've found a valid word of length 9, 8, 7, and 6. It also
 * shows the user the fastest times they've found words in.
 *
 * It only shows statistics for the words of score 6 and above as anything below 6 is more common
 * and isn't worth the effort to implement.
 *
 * @author David Denny
 */
public class Stats extends AppCompatActivity {

    int count9;
    int count8;
    int count7;
    int count6;
    int time9;
    int time8;
    int time7;
    int time6;
    SharedPreferences preferences;
    public static final String MyPREFERENCES = "myprefs";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stats);

        TextView score9Text = findViewById(R.id.noOf9);
        TextView score8Text = findViewById(R.id.noOf8);
        TextView score7Text = findViewById(R.id.noOf7);
        TextView score6Text = findViewById(R.id.noOf6);
        TextView time9Text = findViewById(R.id.fastest9);
        TextView time8Text = findViewById(R.id.fastest8);
        TextView time7Text = findViewById(R.id.fastest7);
        TextView time6Text = findViewById(R.id.fastest6);

        // get SharedPreferences
        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        count9 = preferences.getInt("count9", -1);
        count8 = preferences.getInt("count8", -1);
        count7 = preferences.getInt("count7", -1);
        count6 = preferences.getInt("count6", -1);
        time9 = preferences.getInt("time9", -1);
        time8 = preferences.getInt("time8", -1);
        time7 = preferences.getInt("time7", -1);
        time6 = preferences.getInt("time6", -1);


        score9Text.setText(String.valueOf(count9));
        score8Text.setText(String.valueOf(count8));
        score7Text.setText(String.valueOf(count7));
        score6Text.setText(String.valueOf(count6));

        // makes arrays of the TextViews and variables
        TextView[] textViews = {time9Text, time8Text, time7Text, time6Text};
        Integer[] timeArray = {time9, time8, time7, time6};

        int count = 0;

        // for.. each loop that sets the TextView's text to a stand in if they have no value
        // or their value.
        for (int timeVariable : timeArray) {

            if (timeVariable == -1) {
                textViews[count].setText("-");
            } else {
                textViews[count].setText(String.valueOf(timeVariable));
            }

            count ++;
        }
    }

    /**
     * Sends the user back to the main screen of the app.
     *
     * @param view takes a View as a parameter so it can be called via an xml button
     */
    public void sendToMain(View view) {

        Intent sendToMain = new Intent(Stats.this, MainActivity.class);
        startActivity(sendToMain);
    }
}
