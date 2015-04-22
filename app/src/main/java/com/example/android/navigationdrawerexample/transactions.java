package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class transactions extends Fragment {

    private ListView mTransaction;
    transacitonListItem[] item;
    private static final String LOGIN_URL = "http://gmohammedabdulla.in/chillre/transactionData.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private ProgressDialog progress;
    TransactionListCustomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_transaction, container, false);

        mTransaction = (ListView)view.findViewById(R.id.transaction_list);

         item = new transacitonListItem [9];

        item[0]= new transacitonListItem("Abdulla","Abhishek","12");
        item[1]= new transacitonListItem("Abdulla","ysdu","12");
        item[2]= new transacitonListItem("Abdulla","Abhishek","1235");
        item[3]= new transacitonListItem("Abdulla","Abhishek","15622");
        item[4]= new transacitonListItem("Abdulla","Abhishek","212");
        item[5]= new transacitonListItem("Abdulla","Abhishek","512");
        item[6]= new transacitonListItem("Abdulla","Abhishek","12");
        item[7]= new transacitonListItem("Abdulla","Abhishek","112");
        item[8]= new transacitonListItem("Abdulla","Abhishek","1872");
        item[0]= new transacitonListItem("Abdulla","Abhishek","12");
        adapter = new TransactionListCustomAdapter(getActivity(),R.layout.transaction_list_item,item);

        mTransaction.setAdapter(adapter);


        progress= new ProgressDialog(getActivity());

        item[0]=new transacitonListItem("1","1","1");
        item[0]=new transacitonListItem("1","1","1");


//        adapter = new TransactionListCustomAdapter(getActivity(),R.layout.transaction_list_item,item);
//
  //      mTransaction.setAdapter(adapter);
        new DownloadWebPageTask().execute();


        return view;
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


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", "dummy"));

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
            Log.d("Constatnts", "backgound-" + result);
            Log.d("Constatnts", "in post execute.");

            try{
                Log.d("json","atleast here");
                    JSONArray jArray = new JSONArray(result);
                Log.d("json","this might be errror");
                JSONObject json_data=null;
                Log.d("json","json is reADY");
                for(int i=0;i<jArray.length();i++){
               // JSONObject json_data = new JSONObject(result);
                 json_data = jArray.getJSONObject(i);
                Log.d("json","before parse");
                    item[i]=new transacitonListItem(json_data.getString("sender"),json_data.getString("receiver"),json_data.getString("amount"));
              //  fd_pin=json_data.getInt(TAG_SUCCESS);
              //  fd_email=json_data.getString(TAG_MESSAGE);
                //   password=json_data.getString("password");
                //  fd_id=json_data.getInt("FOOD_ID");
                //  fd_name=json_data.getString("FOOD_NAME");
                Log.d("json","parsing done");
                  }
             //   Toast.makeText(getActivity().getBaseContext(), fd_email, Toast.LENGTH_LONG).show();
            }catch(JSONException e1){
                Toast.makeText(getActivity().getBaseContext(), "No Food Found", Toast.LENGTH_LONG).show();
            }catch (ParseException e1){
                e1.printStackTrace();
            }

           // TransactionListCustomAdapter adapter = new TransactionListCustomAdapter(getActivity(),R.layout.transaction_list_item,item);

           // mTransaction.setAdapter(adapter);

            adapter.notifyDataSetChanged();

        }
    }


}
