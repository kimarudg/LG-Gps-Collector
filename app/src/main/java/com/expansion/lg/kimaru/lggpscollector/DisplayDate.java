package com.expansion.lg.kimaru.lggpscollector;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kimaru on 4/5/17.
 */

public class DisplayDate {

    Long epoch;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat selectFormatter = new SimpleDateFormat("yyyy/M/d");
    SimpleDateFormat timeFormatter = new SimpleDateFormat("hh/mm/a");
    SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy hh/mm/a");

    // public DisplayDate (){}
    public DisplayDate(Long epoch){
        this.epoch = epoch;
    }

    public String dateOnly() {
        Date date = new Date (this.epoch);
        return dateFormatter.format(date);
    }
    public String timeOnly() {
        Date date = new Date (this.epoch);
        return timeFormatter.format(date);
    }

    public String widgetDateOnly(){
        Date date = new Date (this.epoch);
        return selectFormatter.format(date);
    }

    public String dateAndTime() {
        Date date = new Date (this.epoch);
        return dateTimeFormatter.format(date);
    }
}
