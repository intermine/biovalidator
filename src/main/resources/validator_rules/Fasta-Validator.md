## Validates Fasta files

Fasta validator look for formating issues and invalid sequences in a file.
### Types of validation mode 

 - fatsa : look for formating issues and valid dna and protein sequence
 - fsta-dna: validate whether a file has valid dna sequence or not
 - fsta-protein: validates whether a file has valid protein sequence or not

## Validation Rules:
* First Line must start with '>'  
* Allows multiple sequences in a file  
* Strict checking for Nucleic-Acid and Amino-Acid sequences  
* Sequences letters must follow 'IUB/IUPAC'  
* Warning on exceeding 80 letters in a line  
* Empty files will be considered invalid  
* whitespaces and empty-lines are allowed and ignored