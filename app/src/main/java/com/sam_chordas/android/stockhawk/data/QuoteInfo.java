package com.sam_chordas.android.stockhawk.data;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QuoteInfo implements Serializable {
    @SerializedName("query")
    public Query query;
}
