package saugat.chetry.com.currencyconverter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity implements View.OnClickListener,Communicator {

    SharedPreferences pref;
    EditText et_userInput;
    TextView tv_convertedValue;
    Button btn_sourceCurrency,btn_targetCurrency,btn_swapCurrency,btn_convertCurrency;
    FragmentManager fm;
    Double usd,inr,gbp,eur,inputValue;
    CurrencyFragment cf;
    String sourceCurrency = "USD";
    String targetCurrency = "INR";
    String url = "https://openexchangerates.org/api/latest.json?app_id=f62c5d4e009f49e0b3ea91c2c3801cd6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getFragmentManager();
        cf = new CurrencyFragment();

        pref = getSharedPreferences("Data",MODE_PRIVATE);

        readDataFromSharedPreference();

        btn_sourceCurrency = (Button) findViewById(R.id.btnSourceCurrency);
        btn_targetCurrency = (Button) findViewById(R.id.btnTargetCurrency);
        btn_swapCurrency = (Button) findViewById(R.id.btnSwapCurrency);
        btn_convertCurrency = (Button) findViewById(R.id.btnConvertCurrency);
        et_userInput = (EditText) findViewById(R.id.inputValue);
        tv_convertedValue = (TextView) findViewById(R.id.convertedValue);


        btn_sourceCurrency.setOnClickListener(this);
        btn_targetCurrency.setOnClickListener(this);
        btn_convertCurrency.setOnClickListener(this);
        btn_swapCurrency.setOnClickListener(this);
    }


    public void readDataFromSharedPreference()
    {
        usd = Double.parseDouble(pref.getString("USD","1"));
        inr = Double.parseDouble(pref.getString("INR","61"));
        eur = Double.parseDouble(pref.getString("EUR","0.90"));
        gbp = Double.parseDouble(pref.getString("GBP","0.64"));

        Log.d("Saved Value","USD "+usd.toString());
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

        if(id == R.id.action_refresh)
        {
            RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
            StringRequest request = new StringRequest(Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject ratesObject = jsonObject.getJSONObject("rates");

                                    usd = ratesObject.getDouble("USD");
                                    inr = ratesObject.getDouble("INR");
                                    gbp = ratesObject.getDouble("GBP");
                                    eur = ratesObject.getDouble("EUR");

                                    saveToSharedPrefs();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            //Log.d("Response",response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error",error.toString());
                }
            });

            requestQueue.add(request);
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveToSharedPrefs()
    {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("USD",usd.toString());
        editor.putString("GBP", gbp.toString());
        editor.putString("INR",inr.toString());
        editor.putString("EUR",eur.toString());
        editor.commit();
    }
    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {

            case R.id.btnSourceCurrency:

                cf.show(fm,"Source");


                break;

            case R.id.btnTargetCurrency:
                cf.show(fm, "Target");

                break;
            case R.id.btnSwapCurrency:

                sourceCurrency = btn_sourceCurrency.getText().toString();
                targetCurrency = btn_targetCurrency.getText().toString();
                swapCurrencies(sourceCurrency,targetCurrency);
                break;
            case R.id.btnConvertCurrency:
                if(et_userInput.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Enter An Amount",Toast.LENGTH_LONG).show();
                }
                else
                {
                    inputValue = Double.parseDouble(et_userInput.getText().toString());
                    sourceCurrency = btn_sourceCurrency.getText().toString();
                    targetCurrency = btn_targetCurrency.getText().toString();
                    Double value = calculate(inputValue, sourceCurrency, targetCurrency);
                    String answer = value.toString();
                    tv_convertedValue.setText(answer);
                }

                break;
        }

    }

    public Double calculate(Double n,String s, String t)
    {
        Double src = 0.0,tgt = 0.0;
        Double val = 0.0;
        switch(s)
        {
            case "USD" :
                            src = usd;
                            break;

            case "INR" :
                            src = inr;
                            break;

            case "EUR" :
                            src = eur;
                            break;
            case "GBP" :
                            src = gbp;
                            break;
            default:
                            break;


        }

        switch(t)
        {
            case "USD" :
                            tgt = usd;
                            break;

            case "INR" :
                            tgt = inr;
                            break;

            case "EUR" :
                            tgt = eur;
                            break;
            case "GBP" :
                            tgt = gbp;
                            break;
            default:
                            break;


        }

        val = (1/src)*tgt*n;

        return val;
    }


    public void swapCurrencies(String source,String target)
    {
        btn_sourceCurrency.setText(target);
        btn_targetCurrency.setText(source);
    }
    @Override
    public void respond(String d,String tag) {

        if(tag == "Source")
        {
            btn_sourceCurrency.setText(d);
        }

        else if(tag == "Target")
        {
            btn_targetCurrency.setText(d);
        }


    }
}
