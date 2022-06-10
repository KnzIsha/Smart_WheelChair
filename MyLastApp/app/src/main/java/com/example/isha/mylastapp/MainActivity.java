package com.example.isha.mylastapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothSocket btSocket = null;
    ImageView up, down, left, right, stop, mike;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private TextToSpeech mytts;
    private SpeechRecognizer myspechrecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        up = findViewById(R.id.uparrow);
        down = findViewById(R.id.downicon);
        left = findViewById(R.id.lefticon);
        right = findViewById(R.id.righticon);
        stop = findViewById(R.id.stopicon);
        mike = findViewById(R.id.mike);


        up.setOnClickListener(this);
        down.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        stop.setOnClickListener(this);


        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println(btAdapter.getBondedDevices());
        BluetoothDevice hc05 = btAdapter.getRemoteDevice("98:D3:21:F7:3D:64");
        //  System.out.println(hc05.getName());

        int counter = 0;
        do {
            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSocket);
                btSocket.connect();
                System.out.println("its connected !!");
                System.out.println(btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        } while (!btSocket.isConnected() && counter < 3);
        if (!btSocket.isConnected())
            Toast.makeText(MainActivity.this, "YOU MUST CONNECT WITH THE CHAIR", Toast.LENGTH_SHORT).show();


        mike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.RECORD_AUDIO)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    speak("GIVE ME DIRECTION");
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                    myspechrecognizer.startListening(intent);
                }
            }

        });

        initializetexttospeech();
        initializespeechrecognizer();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uparrow:
                sendmsg(1);
                break;
            case R.id.downicon:
                sendmsg(2);
                break;
            case R.id.lefticon:
                sendmsg(3);
                break;
            case R.id.righticon:
                sendmsg(4);
                break;
            case R.id.stopicon:
                sendmsg(5);
                break;

        }
     /*  InputStream inputStream = null;

        try {
            StringBuffer str=null;
            inputStream = btSocket.getInputStream();
           // System.out.println(inputStream.available()+"  are avilable before");
            //inputStream.skip(inputStream.available());
         // inputStream.skip(inputStream.available());
          //  System.out.println(inputStream.available()+"  are avilable after");
         DataInputStream dataInputStream=new DataInputStream(inputStream);
           int count=inputStream.available();
           System.out.println("avilable bytes are  "+count);
           byte[] bs=new byte[count];
           dataInputStream.read(bs);
                for(byte b:bs)
                {
                    char ca = (char) b;
                    System.out.print(ca+" ");
                }
            inputStream.skip(inputStream.available());



                }
        catch (IOException e) {
            e.printStackTrace();
        }
        */


    }


    public void sendmsg(int i) {

        OutputStream outputStream = null;


        try {
            outputStream = btSocket.getOutputStream();

            if (i == 1)//forward
            {
                outputStream.write(49);


            } else if (i == 2)//backward
            {
                outputStream.write(50);

            } else if (i == 3)//left
            {
                outputStream.write(51);

            } else if (i == 4)//right
            {
                outputStream.write(52);
            } else if (i == 5)//stop
            {
                outputStream.write(53);

            }
            else
                outputStream.write(48);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void initializespeechrecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            myspechrecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            myspechrecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    processresult(results.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }

    }

    public void processresult(String command) {
        command = command.toLowerCase();
        if (command.indexOf("forward") != -1) {

            sendmsg(1);
            speak("GOING FORWARD ");

        } else if (command.indexOf("backward") != -1) {
            sendmsg(2);

            speak("GOING BACKWARD ");

        } else if (command.indexOf("left") != -1) {
            sendmsg(3);

            speak("GOING left ");

        } else if (command.indexOf("right") != -1) {
            sendmsg(4);

            speak("GOING right ");

        } else if (command.indexOf("stop") != -1) {
            sendmsg(5);

            speak("STOPPing");

        } else {
            speak("I CANT UNDERSTAND");
        }

    }

    public void initializetexttospeech() {
        mytts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (mytts.getEngines().size() == 0) {

                    Toast.makeText(MainActivity.this, "no engine", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    mytts.setLanguage(Locale.US);
                    speak("THE APP IS READY");
                }

            }
        });
    }

    public void speak(String message) {
        if (Build.VERSION.SDK_INT >= 21) {
            mytts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            mytts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mytts.shutdown();
    }

    @Override
    protected void onResume() {
        super.onResume();
        blueconnection();
//        Reinitialize the recognizer and tts engines upon resuming from background such as after openning the browser
        initializespeechrecognizer();
        initializetexttospeech();

    }

    public void blueconnection()
    {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println(btAdapter.getBondedDevices());
        BluetoothDevice hc05 = btAdapter.getRemoteDevice("98:D3:21:F7:3D:64");
        //  System.out.println(hc05.getName());

        int counter = 0;
        do {
            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSocket);
                btSocket.connect();
                System.out.println("its connected !!");
                System.out.println(btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        } while (!btSocket.isConnected() && counter < 3);
        if (!btSocket.isConnected())
            Toast.makeText(MainActivity.this, "YOU MUST CONNECT WITH THE CHAIR", Toast.LENGTH_SHORT).show();


    }

}






