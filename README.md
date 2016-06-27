[![Build Status](https://travis-ci.org/stevenrowlands/gitistics.png)](https://travis-ci.org/stevenrowlands/gitistics)

gitistics
=========

Generates git statistics for a repository into a h2 database. Idea was to have some non standard stats as part of it (see below)


Get Started
-----------

Clone the repository and then run

```
./gradlew jettyRun
```

A server will be started on localhost:8080/gitistics-web/

A h2 database will be created in your home directory ~/gitistics.db

Then just add the path to your local git repository e.g. '/home/steven/repositories/gitistics/.git'

![ScreenShot](/doco/add_repository.png?raw=true "Add Repository")


Features
--------

Janitor 
~~~~~~~

Most number of commits where the message has something to do with cleaning.

![ScreenShot](/doco/janitor.png?raw=true "Janitor")


WhoWroteThisS#!tAnway
~~~~~~~~~~~~~~~~~~~~~

Most number of self reverted commits 

![ScreenShot](/doco/whowrote.png?raw=true "WhoWroteThisS#!tAnway")


Streaker
~~~~~~~~

Greatest uninterrupted run of consequtive commits

![ScreenShot](/doco/streaker.png?raw=true "Streaker")


Reverter
~~~~~~~~

Most number of reverts

![ScreenShot](/doco/reverter.png?raw=true "Reverter")

Outliers
~~~~~~~~

Sometimes people make a massive change (rename folders). This adds the ability to remove outliers from all stats

![ScreenShot](/doco/outlier.png?raw=true "Outlier")


Breakdown by File Types
~~~~~~~~~~~~~~~~~~~~~~~

View of commits and line changes by file type by year

![ScreenShot](/doco/filetype.png?raw=true "By File Type")


The Normal Stuff
~~~~~~~~~~~~~~~~

And then all the usual stuff

![ScreenShot](/doco/year.png?raw=true "By Year")

![ScreenShot](/doco/author.png?raw=true "Author By Year")