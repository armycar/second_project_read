package com.readers.be3.utilities;

import java.time.LocalDateTime;

public class RandomNameUtils {
    public static String MakeRandomUri(String type, Long seq) {
        LocalDateTime time = LocalDateTime.now();
        String year = Integer.toString(time.getYear()-2022);
        String day = Integer.toString(time.getDayOfYear());
        Integer typeNo = Integer.parseInt(type, 32);
        String h = Integer.toString(time.getHour());
        String m = Integer.toString(time.getMinute());
        String s = Integer.toString(time.getSecond());
        String ns = Integer.toString(time.getNano());

        String randomUri = year+day+Long.toString(seq)+typeNo+h+m+s+ns;

        return randomUri;
    }
}
