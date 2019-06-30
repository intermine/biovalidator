package org.intermine.biovalidator.validator.gff3;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.Map;

/**
 * @author deepak
 */
public class Feature
{
    private String seqId;
    private String soure;
    private String type;
    private long startCord;
    private long endCord;
    private double score;
    private String strand;
    private String phase;
    private String attributes;
    private Map<String, String> attributesMapping;
}
