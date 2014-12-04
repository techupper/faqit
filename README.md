faqit
=====

Mandatory:
1. Download the SEMILAR main package (SEMILAR API) from http://www.semanticsimilarity.org/ <br>
2. Extract the files to core/external libs/semilar/ <br>
3. Copy and paste the folder core/external libs/semilar/WordNet-JWI to core/ and do the same for the file core/external libs/semilar/stop-words.txt (there is a problem in semilar lib that does not allow us to redefine the path of WordNet-JWI) <br>

To use LSA in sentence similarity, do the following steps: <br>
1. Download LSA models (any of your choice) from http://www.semanticsimilarity.org/ <br>
2. Extract both files from the model to core/external libs/semilar/LSA-MODELS/TASA-LEMMATIZED-DIM300/ <br>

To generate the input file for RankLib: <br>
1. Run with the paraments: l2rinput PATH_TO_XML_FILE_ANNOTATED_BY_HUMANS 
2. Check the new generated file in core/
