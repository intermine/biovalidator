package org.intermine.biovalidator.api;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.io.IOException;

/**
 * @author deepak
 */
public class ParsingException extends IOException
{
    /**
     * @param msg parsing failure message.
     */
    public ParsingException(String msg) {
        super(msg);
    }
}
