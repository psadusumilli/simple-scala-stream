/*
 * DataDict.java
 */

package com.idms.csp.ctf.data;

import java.io.*;
import java.util.*;

/**
 * This class contains the definitions for tokens transmitted in CTF messages
 * and their enumeration definitions. This class can't be instantiated. It
 * provides an <code> init() </code> function to load all the token defintions
 * and enumeration definitions from the files "tokens.dat" and "enums.dat" which
 * are distributed as part of this package. The token definitions and 
 * enumerations definitions can also be loaded from files outside the package
 * using a variant of <code> init() </code> function that accepts the file
 * names for both the definitions.
 * <p>
 * Users are required to download the latest tokens and enumerations from Plus
 * Feed servers frequently to take advantage of new data introduced in the feed.
 * <p>
 * It is mandatory to initialize this class before using the CTF API as follows:
 * <br> <code> DataDict.init();
 * 
 */
public class DataDict 
{
    // Default location of files for token and enum definitions
    public static final String DEFAULT_TOKEN_SRC = "ctf/data/tokens.dat";
    public static final String DEFAULT_ENUM_SRC = "ctf/data/enums.dat";

    // Default location of market cneter codes file
    public static final String DEFAULT_MCCODES_SRC = "ctf/data/mccodes.dat";
    
    // Default location of sale and quote condition codes file
    public static final String DEFAULT_CCODES_SRC = "ctf/data/ccodes.dat";

    // The collection of Token Objects keyed on Token Number
    private static Map<Integer, Token> _tokenNumMap 
            = Collections.synchronizedSortedMap(new TreeMap<Integer, Token>());
    
    // The collection of Token Objects keyed on Token Name
    private static Map<String, Token> _tokenNameMap 
            = Collections.synchronizedSortedMap(new TreeMap<String, Token>());

    // The collection of Enum Objects for SRC.ID (Exchange ID)
    private static Map<Integer, Exchange> _exchMap 
            = Collections.synchronizedSortedMap(new TreeMap<Integer, Exchange>());
    
    // The collection of Enum Objects for CURRENCY
    private static Map<String, Currency> _currencyMap 
            = Collections.synchronizedSortedMap(new TreeMap<String, Currency>());

    // The collection of Enum Objects for QUERY.STATUS
    private static Map<Integer, Status> _statusMap 
            = Collections.synchronizedSortedMap(new TreeMap<Integer, Status>());

    // The Dictionary Version
    private static Enum _version = null;

    // Market Center Codes.
    private static Properties _marketCenterCodeMap = new Properties();

    // The map of condition maps. One for each condition token.
    private static Map<Integer, Map<String, String>> _conditionMaps
            = Collections.synchronizedSortedMap(new TreeMap<Integer, Map<String, String>>());

    /**
     *
     */
    private DataDict()
    {
    }

    /**
     * Initialize the data dictionary by loading token definitions from the
     * file "tokens.dat" and enumeration definitions from the file "enums.dat".
     * The files are located by default in the ctf.data package.
     */
    public static void init()
    {
        init(null);
    }

    /**
     * Use this to initialze data dictionary from the files located at the
     * specified path.
     *
     * The token definitions are read from the file "tokens.dat"
     * The enumeration definitions from the file "enums.dat".
     * The sale and quote condition codes from the file "ccodes.dat".
     * The market center codes from the file mccodes.dat.
     *
     * @param filepath The location of the files.
     */
    public static void init(String filepath)
    {
        InputStream is = null;

        // Try to load CTF Token Definitions from the file tokens.dat
        if (filepath != null)
        {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filepath + "/tokens.dat");
        }

