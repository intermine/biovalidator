package org.intermine.biovalidator.validator.csv;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CsvSchemaTest {

    @Test
    public void testCsvSchema() {
        int columns = 16;
        CsvSchema schema = new CsvSchema(columns);
        assertEquals(columns, schema.getColumnLength());

        long totalRows = (long)10e12;
        schema.setTotalRows(totalRows);
        assertEquals(totalRows, schema.getTotalRows());
    }

    @Test
    public void testCsvSchemaColumnMatrics() {
        int columns = 16;
        CsvSchema schema = new CsvSchema(columns);

        schema.incrementBooleansCountAtColumn(0);
        schema.incrementBooleansCountAtColumn(15);
        assertEquals(1, schema.colAt(0).getBooleansCount());
        assertEquals(1, schema.colAt(15).getBooleansCount());
        assertNotEquals(1, schema.colAt(1));

        schema.incrementIntegersCountAtColumn(8);
        assertEquals(1, schema.colAt(8).getIntegersCount());

        schema.incrementFloatsCountAtColumn(10) ;
        assertEquals(1, schema.colAt(10).getFloatsCount());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCsvSchemaColumnIndexOutOfBounds() {
        int columns = 16;
        CsvSchema schema = new CsvSchema(columns);
        schema.incrementBooleansCountAtColumn(100);
    }

    @Test
    public void testCsvSchemaColumnPatternCounts() {
        int columns = 16;
        CsvSchema schema = new CsvSchema(columns);

        schema.colAt(0).addPattern(CsvColumnPattern.randomPattern());
        schema.colAt(0).addPattern(CsvColumnPattern.emptyPattern());
        assertEquals(2, schema.colAt(0).getColumnDataPatterns().size());
    }

    @Test
    public void testCsvSchemaIterator() {
        int totalColumns = 16;
        CsvSchema schema = new CsvSchema(totalColumns);

        int columnCounts = 0;
        for (CsvColumnMatrics col: schema) {
            columnCounts++;
        }
        assertEquals(totalColumns, columnCounts);
    }

    @Test
    public void testCsvSchemaForEach() {
        int totalColumns = 16;
        CsvSchema schema = new CsvSchema(totalColumns);

        MutableInt mutableColumnCounts = new MutableInt();
        schema.forEach(column -> mutableColumnCounts.increment());
        assertEquals(totalColumns, mutableColumnCounts.intValue());
    }
}
