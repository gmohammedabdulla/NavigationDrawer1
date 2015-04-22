package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ParseException;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class receive_ac extends Activity {

    NfcAdapter nadapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag mytag;
    Context ctx;
    int val;
    public GlobalData gdata ;

    EditText reason;
    EditText amount;
    ProgressDialog progress;
    String uid_;

    private static final String LOGIN_URL = "http://gmohammedabdulla.in/chillre/transaction.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_ac);
        gdata = (GlobalData) getApplicationContext();

        nadapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter tagDetected = new IntentFilter(
                NfcAdapter.ACTION_NDEF_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        IntentFilter tagTech = new IntentFilter(
                NfcAdapter.ACTION_TECH_DISCOVERED);
        tagTech.addCategory(Intent.CATEGORY_DEFAULT);
        IntentFilter tagDetect = new IntentFilter(
                NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetect.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected,tagTech,tagDetect };

        final Button button =
                (Button) findViewById(R.id.button2);

        reason = (EditText) findViewById(R.id.towards);
        amount = (EditText) findViewById(R.id.amount);

        progress= new ProgressDialog(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_receive_ac, menu);
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

    @Override
    protected void onNewIntent(Intent intent){
        //   super.onNewIntent(intent);
        dummy(intent);
    }

    public void dummy (Intent intent){
        Log.d("ONNEWINTENT", "ITS BEEN THREE DAYS< COME ON");

        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            gdata.setMytag(mytag);
            Toast.makeText(getApplicationContext(), "TAG DETECTED", Toast.LENGTH_LONG ).show();
            Log.d("activity","we are in on new intent");
        }
        Log.d("ONNEWINTENT","ITS BEEN THREE DAYS< COME ON");
       // setIntent(intent);
    }

    public void receive_pressed(View v) {


        String temp = read_data_11();
        uid_=temp;
        Log.e("reached","will call internet");
        // Toast.makeText(v.getContext(),gdata.getMytag().toString(),Toast.LENGTH_SHORT).show();
        new DownloadWebPageTask11().execute();

    }

    // public void test_pressed(View v){
    //   new DownloadWebPageTask1().execute();
    // }


    private class DownloadWebPageTask11 extends AsyncTask<Void, Void, String> {

        int fd_pin;
        String fd_email = "", fd_mnumber = "", password = "";

        @Override
        protected void onPreExecute() {

            progress.setMessage("Attempting to make transaction... ");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();

        }


        @Override
        protected String doInBackground(Void... urls) {
            int success;
            String reason_ = reason.getText().toString();
            String amount_ = amount.getText().toString();
            //  String uid_ ;
            String sender_ = gdata.getEmail();
            Log.e("sender",sender_);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("reason", reason_));
            params.add(new BasicNameValuePair("amount", amount_));
            params.add(new BasicNameValuePair("uid", uid_));
            params.add(new BasicNameValuePair("sender", sender_));
            ClientServerInterface ht = new ClientServerInterface();
            String result = ht.getdata(params, LOGIN_URL);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            progress.cancel();

            Log.d("Constatnts", "backgound-" + result);
            Log.d("Constatnts", "in post execute.");

            try {
                Log.d("json", "atleast here");
                JSONObject json_data = new JSONObject(result);
                fd_pin = json_data.getInt(TAG_SUCCESS);
                fd_email = json_data.getString(TAG_MESSAGE);

                Toast.makeText(getBaseContext(), fd_email, Toast.LENGTH_LONG).show();
            } catch (JSONException e1) {
                Toast.makeText(getBaseContext(), "something went wrong we are not sure what happened", Toast.LENGTH_LONG).show();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
         /*   if (fd_pin == 1) {
                Log.d("main", "in intent");
                Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                startActivity(intent);

            }*/
        }


    }

    public String read_data_11(){
        String temp="";
        try {
            temp = read_data_1();
        }catch(IOException e){}
        catch (FormatException e){}
        return temp;
    }

    public String read_data_1() throws IOException,FormatException {
        // Get an instance of Ndef for the tag.
        String DataInNFCLan="";
        mytag=gdata.getMytag();
        Log.e("reached","start");
        if (mytag == null) {
            Toast.makeText(this, "No tag where to read", Toast.LENGTH_LONG).show();
        }else {
            Ndef ndef = Ndef.get(mytag);

            // Enable I/O
            String DataInNfc;
            ndef.connect();
            NdefMessage msg = ndef.getNdefMessage();
            NdefRecord cardRecord = msg.getRecords()[0];
            // DataInNfc = (String)cardRecord.getPayload();
            DataInNfc = new String(cardRecord.getPayload());
            //  Toast.makeText(ctx, DataInNFCLan, Toast.LENGTH_LONG).show();

            ndef.close();

            DataInNFCLan = DataInNfc.substring(3);
            Log.e("reached","here");

        }
        return DataInNFCLan;
    }





    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }

    private void WriteModeOn(){
        writeMode = true;
        nadapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    private void WriteModeOff(){
        writeMode = false;
        nadapter.disableForegroundDispatch(this);
    }


}
