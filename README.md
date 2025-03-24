# CS321 Project - Group 16
#### Dr. Reep, Spring 2025
Our Functional Area: Genealogy Records Request - allow a US citizen to request records and selected forms on a deceased immigrant (not all forms can be provided)

# Coding Standards

## 1. Naming Conventions

**Classes and Interfaces**: Use PascalCase *(capitalize each word without spaces)*.
> Example: `RequestForm`, `EmployeeDetails`, `DeceasedImmigrant`

**Methods**: Use camelCase *(start with a lowercase letter and capitalize subsequent words)*.
> Example: `getFormID()`, `validateForm()`, `setLegible()`

**Variables**: Use camelCase for instance variables and method parameters.
> Example: `requestorName`, `deceasedPersonName`

**Constants**: Use UPPER_SNAKE_CASE *(all uppercase letters with underscores separating words)*.
> Example: `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT`

## 2. Code Formatting

**Indentation**: Use 4 spaces for indentation *(avoid tabs)*.

**Brackets**: Place brackets on the same line as the control statement *(e.g. `if`, `for`, `while`)*.
> Example:
> ```java  
> if (condition) {  
>   // Do something  
> }

**Line Length**: Limit lines to 80 characters. If necessary, break longer lines into multiple lines.

**Spacing**: Include a single space after keywords (`if`, `for`, `while`, etc.) and before opening brackets.
> Examples:  
> `if (condition) {`  
> `for (int i = 0; i < 10; i++) {`

**Blank Lines**: Use blank lines to separate logical blocks of code, such as between methods or class definitions.
         
## 3. Code Structure

**Class Structure**: Place methods in the following order:
- Public methods at the top, followed by protected and private methods.
- Constructor(s) should appear first, followed by getter and setter methods.
- Utility methods should come after business logic.

**Method Length**: Keep methods short and focused. Methods should not exceed 30 lines of code. If a method exceeds this length, break it into smaller methods.

## 4. Commenting

**Class-level comments**: Add a brief description of what the class does at the top of each class.
> Example:  
> ```java
> // This class represents the RequestForm used for submitting immigration requests.  
> public class RequestForm {  
>     // class code  
> }

**Method-level comments**: Add Javadoc comments before each method to describe its functionality and parameters.
> Example:  
> ```java
> /**  
>  * Validates the form for completeness and legibility.
>  * 
>  * @return true if the form is valid, false otherwise
>  */  
> public boolean validateForm() {  
>     // method code  
> }

**Inline Comments**: Use inline comments sparingly to explain complex or non-obvious code logic.
      
## 5. Testing Standards

**Unit Tests**: Ensure that every method has an associated unit test, and all edge cases are tested.

**Naming**: Test method names should clearly indicate what they are testing. Use the format `methodName_condition_expectedResult()`.
> Examples:   
> `validateForm_missingRequestorName_invalid()`  
> `processRequest_validRequest_success()`

**Test Coverage**: Aim for at least 80% test coverage on all code that is not trivial (e.g., getters/setters may be excluded).

## 6. Code Reviews

**Pull Requests (PRs)**: Always create a PR for every task. Ensure PRs are focused and small. A PR should contain one logical change or feature.

**Review Guidelines:**    
- Check for correctness, clarity, and efficiency.
- Ensure that unit tests exist and pass for all new functionality.
- Look for consistent use of coding standards (e.g., indentation, naming conventions).
- Avoid redundant code; encourage the use of helper methods or reusing existing methods.
- Comment sparingly; avoid obvious comments and focus on clarifying complex code.
- Ensure that error handling is done properly, and exceptions are logged appropriately.

