package com.example.artwokmabel.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeWrangler {

    public static String changeNanopastToReadableDate(long nanopast){

        String finalString;
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd");

        long diff = System.currentTimeMillis() - nanopast;

        if(diff < 86400000 ){
            if(sdf.format(new Date(nanopast)).equals(sdf.format(new Date(System.currentTimeMillis())))){
                SimpleDateFormat finalFormat = new SimpleDateFormat("HH:mm");
                finalString = finalFormat.format(new Date(nanopast));
            }else {
                finalString = "yesterday";
            }
        }else{
            SimpleDateFormat finalFormat = new SimpleDateFormat("dd/MM/yyyy");
            finalString = finalFormat.format(new Date(nanopast));
        }

        return finalString;
    }


}
