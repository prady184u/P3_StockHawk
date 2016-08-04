package com.sam_chordas.android.stockhawk.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Quote implements Serializable {
    @SerializedName("Symbol")
    public String symbol;
    @SerializedName("Date")
    public String quote_date;
    @SerializedName("Open")
    public String open;
    @SerializedName("High")
    public String high;
    @SerializedName("Low")
    public String low;
    @SerializedName("Close")
    public String close;
    @SerializedName("Volume")
    public String volume;
    @SerializedName("Adj_Close")
    public String Adj_Close;

}
