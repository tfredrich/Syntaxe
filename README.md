**Build Status** [![Build Status](https://buildhive.cloudbees.com/job/RestExpress/job/Syntaxe/badge/icon)](https://buildhive.cloudbees.com/job/RestExpress/job/Syntaxe/)

**Waffle.io** [![Stories in Ready](https://badge.waffle.io/RestExpress/Syntaxe.png?label=ready)](https://waffle.io/RestExpress/Syntaxe)

Syntaxe - Domain Model Validation
=================================
Syntaxe is an annotations-based, functional-style syntactic (and semantic) domain model validation
framework for Java.

Simply annotate fields in your domain model and invoke the ValidationEngine.validate() method to
accomplish syntactic validation. Multiple annotations per field can be used to enforce multiple
validations, if necessary.

Also note that annotations can be used on homogeneous collections as well. For example, annotating
a List<String> property with @StringValidation will enforce the validation rules on the entire
collection.

It works for Map sub-types as well. However, only the values collection (not the keys) is validated.

Supported annotations are:
* @ChildValidation - executes validations on a referenced object or collection of objects.
* @CollectionValidation - Enforce size limits (min & max) on a collection or map.
* @DoubleValidation - enforced min/max value.
* @FieldValidation - utilize your own Validator implementation for the annotated field/property.
* @FloatValidation - enforced min/max value.
* @IntegerValidation - enforces min/max value.
* @LongValidation - enforced min/max value.
* @ObjectValidation - utilize your own Validator implementation for the annotated class.
* @RegexValidation - enforces the string field to comply with a regular expression (Deprected. Use StringValidation instead).
* @Required - enforce presence of a nullable type.
* @StringValidation - enforces 'required-ness', min/max length, regex pattern validation.
* @ValidationProvider - utilize your AnnotatedFieldValidator implementation for the annotated field/property.

In addition, to help protect from cross-site scripting (XSS) attacks, annotations are available
to leverage the OWASP XSS library on string fields (also, see Maven Usage below):
* @JavascriptEncoded - performs Javascript encoding.
* @XmlEncoded - performs XML encoding.
* @HtmlEncoded - performs HTML encoding.
* @WebEncoded - A meta-encoder that performs both Javascript and HTML encoding, in that order.
* @EncodingProvider - a generic field annotation to leverage your own XssEncoder implementation.

Created to be simple, *Syntaxe* supports the following:
* Annotation-based validations on fields and objects.
* AbstractValidatable which is the default implementation of Validatable, which supports the
   use of @Validate and functional validations.
* Validations utility class containing foreign methods to perform your own default validations
   such as requiredness, less-than, greater-than.
* Validatable interface which calls out the validation contract for in-object validations.
* Object graph validation to validate an entire payload in one call.

In addition Syntaxe allows annotation of an entire class with the @ObjectValidation annotation
to provide object-wide validation in addition to field-level annotation-driven validations.

Maven Usage
===========
Stable:
```xml
		<dependency>
			<groupId>com.strategicgains</groupId>
			<artifactId>Syntaxe</artifactId>
			<version>1.0</version>
		</dependency>
```
Development:
```xml
		<dependency>
			<groupId>com.strategicgains</groupId>
			<artifactId>Syntaxe</artifactId>
			<version>1.1-SNAPSHOT</version>
		</dependency>
```
Or download the jar directly from: 
http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22Syntaxe%22

If you want to use the OWASP encoding annotations (available since 0.4.7-SNAPSHOT), also
include the OWASP Encoder jar in your pom:
```xml
		<dependency>
			<groupId>org.owasp.encoder</groupId>
			<artifactId>encoder</artifactId>
			<version>1.2</version>
		</dependency>
```

Note that to use the SNAPSHOT version, you must enable snapshots and a repository in your pom file as follows:
```xml
  <profiles>
    <profile>
       <id>allow-snapshots</id>
          <activation><activeByDefault>true</activeByDefault></activation>
       <repositories>
         <repository>
           <id>snapshots-repo</id>
           <url>https://oss.sonatype.org/content/repositories/snapshots</url>
           <releases><enabled>false</enabled></releases>
           <snapshots><enabled>true</enabled></snapshots>
         </repository>
       </repositories>
     </profile>
  </profiles>
```
Sample Usage:
===================================================================================================
Option 1, POJO with annotations
-------------------------------

--This option enables POJO validations without requiring extension or implementation, using
  annotations and the ValidationEngine to accomplish validation. In the case of numerics, this
  works equally well with primitives and object types (e.g. double/Double, float/Float, int/Integer).

```java
public class MyValidatableClass
{
	@StringValidation(name="ID", required=true)
	private String id;
	
	@StringValidation(name="Name", required=true, maxLength=25)
	private String name;

	@IntegerValidation(name="Count", min=3, max=21)
	private int count;

	@DoubleValidation(name="Radius", min=0.0, max=3.1459, isNullable=false)
	private Double radius;

	@FloatValidation(name="Float Value", min=0.0, max=6.5)
	private float floatValue;

	@RegexValidation(name="Email Address", pattern="(\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,6})", message="must be a valid email address")
	private String email;
}
...
```

elsewhere (e.g. before persisting to storage) call the ValidationEngine.validate(object) method
passing the MyValidatableClass instance.  If the returned List<String> list of error messages is
not empty, validation failed.  The validation error messages are in the list.

Option 2, extend AbstractValidatable
------------------------------------

--This provides the capability to override the validateInto(StringBuffer) method to provide custom
  validations.

```java
public class MyValidatableClass
extends AbstractValidatable
{
	@StringValidation(name="ID", required=true)
	private String id;
	
	@StringValidation(name="Name", required=true, minLength=5, maxLength=25)
	private String name;

	@IntegerValidation(name="Count", min=3, max=21)
	private int count;
}
...
```

elsewhere (e.g. before persisting to storage) call the validate() method on the MyValidatableClass
instance.


Option 3, Validatable implementor, no annotations.
-------------------------------------------------

--This option allows complete customization of validations by implementing the validate() method
  and throwing a ValidationException when validation fails.

```java  
public class MyValidatableClass
implements Validatable
{
	private String id;
	private String name;
	private int count;
	
	public void validate()
	{
		// provide validation throwing new ValidationException(List<String> errors) if validation
		// fails.
	}
}
```

Option 4, Object Validation annotation.
--------------------------------------

--This option enables POJO validations, separating the validation concerns from the object
  itself.  The ValidationEngine will invoke the assigned validator.

--Can be combined with Option 1 (above), POJO with annotations also.

```java
@ObjectValidation(MyPojoValidation.class)
public class SomePojo
{
	...
}
...
```

elsewhere (e.g. before persisting to storage) call the ValidationEngine.validate(object) method
passing the MyValidatableClass instance.  If the returned List<String> list of error messages is
not empty, validation failed.  The validation error messages are in the list.

Creating your own validator:
============================

1. Create an annotation that captures the fields your validator needs to know about (e.g. Message, etc.).
2. Create a class that implements com.strategicgains.syntaxe.annotation.ValidationProvider
	* AbstractValidationProvider might be a good start
	* The perform() method returns void, and should populate the List<String> errors parameters with any validation problems
3. Add a ValidationProvidedBy annotation to your custom validator annotation that points to your class from step 2.
4. See the com.strategicgains.syntaxe.validators.basic or com.strategicgains.syntaxe.validators.regex for examples.


Validating object graphs:
=========================

To validate a graph of objects originating from a root object, annotate the fields with @ChildValidation.
Syntaxe will process the fields of the child object, list of objects, or array of objects as described
in the options above. Can be combined with @Required to ensure that the property has a value.

```java
public class RootPojo
{
  @Required("Child POJO")
  @ChildValidation
  private ChildPojo childPojo;

  @ChildValidation
  private List<ChildPojo> childPojoList;

  @ChildValidation
  private ChildPojo[] childPojoArray;

  ...
}

public class ChildPojo
{
  ...
}
...
```

Change History:
===================================================================================================
1.1 - SNAPSHOT
--------------
* Added dot-notation-style field name prefixes for ChildValidation errors when validating an object graph.
* Added CollectionValidation annotation to support enforcement of collection and map size limits.
* Added support in all validation annotations to validate Map values (not keys) as well as collections and arrays.
* Deprecated RegexValidation annotation in favor of adding the pattern functionality to the StringValidation annotation.

1.0 - Released 10 Feb 2016
--------------------------
* Merge pull request #8 from Noor Dawod, "Added DoubleValidator implementation"
* Merge pull request #7 from Noor Dawad, "Added FloatValidator implementation"

0.4.10 - Released 28 July 2015 
------------------------------
* Upgraded Java output to 1.7 source and target.

0.4.9 - Released 2 Dec 2014
-----------------------------------------------------
* Implemented validation for arrays and collections in all ...Validator sub-classes. Moved validateCollection() and validateArray() to AnnotatedFieldValidator.
* Added object graph validation (thanks to Jason Reicheneker)

0.4.8 - Released 24 Oct 2014
----------------------------
* Enabled @IntegerValidation & @LongValidation on non-primitive Integer & Long types, respectively. Adding an isNullable property of the annotation.
* RegexValidator changed to support validation involving Collections and Arrays.

Release 0.4.7 - Released 3 Apr 2014
-----------------------------------
* Added annotations to leverage the OWASP cross-site scripting encoding library: @JavascriptEncoded,
  @XmlEncoded, @HtmlEncoded, @WebEncoded. To use, include the OWASP library in your pom.xml file (see Maven Usage, above).
* Enhanced @EncodingProvider to be able to use it directly on a field and leverage your own XssEncoder implementation.
* Added @LongValidation per issue #2.
* Fixed stack overflow issue when Validatable.validate() calls ValidationEngine.validate()
  for the same object (e.g. circular validation).  Circular validation will produce more-than
  the expected number of errors, but no longer cause a stack trace.

Release 0.4.6 - Released 17 Jul 2013
------------------------------------
* Added @Required annotation to require non-null values in non-primitive properties. Also
  checks array properties for non-null elements.

Release 0.4.5 - Released 8 Mar 2013
-----------------------------------
* Renamed getAllDeclaredFields(Class, FieldClosure) to computeDeclaredFields(), exposing it publicly
  and making it return a generic type.

Release 0.4.4 - Released 16 Jan 2013
------------------------------------
* Removed Ant build-related cruft.
* Ensured Java 1.6 compatible artifact produced from build.

Release 0.4.3 - Released 08 Jan 2013
-------------------------------------
* Released to Maven Central repository.
* Introduced Maven build.
* Introduced the @FieldValidation annotation to utilize your own Validator at the field level.
* Introduced message(), optional parameter to @RegexValidation annotation to facilitate describing
  the message to end-users instead of giving them the cryptic regex message.
* Introduced ValidationEngine.validateAndThrow(Object), which throws a ValidationException if there
  are validation errors in the object.
* Fixed poor wording in Validations.minLength() error message.

Release 0.3
-----------
* Refactored the ValidationEngine to support the notion of multiple validators per field.
* This refactoring also now allows for extensible validators be created outside this library.
* Renamed com.strategicgains.syntaxe.annotation.Validate to com.strategicgains.syntaxe.validators.basic.BasicValidate
* Removed dependency on the bpel jar, for no other reason than pluggable validators went towards interfaces instead of closures.
* Validators are now found by being Annotations with the ValidationProvidedBy annotation.
* Added a Regular Expression validator.

Release 0.2
-----------
* Refactored Validator into ValidationEngine.

Release 0.1
-----------
* Initial extract from RestExpress. Base implementation on which to expand.
