# BioValidator

BioValidator is a schema validator that validates biological file format like Fasta, GFF, etc.
Currently, it only supports Fasta file format

### Usage

The most simple way to validate supported file types is to use ValidatorHelper provided by library.<br/>
Example: 
Arguments in the below validate method 
 1. filename: absolute file path
 2. validator-type: type of validator
 3. isStrict: whether to validate in strict mode or permissive 
 
```java
ValidationResult validationResult = ValidatorHelper.validate("filepath", ValidatorType.FASTA, true);
OR
ValidationResult validationResult = ValidatorHelper.validate("filepath", "fasta-dna", true);
OR
ValidationResult validationResult = ValidatorHelper.validateFasta("filepath");
```


Check whether file is valid or not:
```java
assertTrue(validationResult.isValid());
```

If file contain errors or warnings:
```java
if (!validationResult.isValid()) {
    validationResult.getErrorMessages().forEach(System.out::println);
    validationResult.getWarningMessages().forEach(System.out::println);
}
```

#### Validation Result Customization
Validator's behaviour and result and can be easily customize, ValidatorBuilder can be used to customize
behaviour and result of the validator.<br/>
Example: To validate a file by disabling warnings and continue validation even if error occurred

```java
Validator validator = ValidatorBuilder.withFile("path", "fasta-protein")
        .disableWarnings()
        .enableErrors()
        .disableStopAtFirstError()
        .build();

assertTrue(validator.validate().isValid());
OR
validator.validate(result -> {
    assertTrue(result.isValid());
})
```

### Construct a raw validator:
```java
String dnaSequence = "> seqId | header name\nACTGACTGACTG";

InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(dnaSequence.getBytes()));
Validator validator = new FastaValidator(isr, SequenceType.DNA);
ValidationResult result = validator.validate();

assertTrue(result.isValid());
```