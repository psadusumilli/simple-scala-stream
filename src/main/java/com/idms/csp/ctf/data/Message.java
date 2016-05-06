/*
 * Message.java
 *
 * Created on February 27, 2008, 11:56 AM
 *
 */

package com.idms.csp.ctf.data;

import com.idms.csp.ctf.util.Util;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class represents the CTF message.
 */
public class Message 
{
    // CTF Frame Constants
    public static final byte CTF_FRAME_START         = 0x04;
    public static final byte CTF_PROTOCOL_SIGNATURE  = 0x20;
    public static final byte CTF_FRAME_END           = 0x03;
    
    // Length of CTF Header and Trailer
    public static final int CTF_FRAME_OVERHEAD_SIZE = 7;

    // The Fields
    private List<Field> _fields 
            = Collections.synchronizedList(new ArrayList<Field>());
            
    /**
     * Default constructor.
     */
    public Message() 
    {
        
    }

    /**
     * Constructs a message object given a CTF message. For e.g.
     * <br>5011=20|5010=PERMISSION|5002=1|5012=INTEGER|5035=3|5026=1|
     * 
     * @param msg The CTF message.
     */
    public Message(String msg)
    {
        StringTokenizer st = new StringTokenizer(msg, "|");
        while (st.hasMoreTokens())
        {
            String[] tvpair = st.nextToken().split("=",2);
            try
            {
                // Missing value ?
                if (tvpair.length == 2)
                {
                    add(Integer.valueOf(tvpair[0]), tvpair[1]);
                }
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the field associated with the token number. For e.g. To obtain
     * the current price field whose token number is "14":
     * <br><code>Field lastPrice = msg.get(14);</code>
     * 
     * @param toknum The token number.
     * @return  The Field object.
     */
    public Field get(int toknum)
    {
        // Find the token definition in the Data Dict by number
        Token token = DataDict.getToken(toknum);

        if (token != null)
        {
            return find(token);
        }
        
        return null;
    }
    
    /**
     * Returns the field associated with the token name. For e.g. To obtain the
     * current price field whose token number is "CURRENT.PRICE":
     * <br><code>Field lastPrice = msg.get("CURRENT.PRICE");</code>
     * 
     * @param tokname The tokne name.
     * @return The Field object.
     */
    public Field get(String tokname)
    {
        // Find the token definition in the Data Dict by name
        Token token = DataDict.getToken(tokname);

        if (token != null)
        {
            return find(token);
        }
        
        return null;
    }

    /**
     * Returns all the fields associated with the token name. For e.g. To 
     * obtain all the fields that contain permission token "PERMISSION":
     * <br><code>Field[] perms = msg.getAll("PERMISSION");</code>
     * 
     * @param tokname The token name.
     * @return The array of field objects.
     */
    public Field[] getAll(String tokname)
    {
        // Find the token definition in the Data Dict by name
        Token token = DataDict.getToken(tokname);
        
        if (token != null)
        {
            return getAll(token.num);
        }
        
        return new Field[0];
    }
    
    /**
     * Returns all the fields associated with the token number. For e.g. To 
     * obtain all the fields that contain permission token "3":
     * <br><code>Field[] perms = msg.getAll(3);</code>
     * 
     * @param toknum The token number.
     * @return The array of field objects.
     */
    public Field[] getAll(int toknum)
    {
        List flist = new ArrayList();
        
        // Find the token definition in the Data Dict by num
        Token token = DataDict.getToken(toknum);

        if (token != null)
        {
            // Search for the token in the fields
            synchronized(_fields)
            {
                for (Field field : _fields)
                {
                    // Collect all the matching tokens
                    if (field.getToken().num == token.num)
                    {
                        flist.add(field);
                    }
                }
            }
        }
        
        return (Field[]) flist.toArray(new Field[flist.size()]);
    }

    /**
     * Returns all the fields in this message.
     * 
     * @return The array of field objects.
     */
    public Field[] getAll()
    {
        return (Field[]) _fields.toArray(new Field[_fields.size()]);
    }
    
    /**
     * Searches for a field that is associated with the given token. Returns
     * the first matching field.
     * 
     * @param token The Token object to search for.
     * @return The Field object.
     */
    public Field find(Token token)
    {
        // Search for the token in the fields
        synchronized(_fields)
        {
            for (Field field : _fields)
            {
                // Return the first field found
                if (field != null && field.getToken().num == token.num)
                {
                    return field;
                }
            }
        }

        return null;
    }
    
    /**
     * Returns a CTF formatted message represented by this object.
     * 
     * @return The string representing this message.
     */
    public String getRawMessage()
    {
        String msg = "";
        StringBuffer sb = new StringBuffer();

        synchronized(_fields)
        {
            for (Field field : _fields)
            {
                sb.append(field.getToken().num).append("=").append(field.getValue());
                sb.append("|");
            }
        }

        // Remove the last "|" character. Some versions of CSPs are not accepting it.
        try
        {
            msg = sb.substring(0, sb.length()-1);
        }
        catch (StringIndexOutOfBoundsException ex)
        {
            
        }

        return msg;
    }
    
    /**
     * Converts this object into a byte stream after applying the CTF protocol
     * headers and trailers.
     * 
     * @return The byte stream representing this CTF message.
     */
    public byte[] serialize()
    {
        String msg = this.getRawMessage();
        byte[] data = msg.getBytes();
        
        // Create the CTF command
        ByteBuffer cmdBuffer = ByteBuffer.allocate(data.length+Message.CTF_FRAME_OVERHEAD_SIZE);

        // Frame Start, Protocol Signature
        cmdBuffer.put(Message.CTF_FRAME_START).put(Message.CTF_PROTOCOL_SIGNATURE);

        // Payload size, Payload
        cmdBuffer.putInt(data.length).put(data);

        // Frame End
        cmdBuffer.put(Message.CTF_FRAME_END);
        
        return cmdBuffer.array();
    }

    /**
     * Clears all the tokens from this message.
     */
    public void clear()
    {
       _fields.clear(); 
    }
    
    /**
     * Returns the field that contains ENUM.QUERY.STATUS token if present.
     * 
     * @return The status field.
     */
    public Status getStatus()
    {
        Field field = get(5001);
        if (field != null)
        {
            return DataDict.getStatus(field.getIntValue());
        }
        
        return null;
    }
    
    /**
     * Returns the string representation of this message.
     * 
     * @return The string representing this message.
     */
    @Override
    public String toString()
    {
        return "[" + this.getRawMessage() + "]";
    }

    /**
     * Prints this message in CTF format.
     * 
     * @param out The PrintStream to print to.
     */
    public void printCTF(PrintStream out)
    {
        out.println(this.getRawMessage());
    }
    
    /**
     * Prints this message in XML format.
     * 
     * @param out The PrintStream to print to.
     */
    public void printXML(PrintStream out)
    {
        out.println("<CTFMessage>");

        synchronized(_fields)
        {
            for (Field field : _fields)
            {
                field.print(out);
            }
        }
        out.println("</CTFMessage>");
    }
    
    /**
     * Prints this message. Defaults to XML format.
     * 
     * @param out The PrintStream to print to.
     */
    public void print(PrintStream out)
    {
        printXML(out);
    }

    /**
     * Prints this message in CSV format.
     * 
     * @param out The PrintStream to print to.
     */
    public void printCSV(PrintStream out)
    {
        StringBuffer sb = new StringBuffer();

        synchronized(_fields)
        {
            for (Field field : _fields)
            {
                sb.append(field.getValue());
                sb.append(",");
            }
        }

        // Remove the last "," character.
        try
        {
            out.println(sb.substring(0, sb.length()-1));
        }
        catch (StringIndexOutOfBoundsException ex)
        {
            
        }
    }
    
    /**
     * Adds a field to this message given the token number and token value. For
     * e.g. To add ticker symbol "IBM" to the message:
     * <br><code>Field sym = msg.add(5, "IBM");
     * 
     * @param toknum The token number.
     * @param value The token value.
     * @return The Field object that has just been created.
     */
    public Field add(int toknum, String value)
    {
        Field field = null;
        Token token = DataDict.getToken(toknum);
        if (token != null)
        {
            _fields.add(field = new Field(token, value));
        }
        
        return field;
    }

    /**
     * Adds a field to this message given the token name and token value. For
     * e.g. To add ticker symbol "IBM" to the message:
     * <br><code>Field sym = msg.add("SYMBOL.TICKER", "IBM");
     * 
     * @param tokname The token name.
     * @param value The token value.
     * @return The Field object that has just been created.
     */
    public Field add(String tokname, String value)
    {
        Field field = null;
        Token token = DataDict.getToken(tokname);
        if (token != null)
        {
            _fields.add(field = new Field(token, value));
        }
        
        return field;
    }
 
    /**
     * Adds a field to this message given the token number and integer token 
     * value. For e.g. To add exchange id 564 to the message:
     * <br><code>Field sym = msg.add(4, 564);
     * 
     * @param toknum The token number.
     * @param value The token value.
     * @return The Field object that has just been created.
     */
    public Field add(int toknum, int value)
    {
        return add(toknum, String.valueOf(value));
    }
    
    /**
     * Checks if the message is a REFRESH (cycle) message.
     * 
     * @return {@code true} if it is a refresh or cycle message, {@code false
     * otherwise}
     */
    public boolean isRefresh()
    {
        return Util.isRefresh(this);
    }
    
    /**
     * 
     * @return
     */
    public boolean isTrade()
    {
        return Util.isTrade(this);
    }
    
    /**
     * 
     * @return
     */
    public boolean isQuote()
    {
        return Util.isQuote(this);
    }
    
    /**
     * 
     * @return
     */
    public boolean isLevel1()
    {
        return Util.isLevel1(this);
    }
    
    /**
     * 
     * @return
     */
    public boolean isLevel2()
    {
        return Util.isLevel2(this);
    }

    /**
     * 
     * @return
     */
    public boolean isStatus()
    {
        return Util.isStatus(this);
    }

    /**
     * 
     * @return
     */
    public String toXML()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append("<CTFMessage>");

        synchronized(_fields)
        {
            for (Field field : _fields)
            {
                sb.append(Util.formatXML(field));
            }
        }
        
        sb.append("</CTFMessage>");
        
        return sb.toString();
    }
    
    /**
     * 
     * @param token
     * @return
     */
    public String toXML(String[] token)
    {
       return "<CTFMessage>" + Util.formatXML(this, token) + "</CTFMessage>"; 
    }

    /**
     * 
     * @return
     */
    public boolean isCall()
    {
        Field sym = this.get("SYMBOL.TICKER");
        
        if (sym != null)
        {
            return Util.isCall(sym.getValue());
        }
        
        return false;
    }
    
    /**
     * 
     * @return
     */
    public boolean isPut()
    {
        Field sym = this.get("SYMBOL.TICKER");
        
        if (sym != null)
        {
            return Util.isPut(sym.getValue());
        }
        
        return false;
    }
    
    /**
     * Main entry point for Java applications.
     * @param args
     */
    public static void main(String[] args) 
    {
        DataDict.init();
        //DataDict.print(System.out);
        
        Message msg = new Message("5026=1|4=9|5=INTC|12=32.08|13=5|355=BTRD|2001=25");
        msg.add(355, "INST");
        msg.print(System.out);
        System.out.println("Ticker Symbol = " + msg.get("SYMBOL.TICKER").getValue());
        System.out.println("Last Price = " + msg.get("TRADE.PRICE").getValue());
        
        System.out.println(new String(msg.serialize()));
    }
}
