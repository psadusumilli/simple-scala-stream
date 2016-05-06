/*
 * NBBORecord.java
 */

package com.idms.csp.ctf.tas;

/**
 * A sample nbbo record:
 *
 *  <CTFMessage>
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
public class NBBORecord extends QuoteRecord
{
    // The participating exchange or market center code for bid side.
    String _bidParticipantCode;

    // The participating exchange or market center code for ask side.
    String _askParticipantCode;

    /**
     *
     */
    public NBBORecord()
    {
        super(TaSRecord.RECTYPE_NBBO);
    }

    @Override
    public Object clone()
    {
        return (NBBORecord) super.clone();
    }

    /**
     *
     * @param newrec
     */
    public void copyto(NBBORecord newrec)
    {
        if (newrec != null)
        {
            super.copyto(newrec);
            newrec.setAskParticipantCode(_askParticipantCode);
            newrec.setBidParticipantCode(_bidParticipantCode);
        }
    }

    /**
     *
     * @param participantCode
     */
    public void setBidParticipantCode(String participantCode)
    {
       _bidParticipantCode = participantCode;
    }

    /**
     *
     * @return
     */
    public String getBidParticipantCode()
    {
        return _bidParticipantCode;
    }

    /**
     *
     * @param participantCode
     */
    public void setAskParticipantCode(String participantCode)
    {
       _askParticipantCode = participantCode;
    }

    /**
     *
     * @return
     */
    public String getAskParticipantCode()
    {
        return _askParticipantCode;
    }

    /**
     * 
     * @return
     */
    public double getBidAskSpread()
    {
        return (_askPrice - _bidPrice);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("NBBO => {");

        sb.append("Time = ").append(_activityDateTime);
        sb.append(", Seq No. = ").append(_tasSeq);
        sb.append(", Bid Mkt = ").append(_bidParticipantCode);
        sb.append(", Bid Price = ").append(_bidPrice);
        sb.append(", Bid Size = ").append(_bidSize);
        sb.append(", Ask Mkt = ").append(_askParticipantCode);
        sb.append(", Ask Price = ").append(_askPrice);
        sb.append(", Ask Size = ").append(_askSize);
        sb.append(", Bid Ask Spread = ").append(getBidAskSpread());

        sb.append("}");

        return sb.toString();
    }
}
