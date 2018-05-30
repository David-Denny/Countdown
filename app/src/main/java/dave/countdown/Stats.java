package dave.countdown;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
//        score8Text.setText(count8);
//        score7Text.setText(count7);
//        score6Text.setText(count6);
//
//        time9Text.setText(time9);
//        time8Text.setText(time8);
//        time7Text.setText(time7);
//        time6Text.setText(time6);

    }

    public void sendToMain(View view) {

        Intent sendToMain = new Intent(Stats.this, MainActivity.class);
        startActivity(sendToMain);
    }
}
