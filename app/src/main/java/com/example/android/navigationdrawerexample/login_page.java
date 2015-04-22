package com.example.android.navigationdrawerexample;

/**
 * Created by gabdulla on 12/30/2014.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
//import android.support.v7.app.ActionBarActivity;
//import android.support.v4.app.Action
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class login_page extends Activity {
    private EditText uid;
    private EditText pass;
    private ProgressDialog progress;
    // JSON parser class
  //  JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://gmohammedabdulla.in/chillre/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private GlobalData gdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        //passing global data to all the activities


        uid = (EditText) findViewById(R.id.editText);
        pass = (EditText) findViewById(R.id.editText2);

        progress= new ProgressDialog(this);
         gdata = (GlobalData) getApplicationContext();


    }

    public void login_pressed(View v){
        //  final GlobalData gdata = (GlobalData) getApplicationContext();
        //  gdata.setEmail(uid.getText().toString());
        Log.d("login","In botton on click method");
        gdata.setEmail(uid.getText().toString());
        new DownloadWebPageTask().execute();
        Log.d("Login","After asynctask");
    }

    public void signup_pressed(View v){
        Intent intent = new Intent(this,Signup.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    private class DownloadWebPageTask extends AsyncTask<Void, Void, String> {

        int fd_pin;
        String fd_email="",fd_mnumber="",password="";

        @Override
        protected void onPreExecute(){

            progress.setMessage("Attempting to login... ");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();

        }




        @Override
        protected String doInBackground(Void... urls) {
            int success;
            String username = uid.getText().toString();
            String password = pass.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", username));
            params.add(new BasicNameValuePair("password", password));
            ClientServerInterface ht = new ClientServerInterface();
            String result = ht.getdata(params,LOGIN_URL);
            return result;
        }

        // @Override
        protected void onPostExecute(String result) {
            progress.cancel();
            //  textView.setText(result);
            // Toast.makeText(getBaseContext(),"Done",Toast.LENGTH_SHORT).show();
            //  return;
            Log.d("Constatnts", "backgound-"+result);
            Log.d("Constatnts", "in post execute.");

            try{
                Log.d("json","atleast here");
                //    JSONArray jArray = new JSONArray(result);
           /*     Log.d("json","this might be errror");
                JSONObject json_data=null;
                Log.d("json","json is reADY");
                for(int i=0;i<jArray.length();i++){*/
                JSONObject json_data = new JSONObject(result);
                //    json_data = jArray.getJSONObject(i);
                Log.d("json","before parse");
                fd_pin=json_data.getInt(TAG_SUCCESS);
                fd_email=json_data.getString(TAG_MESSAGE);
                //   password=json_data.getString("password");
                //  fd_id=json_data.getInt("FOOD_ID");
                //  fd_name=json_data.getString("FOOD_NAME");
                Log.d("json","parsing done");
                //  }
                Toast.makeText(getBaseContext(),fd_email,Toast.LENGTH_LONG).show();
            }catch(JSONException e1){
                Toast.makeText(getBaseContext(), "No Food Found", Toast.LENGTH_LONG).show();
            }catch (ParseException e1){
                e1.printStackTrace();
            }
            if(fd_pin==1){
                Log.d("main","in intent");
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);

            }
        }
    }




}

