# Implementation Details

* Files are read line-by-line using BufferedReader, as opposed to loading all the data into memory
* Use Guava's EventBus to decouple the reading of input data and the writing of output data
* Input beans are mapped to Output beans using reflection, and are written out using super-csv
