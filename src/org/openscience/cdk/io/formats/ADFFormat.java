/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2003-2005  The Chemistry Development Kit (CDK) project
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.cdk.io.formats;


/**
 * @cdk.module io
 * @cdk.set    io-formats
 */
public class ADFFormat implements IChemFormatMatcher {

    public ADFFormat() {}

    public String getFormatName() {
        return "ADF";
    }

    public String getMIMEType() {
        return null;
    }
    public String getPreferredNameExtension() {
        return null;
    }
    public String[] getNameExtensions() {
        return new String[0];
    }

    public String getReaderClassName() { return null; }
    public String getWriterClassName() { return null; }

    public boolean matches(int lineNumber, String line) {
        if (line.indexOf("Amsterdam Density Functional") >= 0) {
            return true;
        }
        return false;
    }

}
