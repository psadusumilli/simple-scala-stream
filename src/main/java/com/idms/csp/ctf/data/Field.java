/*
 * Field.java
 */

package com.idms.csp.ctf.data;

import com.idms.csp.ctf.util.Util;

import java.io.PrintStream;
import java.util.Date;

/**
 * This class encapsulates the token/value pair transmitted in a CTF message.
 * Users rarely run into situations where they have to create instances of this
 * class. Message class provides convenient routines to access CTF fields.
 */
public class Field 
{
    // Token associated with this field
    private Token _token;
    
    // Value associated with this field
    private String _value;
    
    /**
     * Default constructor.
     */
    public Field()
    {
        
    }

    /**
     * Constructs a field object given the token definition and the value for
     * the token.
     * 
     * @param token The Token object
     * @param value The value of the token represented as a string.
     */
    public Field(Token token, String value) 
    {
        this._token = token;
        this._value = value;
    }
    
    /**
     * Sets the token associated with this field.
     * 
     * @param token The Token object.
     */
    public void setToken(Token token)
    {
        this._token = token;
    }
    
    /**
     * Returns the token definition associated with this field.
     * 
     * @return The Token object
     */
    public Token getToken()
    {
        return this._token;
    }
    
    /**
     * Sets the value of the token associated with this field.
     * 
     * @param value The value of the token represented as string.
     */
    public void setValue(String value)
    {
        this._value = value;
    }
    
    /**
     * Returns the value of the token associated with this field represented
     * by a string.
     * 
     * @return The string representation of the token value.
     */
    public String getValue()
    {
        return this._value;
    }
    
    /**
     * Returns the value of the token associated with this field as an integer
     * value.
     * 
     * @return The integer representation of the token value.
     */
    public int getIntValue()
    {
        if (_value != null)
        {
            try
            {
                return Integer.parseInt(_value);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
       
        return -1;
    }

    /**
     * Returns the value of the token associated with this field as a boolean
     * value.
     * 
     * @return The boolean representation of the token value.
     */
    public boolean getBoolValue()
    {
        return ("0".equals(_value)) ? false : true;
    }
    
    /**
     * Returns the value of the token associated with this field as a double
     * value.
     * 
     * @return The double representation of the token value.
     */
    public double getDoubleValue()
    {
        if (_value != null)
        {
            try
            {
                return Double.parseDouble(_value);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
        
        return Double.NaN;
    }

    /**
     * Returns the value of the token associated with this field as the UTC time
     * in milliseconds.
     * 
     * @return The time in milliseconds
     */
    public long getUTCTimeValue()
    {
        if (_value != null)
        {
            return Util.parseCTFDateTime(_value);
        }
        
        return -1;
    }

    /**
     * Returns the Date String associated with the DateTime value for this field
     * in MM/dd/yyyy format. For e.g. "05/20/2008"
     * 
     * @return The Date part of the DateTime value.
     */
    public String getDate()
    {
        if (_value != null)
        {
            return Util.getNetworkDate(_value);
        }
        
        return null;
    }
    
    /**
     * Returns the Time String associated with the DateTime value for this field
     * in HH:mm:ss.SSS zzz format set to New York Time Zone. For e.g. 09:30.10.100 EDT
     * 
     * @return The Time part of the DateTime value.
     */
    public String getTime()
    {
        if (_value != null)
        {
            return Util.getNetworkTime(_value);
        }
        
        return null;
    }
    
    /**
     * 
     * @return The string representation of the field.
     */
    @Override
    public String toString()
    {
        return "<"+_token.name+"=\""+_value+"\"/>";
    }
    
    /**
     * Prints the field.
     * 
     * @param out The PrintStream to print to.
     */
    public void print(PrintStream out)
    {
        if (_token != null && _value != null)
        {
            if (_token.type == Token.Type.DATETIME)
            {
                out.println("  <"+_token.name+"=\""+this.getDate()+" "+this.getTime()+"\"/>");
            }
            else
            {
                out.println("  <"+_token.name+"=\""+_value+"\"/>");
            }
        }
    }

    /**
     * Main entry point for Java applications.
     * 
     * @param args
     */
    public static void main(String[] args) 
    {
        Field curTime = new Field(DataDict.getToken("TRADE.DATETIME"), "1211204195.561");
        System.out.println("Trade DateTime = " + new Date(curTime.getUTCTimeValue()));
    }
}
