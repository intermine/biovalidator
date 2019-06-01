package org.intermine.biovalidator.api;

public interface Parser<T>
{
    /**
     *
     * @return next parsed data of type T
     */
    T parseNext() throws ParsingException;
}
