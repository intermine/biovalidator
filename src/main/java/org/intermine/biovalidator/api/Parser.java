package org.intermine.biovalidator.api;

public interface Parser<T>
{
    /**
     *
     * @return next parsed data of type T
     * @throws ParsingException if parser is not able to parse data from the file
     */
    T parseNext() throws ParsingException;
}
