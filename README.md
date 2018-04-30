 BigQueryAkkaStream

The query configuration is in a json file under root. Please note that you have to have the `GOOGLE_APPLICATION_CREDENTIALS` environment variable set up so it can call BigQuery API for that project.

`sbt assembly` to produce a fat jar

`java -jar loader.jar
