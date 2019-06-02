package org.intermine.biovalidator.api;

public class ValidationResultStrategy
{
    private boolean enableWarning;

    private boolean enableError;

    private boolean constructDetailedMessage;

    private static ValidationResultStrategy defaultStrategy;

    /**
     * returns a default validation result strategy
     * @return default strategy
     */
    public static ValidationResultStrategy getDefaultStrategy() {
        if (defaultStrategy == null) {
            defaultStrategy = new ValidationResultStrategy();
        }
        return defaultStrategy;
    }


}
