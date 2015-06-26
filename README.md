== PageRank with Phoenix and Spark ==

Although Phoenix has lots of great analytics utility built-in, certain tasks, such as graph processing, are much more
suited for external engines, such as Spark's GraphX.

This example loads up from Phoenix the Enron email test set from Stanford Network Analysis Project 
[1], and executes the GraphX implementation of PageRank on it to find interesting entities. It then saves the results
back to Phoenix.


[1] https://snap.stanford.edu/data/email-Enron.html

=== Prerequisites ===

* Setup Phoenix 4.4.0+
* Setup Spark 1.3.0+ (ensure phoenix-client JAR is in the Spark driver classpath)

=== Load sample data ===

Login to node with Phoenix installed:

```
cd /path/to/phoenix/bin
./sqlline.py localhost
```

Once in the SQLLine console, we'll create the tables to hold the input data, and the destination table for the pagerank results


```
CREATE TABLE EMAIL_ENRON(MAIL_FROM BIGINT NOT NULL, MAIL_TO BIGINT NOT NULL CONSTRAINT pk PRIMARY KEY(MAIL_FROM, MAIL_TO));
CREATE TABLE EMAIL_ENRON_PAGERANK(ID BIGINT NOT NULL, RANK DOUBLE CONSTRAINT pk PRIMARY KEY(ID));
```

Copy and extract the file 'enron.csv.gz' to a local directory, I'll use /tmp. We'll use 'psql.py' to load the CSV data

```
gunzip /tmp/enron.csv.gz
./psql.py -t EMAIL_ENRON localhost /tmp/enron.csv
```

=== Compile and Execute ===

Compile
```
sbt package
ls target/scala-2.10/phoenix-spark-pagerank_2.10-1.0.jar
```

Send to Spark cluster

```
cd /path/to/spark/bin
./spark-submit --class PhoenixSparkPageRank --master spark://<spark-host-here>:7077 file:////path/to/phoenix-spark-pagerank_2.10-1.0.jar
```

