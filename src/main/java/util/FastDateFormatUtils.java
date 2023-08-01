package util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;


public class FastDateFormatUtils {

    public static final String YYYY_MM_DD_T_HH_MM_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String YYYY_MM_DD_T_HH_MM = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm:ss";
    public final static FastDateFormat YYYY_MM_DD_HH_MM_SS_GMT_8 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss",TimeZone.getTimeZone("Asia/Shanghai"));
    public final static FastDateFormat YYYY_MM_DD_HH_MM_SS_UTC = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss",TimeZone.getTimeZone("UTC"));
    public final static FastDateFormat YYYY_MM_DD_T_HH_MM_SS_SSS_UTC = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS",TimeZone.getTimeZone("UTC"));

    /**
     * 带时区的字符串时间转化为时间
     *
     * @param dateStr
     * @return
     */
    public static Long zoneDate2Timestamp(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        String dateTime = dateStr;
        String zone = "+00:00";
        //东区
        if (dateStr.contains("+")) {
            zone = dateStr.substring(dateStr.indexOf("+"));
            dateTime = dateStr.substring(0, dateStr.indexOf("+"));
        } else {
            //西区
            int lineIndex = dateTime.lastIndexOf("-");
            if (lineIndex > 7) {
                zone = dateTime.substring(lineIndex);
                dateTime = dateTime.substring(0, lineIndex);
            }
        }
        ZoneId zoneId = ZoneId.of(zone);
        TimeZone timeZone = TimeZone.getTimeZone(zoneId);
        String format = null;
        if(StringUtils.contains(dateTime,".")){
            format = YYYY_MM_DD_T_HH_MM_SSS;
        }else{
            format = YYYY_MM_DD_T_HH_MM;
        }
        FastDateFormat fdf = FastDateFormat.getInstance(format, timeZone);
        Date date = fdf.parse(dateTime, new ParsePosition(0));
        return date.getTime();
    }

    public static String str2Time(String time){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
            Date d = sdf.parse(time);
            sdf.applyPattern(YYYY_MM_DD_T_HH_MM);
            return sdf.format(d);
        }catch (Exception e) {
            return time;
        }
    }

    public static void main(String[] args) {
        String time = "2023-05-18 11:43:01";
        System.out.println(str2Time(time));
    }
}
