/**
 * Back 2 Browser Bytecode Translator
 * Copyright (C) 2012-2018 Jaroslav Tulach <jaroslav.tulach@apidesign.org>
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
package org.apidesign.bck2brwsr.truffle;

import java.io.File;
import java.net.URL;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MainIT {
    @Test
    public void runMain() throws Exception {
        URL url = MainIT.class.getProtectionDomain().getCodeSource().getLocation();
        File testClasses = new File(url.toURI());
        assertTrue("Directory found: " + testClasses, testClasses.isDirectory());

        Main.main("-cp", testClasses.getPath(), Hello.class.getName());
    }
}
