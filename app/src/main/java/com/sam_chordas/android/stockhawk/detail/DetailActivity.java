package com.sam_chordas.android.stockhawk.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.data.DateHigh;
import com.sam_chordas.android.stockhawk.data.DateHighMain;
import com.sam_chordas.android.stockhawk.data.Quote;
import com.sam_chordas.android.stockhawk.data.QuoteInfo;
import com.sam_chordas.android.stockhawk.service.QuoteService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity implements Callback<QuoteInfo> {

    public static final String SYMBOL = "symbol";
    public static final String HTTPS_QUERY_YAHOOAPIS_COM_V1 = "https://query.yahooapis.com/v1/";
    public static final String SELECT_FROM_YAHOO_FINANCE_HISTORICALDATA_WHERE_SYMBOL = "select * from yahoo.finance.historicaldata where symbol = \"";
    public static final String AND_START_DATE = "\" and startDate = \"";
    public static final String AND_END_DATE = "\" and endDate = \"";
    public static final String SORT_FIELD_START_DATE_REVERSE = "\" |  sort(field=\"startDate\")  | reverse() ";
    public static final String TRUE = "true";
    public static final String STORE_DATATABLES_ORG_ALLTABLESWITHKEYS = "store://datatables.org/alltableswithkeys";
    public static final String JSON = "json";
    public static final String WEEK = "1 Week";
    public static final String MONTH = "1 Month";
    public static final String MONTHS = "6 Months";
    public static final String YEAR = "1 Year";
    public static final String SELECT_THE_STOCK_HISTORY_RANGE = "select the stock history range";
    private TextView textView;
    private String result;
    private ProgressBar pb;
    public static String symbol;

    DateHighMain dhm = new DateHighMain();

    public ArrayList<Entry> valueSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        symbol = getIntent().getExtras().getString(SYMBOL);
        pb = (ProgressBar)findViewById(R.id.progressBar);
        getSupportActionBar().setTitle(symbol);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void callRetrofitFetch(String symbol, String startDate, String endDate) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HTTPS_QUERY_YAHOOAPIS_COM_V1)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call in Retrofit 2.0
        QuoteService stackOverflowAPI = retrofit.create(QuoteService.class);
        //Make sure we sort it based on correct date. Use sort
         String q = SELECT_FROM_YAHOO_FINANCE_HISTORICALDATA_WHERE_SYMBOL +symbol+ AND_START_DATE +endDate+ AND_END_DATE +startDate+ SORT_FIELD_START_DATE_REVERSE;
        String diagnostics = TRUE;
        String env = STORE_DATATABLES_ORG_ALLTABLESWITHKEYS;
        String format = JSON;
        Call<QuoteInfo> call = stackOverflowAPI.getObjectWithNestedArraysAndObject(q,diagnostics,env,format);

        //asynchronous call
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<QuoteInfo> call, Response<QuoteInfo> response) {
        pb.setVisibility(View.GONE);
        QuoteInfo quoteInfo = response.body();
        //Storing the response if any

        valueSet = new ArrayList<>();
        ArrayList<Quote> quoteArray = quoteInfo.query.results.quote;
        int i;
        dhm.getDatehigh().clear();
        for (i=0;i<quoteArray.size();i++
             ) {
            DateHigh dh = new DateHigh();
            dh.setQuoteDate(quoteArray.get(i).quote_date);
            dh.setQuoteHighValue(quoteArray.get(i).high);
            dhm.getDatehigh().add(dh);
        }

        Fragment fragment = LineChartFragment.newInstance(dhm);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fg, fragment)
                .commit();
    }

    @Override
    public void onFailure(Call<QuoteInfo> call, Throwable t) {
        Toast.makeText(DetailActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_activity, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayList<String> list = new ArrayList<String>();
        list.add(getString(R.string.one_week));
        list.add(getString(R.string.one_month));
        list.add(getString(R.string.six_month));
        list.add(getString(R.string.one_year));
        CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(
                getApplicationContext(), list);
        spinner.setAdapter(spinAdapter); // set the adapter to provide layout of rows and content
         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String item = adapter.getItemAtPosition(position).toString();
                String startDate = Utils.getFormattedDate(System.currentTimeMillis());
                Date date = new Date();
                switch (item)
                {
                    case WEEK:
                        callRetrofitFetch(symbol,startDate,Utils.getBackwardDate(date, Calendar.DATE, -7));
                        break;
                    case MONTH:
                        callRetrofitFetch(symbol,startDate,Utils.getBackwardDate(date, Calendar.MONTH, -1));
                        break;
                    case MONTHS:
                        callRetrofitFetch(symbol,startDate,Utils.getBackwardDate(date, Calendar.MONTH, -6));
                        break;
                    case YEAR:
                        callRetrofitFetch(symbol,startDate,Utils.getBackwardDate(date, Calendar.YEAR, -1));
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(DetailActivity.this, SELECT_THE_STOCK_HISTORY_RANGE,Toast.LENGTH_SHORT).show();

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
