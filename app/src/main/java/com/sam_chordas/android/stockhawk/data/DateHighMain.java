package com.sam_chordas.android.stockhawk.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DateHighMain implements Serializable {
    public List<DateHigh> datehigh = new ArrayList<DateHigh>();

    public List<DateHigh> getDatehigh() {
        return datehigh;
    }

    public void setDatehigh(List<DateHigh> datehigh) {
        this.datehigh = datehigh;
    }
    public void addDatehigh(DateHigh datehigh) {
        this.datehigh.add(datehigh);
    }
}
