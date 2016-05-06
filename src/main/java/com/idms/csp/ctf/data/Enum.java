/*
 * Enum.java
 */

package com.idms.csp.ctf.data;

/**
 * This class encapsulates an enumeration definition. For e.g. A U.S Dollar 
 * in currency enumerations is created as follows:<br>
 * <code>Enum enum = new Enum("262", "252304", "US Dollars");
 */
public class Enum 
{
    // CTF.TOKEN.NUM
    protected int toknum;
    
    // CTF.ENUM.DESC
    protected String enumdesc = null;
    
    // CTF.ENUM.VALUE
    protected int enumval;
    
    /**
     * Creates an enum object given the CTF.TOKEN.NUM for which the enumeration
     * is defined as CTF.ENUM.VALUE and CTF.ENUM.DESC.
     * 
     * @param toknum The CTF Token Number
     * @param enumval The Enumerated Value
     * @param enumdesc The Enumerated Description
     */
    public Enum(int toknum, int enumval, String enumdesc)
    {
        this.toknum = toknum;
        this.enumval = enumval;
        this.enumdesc = enumdesc;
    }
    
    /**
     * Creates an enum object given the CTF.TOKEN.NUM for which the enumeration
     * is defined as CTF.ENUM.VALUE and CTF.ENUM.DESC.
     * 
     * @param toknum The CTF Token Number
     * @param enumval The Enumerated Value
     * @param enumdesc The Enumerated Description
     */
    public Enum(String toknum, String enumval, String enumdesc)
    {
        try
        {
            this.toknum = Integer.parseInt(toknum);
            this.enumval = Integer.parseInt(enumval);
            this.enumdesc = enumdesc;
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Creates an enum object given a CTF message containing the enumeration 
     * definition. For e.g. <br>
     * 5033=US Dollars|5034=252304|5035=262|5026=1|
     * 
     * @param msg The CTF Message
     */
    public Enum(Message msg)
    {
        this.toknum = msg.get(5035).getIntValue();
        this.enumval = msg.get(5034).getIntValue();
        this.enumdesc = msg.get(5033).getValue();
    }

    /**
     * Returns the token number associated with this enumeration.
     * 
     * @return The token number.
     */
    public int getTokenNum()
    {
        return this.toknum;
    }
    
    /**
     * Returns the enumerated value.
     * 
     * @return The enumerated value.
     */
    public int getValue()
    {
        return this.enumval;
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
     * Returns the string representation of this enumeration definition.
     * 
     * @return The string representing this enumeration.
     */
    @Override
    public String toString()
    {
        return "Enum [Token="+toknum+",Value="+enumval+",Desc="+enumdesc+"]";
    }
}
