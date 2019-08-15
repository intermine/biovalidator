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
public class FeatureLine implements Gff3Line
{
    private String seqId;
    private String source;
    private String type;
    private String startCord;
    private String endCord;
    private String score;
    private String strand;
    private String phase;
    private String attributes;
    private Map<String, String> attributesMapping;

    /**
     * Construct a FeatureLine
     * @param seqId sequenceId
     * @param source sequence source
     * @param type type
     * @param startCord start coordinate
     * @param endCord end coordinate
     * @param score score
     * @param strand strands
     * @param phase phase
     * @param attributes attributes
     * @param attributesMapping attributes map
     */
    public FeatureLine(String seqId, String source, String type, String startCord, String endCord,
                       String score, String strand, String phase, String attributes,
                       Map<String, String> attributesMapping) {
        this.seqId = seqId;
        this.source = source;
        this.type = type;
        this.startCord = startCord;
        this.endCord = endCord;
        this.score = score;
        this.strand = strand;
        this.phase = phase;
        this.attributes = attributes;
        this.attributesMapping = attributesMapping;
    }


    /**
     * Gets endCord.
     *
     * @return Value of endCord.
     */
    public String getEndCord() {
        return endCord;
    }

    /**
     * Gets attributesMapping.
     *
     * @return Value of attributesMapping.
     */
    public Map<String, String> getAttributesMapping() {
        return attributesMapping;
    }

    /**
     * Gets type.
     *
     * @return Value of type.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets soure.
     *
     * @return Value of soure.
     */
    public String getSoure() {
        return source;
    }

    /**
     * Gets attributes.
     *
     * @return Value of attributes.
     */
    public String getAttributes() {
        return attributes;
    }

    /**
     * Gets seqId.
     *
     * @return Value of seqId.
     */
    public String getSeqId() {
        return seqId;
    }

    /**
     * Gets strand.
     *
     * @return Value of strand.
     */
    public String getStrand() {
        return strand;
    }

    /**
     * Gets phase.
     *
     * @return Value of phase.
     */
    public String getPhase() {
        return phase;
    }

    /**
     * Gets score.
     *
     * @return Value of score.
     */
    public String getScore() {
        return score;
    }

    /**
     * Gets startCord.
     *
     * @return Value of startCord.
     */
    public String getStartCord() {
        return startCord;
    }
}
