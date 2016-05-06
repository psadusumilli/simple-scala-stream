/*
 * TimeAndSales.java
 */

package com.idms.csp.ctf.tas;

import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates the Time and Sales data.
 * Here is a sample:
 *
    <CTFMessage>
      <TAS.RECORD.TYPE="T"/>
      <PART.CODE="d"/>
      <ACTIVITY.DATETIME="05/26/2009 09:30:46.352"/>
      <TAS.SEQ="1485654194"/>
      <TRADE.SEQ="17"/>
      <TRADE.COND_1="0"/>
      <TRADE.SIZE="100"/>
      <TRADE.PRICE="101.19"/>
      <VWAP="101.3891"/>
      <QUERY.TAG="1"/>
    </CTFMessage>
    <CTFMessage>
      <TAS.RECORD.TYPE="Q"/>
      <PART.CODE="i"/>
      <ACTIVITY.DATETIME="05/26/2009 09:30:45.817"/>
      <TAS.SEQ="1485622613"/>
      <QUOTE.COND_1="0"/>
      <QUOTE.COND_2="0"/>
      <QUOTE.COND_4="0"/>
      <BID.SIZE="1"/>
      <ASK.SIZE="1"/>
      <BID.PRICE="101.01"/>
      <ASK.PRICE="101.19"/>
      <QUERY.TAG="1"/>
    </CTFMessage>
    <CTFMessage>
      <TAS.RECORD.TYPE="B"/>
      <BID.PART.CODE="z"/>
      <ASK.PART.CODE="i"/>
      <ACTIVITY.DATETIME="05/26/2009 09:30:45.817"/>
      <TAS.SEQ="1485622612"/>
      <BID.SIZE="1"/>
      <ASK.SIZE="1"/>
      <BID.PRICE="101.14"/>
      <ASK.PRICE="101.19"/>
      <QUERY.TAG="1"/>
    </CTFMessage>
 */
public class TimeAndSales
{
    // The source id.
    private int _sourceID;

    // The ticker symbol.
    private String _tickerSymbol;

    // The from date time in CTF DateTime format.
    private String _fromCTFDateTime;

    // The to date time in CTF DateTime format.
    private String _toCTFDateTime;

    // The pivot date time in CTF DateTime format.
    private String _pivotTime;

    // The pivot count.
    private int _pivotCount;

    // Status associated with the request for this data.
    private String _status;

    // All records.
    List<TaSRecord> _allRecords = new ArrayList<TaSRecord>();

    // NBBO records.
    List<NBBORecord> _nbboRecords;

    // Quote records.
    List<QuoteRecord> _quoteRecords;

    // Trade records.
    List<TradeRecord> _tradeRecords;

    /**
     *
     * @param srcid
     * @param sym
     * @param fromCTFDateTime
     * @param toCTFDateTime
     */
    public TimeAndSales(int srcid, String sym, String fromCTFDateTime, String toCTFDateTime)
    {
        _sourceID = srcid;
        _tickerSymbol = sym;
        _fromCTFDateTime = fromCTFDateTime;
        _toCTFDateTime = toCTFDateTime;
    }

    /**
     *
     * @param srcid
     * @param sym
     * @param pivotCTFDateTime
     * @param pivotCount
     */
    public TimeAndSales(int srcid, String sym, String fromCTFDateTime, String toCTFDateTime, String pivotCTFDateTime, int pivotCount)
    {
        _sourceID = srcid;
        _tickerSymbol = sym;
        _fromCTFDateTime = fromCTFDateTime;
        _toCTFDateTime = toCTFDateTime;
        _pivotTime = pivotCTFDateTime;
        _pivotCount = pivotCount;
    }

    /**
     * 
     * @return
     */
    public int getSourceID()
    {
        return _sourceID;
    }

    /**
     *
     * @param srcid
     */
    public void setSourceID(int srcid)
    {
        _sourceID = srcid;
    }

    /**
     *
     * @return
     */
    public String getTickerSymbol()
    {
        return _tickerSymbol;
    }

    /**
     *
     * @param sym
     */
    public void setTickerSymbol(String sym)
    {
        _tickerSymbol = sym;
    }

    /**
     *
     * @param time
     */
    public void setFromDateTime(String time)
    {
        _fromCTFDateTime = time;
    }

    /**
     *
     * @return
     */
    public String getFromdDateTime()
    {
        return _fromCTFDateTime;
    }

    /**
     *
     * @param time
     */
    public void setToDateTime(String time)
    {
        _toCTFDateTime = time;
    }

    /**
     *
     * @return
     */
    public String getToDateTime()
    {
        return _toCTFDateTime;
    }
    
    /**
     * 
     * @param pivotTime
     */
    public void setPivotTime(String pivotTime)
    {
       _pivotTime = pivotTime;
    }

    /**
     * 
     * @return
     */
    public String getPivotTime()
    {
       return _pivotTime;
    }

    /**
     *
     * @param pivotCount
     */
    public void setPivotCount(int pivotCount)
    {
       _pivotCount = pivotCount;
    }

    /**
     *
     * @return
     */
    public int getPivotCount()
    {
        return _pivotCount;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status)
    {
        _status = status;
    }

    /**
     * Returns the status of the tas request.
     *
     * @return The status description.
     */
    public String getStatus()
    {
        return _status;
    }

    /**
     * 
     * @return
     */
    public List<TaSRecord> getAllRecords()
    {
        return _allRecords;
    }

    /**
     *
     * @return
     */
    public List<NBBORecord> getNBBORecrods()
    {
        if (_nbboRecords == null)
        {
            _nbboRecords = new ArrayList<NBBORecord>();
            for (TaSRecord record : _allRecords)
            {
                // Check if it is a "B" record and add it to the list.
                if (record.getType().equals("B"))
                {
                    _nbboRecords.add((NBBORecord)record);
                }
            }
        }

        return _nbboRecords;
    }

    /**
     *
     * @return
     */
    public List<QuoteRecord> getQuoteRecrods()
    {
        if (_quoteRecords == null)
        {
            _quoteRecords = new ArrayList<QuoteRecord>();
            for (TaSRecord record : _allRecords)
            {
                // Check if it is a "Q" record and add it to the list.
                if (record.getType().equals("Q"))
                {
                    _quoteRecords.add((QuoteRecord)record);
                }
            }
        }

        return _quoteRecords;
    }

    /**
     *
     * @return
     */
    public List<TradeRecord> getTradeRecrods()
    {
        if (_tradeRecords == null)
        {
            _tradeRecords = new ArrayList<TradeRecord>();
            for (TaSRecord record : _allRecords)
            {
                // Check if it is a "T" record and add it to the list.
                if (record.getType().equals("T"))
                {
                    _tradeRecords.add((TradeRecord)record);
                }
            }
        }

        return _tradeRecords;
    }

    /**
     * 
     * @param quote
     */
    public void add(TaSRecord newrec)
    {
        _allRecords.add(newrec);
    }
}
