%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Project:	FaqIt - Information Retrival from FAQ Databases
Authors:	Fábio Ribeiro (70619), Paulo Marques (69298)
Group:		7                  
Course:		Recuperação e Gestão de Informação
Year:			2014/2015
Faculty:	Instituto Superior Técnico
Degree: 	MEIC - Alameda 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Required Software
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	The project was developed using Java (version 8, it should also run with 
	version 7) Eclipse Luna as IDE:
		https://projects.eclipse.org/releases/luna
	The FAQ database and the training/test data used: 
		http://www.isical.ac.in/~fire/faq-retrieval/data.html
	To index the FAQ database, we have used Lucene:
		http://lucene.apache.org/core/
	We have used the following libraries written in Java: 
		http://www.semanticsimilarity.org/
		http://sourceforge.net/p/lemur/wiki/RankLib/
		http://sourceforge.net/projects/simmetrics/
		
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
How to run the application
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

1. Open Eclipse and import the project
	1.1 File>Import
	1.2 Select General>Existing Projects into Workspace and select next.
	1.3 Browse the root directory of the project that is project7/core and Finish
2 Import all the libs presented in "project7/core/external libs" to the project.
	2.1 Right click on the project presented in Package Folder
	2.2 select Properties>Java Build Path>Add JARs... and select all the libs in
	.jar format presented in "project7/core/external libs"
3. Download the SEMILAR main package (SEMILAR API) from 
   http://www.semanticsimilarity.org/
	3.1 Extract the files to core/external libs/semilar/
	3.2 Copy and paste the folder core/external libs/semilar/WordNet-JWI to core/ 
	and do the same for the file core/external libs/semilar/stop-words.txt 
	(there is a problem in semilar lib that does not allow us to redefine the path 
	of WordNet-JWI) 
	3.3 Download one LSA model (any of your choice) from the same page and 
	extract both files from the model to 
	core/external libs/semilar/LSA-MODELS/TASA-LEMMATIZED-DIM300/ and change their
	type of file to .txt
4. Run the project from the class FaqIt.java
	4.1 Eclipse>Run>Run Configurations
	4.2 Select "New launch configuration" and Search the main class FaqIt.java, it 
	is located in the default package.
5. If everything was successful, now we can interact with the project prototype.
	5.1 Type any query of your choice or "q" to exit the program.

Note1: the current configuration of the prototype will run using Simon White and
Monge Elkan similarity measures, with the respective weights of 0.8 and 0.2.
To try with other measures, you have to uncomment the desired similarity measure
and it should be added to the measures List. All used measures receive their
respective weight as parameter, and the prototype to rank them correctly, all 
the weights should be equal to 1 or their total sum should be 1.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
How to run evaluation
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

To train and test a model, we need to generate an input for the RankLib toolkit.
If we want to generate input based on the baseline used (tf-idf), we run the 
project with the following program arguments (through Eclipse):
	l2rInput to_order_400_10.xml true
If we are interested in generate input based on the similarity measures defined
in the class com.faqit.similarity.Ranker, we run the project with the following
program arguments (through Eclipse):
	l2rInput to_order_400_10.xml false
	
to_order_400_10.xml is the train/test corpus.

After running the project with any of the previous arguments, a file named 
l2r.in has been created in project7/core. This file is the input for the 
RankLib command line program. For more information its format, check the 
following url: http://sourceforge.net/p/lemur/wiki/RankLib%20File%20Format/
	
Now to train and evaluate a model as it is described in the Section 5 of the 
report, we should open a terminal in the path of the project "project7/core" 
and run the following commands (this will use the RankLib toolkit), replacing
the word METRIC with the desired metric, RR@10 for reciprocal ranking and P@1,
P@2, P@5 for precision at 1, 2 or 5:
	
java -jar external\ libs/ranklib/RankLib-2.1-patched.jar -train l2r.in -ranker 4 -kcv 5 -metric2t METRIC

This command will produce a summary with the desired information.
	
		
		
		
	


