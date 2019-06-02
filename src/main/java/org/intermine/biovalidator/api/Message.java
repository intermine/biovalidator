package org.intermine.biovalidator.api;

public abstract class Message
{
    private String message;
    private int code;


    /**
     * Gets code.
     *
     * @return Value of code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets new message.
     *
     * @param message New value of message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets new code.
     *
     * @param code New value of code.
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets message.
     *
     * @return Value of message.
     */
    public String getMessage() {
        return message;
    }
}
