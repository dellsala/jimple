Jimple
======

Simple and small Dependency Injection Container for Java.
Inspired by [Pimple](http://pimple.sensiolabs.org/) - simple and elegant DI container for PHP (suddenly!). This class 
was born during simple Android app development, created by the PHP developer who started his way to
Android-development :)

The main concept of Jimple (and Pimple) is to instantiate contained object on first acces to it. Pimple uses closures
to store dependency creation and initialization code, but since Jimple has been developed in context of Android app -
it uses subclasses instead of closures.
