/*
 * Util.java
 */

package com.idms.csp.ctf.util;

import com.idms.csp.ctf.data.Field;
import com.idms.csp.ctf.data.Message;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Util class provides utility functions that are frequently used in a typical
 * market data application. For e.g. To parse the raw CTF DateTime string a
 * convenient routine parseCTFDateTime() is available.
 */
public class Util 
{
    // Date Formatters set to New York Time Zone to be used in CTF DateTime formatting.
    static Date _utcTime = new Date();
    static SimpleDateFormat _dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat _timeFormatter = new SimpleDateFormat("HH:mm:ss.SSS");
    static SimpleDateFormat _dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

    static SimpleDateFormat _utcDateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    static SimpleDateFormat _utcTimeFormatter = new SimpleDateFormat("HH:mm:ss.SSS");
    static SimpleDateFormat _utcDateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

    static
    {
        _dateFormatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        _timeFormatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        _dateTimeFormatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        _utcDateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        _utcTimeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        _utcDateTimeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    // Call Options Expiration Month Codes
    static final TreeMap<String, String> _callExpirationMonthCodeMap 
            = new TreeMap<String, String>();
    static
    {
        _callExpirationMonthCodeMap.put("A", "JAN");
        _callExpirationMonthCodeMap.put("B", "FEB");
        _callExpirationMonthCodeMap.put("C", "MAR");
        _callExpirationMonthCodeMap.put("D", "APR");
        _callExpirationMonthCodeMap.put("E", "MAY");
        _callExpirationMonthCodeMap.put("F", "JUN");
        _callExpirationMonthCodeMap.put("G", "JUL");
        _callExpirationMonthCodeMap.put("H", "AUG");
        _callExpirationMonthCodeMap.put("I", "SEP");
        _callExpirationMonthCodeMap.put("J", "OCT");
        _callExpirationMonthCodeMap.put("K", "NOV");
        _callExpirationMonthCodeMap.put("L", "DEC");
    }
    
    // Put Options Expiration Month Code
    static final TreeMap<String, String> _putExpirationMonthCodeMap 
            = new TreeMap<String, String>();
    static
    {
        _putExpirationMonthCodeMap.put("M", "JAN");
        _putExpirationMonthCodeMap.put("N", "FEB");
        _putExpirationMonthCodeMap.put("O", "MAR");
        _putExpirationMonthCodeMap.put("P", "APR");
        _putExpirationMonthCodeMap.put("Q", "MAY");
        _putExpirationMonthCodeMap.put("R", "JUN");
        _putExpirationMonthCodeMap.put("S", "JUL");
        _putExpirationMonthCodeMap.put("T", "AUG");
        _putExpirationMonthCodeMap.put("U", "SEP");
        _putExpirationMonthCodeMap.put("V", "OCT");
        _putExpirationMonthCodeMap.put("W", "NOV");
        _putExpirationMonthCodeMap.put("X", "DEC");
    }

    /**
     * Decodes an octal value into its text representation. This function is
     * very limited to support PlusFeed data. It is not meant for general 
     * decoding. For instance, the currency codes are represented as octally encoded
     * strings in the CTF feed. E.g. "USD" (U.S Dollars) is represented as the 
     * octal string "252304". This octal string contains three octal numbers
     * "25", "23" and "04" each mapping to "U", "S" and "D" respectively. The 
     * leading "1" is omitted from each octal number to conserve bandwidth. Also,
     * the leading "0" is omitted for the entire octal string.
     * 
     * Outputs "?" to represent a non-octal value in the input.
     * 
     * @param octal The octal number to decode
     * @return The decoded string
     */
    public static String octalDecode(String octal)
    {
        StringBuffer sb = new StringBuffer();
        
        // Pad with leading "0" for odd numbered string. e.g. "23241" -> "023241"
        if ((octal.length() % 2) != 0)
        {
            octal = "0" + octal;
        }
        
        for (int i=0; i<octal.length(); i=i+2)
        {
            // Take pairs of digits and decode. For e.g. "02" from "023241"
            String onum = octal.substring(i, i+2);
            
            // Pad with leading "01" because "1" is omitted from CTF message.
            // "0" is needed to represent octal numbers.
            onum = "01" + onum;

            // Decode
            try
            {
                Integer num = Integer.decode(onum);
                sb.append(String.format("%c", num));
            }
            catch (NumberFormatException e)
            {
                sb.append("?");
            }
        }
        
        return sb.toString();
    }

    /**
     * Parses the CTF DateTime String value into long data type.
     * For e.g. "1210152744.006" -> 1210152744006
     * 
     * @param ctfdate The CTF DateTime String
     * @return The long value representing time in milliseconds.
     */
    public static long parseCTFDateTime(String ctfdate)
    {
        try
        {
            double datetime = Double.parseDouble(ctfdate) * 1000; 
            return (long) datetime; 
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            return -1L;
        }
    }

    /**
     * Returns the Date portion of the CTF DateTime string in MM/dd/yyyy format.
     * 
     * @param ctfdate The CTF DateTime String.
     * @return The Date String in MM/dd/yyyy format.
     */
    public static String getNetworkDate(String ctfdate)
    {
        long time = parseCTFDateTime(ctfdate);
        
        if (time == -1)
        {
            return null;
        }

        return getNetworkDate(time);
    }

    /**
     * Returns the Date portion of the UTC time in MM/dd/yyyy format.
     * 
     * @param utctime The UTC time in milliseconds.
     * @return The Date String in MM/dd/yyyy format.
     */
    public static String getNetworkDate(long utctime)
    {
        _utcTime.setTime(utctime);
        return _dateFormatter.format(_utcTime);
    }
 
    /**
     * Returns the Time portion of the CTF DateTime string in HH:mm:ss.SSS
     * format set to New York Time Zone.
     * 
     * @param ctfdate The CTF DateTime String.
     * @return The Time String in HH:mm:ss.SSS format.
     */
    public static String getNetworkTime(String ctfdate)
    {
        long time = parseCTFDateTime(ctfdate);
        
        if (time == -1)
        {
            return null;
        }
        
        return getNetworkTime(time);
    }

    /**
     * Returns the Time portion of the UTC time in HH:mm:ss.SSS format set 
     * to New York Time Zone.
     * 
     * @param utctime The UTC time in milliseconds.
     * @return The Time String in HH:mm:ss.SSS format.
     */
    public static String getNetworkTime(long utctime)
    {
        _utcTime.setTime(utctime);
        return _timeFormatter.format(_utcTime);
    }

    /**
     * 
     * @param dateTime
     * @return
     */
    public static String getCTFDateTime(String dateTime)
    {
        try 
        {
            //  For e.g. Convert 08/12/2008 10:56:00.000 -> 1218552960.
            Date date = _dateTimeFormatter.parse(dateTime);

            if (date.getTime() % 1000 == 0)
            {
                // Return a string without the milliseconds.
                return String.format("%ts", date);
            }
            else
            {
                // Retrun a string with milliseconds.
                return String.format("%ts.%tL", date, date);
            }
        } 
        catch (ParseException ex) 
        {
            try 
            {
                //  For e.g. Convert 08/12/2008 -> 1218513600.
                Date date = _dateFormatter.parse(dateTime);

                // Return a string without the milliseconds.
                return String.format("%ts", date);
            } 
            catch (ParseException ex1) 
            {
                try 
                {
                    //  For e.g. Convert 10:30:00.000 -> 1218513600.
                    Date date = _timeFormatter.parse(dateTime);

                    if (date.getTime() % 1000 == 0)
                    {
                        // Return a string without the milliseconds.
                        return String.format("%ts", date);
                    }
                    else
                    {
                        // Retrun a string with milliseconds.
                        return String.format("%ts.%tL", date, date);
                    }
                } 
                catch (ParseException ex2) 
                {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex2);
                }
            }
        }
        
        return null;
    }

