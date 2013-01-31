Jimple
======

Simple and small Dependency Injection Container for Java.
Inspired by [Pimple](http://pimple.sensiolabs.org/) - simple and elegant DI container for PHP (suddenly!). This class 
was born during development of simple Android app, created by the PHP developer who started his way to
Android and Java :).

The main concept of Jimple (and Pimple) is to instantiate contained object on acces to it. Pimple uses closures
to store dependency creation and initialization code, but since Jimple has been developed in context of Android app,
where closures are not available - it uses subclasses instead of closures, so Jimple is port of Pimple to Java, without closures.

## Usage
("Port" of offcial Pimple doc)

### Defining parameters
Defining a parameter is as simple as using the Jimple instance as an HasMap:

```java
import org.jimple.Jimple;

Jimple container = new Jimple();

container.put("param", "theParam");
```

### Defining objects
Objects are defined by `Jimple.Item` in `create()` method:

```java
container.put("Foo", new Jimple.Item() {
	@Override
	public Object create(Jimple c) {
	    return new Foo((String) c.get("param"));
	}
});

container.put("Bar", new Jimple.Item() {
	@Override
	public Object create(Jimple c) {
	    return new Bar((Foo) c.get("Foo"));
	}
});
```
Notice that the `create()` mthod has the current container instance as a paramter, allowing references to other objects or parameters.
As objects are only created when you get them, the order of the definitions does not matter, and there is no performance penalty.
Using the defined objects is also very easy:

```java
// get the `Bar` object
Bar bar = (Bar) container.get("Bar");

// the above call is roughly equivalent to the following code:
// Foo foo = new Foo("param");
// Bar bar = new Bar(foo);
```
### Defining Shared Objects
By default, each time you get an object, Pimple returns a new instance of it. If you want the same instance to be returned for all calls, wrap your `Jimple.Item` with the `share()` method:

```java
container.put("Bar", container.share(new Jimple.Item() {
	@Override
	public Object create(Jimple c) {
	    return new Bar((Foo) c.get("Foo"));
	}
}));
```

See tests for more usage examples.

## Authors
[Fabien Potencier](http://fabien.potencier.org/) - creator of the Symfony framework and [Pimple](http://pimple.sensiolabs.org/).

Leonid Kuzmin ported Pimple to Java.