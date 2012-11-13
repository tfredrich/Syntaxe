Syntaxe is an annotations-based, functional-style syntactic (and semantic) domain model validation
framework for Java.

Simply annotate fields in your domain model and invoke the ValidationEngine.validat() method to
accomplish syntactic validation. Multiple annotations per field can be used to enforce multiple
validations, if necessary.

Supported annotations are:

# @StringValidation - enforces 'required-ness', min/max length.
# @RegexValidation - enforces the string field to comply with a regular expression.
# @IntegerValidation - enforces min/max value.

Created to be simple, Syntaxe supports the following:

# Annotation-based validations on fields.
# Functional-style closures that allow users to create additional custom validations,
   whether syntactic or semantic.
# Validatable interface which calls out the validation contract.
# AbstractValidatable which is the default implementation of Validatable, which supports the
   use of @Validate and functional validations.
# Validations utility class containing foreign methods to perform your own default validations
   such as requiredness, less-than, greater-than.
# Validator utility class that implements validation on an instance that leverages the 
   @BasicValidate annotation.

In addition Syntaxe allows annotation of an entire class with the @ObjectValidation annotation
to provide object-wide validation in addition to field-level annotation validations.

Sample Usage:
===================================================================================================
Option 1, POJO with annotations
-------------------------------

--This option enables POJO validations without requiring extension or implementation, using
  annotations and the ValidationEngine to accomplish validation.

```java
public class MyValidatableClass
{
	@StringValidation(name="ID", required=true)
	private String id;
	
	@StringValidation(name="Name", required=true, maxLenth=25)
	private String name;

	@IntegerValidation(name="Count", min=3, max=21)
	private int count;
	
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
	
	@StringValidation(name="Name", required=true, minLength=5, maxLenth=25)
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

# Create an annotation the captures the fields your validator needs to know about.
# Create a class that implements com.strategicgains.syntaxe.annotation.ValidationProvider
	* AbstractValidationProvider might be a good start
	* The perform() method returns void, and should populate the List<String> errors parameters with any validation problems
# Add a ValidationProvidedBy annotation to your custom validator annotation that points to your class from step 2.
# See the com.strategicgains.syntaxe.validators.basic|regex for examples.


Change History:
===================================================================================================
Release 0.4.x (current master branch)
-------------------------------------
* Introduced message(), optional parameter to @RegexValidation annotation to facilitate describing
  the message to end-users instead of giving them the cryptic regex message.
* Introduced ValiationEngine.validateAndThrow(Object), which throws a ValidationException if there
  are validation errors in the object.

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