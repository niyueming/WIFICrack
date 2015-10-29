package net.nym.wificrack.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

/**
 * @author nym
 * @date 2014/9/25 0025.
 */
public class StringUtils {

    public static String join(Object[] elements, CharSequence separator) {
        return join(Arrays.asList(elements), separator);
    }

    public static String join(Iterable<? extends Object> elements, CharSequence separator) {
        StringBuilder builder = new StringBuilder();

        if (elements != null) {
            Iterator<? extends Object> iter = elements.iterator();
            if (iter.hasNext()) {
                builder.append(String.valueOf(iter.next()));
                while (iter.hasNext()) {
                    builder.append(separator).append(String.valueOf(iter.next()));
                }
            }
        }

        return builder.toString();
    }

    public static String fixLastSlash(String str) {
        String res = str == null ? "/" : str.trim() + "/";
        if (res.length() > 2 && res.charAt(res.length() - 2) == '/')
            res = res.substring(0, res.length() - 1);
        return res;
    }

    public static int convertToInt(String str) throws NumberFormatException {
        int s, e;
        for (s = 0; s < str.length(); s++)
            if (Character.isDigit(str.charAt(s)))
                break;
        for (e = str.length(); e > 0; e--)
            if (Character.isDigit(str.charAt(e - 1)))
                break;
        if (e > s) {
            try {
                return Integer.parseInt(str.substring(s, e));
            } catch (NumberFormatException ex) {
                Log.e("convertToInt", ex);
                throw new NumberFormatException();
            }
        } else {
            throw new NumberFormatException();
        }
    }

    public static boolean isNullOrEmpty(String str) {
        if (str == null)
        {
            return true;
        }
        if (str.equals(""))
        {
            return true;
        }
        return false;
    }

    public static String subString(String str,int end)
    {
        String result = str;
        if (str.length() > end)
        {
            result = str.substring(0,end);
        }
        return result;
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c).replaceAll(":",": ");
    }

    /**
     *
     * @param time 毫秒时间差
     * @return 返回00:00:00 格式
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * 返回默认格式"yyyy-MM-dd"的时间
     * @param time Unix long型时间，秒
     *
     *
     * */
    public static String parseLongToTime(long time)
    {
        return parseLongToTime("yyyy-MM-dd",time);
    }

    /**
     * @param time Unix long型时间，秒
     *
     *
     * */
    public static String parseLongToTime(String format,long time)
    {
        long now = System.currentTimeMillis();
        time *= 1000;
        long interval = (now - time)<0 ? 0:(now - time);
        if (interval < 24*60*60*1000)
        {
            if (interval > 60*60*1000)
            {
                return interval/(60*60*1000) + "小时前";
            }
            else {
                return (interval/(60*1000) == 0 ? 1:interval/(60*1000)) + "分钟前";
            }
        }
        else {
            return getDateToString(format,time);
        }
    }

    /**
     *
     * @param time long型时间，毫秒
     * @param format 如："yyyy-MM-dd HH:mm:ss"
     * @return 返回format格式的时间
     *
     * 时间戳转换成字符窜
     * */
    public static String getDateToString(String format,long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(d);
    }

    /**
     * 返回默认格式"yyyy-MM-dd HH:mm:ss"的时间
     * @param time long型时间，毫秒
     *
     *
     * */
    public static String getDateToString(long time) {
        return getDateToString("yyyy-MM-dd HH:mm:ss",time);
    }

    /**
     * 把字符串的第start位到end位（不包括end）用“*”号代替
     * @param str 要代替的字符串
     * @return
     */

    public static String replaceSubString(String str,int start,int end){
        String sub1="";
        String sub2="";
        StringBuffer sb=new StringBuffer();
        try {
            sub1 = str.substring(0, start);
            sub2 = str.substring(end, str.length());
            sb.append(sub1);
            for(int i=0;i<end -start;i++){
                sb=sb.append("*");
            }
            sb.append(sub2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
