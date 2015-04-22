package com.example.android.navigationdrawerexample;

import android.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabdulla on 1/19/2015.
 */
public class home extends Fragment {

    TextView text1;
    GlobalData gdata;

    EditText reason;
    EditText amount;
    ProgressDialog progress;

    NfcAdapter adapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag mytag;
    Context ctx;
    int val;
    String uid_;


    private static final String LOGIN_URL = "http://gmohammedabdulla.in/chillre/transaction.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    private static TextView textview;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
    //    text1=(TextView)view.findViewById(R.id.gdata_add);

        gdata=(GlobalData) getActivity().getApplicationContext();


     //   text1.setText(gdata.getEmail());

          textview = (TextView) view.findViewById(R.id.textView7);
          textview.setText(gdata.getEmail());

        ctx = view.getContext();

        progress= new ProgressDialog(getActivity());


        adapter = NfcAdapter.getDefaultAdapter(ctx);
        pendingIntent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };

        //   textview = (TextView) view.findViewById(R.id.textView1);

        final Button button =
                (Button) view.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                receive_main(v);
            }
        });



    //    reason = (EditText) view.findViewById(R.id.towards);
    //    amount = (EditText) view.findViewById(R.id.amount);


        return view;
    }

    public void  receive_main (View v){
      ////  MainActivity m1 = new MainActivity();
      //  m1.receive_wrapper();

        Intent intent = new Intent(getActivity(), receive_ac.class);
        startActivity(intent);
    }

/*
    public void receive_pressed(View v) {


        String temp = read_data_11();
        uid_=temp;

        // Toast.makeText(v.getContext(),gdata.getMytag().toString(),Toast.LENGTH_SHORT).show();
          new DownloadWebPageTask1().execute();

    }
*/
   // public void test_pressed(View v){
     //   new DownloadWebPageTask1().execute();
   // }





    private class DownloadWebPageTask1 extends AsyncTask<Void, Void, String> {

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

                Toast.makeText(getActivity().getBaseContext(), fd_email, Toast.LENGTH_LONG).show();
            } catch (JSONException e1) {
                Toast.makeText(getActivity().getBaseContext(), "something went wrong we are not sure what happened", Toast.LENGTH_LONG).show();
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
        if (mytag == null) {
            Toast.makeText(getActivity(), "No tag where to read", Toast.LENGTH_LONG).show();
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
        adapter.enableForegroundDispatch(getActivity(), pendingIntent, writeTagFilters, null);
    }

    private void WriteModeOff(){
        writeMode = false;
        adapter.disableForegroundDispatch(getActivity());
    }




}

