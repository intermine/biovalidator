## Welcome to the BioValidator documentation!

As file formats like FASTA, GFF, etc.. can have possible errors and it would be good to validate these file`
before processing, BioValidator validates various biological file formats and look for possible errors and warning in a file. It can be used as a dependency to a project or can be used as command-line utility.

## Currently supported file formats:
* FASTA
* GFF
* CSV/TSV [Beta]

## Using biovalidator in your application

Maven dependency
```xml
<dependency>
  <groupId>org.intermine</groupId>
  <artifactId>biovalidator</artifactId>
  <version>0.1.0</version>
  <type>pom</type>
</dependency>
```

Gradle dependency
```groovy
compile 'org.intermine:biovalidator:0.1.0'
```


