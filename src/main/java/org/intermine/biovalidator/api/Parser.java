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

/**
 * @param <T> type of result returned by the parse's parseNext() method
 * @author deepak
 */
public interface Parser<T>
{
    /**
     *
     * @return next parsed data of type T
     * @throws ParsingException if parser is not able to parse data from the file
     */
    T parseNext() throws ParsingException;
}
