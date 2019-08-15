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
 * @author deepak
 */
public final class Gff3DirectiveLine implements Gff3Line
{
    private String comment;

    private Gff3DirectiveLine(String comment) {
        this.comment = comment;
    }

    /**
     * Creates a CommentLine
     * @param comment comment string
     * @return instance of CommentLine
     */
    public static Gff3DirectiveLine of(String comment) {
        return new Gff3DirectiveLine(comment);
    }

    /**
     * Gets comment.
     *
     * @return Value of comment.
     */
    public String getComment() {
        return comment;
    }
}