    /**
     * 
     * @param dateTime
     * @return
     */
    public static String getCTFDateTime(long dateTime)
    {
        _utcTime.setTime(dateTime);

        if (dateTime % 1000 == 0)
        {
            // Return a string without the milliseconds.
            return String.format("%ts", _utcTime);
        }
        else
        {
            // Retrun a string with milliseconds.
            return String.format("%ts.%tL", _utcTime, _utcTime);
        }
    }

    /**
     *
     * @param timeZoneID
     * @return
     */
    public static int getTimeZoneOffset(String timeZoneID)
    {
        TimeZone tz = TimeZone.getTimeZone(timeZoneID);
        return tz.getOffset(System.currentTimeMillis())/(1000 * 60);
    }
    
    /**
     * 
     * @param msg
     * @return
     */
    public static boolean isRefresh(Message msg)
    {
        Field refresh = msg.get("REFRESH");
        if (refresh != null)
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 
     * @param msg
     * @return
     */
    public static boolean isBuyOrder(Message msg)
    {
        Field bidPrice = msg.get("BID.PART.PRICE");
        Field bidSize = msg.get("BID.PART.SIZE");
        
        if (bidPrice != null || bidSize != null)
        {
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @param msg
     * @return
     */
    public static boolean isSellOrder(Message msg)
    {
        Field askPrice = msg.get("ASK.PART.PRICE");
        Field askSize = msg.get("ASK.PART.SIZE");
        
        if (askPrice != null || askSize != null)
        {
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @param msg
     * @return
     */
    public static boolean isDeleteOrder(Message msg) 
    {
        Field askSize = msg.get("ASK.PART.SIZE");
        if (askSize != null && askSize.getIntValue() == 0)
        {
            return true;
        }

        Field bidSize = msg.get("BID.PART.SIZE");
        if (bidSize != null && bidSize.getIntValue() == 0)
        {
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @return
     */
    public static boolean isTrade(Message msg)
    {
        Field tradePrice = msg.get("TRADE.PRICE");
        Field tradeSize = msg.get("TRADE.SIZE");
        
        if (tradePrice != null || tradeSize != null)
        {
            return true;
        }

        return false;
    }
    
    /**
     * 
     * @return
     */
    public static boolean isQuote(Message msg)
    {
        Field bidPrice = msg.get("BID.PRICE");
        Field bidSize = msg.get("BID.SIZE");
        Field askPrice = msg.get("ASK.PRICE");
        Field askSize = msg.get("ASK.SIZE");
        
        if (bidPrice != null || bidSize != null || askPrice != null || askSize != null)
        {
            return true;
        }

        return false;
    }
    
    /**
     * 
     * @return
     */
    public static boolean isLevel1(Message msg)
    {
        if (Util.isTrade(msg) || Util.isQuote(msg))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 
     * @return
     */
    public static boolean isLevel2(Message msg)
    {
        if (Util.isBuyOrder(msg) || Util.isSellOrder(msg))
        {
            return true;
        }

        return false; 
    }

    /**
     *
     * @param msg
     * @return
     */
    public static boolean isStatus(Message msg)
    {
        Field field = msg.get(5001);
        if (field != null)
        {
            return true;
        }

        return false;
    }

    /**
     * 
     * @param msg
     * @param token
     * @return
     */
    public static String formatCSV(Message msg, String[] token)
    {
        StringBuffer sb = new StringBuffer();
        
        for (int i=0; i<token.length; i++)
        {
            Field field = msg.get(token[i]);
            if (field != null)
            {
                sb.append(field.getValue());
            }

            // Skip comma for the last field.
            if (i != token.length-1)
            {
                sb.append(",");
            }
        }
        
        return sb.toString();
    }

    /**
     * 
     * @param field
     * @return
     */
    public static String formatXML(Field[] field)
    {
        StringBuffer sb = new StringBuffer();
        
        for (int i=0; i<field.length; i++)
        {
            sb.append(formatXML(field[i]));
        }
        
        return sb.toString();
    }
    
    /**
     * 
     * @param msg
     * @param token
     * @return
     */
    public static String formatXML(Message msg, String[] token)
    {
        StringBuffer sb = new StringBuffer();
        
        for (int i=0; i<token.length; i++)
        {
            Field field = msg.get(token[i]);
            if (field != null)
            {
                sb.append(formatXML(field));
            }
        }
        
        return sb.toString();
    }

    /**
     * 
     * @param field
     * @return
     */
    public static String formatXML(Field field)
    {
        String nameTag = field.getToken().getName().replace('.', '_');

        return "<" + nameTag + ">" + field.getValue() + "</" + nameTag + ">";
    }

    /**
     * Decodes the option expiration month code and returns the three letter
     * month. For e.g. A -> JAN.
     * 
     * @param code
     * @return
     */
    public static String decodeExpirationMonth(String code)
    {
        if (_callExpirationMonthCodeMap.containsKey(code))
        {
            return _callExpirationMonthCodeMap.get(code);
        }
        
        return _putExpirationMonthCodeMap.get(code);
    }

    /**
     * Encodes the call option expiration month. For e.g. JAN -> A
     * 
     * @param month
     * @return
     */
    public static String encodeCallExpirationMonth(String month)
    {
        Set<Map.Entry<String, String>> set = _callExpirationMonthCodeMap.entrySet();
        for (Map.Entry<String, String> item : set)
        {
            if (item.getValue().equals(month))
            {
                return item.getKey();
            }
        }
        
        return null;
    }

    /**
     * Encodes the put option expiration month. For e.g. JAN -> M
     * 
     * @param month
     * @return
     */
    public static String encodePutExpirationMonth(String month)
    {
        Set<Map.Entry<String, String>> set = _putExpirationMonthCodeMap.entrySet();
        for (Map.Entry<String, String> item : set)
        {
            if (item.getValue().equals(month))
            {
                return item.getKey();
            }
        }
        
        return null;
    }
    
    /**
     * Determines if the given option symbol is a call or not based on the 
     * expiration month code character.
     * 
     * For e.g. IBMAR is a call option because 'A' is JAN expiration month for calls.
     * 
     * @param sym
     * @return
     */
    public static boolean isCall(String sym)
    {
       // Obtain the last but one character which is a call/put indicator 
       // that identifies the expiration month. For e.g. IBMAR -> IBM Jan 2006 90 call.
       if (sym != null)
       {
           try
           {
               String code = sym.substring(sym.length()-2, sym.length()-1);
               if (_callExpirationMonthCodeMap.containsKey(code))
               {
                   return true;
               }

           }
           catch (IndexOutOfBoundsException e)
           {
               
           }
       }
       
       return false;
    }

    /**
     * Determines if the given option symbol is a put or not based on the 
     * expiration month code character.
     * 
     * For e.g. IBMMR is a put option because 'M' is JAN expiration month for puts.
     * 
     * @param sym
     * @return
     */
    public static boolean isPut(String sym)
    {
       // Obtain the last but one character which is a call/put indicator 
       // that identifies the expiration month. For e.g. IBMMR -> IBM Jan 2006 90 put.
       if (sym != null)
       {
           try
           {
               String code = sym.substring(sym.length()-2, sym.length()-1);
               if (_putExpirationMonthCodeMap.containsKey(code))
               {
                   return true;
               }

           }
           catch (IndexOutOfBoundsException e)
           {
               
           }
       }
       
       return false;
    }

    /**
     * Main entry point for Java applications.
     * @param args
     */
    public static void main(String[] args) 
    {
        System.out.println("Time Zone Offset for America/New_York = " + Util.getTimeZoneOffset("America/New_York"));
    }
}
