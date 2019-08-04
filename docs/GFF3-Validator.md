
## GFF3 validator
Validates only GFF3 format against the GFF3 specification https://github.com/The-Sequence-Ontology/Specifications/blob/master/gff3.md

## Validation rules

Currently Implemented GFF3 rules(only single feature errors):

* Start/Stop is not a valid 1-based integer coordinate
* strand information missing
* If a feature specifies its parent's attribute then it must be present in the file.
* Seqid not found in any ##sequence-region:
    ignore if not found, but found then features start/end coordinate must be within the range of ##sequence-region

* White chars not allowed at the start of a line(but escaped spaces are allowed):
   * '123'    -> valid
   * '12 3'   -> invalid due to space
   * '123>4'  -> invalid due to character '>'
   * '123\ 4' -> valid becase space is escaped with '\ '
   * '123\>4' -> valid becase '>' is escaped with '\>'

* ##gff-version missing from the first line
* Start/End is not a valid integer
* Start is not less than or equal to end

* Version is not "3":
   * correct format for version is '3.#.#'
   * valid '3', '3.0', '3.1.3'
   * invalid '4', '10', '30.1'

* Version is not a valid integer
* Features should contain 9 fields
* Strand has illegal characters
* Score is not a valid floating-point number
* Phase is not 0, 1, or 2, or not a valid integer
* Phase required for CDS features

* Attributes must contain one and only one equal (=) sign:
  a. warning if key or value is mission

* Empty attribute tag [WARNING]
* Empty attribute value [WARNING]
* Feature-type(column 3) must be one of the SO term or SO ID:
    Sequence-Ontology file used by validator for validating feature-type(column 3) is: https://github.com/The-Sequence-Ontology/SO-Ontologies/blob/master/so.obo

* Warning on a duplicate entry for ##sequence-region directive [WARNING]
* Error if attribute tag is duplicated in column 9