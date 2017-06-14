package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/";
    final String KEY_ID = "8a3b4394ba811a9e2b0bbf3cc56888d053ea21909299b2703cdc35e156c860ff";

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String api_url;
                Log.d("BitCoin", "" +parent.getItemAtPosition(position)+ " Selected");
                api_url = BASE_URL+"BTC"+parent.getItemAtPosition(position);
                Log.d("BitCoin", api_url);
                letsDoSomeNetworking(api_url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Log.d("BitCoin", "Nothing Selected");
            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public  void onStart()
            {
                //start
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("BitCoin", "JSON: " + response.toString());
                updateUI(response);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("BitCoin", "Request fail! Status code: " + statusCode);
                Log.d("BitCoin", "Fail response: " + response);
                Log.e("BitCoin", e.toString());
            }
        });


    }
            public void updateUI(JSONObject jsonObject)
            {
                try {
                    mPriceTextView.setText(jsonObject.getString("ask"));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Log.e("BitCoin", e.toString());
                }
            }

}
