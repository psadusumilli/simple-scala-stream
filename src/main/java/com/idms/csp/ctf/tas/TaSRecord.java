/*
 * TaSRecord.java
 */

package com.idms.csp.ctf.tas;

/**
 *
 */
public abstract class TaSRecord
{
    // TaS Record types - Trade Records, Quote Records, NBBO Records.
    public static final String RECTYPE_QUOTE = "Q";
    public static final String RECTYPE_NBBO = "B";
    public static final String RECTYPE_TRADE = "T";

    // The record type.
    String _type = RECTYPE_QUOTE;

    // The participating exchange or market center code for trade/quote records.
    String _participantCode;

    // The tas sequence number.
    int _tasSeq = -1;

    // The DateTtime of quote or trade in UTC format (CTF DateTime Format).
    long _activityDateTime = -1L;

    /**
     *
     */
    public TaSRecord()
    {
    }

    /**
     *
     * @param type
     */
    public TaSRecord(String type)
    {
        _type = type;
    }

    @Override
    public Object clone()
    {
        try
        {
            return (TaSRecord) super.clone();
        }
        catch (CloneNotSupportedException ex)
        {
        }

        return null;
    }

    /**
     *
     * @param newrec
     */
    public void copyto(TaSRecord newrec)
    {
        if (newrec != null)
        {
            newrec.setType(_type);
            newrec.setParticipantCode(_participantCode);
            newrec.setActivityDateTime(_activityDateTime);
            newrec.setTaSSeq(_tasSeq);
        }
    }

    /**
     *
     * @param participantCode
     */
    public void setParticipantCode(String participantCode)
    {
        _participantCode = participantCode;
    }

    /**
     *
     * @return
     */
    public String getParticipantCode()
    {
        return _participantCode;
    }

    /**
     *
     * @param activityDateTime
     */
    public void setActivityDateTime(long activityDateTime)
    {
       _activityDateTime = activityDateTime;
    }

    /**
     *
     * @return
     */
    public long getActivityDateTime()
    {
        return _activityDateTime;
    }

    /**
     *
     * @param type
     */
    public void setType(String type)
    {
       _type = type;
    }

    /**
     *
     * @return
     */
    public String getType()
    {
        return _type;
    }

    /**
     * 
     * @param seqno
     */
    public void setTaSSeq(int seqno)
    {
        _tasSeq = seqno;
    }

    /**
     *
     * @return
     */
    public int getTaSSeq()
    {
        return _tasSeq;
    }
}
