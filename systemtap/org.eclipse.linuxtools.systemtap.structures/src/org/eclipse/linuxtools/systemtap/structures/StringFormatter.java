/*******************************************************************************
 * Copyright (c) 2006, 2018 IBM Corporation and others.
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - Jeff Briggs, Henry Hughes, Ryan Morse
 *******************************************************************************/

package org.eclipse.linuxtools.systemtap.structures;

import java.text.DateFormat;
import java.util.Date;


public class StringFormatter implements IFormattingStyles {
    public StringFormatter() {
        format = IFormattingStyles.UNFORMATED;
    }

    @Override
    public int getFormat() {
        return format;
    }

    @Override
    public void setFormat(int format) {
        this.format = format;
    }

    /**
     * Potentially modifies a string value according to a certain format based on the current value
     * of format.
     *
     * @param s The string to potential modify.
     *
     * @return The modified string.
     */
    @Override
    public String format(String s) {
        switch (format) {
            case STRING:
                return s;
            case DATE:
                return DateFormat.getDateTimeInstance().format(new Date(Long.parseLong(s)));
            case HEX:
                return "0x"+Long.toHexString(Long.parseLong(s)); //$NON-NLS-1$
            case OCTAL:
                return "0x"+Long.toOctalString(Long.parseLong(s)); //$NON-NLS-1$
            case BINARY:
                return "0x" + Long.toBinaryString(Long.parseLong(s)); //$NON-NLS-1$
            case DOUBLE:
                return "" + Double.parseDouble(s); //$NON-NLS-1$
        }
        return s;
    }

    private int format;
}
