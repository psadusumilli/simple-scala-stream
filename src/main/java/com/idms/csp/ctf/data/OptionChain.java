/*
 * OptionChain.java
 *
 * Created on December 12, 2008, 11:56 AM
 *
 */

package com.idms.csp.ctf.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author MGudipati
 */
public class OptionChain
{
    // Call chain.
    private List<Message> _callChain = new ArrayList<Message>();
    
    // Put chain.
    private List<Message> _putChain = new ArrayList<Message>();
    
    // Status associated with the request for this chain
    private Status _status = null;
    
    /**
     * 
     * @param msg
     */
    public OptionChain(Message[] msg)
    {
        for (Message item : msg)
        {
           if (item.isCall())
           {
               _callChain.add(item);
           }
           else if (item.isPut())
           {
               _putChain.add(item);
           }
           else
           {
                Status status = item.getStatus();

                if (status != null)
                {
                    // Is either the final message indicated by the token 5001=0
                    // Or error message
                    setStatus(status);
                }
           }
        }
    }
    
    /**
     * Sets the status of the order book.
     * 
     * @param status The Status object.
     */
    public void setStatus(Status status)
    {
        this._status = status;
    }
    
    /**
     * Returns the status of the order book.
     * 
     * @return The Status object.
     */
    public Status getStatus()
    {
        return this._status;
    }
    
    /**
     * 
     * @return
     */
    public List<Message> getCallChain()
    {
        return _callChain;
    }
    
    /**
     * 
     * @param expirationDate
     * @return
     */
    public List<Message> getCallChainByExpiration(String expirationDate)
    {
        List<Message> calls = new ArrayList();
        
        for (Message call : _callChain)
        {
            Field expiration = call.get("EXPIRATION.DATE");
            if (expiration != null && expiration.getValue().equals(expirationDate))
            {
                calls.add(call);
            }
        }
        
        return calls;
    }
    
    /**
     * 
     * @param strikePrice
     * @return
     */
    public List<Message> getCallChainByStrike(String strikePrice)
    {
        List<Message> calls = new ArrayList();
        
        for (Message call : _callChain)
        {
            Field strike = call.get("STRIKE.PRICE");
            if (strike != null && strike.getValue().equals(strikePrice))
            {
                calls.add(call);
            }
        }
        
        return calls;
    }

    /**
     * 
     * @return
     */
    public List<Message> getPutChain()
    {
        return _putChain;
    }

    /**
     * 
     * @param expirationDate
     * @return
     */
    public List<Message> getPutChainByExpiration(String expirationDate)
    {
        List<Message> puts = new ArrayList();
        
        for (Message put : _putChain)
        {
            Field expiration = put.get("EXPIRATION.DATE");
            if (expiration != null && expiration.getValue().equals(expirationDate))
            {
                puts.add(put);
            }
        }
        
        return puts;
    }
    
    /**
     * 
     * @param strikePrice
     * @return
     */
    public List<Message> getPutChainByStrike(String strikePrice)
    {
        List<Message> puts = new ArrayList();
        
        for (Message put : _putChain)
        {
            Field strike = put.get("STRIKE.PRICE");
            if (strike != null && strike.getValue().equals(strikePrice))
            {
                puts.add(put);
            }
        }
        
        return puts;
    }

    /**
     * 
     * @return
     */
    public Set<String> getStikePrices()
    {
        Set strikePrices = new TreeSet<String>();
        
        // get all the strike prices from the call chain.
        for (Message option : _callChain)
        {
            Field strike = option.get("STRIKE.PRICE");
            if (strike != null)
            {
                strikePrices.add(strike.getValue());
            }
        }

        // get all the strike prices from the put chain.
        for (Message option : _putChain)
        {
            Field strike = option.get("STRIKE.PRICE");
            if (strike != null)
            {
                strikePrices.add(strike.getValue());
            }
        }
        
        return strikePrices;
    }

    /**
     * 
     * @return
     */
    public Set<String> getExpiryDates()
    {
        Set expiryDates = new TreeSet<String>();
        
        // get all the expiry dates from the call chain.
        for (Message option : _callChain)
        {
            Field expiration = option.get("EXPIRATION.DATE");
            if (expiration != null)
            {
                expiryDates.add(expiration.getValue());
            }
        }

        // get all the expiry dates from the put chain.
        for (Message option : _putChain)
        {
            Field expiration = option.get("EXPIRATION.DATE");
            if (expiration != null)
            {
                expiryDates.add(expiration.getValue());
            }
        }
        
        return expiryDates;
    }
}
