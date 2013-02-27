/**
 * Back 2 Browser Bytecode Translator
 * Copyright (C) 2012 Jaroslav Tulach <jaroslav.tulach@apidesign.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://opensource.org/licenses/GPL-2.0.
 */
package org.apidesign.vm4brwsr;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Jaroslav Tulach <jtulach@netbeans.org>
 */
public class NumberTest {
    @Test public void integerFromString() throws Exception {
        assertExec("Can convert string to integer", Integer.class, "parseInt__ILjava_lang_String_2",
            Double.valueOf(333), "333"
        );
    }

    @Test public void doubleFromString() throws Exception {
        assertExec("Can convert string to double", Double.class, "parseDouble__DLjava_lang_String_2",
            Double.valueOf(33.3), "33.3"
        );
    }

    @Test public void autoboxDouble() throws Exception {
        assertExec("Autoboxing of doubles is OK", Numbers.class, "autoboxDblToString__Ljava_lang_String_2",
            "3.3"
        );
    }
    
    @Test public void javalog1000() throws Exception {
        assertEquals(3.0, Math.log10(1000.0), 0.00003, "log_10(1000) == 3");
    }

    @Test public void jslog1000() throws Exception {
        assertExec("log_10(1000) == 3", Math.class, "log10__DD", 
            Double.valueOf(3.0), 1000.0
        );
    }
    
    @Test public void javaRem() {
        assertEquals(3, Numbers.rem(303, 10));
    }
    @Test public void jsRem() throws Exception {
        assertExec("Should be three", Numbers.class, "rem__III", 
            Double.valueOf(3.0), 303, 10
        );
    }
    
    @Test public void deserializeInt() throws Exception {
        int exp = Numbers.deserInt();
        assertExec("Should be the same", Numbers.class, "deserInt__I", 
            Double.valueOf(exp)
        );
    }

    @Test public void deserializeSimpleLong() throws Exception {
        assertExec("Should be 3454", Numbers.class, "deserLong__J_3B", 
            Double.valueOf(3454), 
            new byte[] { (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)13, (byte)126 }
        );
    }
    /* XXX: JavaScript cannot represent as big longs as Java. 
    @Test public void deserializeLargeLong() throws Exception {
        final byte[] arr = new byte[] {
            (byte)64, (byte)8, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0
        };
        long exp = Numbers.deserLong(arr);
        assertExec("Should be " + exp, "org_apidesign_vm4brwsr_Numbers_deserLong__JAB", 
            Double.valueOf(exp), arr);
    }
    */
    
    @Test public void deserializeFloatInJava() throws Exception {
        float f = 54324.32423f;
        float r = Numbers.deserFloat();
        assertEquals(r, f, "Floats are the same");
    }
    
    @Test public void deserializeFloatInJS() throws Exception {
        float f = 54324.32423f;
        assertExec("Should be the same", Numbers.class, "deserFloat__F", 
            Double.valueOf(f)
        );
    }

    @Test public void deserializeDoubleInJava() throws Exception {
        double f = 3.0;
        double r = Numbers.deserDouble();
        assertEquals(r, f, 0.001, "Doubles are the same");
    }
    
    @Test public void deserializeDoubleInJS() throws Exception {
        double f = 3.0;
        assertExec("Should be the same", Numbers.class, "deserDouble__D", f);
    }
    /*
    @Test public void serDouble() throws IOException {
        double f = 3.0;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream d = new DataOutputStream(os);
        d.writeLong(3454);
        d.close();
        
        StringBuilder sb = new StringBuilder();
        byte[] arr = os.toByteArray();
        for (int i = 0; i < arr.length; i++) {
            sb.append("(byte)").append(arr[i]).append(", ");
        }
        fail("" + sb);
    }
*/    
    @Test public void fiveInStringJS() throws Exception {
        String s = Numbers.intToString();
        assertExec("Should be the same: " + s, 
            Numbers.class, "intToString__Ljava_lang_String_2", 
            s
        );
    }

    @Test public void sevenInStringJS() throws Exception {
        String s = Numbers.floatToString();
        assertExec("Should be the same: " + s, 
            Numbers.class, "floatToString__Ljava_lang_String_2", 
            s
        );
    }

    private static TestVM code;

    @BeforeClass
    public void compileTheCode() throws Exception {
        code = TestVM.compileClass("org/apidesign/vm4brwsr/Numbers");
    }

    private static void assertExec(
        String msg, Class<?> clazz, String method, Object expRes, Object... args) throws Exception
    {
        Object ret = code.execCode(msg, clazz, method, expRes, args);
        if (ret == null) {
            return;
        }
        if (expRes instanceof Double && ret instanceof Double) {
            double expD = ((Double)expRes).doubleValue();
            double retD = ((Double)ret).doubleValue();
            assertEquals(retD, expD, 0.000004, msg + " "
                    + code.toString());
            return;
        }
        assertEquals(ret, expRes, msg + " " + code.toString());
    }
    
}