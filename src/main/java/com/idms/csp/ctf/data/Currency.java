/*
 * Currency.java
 */

package com.idms.csp.ctf.data;

import com.idms.csp.ctf.util.Util;

/**
 * This class encapsulates the enumerated definition of a currency. For e.g. 
 * A U.S Dollar currency object is created as follows:<br>
 * <code>Currency usd = new Currency("262", "252304", "US Dollars");
 */
public class Currency
{
    // CTF.TOKEN.NUM
    protected int toknum;

    // CTF.ENUM.DESC (Currency Description)
    protected String enumdesc;
    
    // CTF.ENUM.VALUE (Octal String Representation)
    protected int enumval;
    
    // Currency Code (Decoded String)
    protected String code;

    /**
     * Creates a currency object given the CTF.TOKEN.NUM for currency (262), 
     * CTF.ENUM.VALUE, CTF.ENUM.DESC.
     * 
     * @param toknum The CTF Token Number used to represent currency (262)
     * @param enumval The Enumerated Value for the currency
     * @param enumdesc The Enumerated Description for the currency
     */
    public Currency(int toknum, int enumval, String enumdesc)
    {
        this.toknum = toknum;
        this.enumval = enumval;
        this.enumdesc = enumdesc;
        this.code = Util.octalDecode(String.valueOf(this.enumval));
    }
    
    /**
     * Creates a currency object given the CTF.TOKEN.NUM for currency (262), 
     * CTF.ENUM.VALUE, CTF.ENUM.DESC.
     * 
     * @param toknum The CTF Token Number used to represent currency (262)
     * @param enumval The Enumerated Value for the currency
     * @param enumdesc The Enumerated Description for the currency
     */
    public Currency(String toknum, String enumval, String enumdesc)
    {
        try
        {
            this.toknum = Integer.parseInt(toknum);
            this.enumval = Integer.parseInt(enumval);
            this.enumdesc = enumdesc;
            this.code = Util.octalDecode(String.valueOf(this.enumval));
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a currency object given a CTF message containing the enumeration 
     * definition. For e.g. <br>
     * 5033=US Dollars|5034=252304|5035=262|5026=1|
     * 
     * @param msg The CTF Message
     */
    public Currency(Message msg)
    {
        this(msg.get(5035).getValue(), msg.get(5034).getValue(), msg.get(5033).getValue());
    }
    
    /**
     * Returns the octal value.
     * 
     * @return The octal value.
     */
    public String getOctalCode()
    {
        return String.valueOf(this.enumval);
    }
    
    /**
     * Returns the currency code. For e.g. "USD" for U.S Dollars.
     * 
     * @return The three character code.
     */
    public String getCode()
    {
        return this.code;
    }
    
    /**
     * Returns the enumerated description.
     * 
     * @return The enumerated description
     */
    public String getDescription()
    {
       return this.enumdesc; 
    }

    /**
     * 
     * @return The string representing this currency.
     */
    @Override
    public String toString()
    {
       return "Currency Enum [Token="+toknum+",Value="+enumval+",Code="+code+",Desc="+enumdesc+"]";
    }
}
