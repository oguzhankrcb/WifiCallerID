package com.mikale.wificallerid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CallReceiver extends BroadcastReceiver {
    static int lastState = TelephonyManager.CALL_STATE_IDLE;


    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                if (getStatus(context) == 0)
                    return;
                if (state == TelephonyManager.CALL_STATE_RINGING && lastState != state){
                    System.out.println("incomingNumber : " + incomingNumber);
                    Log.d("MY_DEBUG", incomingNumber);

                    Thread Thread1 = new Thread(new Thread1(incomingNumber, context));
                    Thread1.start();
                }
                lastState = state;
            }
        },PhoneStateListener.LISTEN_CALL_STATE);
    }

    public int getStatus(Context context){
        if (LocalDataManager.getSharedPreference(context, "status", "0").equals("0"))
            return 0;
        else
            return 1;
    }

    private PrintWriter output;

    class Thread1 implements Runnable {
        String message;
        Context context;

        public Thread1(String message, Context context){
            this.message = message;
            this.context = context;
        }

        public void run() {
            Socket socket = null;
            try {
                socket = new Socket(LocalDataManager.getSharedPreference(context, "ip", ""), Integer.valueOf(LocalDataManager.getSharedPreference(context, "port", "0")));
                output = new PrintWriter(socket.getOutputStream());

                output.write("call|" + message);
                output.flush();

                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}