/*
 * Symbol.java
 */

package com.idms.csp.ctf.data;

/**
 * This class encapsulates the symbol as a combination of exchange id and ticker
 * symbol as defined by PlusFeed. An instrument is uniquely identified in the
 * PlusFeed using exchange id and ticker symbol together. For e.g. IBM data
 * can be queried on mulitple exchanges (regional) as follows:
 * <br>
 * NYSE - American Stock Exchange Regional Only => 225,IBM<br>
 * NYSE - Boston Stock Exchange Regional Only => 226,IBM<br>
 * NYSE - Cincinnati Stock Exchange Regional Only => 227,IBM<br>
 * NYSE - Chicago Stock Exchange Regional Only => 228,IBM<br>
 * NYSE - New York Stock Exchange Regional Only => 229,IBM<br>
 * NYSE - Pacific Stock Exchange Regional Only => 230,IBM<br>
 * NYSE - NASDAQ Regional Only => 231,IBM<br>
 * NYSE - CBOE Regional Only => 232,IBM<br>
 * NYSE - Philadelphia Stock Exchange Regional Only => 233,IBM<br>
 */
public class Symbol 
{
    // Exchange
    public Exchange exch;
    
    // Ticker Symbol
    public String ticker;
    
    /**
     * Constructs a symbol object given exchange,symbol combination.
     * 
     * @param sym exchange,ticker symbol combination. For e.g. "564,MSFT"
     */
    public Symbol(String sym)
    {
        String[] s = sym.split(",");
        
        try
        {
            this.exch = DataDict.getExchange(Integer.parseInt(s[0]));
            this.ticker = s[1];
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Constructs a symbol object given exchange id and ticker symbol
     * 
     * @param exch The exchange id
     * @param ticker The ticker symbol
     */
    public Symbol(String exch, String ticker)
    {
        try
        {
            this.exch = DataDict.getExchange(Integer.parseInt(exch));
            this.ticker = ticker;
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Constructs a symbol object given exchange id and ticker symbol
     * 
     * @param exch The exchange id
     * @param ticker The ticker symbol
     */
    public Symbol(int exch, String ticker)
    {
        this.exch = DataDict.getExchange(exch);
        this.ticker = ticker;
    }
    
    /**
     * Constructs a symbol object from CTF message that contains the exchange
     * id and ticker symbol tokens
     * 
     * @param msg The CTF message
     */
    public Symbol(Message msg)
    {
        this.exch = DataDict.getExchange(msg.get(4).getIntValue());
        this.ticker = msg.get(5).getValue();
    }
    
    /**
     * Returns th exchange object associated with this symbol
     * 
     * @return Exchange object
     */
    public Exchange getExchange()
    {
       return this.exch; 
    }
    
    /**
     * Returns the exchange id associated with this symbol
     * 
     * @return The exchange id
     */
    public int getExchangeID()
    {
        return this.exch.enumval;
    }

    /**
     * Returns the ticker symbol associated with this symbol
     * 
     * @return The ticker symbol
     */
    public String getTicker()
    {
        return this.ticker;
    }
    
    /**
     * Returns string representation of this symbol.
     * 
     * @return The string representation of this symbol.
     */
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("[Exch="+exch+",Ticker Symbol="+ticker+"]");
        return sb.toString();
    }
}
