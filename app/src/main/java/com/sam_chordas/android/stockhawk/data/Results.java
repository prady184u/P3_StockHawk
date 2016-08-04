package com.sam_chordas.android.stockhawk.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class Results implements Serializable {
    @SerializedName("quote")
    public ArrayList<Quote> quote;

}
