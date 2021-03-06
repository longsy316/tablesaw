Tables
======
Tables are the primary data-type and general focus of Tablesaw. Here we’ll provide an overview of the operations they provide. Coverage of most of the topics below is introductory. They often will have their own section of the User Guide where they are examined more fully. For still more detail, see the JavaDoc for tech.Tablesaw.api.Table.

## Tables "all the way down"
Tablesaw has a huge number of methods for creating, querying, manipulating, displaying, and saving tables, so it makes sense that we use tables widely, and that many operations on tables return other tables. For example, when you ask a table to describe its structure, it returns a new table that contains the column names, types, and order. 

## Creating tables

You can create tables programmatically or by loading data from an external source.

### Create programmatically

```java
Table t = Table.create("name")
```

It's often convenient to add columns while you're creating the table. 

```java
Table t = Table.create("name", column1, column2, column3...)
```

You can also add columns later.

### Import data

Tablesaw can load data from character delimited text files (including CSV and Tab-separated files), from streams, and from any data source that can create a JDBC result set. As this includes essentially all relational databases (and many non-relational ones), most of the world’s structured data in can be loaded without a prior transformation. 

#### Import from a CSV file

You can load a table from a CSV file by providing the file name. 

    Table t = Table.read().csv("myFile.csv");

This simple method supplies default values for a number of parameters like the type of the separator character (a comma). It also attempts to infer the types for each column. If the inferred types are incorrect, you can specify the types at import time. See [Importing data](https://jTablesaw.github.io/Tablesaw/userguide/importing_data) for other options and more detail.    



## Displaying data

The simplest way to display a table is to call "print()" on it, which return a formatted String representation.

    aTable.print();

The default implementation of print displays the first ten and last ten records. To specifically control the output, the methods first(n) and last(n) are available. These return a copy of the table that contains only the first n or last n rows respectively.

    aTable.first(3);
    aTable.last(4); 

Table overides toString() to return print(). This makes for rather funky output in a debugger, but during analysis, you frequently want to look at the table data so frequently that the shortcut is worth the hassle it causes people programming Tablesaw.

## Getting table metadata

There are a number of ways to get familiar with a new dataset. Here are some of the most useful.

*table.name()* returns its name, which defaults to the name of the file it was created from. You can change it if you like using *setName(aString).*

*t.columnNames()* returns an array of column-name strings.

*t.structure()* returns a list of columns with their position and types:

    Structure of data/tornadoes_1950-2014.csv
        Index Column Names Column Type 
        0     The Date     LOCAL_DATE  
        1     The Time     LOCAL_TIME  
        2     State        CATEGORY    
        3     State No     INTEGER     
        4     Scale        INTEGER 

table.shape() returns the table’s size in rows and columns:

    59945 rows X 10 cols

You can also get the *rowCount()* and *columnCount()* individually from a table.

## Add and remove columns

You can add one or more columns to a Table using the *addColumns()* method:

```java
t.addColumns(aColumn...)
```

You can also specify that the column be inserted at a particular place by providing an index:

```java
t.addColumn(3, aColumn);
```

As usual in java, column numbering begins at 0, rather than 1.

The column you add must either be empty or have the same number of elements as the other columns in the table.

To remove a column or columns:

```java
t.removeColumns(aColumn...)
t.removeColumns("columnName"...)
```

Columns can also be removed by referencing them by name. Alternately, you can specify just the columns to retain:

```java
t.retainColumns(aColumn);
```

Again you can specify the columns either directly, or by using their names.

While *addColumns()* and *removeColumns()* update the receiver in place, you can also create a new table with a subset of the columns in the receiver. This can be done by specifying the names of the columns that you want to retain, in the order you want them to appear.

```java
Table reduced = t.select("Name", "Age", "Height", "Weight");
```

You can also create a new table by specifying the columns in the current table that you don’t want, which might save some typing:

```java
Table reduced = t.rejectColumns("Street Address");
```

In this case the columns in the result table are in the same order as in the original.

## Selecting columns

Often you will want a reference to a column in the table. To get all the columns as a list:

```java
t.columns();
```

Columns can also be selected by index or name:

```java
t.columns("column1", "column2");
```

Often you want just one column, which you can get using *t.column(“columnName”)*.

Since Tablesaw columns are typed, you often need to cast the returned column to something more specific. For example:

```java
DoubleColumn dc = (DoubleColumn) t.column();
```

as a convenience, tables have column accessors that are type specific: The do the casting for you.

```java
DoubleColumn dc = t.doubleColumn();
```

## Combining Tables

Tables can be combined in one of several ways.  The most basic is to append the rows of one table to another. This is only possible if the two tables have the same columns in the same order, but can be useful when, for example, you have the same data from two time periods.

```java
Table result = t.append(t2);
```

You can concatenate two tables, adding the columns of one to the other by using the *concat()* method.  The method returns the receiver rather than a new table. Two tables can be concatenated only if they have the same number of rows.

```java
t.concat(t2)
```

### Joining Tables





## Add and remove rows



## Filter

One of the most useful operations is filtering. 

### Filter by index

You can select rows by specifying the index (zero-based):

 ```java
t.inRows(i...)
 ```

You can also select by range:

```
t.inRange(start, end)
```

You can also select a random sample of data. See the section on filters for more detail.



### Filter by predicate

Complex queries can be created by forming expressions that produce a *Selection*, which effectively turns the query result into an object that can be used to filter by index. 

```
t.where()
```

The method where() takes either a Filter or Selection as a parameter. Filters allow a more fluent form of composition.  

### Combining row and column filters

Given a list of columns as arguments, the *select()* statement returns a table containing only those columns, by chaning *select()* and *where()*, you get something that looks a lot like a sql statement that returns a subset of the data in the original table.

```java
Table subset = t.select("columnA", "columnB").where(t.nCol("columnC").isGreaterThan(4));
```

See filter for more info.

## Reduce

There are numerous ways to summarize the data in a table. 

### Summarize

The summarize() method and its variants let you specify the columns to summarize.

```java
Table summary = t.summarize("age", "weight", mean, median, standardDeviation).apply()
```

Summarize returns a Summarizer object. 

The apply() method sent to summary above returns the result of applying the function to the table, and combining the results into a new table.  It computes one summary for the original table.

#### Groups

To calculate subtotals, we use *by()* instead of *apply().*

By takes a list of columns that are used to group the data. The example below calculates the average delay for each airport in the table. 

```java
Table result = t.summarize("delay", mean).by("airport");
```

### Cross Tabs 

Cross tabs (or cross-tabulations) are like groups, but return the data in a layout that faciliates interpretation. A cross tab in Tablesaw takes two grouping columns and returns the number of observations for each combination of the two columns. They can also produce the proportions, and subtotals by row or column. 

## Sort

 ```java
t.sortDescending("column1",,,)
 ```

The *sortOn()* method lets you combine descending and ascending sorts in the same operation. Descending sorts are specified by putting a "-" in front of the column name.

```java
t.sortOn("-column1", "column2")
```

## Rows

There are no real rows in Tablesaw. Data is organized in columns. The closest you get to an actual row is a table with one line. However, rows are useful abstractions in tabular data, so we provide a kind of virtual row that may be useful for table operations. 

### What we mean by a "virtual row"



## Export

````java
table.write().csv("filename.csv");
````


