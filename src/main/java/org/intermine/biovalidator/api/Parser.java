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
 * Base interface for Parsers, user of this interface can simple call to parseNext(), in order
 * to get next parsed data to be validated.
 *
 * The main purpose of creating parser as a separate entity is to make validator free from how
 * and where data is being read.
 *
 * Each validator can define its own strategy for reading file data. Some implementation can read
 * the file line by line while some can read more than one line in one step.
 *
 * @param <T> type of result returned by the parse's parseNext() method
 *
 * @author deepak
 */
public interface Parser<T> extends AutoCloseable
{
    /**
     *
     * @return next parsed data of type T
     * @throws ParsingException if parser is not able to parse data from the file
     */
    T parseNext() throws ParsingException;

    /**
     * Close the resources associated with Parser
     * @throws IOException if closing resource fails
     */
    @Override void close() throws IOException;
}
