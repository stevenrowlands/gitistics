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
~~~~~~~~~

Most number of commits where the message has something to do with cleaning.

![ScreenShot](https://raw.githubusercontent.com/stevenrowlands/gitistics/master/doco/janitor.png])

