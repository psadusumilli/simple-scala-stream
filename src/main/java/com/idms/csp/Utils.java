/**
 * Created by padusumilli on 4/2/2016.
 */

package com.idms.csp;

import java.nio.ByteOrder;

public class Utils {
    public static byte[] htonl(int x)
    {
        byte[] res = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            res[i] = (new Integer(x >>> 24)).byteValue();
            x <<= 8;
        }
        return res;
    }

}
