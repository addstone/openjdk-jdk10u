/*
 * Copyright (c) 2015, 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */

/*
 * @test
 * @summary Test locking on shared strings
 * Feature support: G1GC only, compressed oops/kptrs, 64-bit os, not on windows
 * @requires (sun.arch.data.model != "32") & (os.family != "windows")
 * @requires (vm.opt.UseCompressedOops == null) | (vm.opt.UseCompressedOops == true)
 * @requires vm.gc.G1
 * @library /test/hotspot/jtreg/runtime/appcds /test/lib
 * @modules java.base/jdk.internal.misc
 * @modules java.management
 *          jdk.jartool/sun.tools.jar
 * @compile LockStringTest.java LockStringValueTest.java
 * @build sun.hotspot.WhiteBox
 * @run main ClassFileInstaller sun.hotspot.WhiteBox
 * @run main LockSharedStrings
 */

public class LockSharedStrings {
    public static void main(String[] args) throws Exception {
        SharedStringsUtils.buildJarAndWhiteBox("LockStringTest", "LockStringValueTest");

        SharedStringsUtils.dumpWithWhiteBox(
            TestCommon.list("LockStringTest", "LockStringValueTest"),
            "ExtraSharedInput.txt");

        String[] extraMatch = new String[] {"LockStringTest: PASS"};
        SharedStringsUtils.runWithArchiveAndWhiteBox(extraMatch, "LockStringTest");

        extraMatch = new String[] {"LockStringValueTest: PASS"};
        SharedStringsUtils.runWithArchiveAndWhiteBox(extraMatch, "LockStringValueTest",
            "--add-opens=java.base/java.lang=ALL-UNNAMED");
    }
}
