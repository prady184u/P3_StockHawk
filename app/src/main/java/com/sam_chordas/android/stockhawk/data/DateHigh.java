package com.sam_chordas.android.stockhawk.data;


import java.io.Serializable;

public class DateHigh implements Serializable {

    private String quoteDate;
    private String quoteHighValue;


    public String getQuoteDate() {
        return quoteDate;
    }


    public void setQuoteDate(String quoteDate) {
        this.quoteDate = quoteDate;
    }

    public String getQuoteHighValue() {
        return quoteHighValue;
    }

    public void setQuoteHighValue(String quoteHighValue) {
        this.quoteHighValue = quoteHighValue;
    }
}
