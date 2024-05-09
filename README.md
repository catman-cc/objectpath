# ObjectPath

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=catman-cc_objectpath&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=catman-cc_objectpath)
[![unit test](https://github.com/catman-cc/objectpath/actions/workflows/mvn.yml/badge.svg)](https://github.com/catman-cc/objectpath/actions/workflows/mvn.yml)

> ObjectPath is a path parser for accessing Java objects, similar to jsonpath, but ObjectPath is used for accessing Java objects rather than JSON objects.

[中文文档](docs/README-zh.md) | [English Document](README.md)

---
## Quick Start
### 1. Add Dependency
```xml
<dependency>
    <groupId>cc.catman</groupId>
    <artifactId>objectpath</artifactId>
    <version>0.0.1</version>
</dependency>
```

## 1. Syntax Rules
### 1.1. Syntax Rules
The syntax of objectpath is a superset of jsonpath, so the syntax rules of jsonpath also apply in objectpath, and objectpath also supports some additional syntax rules.
#### 1.1.1. Basic Syntax
To better demonstrate the syntax rules of objectpath, the java object information used in the following examples is as follows:
```json
{
    "store": {
        "book": [
            {
                "category": "reference",
                "author": "Nigel Rees",
                "title": "Sayings of the Century",
                "price": 8.95
            },
            {
                "category": "fiction",
                "author": "Evelyn Waugh",
                "title": "Sword of Honour",
                "price": 12.99
            },
            {
                "category": "fiction",
                "author": "Herman Melville",
                "title": "Moby Dick",
                "isbn": "0-553-21311-3",
                "price": 8.99
            },
            {
                "category": "fiction",
                "author": "J. R. R. Tolkien",
                "title": "The Lord of the Rings",
                "isbn": "0-395-19395-8",
                "price": 22.99
            }
        ],
        "bicycle": {
            "color": "red",
            "price": 19.95
        }
    },
    "expensive": 10
}
```


| Syntax | Description             |
|----|--------------------------|
| $  | Root node                |
| @  | Current node             |
| @@ | Parent node              |
| ID | Child element with the current node named ID   |
| .ID | Child element with the current node named ID   |
| ..ID | Recursively get all child elements named ID |
| [ID] | Child element with the current node named ID   |
| [NUMBER] | Element at the specified index in the array |
| [*] | All elements in the array |
| [] | All elements in the array |
| [?(expression)] | Filter elements in the current array |

#### 1.1.2. Built-in Simple Function Support
| Function Name | Parameter List | Description |
|---------------|----------------|-------------|
| isNull | - | Whether it is null |
| between | Number, Number | Whether it is between two numbers |
| like | - | Similar to SQL's like, using %% as pattern matching before and after |
| in | expr | Whether it is in the collection |
| notIn | expr | Not in the specified collection |
| contains | expr | Whether it contains the specified element/text |
| startWith | expr | Whether it starts with the specified text |
| endWith | expr | Whether it ends with the specified text |
| matches | expr | Regular expression matching |

#### 1.1.3. Built-in High-order Function Support
| Function Name | Parameter List | Description |
|---------------|----------------|-------------|
| min | expr | Get the minimum value in the array |
| max | expr | Get the maximum value in the array |
| sum | expr | Get the sum in the array |
| avg | expr | Get the average value in the array |
| size | - | Get the size of the array |
| isEmpty | - | Determine whether the array is empty |
| length | - | Get the length of the string |
| keys | - | Get all keys of the object |
| values | - | Get all values of the object |
| reverse | - | Reverse the array |
| sort | expr | Sort the array |
| groupBy | expr | Group the array |
| substring | start, end | Substring |
| replace | old, new | Replace |
| trim | - | Trim whitespace from both ends of the string |
| ltrim | - | Trim whitespace from the left end of the string |
| rtrim | - | Trim whitespace from the right end of the string |
| toUpper | - | Convert to uppercase |
| toLower | - | Convert to lowercase |
| concat | expr | Concatenate strings |
| first | - | Get the first element of the array |
| last | - | Get the last element of the array |

#### 1.1.4. Built-in Script Support
In addition to built-in simple functions and high-order functions, ObjectPath also supports built-in scripts. The syntax of the script is similar to Java's lambda expressions, but the script in ObjectPath is a string, not a Java object.
Example script:
```xml
<js name="a" age="18">
    return {
        name: a,
        age: age
    };
</js>
```
The 'js' tag in the script is used to indicate that this is a JavaScript script, 'name' and 'age' are parameters of the script, and the content within the 'js' tag is the body of the script. The return value of the example script is a JSON object.
It is similar to the JavaScript code:
```javascript
    function x(name, age){
        return {
            name: a,
            age: age
        };
    x("a", 18);
}
```
Script parameters support objectpath syntax, so you can use objectpath syntax to get the values of parameters. Example script:
```xml
<js name="$.name" age="$.age">
    return {
        name: name,
        age: age
    };
</js>
```

### Pipeline Operator
Objectpath supports the pipeline operator, which is used to pass the result of the previous operation as input to the subsequent operation. Example:
```
$.store.book[?(@.price > 10)] | $.title
```
In the above example, it first retrieves all books with prices greater than 10, then retrieves the 'title' attribute of these books, and finally returns an array of titles.

In addition to the pipeline usage scenarios, a special operator is defined for the pipeline, which is used to flatten arrays. Example:
```
$.store.book[?(@.price > 10)] | $.title | [^]
```
The '[^]' operator at the end is used to flatten the array, expanding all elements in the array into a one-dimensional array.
The '[^]' operator also supports parameters for specifying the depth of expansion, for example:

```
$.store.book[?(@.price > 10)] | $.title | [^2]
```
In the above example, the depth of expansion is 2, indicating that two levels of expansion are performed.

### Development Guide
#### Custom Functions
Objectpath supports custom functions and provides support for custom functions at the syntax parser level. The syntax of custom functions is as follows:
```xml
<fn name="functionName" args="parameterList">
    functionBody
</fn>
```