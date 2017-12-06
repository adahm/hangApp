package network.hangman.Client.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ForkJoinPool;

import network.hangman.Client.ClientConnect.*;
import network.hangman.R;

public class MainActivity extends AppCompatActivity {
    private TextView output;
    private PrintOut out = new PrintOut();
    private ClientConnect connect = new ClientConnect(out);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUP();
    }
    public void setUP(){
        //set up the connection to the server called on a diffrent thread to not block
        ForkJoinPool.commonPool().execute(() -> connect.connect());

        // get refrence to the diffrent ui elements
        Button start = findViewById(R.id.start);
        Button quit =  findViewById(R.id.quit);
        Button sendGuess = findViewById(R.id.sendGuess);
        EditText input =  findViewById(R.id.input);
        output = findViewById(R.id.result);

        //send start message when start button is pressed
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.send("START");
            }
        });

        //send the guess to the server by geting the text in the inputbox
        sendGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.send(input.getText().toString());
            }
        });

        //quit the app when  quit button is pressed
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAndRemoveTask();
            }
        });

        //set the text in the inputbox to none when clicked
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText("");
            }
        });
    }

    private class PrintOut implements OutObserver {
        //chnage the text to the message server sent
        @Override
        public void getServerInput(String msg){
            output.setText(msg);
        }
    }


}
