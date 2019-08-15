
## Csv|Tsv Validator

CSV validator does not validate against schema but rather it validates the consistency of CSV data, it checks whether a particular column is consistent throughout all the rows of CSV data or not.

## Features:
* Performs consistency check on CSV/TSV data
* Detect CSV delimiter automatically
* Detect whether a CSV data has header line or not(so you may add a header or not may not in your CSV file, will work in most cases)

## Two type of checks performed by validator:
1. Whether a column has one particular type of data or not(such as integer, boolean, etc..) if it is then validator checks whether all of the rows have the same type of data or not.

2. If data of a column has mixed kind of data, then validator checks whether all of the rows of a particular column are following on more patterns or not.

## Two mode of validator:
* permissive
  * Checks for a single type of data (currently boolean and numbers)
  * This will validate a CSV file successfully even if patterns are roughly same.
* Strict
  * Checks for a single type of data (currently boolean, integer and floats)
  * Strict mode will check if data of a column are evenly distributed among one are more patterns are not.

## Example:
* Data
   * Example:
```text
    ---------------------------
    | gene    | bool | score  | 
    ---------------------------
    | FBgn000 | true |	99.85 |
    | FBgn001 | true |	99.8  |
    | FBgn000 |	true |	99.85 |
    | FBgn001 |	true |	99.8  |
    | FBgn000 |	true |	99.85 |
    | FBg     |	f    |	4     |
    ---------------------------
```
* Validation on permissive mode:
   * $ java -jar biovalidator.jar -f /path/to/file -w (permissive by default, or you can specify '-p')
   * Warnings(only one warning):
      1. data is not consistent in column 2 and row 5, most rows have boolean but 1 rows has non-boolean values(s).

* Validating same data with strict mode:
    * $ java -jar biovalidator.jar -f /path/to/file -w -s (here '-w' for showing warnings and '-s' for strict mode) 
   * Warnings:
      1. data in column 1 does not confirms to one or more pattern, look like this column has data with some random pattern:
		   * values similar to pattern(LD)  'FBgn000' has 5 counts
		   * values similar to pattern(L)  'FBg' has 1 counts

	  2. data is not consistent in column 2 and row 5, most rows have boolean but 1 rows have non-boolean values(s)
	  3. data is not consistent in column 3 and row 5, most rows have float but 1 rows have non-float values(s)

## Notes:
* warning is not enabled by default by the validator, so use '-w' to show the warning messages
* use '-s' argument to validate strictly
* use '-p' argument to validate permissively (by default)