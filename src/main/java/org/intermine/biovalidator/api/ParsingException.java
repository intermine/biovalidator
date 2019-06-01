package org.intermine.biovalidator.api;

public class ParsingException extends Exception
{
    /**
     * @param msg parsing failure message.
     */
    public ParsingException(String msg) {
        super(msg);
    }
}
