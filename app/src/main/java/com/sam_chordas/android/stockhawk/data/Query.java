package com.sam_chordas.android.stockhawk.data;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Query implements Serializable {
    @SerializedName("count")
    public String count;
    @SerializedName("created")
    public String created;
    @SerializedName("lang")
    public String lang;
    @SerializedName("diagnostics")
    public Diagnostics diagnostics;
    @SerializedName("results")
    public Results results;
}
