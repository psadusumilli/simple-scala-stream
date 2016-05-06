/*
 * OrderBook.java
 */

package com.idms.csp.ctf.data;

import java.util.ArrayList;

/**
 * This class encapsulates the Market Depth data.
 */
public class OrderBook extends ArrayList
{
    // Status associated with the request for this book
    private Status _status = null;
    
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
}
