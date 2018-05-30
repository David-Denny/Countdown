package dave.countdown;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.os.AsyncTask;

import java.util.Random;

/**
 * This class covers the main screen of the app.
 *
 * It can take the user's input to generate 9 vowels/consonants. Doing so will activate a timer
 * that plays the Countdown music.
 *
 * The timer will enable the user to have 30 seconds to enter a valid anagram of the generated
 * characters. This anagram will be validated against the Oxford English Dictionary using a call to
 * their API.
 *
 * The user will be informed of the result of the API call and if it's a valid word and anagram
 * then a TextView will be updated to show inform the user and display their score.
 *
 * @author David Denny
 */
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
    int statusCode;
    EditText userInput;
    String generatedString;
    String resultString;
    Button restartButton;
    MediaPlayer mp;
    int count9;
    int count8;
    int count7;
    int count6;
    int time9;
    int time8;
    int time7;
    int time6;
    int currentTime;
    SharedPreferences preferences;
    public static final String MyPREFERENCES = "myprefs";

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

        generatedString = "";
        timerFlag = false;

        restartButton = findViewById(R.id.restartButton);
        restartButton.setVisibility(View.GONE);
        userInput = findViewById(R.id.userInput);
        userInput.setEnabled(false);

        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startAnswerCheck();
                    hideSoftKeyboard();
                    handled = true;
                }
                return handled;
            }
        });

        preferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        count9 = preferences.getInt("count9", -1);
        count8 = preferences.getInt("count8", -1);
        count7 = preferences.getInt("count7", -1);
        count6 = preferences.getInt("count6", -1);
        time9 = preferences.getInt("time9", -1);
        time8 = preferences.getInt("time8", -1);
        time7 = preferences.getInt("time7", -1);
        time6 = preferences.getInt("time6", -1);

        // if the SharedPreferences object is empty (when value is default), initialises all
        // stat variables as 0 because this must be first time start up or stats have been wiped.
        if (count9 == -1) {
            count9 = 0;
            count8 = 0;
            count7 = 0;
            count6 = 0;
            time9 = 0;
            time8 = 0;
            time7 = 0;
            time6 = 0;
        }

    }


    /**
     * Hides the soft keyboard when called
     */
    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Generates a random vowel using pseudo random number generation
     *
     * addCharacter() is then called with the argument being the random vowel that was just generated
     *
     * @param view Takes a View data type as a parameter so the method can be called onClick in xml
     */
    public void getVowel(View view) {

        // generate pseudo random vowel
        char character = vowels.charAt(rand.nextInt(vowels.length()));

        // add one to the character count
        charCount++;

        addCharacter(character);

    }

    /**
     * Generates a random consonant using pseudo random number generation
     *
     * addCharacter is then called with the argument being the consonant that was just generated
     *
     * @param view Takes a View data type as a parameter so the method can be called onClick in xml
     */
    public void getConsonant(View view) {

        // generate pseudo random consonant
        char character = consonants.charAt(rand.nextInt(consonants.length()));

        // add one to the character count
        charCount++;

        addCharacter(character);
    }

    /**
     * addCharacter takes a character as a parameter and adds it to the xml layout so the user can
     * see the outcome of their vowel/consonant pick.
     *
     * There is a switch.. case statement around the number of characters that have already been
     * added so the current character that is being processed becomes the text of the TextView that
     * has the same index in the TextView array as the charCount.
     *
     * If the charCount is greater than 9, all the characters have already been selected and so the
     * user gets informed of the error.
     *
     * When the charCount equals 9, the timer starts as long as the timerFlag is false. The
     * timerFlag prevents the timer being incorrectly restarted.
     *
     * @param character a char data type that is the character that will be added to the TextView
     *
     *
     * */
    public void addCharacter(char character) {

        String stringChar = String.valueOf(character);
        if (charCount <= 9) {

            // switch.. case statement starts at 1 because the count has 1 added to it on the
            // first click
            switch (charCount) {

                case 1:
                    textViews[0].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;

                case 2:
                    textViews[1].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;

                case 3:
                    textViews[2].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;

                case 4:
                    textViews[3].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;
                case 5:
                    textViews[4].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;
                case 6:
                    textViews[5].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;
                case 7:
                    textViews[6].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;
                case 8:
                    textViews[7].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;
                case 9:
                    textViews[8].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;
            }

            resultString = generatedString;
        }

        if (charCount > 9) {
            Toast.makeText(this, "You've picked all your characters!",
                    Toast.LENGTH_SHORT).show();

        }
        if (charCount == 9) {
            userInput.setEnabled(true);
            // start timer only if it has not already started
            if (!timerFlag) {
                timer();
            }

            timerFlag = true;
        }
    }

    /**
     * timer() starts the Countdown music and the 30 second timer.
     *
     * A TextView is updated every tick that displays the countdown to the user. Also, the variable
     * currentTime is also updated so it can be used in the user's statistics.
     *
     * When the timer finish, the displaying TextView informs the user that they're out of time and
     * the EditText that the user uses for inputting their guess is disabled and the restart button
     * is shown.
     */
    public void timer() {

        // play countdown music once timer starts
        final TextView timerTextView = findViewById(R.id.timer);
        mp = MediaPlayer.create(this, R.raw.music);
        mp.start();


        new CountDownTimer(30000, 1000) {


            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

                // update timer display
                timerTextView.setText("Seconds remaining: " + millisUntilFinished / 1000);

                // update the current time
                currentTime = (int) (30 - millisUntilFinished / 1000);

            }
            @SuppressLint("SetTextI18n")
            public void onFinish() {
                timerTextView.setText("You're out of time!");

                // disable user input EditText and show restart button
                userInput.setEnabled(false);
                restartButton.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    /**
     * This function takes a string as an argument and checks to see if that string can be made
     * from the characters that have been generated by the user. It will return a boolean depending
     * on the outcome of this check.
     *
     * @param word the string that will be validated
     * @return the boolean that is returned will be true or false depending if the argument is
     *         valid or not*/
    public boolean isValid(String word) {

        boolean valid = true;

        word = word.toLowerCase();
        generatedString = generatedString.toLowerCase();

        char[] chars = word.toCharArray();

        Character filler = Character.MIN_VALUE;

        for (char c : chars) {
            int charIndex = generatedString.indexOf(c);

            if (charIndex != -1) {
                char[] generatedChars = generatedString.toCharArray();
                generatedChars[charIndex] = filler;
                generatedString = new String(generatedChars);
            } else {
                valid = false;
            }
        }

        // resets the string so input can be validated again
        generatedString = resultString;

        return valid;
    }

    /**
     * Checks the validity of the word. If it's valid (i.e. it doesn't use any characters that
     * aren't given) then the web service is called to check if the word exists in the dictionary.
     *
     * If the word is not valid, the user is informed through a Toast.
     *
     * @param view takes a View as the parameter so the method can be used in the onClick
     *             xml attribute
     */
    public void startAnswerCheck(View view) {
        String word = userInput.getText().toString();
        if (isValid(word)) {
            new CallbackTask().execute(inflections());
        } else {
            Toast.makeText(this, "Input is not valid", Toast.LENGTH_SHORT).show();
        }

        restartButton.setVisibility(View.VISIBLE);
    }

    /**
     * {@see startAnswerCheck(View view)}
     *
     * Works the same as above, but without the View parameter so it can be used programmatically
     * as opposed to in the xml.
     *
     */
    public void startAnswerCheck() {
        String word = userInput.getText().toString();
        if (isValid(word)) {
            new CallbackTask().execute(inflections());
        } else {
            Toast.makeText(this, "Input is not valid", Toast.LENGTH_SHORT).show();
        }

        restartButton.setVisibility(View.VISIBLE);
    }

    /**
     * Generates the url to be used for the api call using the language (english) and the user's
     * word.
     *
     * @return returns a string that contains the url.
     */
    private String inflections() {
        final String language = "en";

        EditText userInput = findViewById(R.id.userInput);
        String word = userInput.getText().toString();

        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/inflections/" + language + "/" + word_id;
    }

    /**
     * Stops the Countdown music, finishes the activity, then starts it again.
     *
     * (a very lazy way to reset all variables and everything to the values that started with. This
     * is effectively just calling onCreate() again.
     *
     * @param view takes a View as an argument so the method can be called from an xml button.
     */
    public void restart(View view) {
        mp.stop();
        finish();
        startActivity(getIntent());
    }

    /**
     * Sends the user to the Stats activity
     *
     * @param view  takes a View as an argument so the method can be called from an xml button
     */
    public void sendToStats(View view) {
        Intent sendToStats = new Intent(MainActivity.this, Stats.class);
        startActivity(sendToStats);
    }


    /**
     * An AsyncTask Task(as network calls on the main thread are a big no no) that will open a url
     * connection to the Oxford Dictionary API. The call will be made with the url that is
     * generated in {@see inflections()}.
     *
     * The Task returns the status code of the webservice call.
     *
     * After the call is executed, the resulting statusCode is used to
     * call {@see useResult(int status)}
     *
     */
    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            final String app_id = "0c92f591";
            final String app_key = "af3bcd697e793cf622c612a0f8cac2f4";
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("app_id", app_id);
                urlConnection.setRequestProperty("app_key", app_key);

                statusCode = urlConnection.getResponseCode();
                Log.d("Status", String.valueOf(statusCode));

                return String.valueOf(statusCode);

            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            useResult(statusCode);
        }

    }

    /**
     * Uses a switch.. case on the status variable parameter to inform the user of the result of the
     * web service call.
     *
     * If the status code is 200 (no errors), then it means the word exists in the dictionary, so
     * the TextView that shows the result is updated with a message to the user with their score,
     * the word, etc.
     *
     * If the length of the word is above 6, the time taken and number of times they've got that
     * score are saved to variables so they can saved in SharedPreferences and used in the
     * Statistics activity.
     *
     * If the status code is 404, it means the word does not exist in the dictionary and therefore
     * doesn't count as a word. The user is informed of this through a Toast.
     *
     * The default covers any errors that might occur that don't signal that the word exists or not
     * and shows the user a generic error message.
     *
     * @param status an integer variable that contains the status code of the web service call
     */
    public void useResult(int status) {
        final TextView resultText = findViewById(R.id.result);
        final String word = userInput.getText().toString();

        switch(status) {

            case 200:

                new Thread() {
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            public void run() {

                                resultText.setText("Congratulations! You found the word '" +
                                        word +
                                        "' from the characters '" +
                                        resultString +
                                        "'. \n\nYou scored " +
                                        String.valueOf(word.length()) +
                                        " points.");
                            }
                        });
                    }
                }.start();


                switch(word.length()) {
                    case 9:
                        count9 ++;
                        if (currentTime > time9) {
                            time9 = currentTime;
                        }

                        break;

                    case 8:
                        count8 ++;
                        if (currentTime > time8) {
                            time8 = currentTime;
                        }
                        break;

                    case 7:

                        count7 ++;
                        if (currentTime > time7) {
                            time7 = currentTime;
                        }
                        break;

                    case 6:

                        count6 ++;
                        if (currentTime > time6) {
                            time6 = currentTime;
                        }
                        break;

                    default:
                        break;

                }
                break;

            case 404:

                new Thread() {
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Sorry, that's" +
                                                " not a word.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();

                break;

            default:

                new Thread() {
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Something's gone " +
                                                "wrong, try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();
        }
    }
}

