### Project Title
```
Code Labeller:

This is an application which can aid a researcher can share code snippets publically whereupon interested personals can annotate code snippets following the annotations provided by said researcher.
Researcher can visit the uploaded code snippets later and see the annotations completed till that point.
```

### Goal:
```
The goal here is to provide the admin an ability to create surveys, provide the ability to annotator to "mark" or annotate those surveys and then admin can see whatever those annotators have annotated or "mark".
```

### Run the code:
##### Step 1:  Compile
```shell
Step 1: Compile
mvn compile

Step 2: 
mvn build

Step 3:
mvn clean package

Step 4:
mvn install

Step 5:
java -jar <jar-file-name>.jar
```

### How To Contribute:
```bash
Step 1:
First step would be to fork the repository.

Step 2:
Make your changes.

Step 3:
Test your changes.

Step 4:
Make appropriate changes to documentation.

Step 5:
Create a pull request.
```

### Use Cases: Flow:
```
The general flow here will demonstrate a complete and most general use case for both annotator and admin:

Step 1:
Admin Signs Up:
Admin chooses a acceptable username and password, also they chooses their role as admin to sign up.

Step 2:
Admin starts creating a survey:
Admin lands on their home page where they can see two options, to see or to create surveys.
Admin chooses to create survey.

Step 3:
Admin creates a survey:
Admin upload snippets( bunch of .java files), chooses language as .java, and upload snippets from their machine.
Admin adds annotations to the survey and submit and logs out.

Step 4:
Annotator signs up/log in:
Annotator lands on their home page where they can see list of surveys created.
Annotator chooses a survey and start the pagination to start annotating the survey.

Step 5:
Annotator goes through each snippet of the chosen survey:
Annotator sees one snippet at a time.
Annotator highlights the code and marks it with an annotation.
Annotator chooses a tag for the snippet and click on "next" button to move to next snippet.

Step 6:
Annotator submits:
Annotator after annotating all the snippets, submits.

Step 7:
Admin logs in adn see the annotated snippets:
Admin logs in.
Admin lands on home page.
Admin chooses "View Surveys".
Admin clicks on the desired survey.
Admin can see the annoated tags for each snippet under that survey.
Admin can start the pagination for each snippet to see the highlighted part.
Admin logs out.
```
