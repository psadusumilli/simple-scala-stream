/*
 * Token.java
 *
 * Created on February 28, 2008, 7:04 PM
 *
 */

package com.idms.csp.ctf.data;

/**
 * This class encapsulates the CTF token definition.
 */
public class Token 
{
    /** CTF Token Types */
    public static enum Type { INTEGER, STRING, FLOAT, DATETIME, BOOL }
    
    // CTF.TOKEN.NAME
    protected String name = null;
    
    // CTF.TOKEN.NUM
    protected int num;
    
    // CTF.TOKEN.TYPE
    protected Type type = Type.STRING;
    
    // CTF.TOKEN.SIZE
    protected int size;
    
    // CTF.TOKEN.STORE
    protected boolean store;
    
    /**
     * Creates a new instance of Token.
     * 
     * @param num The CTF token number.
     * @param name The CTF token name.
     * @param type The CTF token type.
     * @param size The CTF token store.
     */
    public Token(String num, String name, String type, String size, String store)
    {
        try
        {
            this.num = Integer.valueOf(num);
            this.name = name;
            this.setType(type);
            this.size = Integer.valueOf(size);
            this.store = ("0".equals(store)) ? false : true;
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new instance of token defined in a CTF message. For e.g.
     * <br>5011=20|5010=PERMISSION|5002=1|5012=INTEGER|5035=3|5026=1|
     * 
     * @param msg The CTF message defining the token.
     */
    public Token(Message msg)
    {
        this.num = msg.get(5035).getIntValue();
        this.name = msg.get(5010).getValue();
        this.size = msg.get(5011).getIntValue();
        this.setType(msg.get(5012).getValue());
        this.store = msg.get(5002).getBoolValue();
    }

    /**
     * Sets the token type. Defaults to STRING type.
     * 
     * @param type The type of CTF token. Should be one of - "INTEGER", "FLOAT",
     * "DATETIME", "BOOL", and "STRING".
     */
    public void setType(String type)
    {
        if ("INTEGER".equals(type))
        {
            this.type = Type.INTEGER;
        }
        else if ("FLOAT".equals(type))
        {
            this.type = Type.FLOAT;
        }
        else if ("DATETIME".equals(type))
        {
            this.type = Type.DATETIME;
        }
        else if ("BOOL".equals(type))
        {
            this.type = Type.BOOL;
        }
        else
        {
            this.type = Type.STRING;
        }
    }
    
    /**
     * Returns the token number.
     * 
     * @return The token number.
     */
    public int getNum()
    {
       return this.num; 
    }
    
    /**
     * Returns the token name.
     * 
     * @return The token name.
     */
    public String getName()
    {
       return this.name; 
    }
    
    /**
     * Returns the token type.
     * 
     * @return The token type.
     */
    public Type getType()
    {
       return this.type; 
    }
    
    /**
     * Returns token size.
     * 
     * @return The token size.
     */
    public int getSize()
    {
       return this.size; 
    }
    
    /**
     * Returns the token store.
     * 
     * @return The token store.
     */
    public boolean getStore()
    {
       return this.store; 
    }
    
    /**
     * Returns the string representation of this token.
     * 
     * @return The string representation of this token.
     */
    @Override
    public String toString()
    {
        return "CTF Token [num="+num+",name="+name+",type="+type+",size="+size+",store="+store+"]";
    }
}
