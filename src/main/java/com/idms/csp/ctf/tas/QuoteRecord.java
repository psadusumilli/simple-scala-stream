/*
 * QuoteRecord.java
 */

package com.idms.csp.ctf.tas;

/**
 * A sample quote record:
 *
 *  <CTFMessage>
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
 */
public class QuoteRecord extends TaSRecord
{
    // The bid price.
    double _bidPrice = Double.NaN;

    // The bid size.
    int _bidSize;

    // The ask price.
    double _askPrice = Double.NaN;

    // The ask size.
    int _askSize;

    // The quote condition 1.
    int _quoteCondition1;

    // The quote condition 2.
    int _quoteCondition2;

    // The quote condition 4.
    int _quoteCondition4;

    /**
     *
     */
    public QuoteRecord()
    {
        super(TaSRecord.RECTYPE_QUOTE);
    }

    /**
     *
     * @param type
     */
    public QuoteRecord(String type)
    {
        super(type);
    }

    @Override
    public Object clone()
    {
        return (QuoteRecord) super.clone();
    }

    /**
     *
     * @param newrec
     */
    public void copyto(QuoteRecord newrec)
    {
        if (newrec != null)
        {
            super.copyto(newrec);
            newrec.setAskPrice(_askPrice);
            newrec.setAskSize(_askSize);
            newrec.setBidPrice(_bidPrice);
            newrec.setBidSize(_askSize);
            newrec.setQuoteCondition1(_quoteCondition1);
            newrec.setQuoteCondition2(_quoteCondition2);
            newrec.setQuoteCondition4(_quoteCondition4);
        }
    }

    /**
     *
     * @param price
     */
    public void setBidPrice(double price)
    {
        _bidPrice = price;
    }

    /**
     *
     * @return
     */
    public double getBidPrice()
    {
        return _bidPrice;
    }

    /**
     *
     * @param size
     */
    public void setBidSize(int size)
    {
        _bidSize = size;
    }

    /**
     *
     * @return
     */
    public int getBidSize()
    {
        return _bidSize;
    }

    /**
     *
     * @param price
     */
    public void setAskPrice(double price)
    {
        _askPrice = price;
    }

    /**
     *
     * @return
     */
    public double getAskPrice()
    {
        return _askPrice;
    }

    /**
     *
     * @param size
     */
    public void setAskSize(int size)
    {
        _askSize = size;
    }

    /**
     *
     * @return
     */
    public int getAskSize()
    {
        return _askSize;
    }

    /**
     *
     * @param qc
     */
    public void setQuoteCondition1(int qc)
    {
        _quoteCondition1 = qc;
    }

    /**
     *
     * @return
     */
    public int getQuoteCondition1()
    {
        return _quoteCondition1;
    }

    /**
     *
     * @param qc
     */
    public void setQuoteCondition2(int qc)
    {
        _quoteCondition2 = qc;
    }

    /**
     *
     * @return
     */
    public int getQuoteCondition2()
    {
        return _quoteCondition2;
    }


    /**
     *
     * @param qc
     */
    public void setQuoteCondition4(int qc)
    {
        _quoteCondition4 = qc;
    }

    /**
     *
     * @return
     */
    public int getQuoteCondition4()
    {
        return _quoteCondition4;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("Quote => {");

        sb.append("Time = ").append(_activityDateTime);
        sb.append(", Seq No. = ").append(_tasSeq);
        sb.append(", Bid Price = ").append(_bidPrice);
        sb.append(", Bid Size = ").append(_bidSize);
        sb.append(", Ask Price = ").append(_askPrice);
        sb.append(", Ask Size = ").append(_askSize);
        sb.append(", Quote Cond 1 = ").append(_quoteCondition1);
        sb.append(", Quote Cond 2 = ").append(_quoteCondition2);
        sb.append(", Quote Cond 4 = ").append(_quoteCondition4);

        sb.append("}");

        return sb.toString();
    }
}
