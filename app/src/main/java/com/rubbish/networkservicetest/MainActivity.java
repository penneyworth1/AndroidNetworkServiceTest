package com.rubbish.networkservicetest;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MainActivity extends ActionBarActivity {

    private String mServiceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SocketServerThread socketServerThread = new SocketServerThread();
        socketServerThread.start();

        NsdManager.RegistrationListener mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                // Save the service name.  Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                mServiceName = NsdServiceInfo.getServiceName();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed!  Put debugging code here to determine why.
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                // Service has been unregistered.  This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed.  Put debugging code here to determine why.
            }

        };

        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setServiceName("AndroidService");
        serviceInfo.setServiceType("_witap8._tcp.");
        serviceInfo.setPort(1234);

        NsdManager mNsdManager = (NsdManager)this.getSystemService(Context.NSD_SERVICE);

        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);



    }

    private class SocketServerThread extends Thread {

        private void pollInputStream(DataInputStream dataInputStream)
        {
            try
            {
                while (true)
                {
                    int aByte = dataInputStream.read();
                    Log.i("asdf",Integer.toString(aByte));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void run()
        {
            String TAG = "crap";

            Socket socket = null;
            DataInputStream dataInputStream = null;
            DataOutputStream dataOutputStream = null;

            try {
                Log.i(TAG, "Creating server socket");
                ServerSocket serverSocket = new ServerSocket(1234);

                while (true) {
                    socket = serverSocket.accept();
                    dataInputStream = new DataInputStream(
                            socket.getInputStream());
                    dataOutputStream = new DataOutputStream(
                            socket.getOutputStream());

                    pollInputStream(dataInputStream);

                    //String messageFromClient, messageToClient, request;
                    //If no message sent from client, this code will block the program
                    //messageFromClient = dataInputStream.readUTF();
                    //Log.i(TAG,messageFromClient);

                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (socket != null)
                {
                    try
                    {
                        socket.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null)
                {
                    try
                    {
                        dataInputStream.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null)
                {
                    try
                    {
                        dataOutputStream.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

        }

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