        if (is == null)
        {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_TOKEN_SRC);
        }

        if (is != null)
        {
            loadTokens(is);
        }

        // Try to load CTF Enumeration Definitions from the file enums.dat
        is = null;
        if (filepath != null)
        {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filepath + "/enums.dat");
        }

        if (is == null)
        {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_ENUM_SRC);
        }

        if (is != null)
        {
            loadEnums(is);
        }

        // Try to load Market Center Codes.
        is = null;
        if (filepath != null)
        {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filepath + "/mccodes.dat");
        }

        if (is == null)
        {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_MCCODES_SRC);
        }

        if (is != null)
        {
            loadMarketCenterCodes(is);
        }

        // Try to load Sale and Quote Condition Codes.
        is = null;
        if (filepath != null)
        {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filepath + "/ccodes.dat");
        }

        if (is == null)
        {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_CCODES_SRC);
        }

        if (is != null)
        {
            loadConditionCodes(is);
        }
    }

    /**
     * Initialized the token and enumerations definitions from the files
     * specified. Default definitions from the api jar file are load if the
     * files are not found.
     *
     * @param tokensFile The file containing token definitions.
     * @param enumsFile The file containing enumerations definitions.
     */
    public static void init(String tokensFile, String enumsFile)
    {
        InputStream is = null;

        try
        {
            // Open tokens file
            is = new FileInputStream(tokensFile);
            loadTokens(is);

            // Open enums file
            is = new FileInputStream(enumsFile);
            loadEnums(is);
        }
        catch (FileNotFoundException ex)
        {
            // File(s) not found. Load default tokens and enums
            init();
        }
    }

    /**
     * Loads token definitions from an input stream. The stream is expected
     * to contains lines of CTF messages. For e.g. <br>
     *
     * 5011=20|5010=PERMISSION|5002=1|5012=INTEGER|5035=3|5026=1|
     *
     * @param is The InputStream that contains the token definitions.
     */
    public static void loadTokens(InputStream is)
    {
        BufferedReader in
                = new BufferedReader(new InputStreamReader(is));

        try
        {
            HashMap tvmap = new HashMap();
            String line = null;

            // Read a line of CTF Token Definition
            while ( (line = in.readLine()) != null )
            {
                tvmap.clear();
                StringTokenizer st = new StringTokenizer(line, "|");
                while (st.hasMoreTokens())
                {
                    String[] tvpair = st.nextToken().split("=");
                    tvmap.put(tvpair[0], tvpair[1]);
                }

                String toknum = (String) tvmap.get("5035");
                String tokname = (String) tvmap.get("5010");
                String toksize = (String) tvmap.get("5011");
                String toktype = (String) tvmap.get("5012");
                String tokstore = (String) tvmap.get("5002");

                if (toknum != null && tokname != null)
                {
                    setToken(new Token(toknum, tokname, toktype, toksize, tokstore));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Loads enumeration definitions from an input stream. The stream is expected
     * to contains lines of CTF messages. For e.g. <br>
     * 5033=AMEX - Composite Only|5034=545|5035=201|5026=1|
     *
     * @param is The InputStream that contains the enumeration definitions.
     */
    public static void loadEnums(InputStream is)
    {
        BufferedReader in
                = new BufferedReader(new InputStreamReader(is));

        try
        {
            HashMap tvmap = new HashMap();
            String line = null;

            // Read a line of Enum Definition
            while ( (line = in.readLine()) != null )
            {
                tvmap.clear();
                StringTokenizer st = new StringTokenizer(line, "|");
                while (st.hasMoreTokens())
                {
                    String[] tvpair = st.nextToken().split("=");
                    tvmap.put(tvpair[0], tvpair[1]);
                }

                String toknum = (String) tvmap.get("5035");
                String enumval = (String) tvmap.get("5034");
                String enumdesc = (String) tvmap.get("5033");

                if (toknum != null)
                {
                    setEnum(new Enum(toknum, enumval, enumdesc));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Loads enumeration definitions for sale and quote conditions from an input stream.
     * The stream is expected to contains lines of CTF messages. For e.g. <br>
     * 5033=Regular sale|5034=0|5035=2500|5026=1|
     *
     * @param is The InputStream that contains the enumeration definitions.
     */
    public static void loadConditionCodes(InputStream is)
    {
        BufferedReader in
                = new BufferedReader(new InputStreamReader(is));

        try
        {
            HashMap tvmap = new HashMap();
            String line = null;

            // Read a line of Enum Definition
            while ( (line = in.readLine()) != null )
            {
                tvmap.clear();
                StringTokenizer st = new StringTokenizer(line, "|");
                while (st.hasMoreTokens())
                {
                    String[] tvpair = st.nextToken().split("=");
                    tvmap.put(tvpair[0], tvpair[1]);
                }

                String toknum = (String) tvmap.get("5035");
                String enumval = (String) tvmap.get("5034");
                String enumdesc = (String) tvmap.get("5033");

                if (toknum != null && enumval != null && enumdesc != null)
                {
                    // Create a condition map for this token if one doesn't exist.
                    Map<String, String> cmap = _conditionMaps.get(Integer.valueOf(toknum));
                    if (cmap == null)
                    {
                        _conditionMaps.put(Integer.valueOf(toknum), cmap=new TreeMap<String, String>());
                    }

                    // Add the condition definition to the map
                    cmap.put(enumval, enumdesc);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param is
     */
    public static void loadMarketCenterCodes(InputStream is)
    {
        try
        {
            _marketCenterCodeMap.load(is);
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    /**
     * Returns the token object represented by the token name.
     *
     * @param name The token name.
     * @return The Token object.
     */
    public static Token getToken(String name)
    {
        return _tokenNameMap.get(name);
    }

    /**
     * Returns th token object represented by the token number.
     *
     * @param num The token number.
     * @return The Token object.
     */
    public static Token getToken(int num)
    {
        return _tokenNumMap.get(Integer.valueOf(num));
    }

    /**
     * Returns the exchange object represented by the exchange id.
     *
     * @param id The exchange id.
     * @return The Exchange object.
     */
    public static Exchange getExchange(int id)
    {
        return _exchMap.get(Integer.valueOf(id));
    }

    /**
     * Returns the currency object represented by the octal code.
     *
     * @param code The octal code.
     * @return The Currency object.
     */
    public static Currency getCurrency(String code)
    {
        return _currencyMap.get(code);
    }

    /**
     * Returns the status object associated with the status code.
     *
     * @param code The status code
     * @return The Status object.
     */
    public static Status getStatus(int code)
    {
        return _statusMap.get(Integer.valueOf(code));
    }

    /**
     * Returns the version of the currency data dictionary.
     *
     * @return The version.
     */
    public static String getVersion()
    {
        if (_version != null)
        {
            return _version.enumdesc;
        }

        return null;
    }

    /**
     * Creates or updates the token definition.
     *
     * @param token
     */
    public static void setToken(Token token)
    {
        _tokenNumMap.put(Integer.valueOf(token.num), token);
        _tokenNameMap.put(token.name, token);
    }

    /**
     * Creates or updates the enumeration definition.
     *
     * @param enm The enumeration object
     */
    public static void setEnum(Enum enm)
    {
        if (enm.toknum == 201)
        {
            // Collect Exchange Definitions
            _exchMap.put(Integer.valueOf(enm.enumval), new Exchange(enm.toknum, enm.enumval, enm.enumdesc));
        }
        else if (enm.toknum ==262)
        {
            // Collect Currency Definitions
            _currencyMap.put(String.valueOf(enm.enumval), new Currency(enm.toknum, enm.enumval, enm.enumdesc));
        }
        else if (enm.toknum == 5025)
        {
            // Collect Status Definitions
            _statusMap.put(Integer.valueOf(enm.enumval), new Status(enm.toknum, enm.enumval, enm.enumdesc));
        }
        else if (enm.toknum == 5051)
        {
            // Save the Dictionary Version
            _version = enm;
        }
        // TODO: What about enumerations for rest of the tokens ?
    }

    /**
     * 
     * @return
     */
    public static Collection<Exchange> getExchanges()
    {
        return Collections.unmodifiableCollection(_exchMap.values());
    }
    
    /**
     * 
     * @return
     */
    public static Collection<Token> getTokens()
    {
       return Collections.unmodifiableCollection(_tokenNumMap.values());
    }

    /**
     *
     * @return
     */
    public static Collection<Currency> getCurrencies()
    {
       return Collections.unmodifiableCollection(_currencyMap.values());
    }

    /**
     *
     * @return
     */
    public static Properties getMarketCenterCodeMap()
    {
        return _marketCenterCodeMap;
    }

    /**
     * 
     * @param toknum
     * @return
     */
    public static Map<String, String> getConditionMap(int toknum)
    {
       return _conditionMaps.get(Integer.valueOf(toknum));
    }

    /**
     * 
     * @param tokname
     * @return
     */
    public static Map<String, String> getConditionMap(String tokname)
    {
       Token token = getToken(tokname);

       if (token != null)
       {
           return getConditionMap(token.num);
       }

       return null;
    }
    
    /**
     * Prints the data dictionary.
     * 
     * @param out The PrintStream to print to.
     */
    public static void print(PrintStream out)
    {
        out.println("/=========================TOKEN LIST - BEGIN=================================/");
        Set<Integer> tokenSet = _tokenNumMap.keySet();
        for (Integer key : tokenSet)
        {
            out.println(_tokenNumMap.get(key));
        }
        out.println("/=========================TOKEN LIST - END===================================/");

        out.println("/=========================SRD.ID ENUM - BEGIN================================/");
        Set<Integer> exchSet = _exchMap.keySet();
        for (Integer key : exchSet)
        {
            out.println(_exchMap.get(key));
        }
        out.println("/=========================SRD.ID ENUM - END==================================/");

        out.println("/=========================CURRENCY ENUM - BEGIN==============================/");
        Set<String> currencySet = _currencyMap.keySet();
        for (String key : currencySet)
        {
            out.println(_currencyMap.get(key));
        }
        out.println("/=========================CURRENCY ENUM - END================================/");

        out.println("/=========================STATUS ENUM - BEGIN================================/");
        Set<Integer> statusSet = _statusMap.keySet();
        for (Integer key : statusSet)
        {
            out.println(_statusMap.get(key));
        }
        out.println("/=========================STATUS ENUM - END==================================/");

        out.println("/=========================MARKET CENTER CODES - BEGIN================================/");
        _marketCenterCodeMap.list(System.out);
        out.println("/=========================MARKET CENTER CODES - END==================================/");

        out.println("/=========================SALE AND QUOTE CONDITIONS - BEGIN================================/");
        for (Map.Entry<Integer, Map<String, String>> set : _conditionMaps.entrySet())
        {
            out.println(set.getKey() + "->");
            Map<String, String> cmap = set.getValue();
            for (Map.Entry<String, String> cond : cmap.entrySet())
            {
                System.out.println(cond.getKey() + "=" + cond.getValue());
            }
        }
        out.println("/=========================SALE AND QUOTE CONDITIONS - END==================================/");

    }

    /**
     * Main entry point for Java applications.
     * @param args
     */
    public static void main(String[] args) 
    {
        //DataDict.init("C:/tokens.dat", "C:/enums.dat");
        DataDict.init();
        //DataDict.init(".");
System.out.println("Loaded Data Dictionary Version = " + DataDict.getVersion());
        DataDict.print(System.out);
    }
}
