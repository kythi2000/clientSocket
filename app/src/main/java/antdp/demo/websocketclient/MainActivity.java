package antdp.demo.websocketclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private static TextView txtMsgReceived;
    private EditText edtMsg;
    private static final String IP_ADDRESS = "192.168.178.24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMsgReceived = findViewById(R.id.txtMsgReceived);
        edtMsg = findViewById(R.id.editMessage);
    }

    public void clickToSend(View view) {
        MyClientTask client = new MyClientTask(IP_ADDRESS, Integer.parseInt(edtMsg.getText().toString()));
        client.execute();
    }

    private class MyClientTask extends AsyncTask<Void, Void, Void>{
        private String dstAddress;
        private int dstPort;
        private String response = "";

        public MyClientTask(String address, int port) {
            this.dstAddress = address;
            this.dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Socket socket = null;
            try {
                socket = new Socket(dstAddress, dstPort);
                ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int byteRead;
                InputStream is = socket.getInputStream();
                while ((byteRead = is.read(buffer)) != -1){
                    baos.write(buffer, 0 , byteRead);
                    response += baos.toString("UTF-8");
                }

            }catch (UnknownHostException e){
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            }catch (IOException e){
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally {
                if(socket != null){
                    try{
                        socket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            txtMsgReceived.setText(response);
            super.onPostExecute(result);
        }
    }

}

