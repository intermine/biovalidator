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


/**
 * Represents a ##sequence-region directive range
 * @author deepak
 */
public final class SequenceRegion
{
    private final long sequenceRegionStart;
    private final long sequenceRegionEnd;

    private SequenceRegion(long sequenceRegionStart, long sequenceRegionEnd) {
        this.sequenceRegionStart = sequenceRegionStart;
        this.sequenceRegionEnd = sequenceRegionEnd;
    }

    /**
     * Construct a SeqId
     * @param start sequence-region start
     * @param end sequence-region end
     * @return instance of SequenceRegion
     */
    public static SequenceRegion of(long start, long end) {
        return new SequenceRegion(start, end);
    }

    /**
     * Gets sequenceRegionStart
     * @return value of sequenceRegionStart
     */
    public long getSequenceRegionStart() {
        return sequenceRegionStart;
    }

    /**
     * Gets sequenceRegionEnd
     * @return value of sequenceRegionStart
     */
    public long getSequenceRegionEnd() {
        return sequenceRegionEnd;
    }
}
