Jimple
======

Simple and small Dependency Injection Container for Java.
Inspired by [Pimple](http://pimple.sensiolabs.org/) - simple and elegant DI container for PHP (suddenly!). This class 
was born during development of simple Android app, created by the PHP developer who started his way to
Android and Java :)

The main concept of Jimple (and Pimple) is to instantiate contained object on first acces to it. Pimple uses closures
to store dependency creation and initialization code, but since Jimple has been developed in context of Android app,
where closures are not available - it uses subclasses instead of closures.
