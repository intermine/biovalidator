package org.intermine.biovalidator.api;

public class ValidationFailureException extends Exception
{
    /**
     * Construct a ValidationFailue Exception with a message as the cause.
     * @param msg the reason for failure.
     */
    public ValidationFailureException(String msg) {
        super(msg);
    }
}
