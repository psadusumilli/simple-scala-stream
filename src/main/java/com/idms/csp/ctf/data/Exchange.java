/*
 * Exchange.java
 */

package com.idms.csp.ctf.data;

/**
 * This class encapsulates the enumerated definition for an exchange. For e.g. 
 * "AMEX - Composite Only" exchange object is created as follows:<br>
 * <code>Exchange exch = new Exchange("201", "545", "AMEX - Composite Only");
 */
public class Exchange extends Enum
{
    /**
     * Creates an exchange object given the CTF.TOKEN.NUM for exchange (201)  
     * CTF.ENUM.VALUE, CTF.ENUM.DESC.
     * 
     * @param toknum The CTF Token Number used to represent exchange (201)
     * @param enumval The Enumerated Value for the currency
     * @param enumdesc The Enumerated Description for the currency
     */
    public Exchange(int toknum, int enumval, String enumdesc)
    {
        super(toknum, enumval, enumdesc);
    }
    
    /**
     * Creates an exchange object given the CTF.TOKEN.NUM for exchange (201), 
     * CTF.ENUM.VALUE, CTF.ENUM.DESC.
     * 
     * @param toknum The CTF Token Number used to represent exchange (201)
     * @param enumval The Enumerated Value for the currency
     * @param enumdesc The Enumerated Description for the currency
     */
    public Exchange(String toknum, String enumval, String enumdesc)
    {
        super(toknum, enumval, enumdesc);
    }
}
