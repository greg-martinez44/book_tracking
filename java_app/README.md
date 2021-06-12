# About The Book Tracker Project

The Book Tracker Project is a custom solution for logging statistics on books in your library. It is meant to replace cumbersome CSV files with a sleeker, easier to use GUI built in Java.

## Elements of a Book

Every book can be set with the following properties:
* Title (required)
* Author (required)
* Pages
* Format
* Genre
* Translator
* Illustrator
* Publisher
* Year of Publication
* Rating

## Saving Data

The data are saved as a few big JSON arrays OR a relational database.

These data include things like the book objects, the authors, the publishers, etc.

# The App

The app should be a GUI that has fields for each element of the book.

The user should be able to input elements into each field; the app should then build a Book object using that data.

The user should also be able to read a book from the database into the app, so that data can be altered. The only pieces of data that cannot be altered are the title and the author of the book. The user would load the book record based on the name of the book. There should be a searchable list of current books in the database to choose from.