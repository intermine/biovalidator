package org.intermine.biovalidator.api;

public abstract class AbstractValidator implements Validator
{
    protected ValidatorStrictnessPolicy strictnessPolicy;
    protected ValidationResultStrategy resultStrategy;
}
