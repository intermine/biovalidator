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

import java.util.Objects;

/**
 * Represents a ##sequence-region directive range
 * @author deepak
 */
public final class SequenceRegion
{
    private final String seqId;
    private final long sequenceRegionStart;
    private final long sequenceRegionEnd;

    private SequenceRegion(String seqId, long sequenceRegionStart, long sequenceRegionEnd) {
        this.seqId = seqId;
        this.sequenceRegionStart = sequenceRegionStart;
        this.sequenceRegionEnd = sequenceRegionEnd;
    }

    /**
     * Construct a SeqId
     * @param seqId seqId value
     * @param start sequence-region start
     * @param end sequence-region end
     * @return instance of SequenceRegion
     */
    public static SequenceRegion of(String seqId, long start, long end) {
        return new SequenceRegion(seqId, start, end);
    }

    @Override
    public int hashCode() {
        return seqId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof SequenceRegion) {
            final SequenceRegion other = (SequenceRegion) obj;
            return Objects.equals(this.getSeqId(), other.getSeqId());
        }
        return false;
    }

    /**
     * Gets seqId
     * @return value of seqId
     */
    public String getSeqId() {
        return seqId;
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
