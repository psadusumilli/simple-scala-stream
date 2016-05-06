/*
 * TradeRecord.java
 */

package com.idms.csp.ctf.tas;

/**
 * A sample trade record:
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
 */
public class TradeRecord extends TaSRecord
{
    // The trade price.
    double _tradePrice = Double.NaN;

    // The trade size.
    int _tradeSize;

    // The trade sequence number.
    int _tradeSeq;

    // The trade condition.
    int _tradeCondition1;

    // The VWAP
    double _vwap = Double.NaN;

    /**
     *
     */
    public TradeRecord()
    {
        super(TaSRecord.RECTYPE_TRADE);
    }

    @Override
    public Object clone()
    {
        return (TradeRecord) super.clone();
    }

    /**
     *
     * @param newrec
     */
    public void copyto(TradeRecord newrec)
    {
        if (newrec != null)
        {
            super.copyto(newrec);
            newrec.setTradeSeq(_tradeSeq);
            newrec.setTradePrice(_tradePrice);
            newrec.setTradeSize(_tradeSize);
            newrec.setVWAP(_vwap);
            newrec.setTradeCondition1(_tradeCondition1);
        }
    }

    /**
     *
     * @param price
     */
    public void setTradePrice(double price)
    {
        _tradePrice = price;
    }

    /**
     *
     * @return
     */
    public double getTradePrice()
    {
        return _tradePrice;
    }

    /**
     *
     * @param size
     */
    public void setTradeSize(int size)
    {
        _tradeSize = size;
    }

    /**
     *
     * @return
     */
    public int getTradeSize()
    {
        return _tradeSize;
    }

    /**
     *
     * @param tc
     */
    public void setTradeCondition1(int tc)
    {
        _tradeCondition1 = tc;
    }

    /**
     *
     * @return
     */
    public int getTradeCondition1()
    {
        return _tradeCondition1;
    }

    /**
     *
     * @param seqno
     */
    public void setTradeSeq(int seqno)
    {
        _tradeSeq = seqno;
    }

    /**
     *
     * @return
     */
    public int getTradeSeq()
    {
        return _tradeSeq;
    }

    /**
     *
     * @param vwap
     */
    public void setVWAP(double vwap)
    {
        _vwap = vwap;
    }

    /**
     *
     * @return
     */
    public double getVWAP()
    {
        return _vwap;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("Trade => {");

        sb.append("Time = ").append(_activityDateTime);
        sb.append(", TaS Seq No. = ").append(_tasSeq);
        sb.append(", Trade Seq No. = ").append(_tradeSeq);
        sb.append(", Trade Mkt = ").append(_participantCode);
        sb.append(", Trade Price = ").append(_tradePrice);
        sb.append(", Trade Size = ").append(_tradeSize);
        sb.append(", VWAP = ").append(_vwap);
        sb.append(", Trade Cond 1 = ").append(_tradeCondition1);

        sb.append("}");

        return sb.toString();
    }
}
