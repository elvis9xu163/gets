# gets
简化烦人的null值判断操作<br/>
simplify boring null judgement. e.g: if (a != null &amp;&amp; a.getB() !=null &amp;&amp; a.getB().getC() != null &amp;&amp; ...)
<br/>
Example:
```java
A a = null;

// ... some operations

// traditional way
if (a != null && a.getB() != null && a.getB().getC() != null
		&& a.getB().getC().getD() != null && a.getB().getC().getD().getList() != null) {
	for (Object item : a.getB().getC().getD().getList()) {
		// ... do some business
	}
}

// use Gets way
if (Gets.isNotNull(Gets.wrap(a, A.class).getB().getC().getD().getList())) {
	// ... do 'for' business
}

// or use Gets as
if (Gets.get(Gets.wrap(a, A.class).getB().getC().getD().getList()) != null) {
	// ... do 'for' business
}

// some other use
String key = Gets.get(Gets.wrap(a, A.class).getB().getC().getD().getKey(), "NO_KEY");
System.out.println(key); // key = "NO_KEY";
```