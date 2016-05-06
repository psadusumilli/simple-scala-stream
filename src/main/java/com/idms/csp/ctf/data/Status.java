/*
 * Status.java
 */

package com.idms.csp.ctf.data;

/**
 * This class encapsulates the enumerated definition for status messages. 
 * For e.g. Login failure status object is created as follows:<br>
 * <code>Status status = new Status("5025", "-5", "ERR.AUTH.LOGIN");
 */
public class Status extends Enum
{
    /**
     * Creates a status object given the CTF.TOKEN.NUM for ENUM.QUERY.STATUS (5025), 
     * CTF.ENUM.VALUE, CTF.ENUM.DESC.
     * 
     * @param toknum The CTF Token Number used to represent ENUM.QUERY.STATUS (5025)
     * @param enumval The Enumerated Status Value
     * @param enumdesc The Enumerated Status Description
     */
    public Status(int toknum, int enumval, String enumdesc)
    {
        super(toknum, enumval, enumdesc);
    }
    
    /**
     * Creates a status object given the CTF.TOKEN.NUM for ENUM.QUERY.STATUS (5025), 
     * CTF.ENUM.VALUE, CTF.ENUM.DESC.
     * 
     * @param toknum The CTF Token Number used to represent ENUM.QUERY.STATUS (5025)
     * @param enumval The Enumerated Status Value
     * @param enumdesc The Enumerated Status Description
     */
    public Status(String toknum, String enumval, String enumdesc)
    {
        super(toknum, enumval, enumdesc);
    }
}
