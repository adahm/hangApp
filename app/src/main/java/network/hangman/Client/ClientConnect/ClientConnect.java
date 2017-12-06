package network.hangman.Client.ClientConnect;

import android.os.AsyncTask;

import java.io.*;
import java.net.Socket;

/**
 * Created by Andreas on 2017-12-06.
 */

public class ClientConnect{
    private Socket client;
    private PrintWriter output;
    private BufferedReader input;
    private OutObserver out;
    //consturctor take a outobserver object used to change the view based on the message server has sent
    public ClientConnect(OutObserver out){
        this.out = out;
    }
    public void connect(){ //set up the connection and the input and output streams
        try {
            client = new Socket("10.0.2.2", 20000);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void send(String guess){ //create a new asyncTask to send a guess
        new SendMsg().execute(guess);
    }
    private class SendMsg extends AsyncTask <String,Void,String>{
        @Override
        protected String doInBackground(String... params){
            String msg = "";
            //send the message
            output.println(params[0]);
            try {
                //recive the reply
                msg = input.readLine();

            }catch (IOException e){
                e.printStackTrace();
            }
            return msg;
        }
        @Override
        protected void onPostExecute(String msg){
            //on the main thread change the view to the reply
            out.getServerInput(msg);
        }
    }


}
