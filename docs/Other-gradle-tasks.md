## Building a jar with dependencies
* ```$ ./gradlew createFatJar```
* This will create an executable jar with all the dependencies that the project has in 'build/libs' directory.

## Updating Sequence-Ontology terms file for GFF3 validator
* Replace new 'so-simple.obo' with the existing file 'src/resources/gff3/so-simple.obo'<br/>You can download Sequence Ontology file from [So-Ontology Github repository](https://github.com/The-Sequence-Ontology/SO-Ontologies).
* then run task: 
  - ```$ ./gradlew processSequenceOntologyTerms```
  - this gradle task will extract all the SO terms and create or replace 'processed_so_terms.obo' file in the resource directory(src/resources/gff3) that will be used by GFF3 validator.