# ObjectPath Library

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=catman-cc_objectpath&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=catman-cc_objectpath)
[![Compile And Unit Test](https://github.com/catman-cc/objectpath/actions/workflows/compile-and-unit-test.yml/badge.svg)](https://github.com/catman-cc/objectpath/actions/workflows/compile-and-unit-test.yml)

[中文文档](./docs/README-zh.md) | [English Document](../README.md)

ObjectPath is a path parser for accessing Java objects, similar to jsonpath but designed for accessing and modifying Java objects instead of JSON objects.

## Why ObjectPath?

Currently, there are two mainstream libraries that support jsonpath syntax:

* The [json-path library by Jayway](https://github.com/json-path/JsonPath), which is used to read content from JSON documents, but only supports operating on JSON strings or its defined JSON Document objects.
* The [fastjson library by Alibaba](https://github.com/alibaba/fastjson2), which can operate on both JSON content and Java objects with excellent performance, but its syntax support is not comprehensive and lacks many advanced jsonpath features.

Both have very obvious advantages and disadvantages. The json-path library supports more flexible and rich syntax but can only operate on JSON strings or specific JSON Document objects. Fastjson has a significant performance advantage and can directly operate on Java objects, but its drawback is that it only supports basic jsonpath syntax and is not very flexible.

I needed a library that could directly operate on native Java objects while maintaining the flexible and rich jsonpath syntax. After searching, I couldn’t find a suitable library, so I developed this one.

> If you need a library that can use jsonpath syntax to access Java objects, requires advanced jsonpath features, and needs flexible extension capabilities, then ObjectPath is for you.

> ObjectPath aims to provide an easy-to-use, flexible, feature-rich, extensible, and reliable Java object access library.

> The syntax of ObjectPath is a superset of jsonpath, so jsonpath syntax rules are also applicable in ObjectPath, with some additional syntax rules supported by ObjectPath.

## Getting Started

ObjectPath has been packaged and uploaded to the Maven Central Repository, so it can be introduced directly using the following code:

```xml
<dependency>
    <groupId>cc.catman</groupId>
    <artifactId>objectpath</artifactId>
    <version>0.0.1</version>
</dependency>
```
ObjectPath provides an out-of-the-box OP object with no configuration required. You can directly call its parse method to parse jsonpath syntax, and then call the eval method to get the parsed data.

```java
OP.parse("$.book[0].price").eval(new Order());
```
Additionally, we provide another ObjectPath object that offers two overloaded static of methods:

- `public static ObjectPathAccessor of(String path, ObjectPathConfiguration config)`
- `public static ObjectPathAccessor of(String path)`
- 
The `public static ObjectPathAccessor of(String path)` method works similarly to the OP parse method, while the other method allows users to provide a configuration object to customize the jsonpath parsing process.

See [Configuration Objects for ObjectPath section](#Configuration Objects for ObjectPath) for specific details on configuration objects.

To better demonstrate the syntax rules of ObjectPath, the Java object information used in the following examples is as follows:

```json
{
  "store": {
    "id": "storeID",
    "book": [
      {
        "id": "book1",
        "category": "reference",
        "author": "Nigel Rees",
        "title": "Sayings of the Century",
        "price": 8.95,
        "costPrice": 5.1
      },
      {
        "id": "book2",
        "category": "fiction",
        "author": "Evelyn Waugh",
        "title": "Sword of Honour",
        "price": 12.99,
        "costPrice": 5.99
      },
      {
        "id": "book3",
        "category": "fiction",
        "author": "Herman Melville",
        "title": "Moby Dick",
        "isbn": "0-553-21311-3",
        "price": 8.99,
        "costPrice": 4.99
      },
      {
        "id": "book4",
        "category": "fiction",
        "author": "J. R. R. Tolkien",
        "title": "The Lord of the Rings",
        "isbn": "0-395-19395-8",
        "price": 22.99,
        "costPrice": 12
      }
    ],
    "bicycle": {
      "id": "bicycle1",
      "color": "red",
      "price": 19.95,
      "costPrice": 12.05
    }
  },
  "expensive": 10
}
```

ObjectPath syntax is extremely flexible and powerful. In most cases, we can directly use inline JSONPath syntax.

For example, for the standard JSONPath syntax: `$["book"][0]`, we can use the following syntaxes that have the same effect:

- `$['b'+'ook'][1-1]`
- `$['b'.concat('o')+'ok'][1-1]`

ObjectPath syntax is extremely flexible and powerful. In most cases, we can directly use inline JSONPath syntax.

For example, for the standard JSONPath syntax: `$["book"][0]`, we can use the following syntaxes that have the same effect:

- `$['b'+'ook'][1-1]`
- `$['b'.concat('o')+'ok'][1-1]`
```java
    @Test
public void useExprFillAnyPart() {
    StoreHolder sh= Cases.storeHolder();
    Store store = sh.getStore();
    Book want = store.getBook().get(0);
    Stream.of(
            "$[book][0]"
            ,"$['b'+'ook'][1-1]"
            ,"$['b'.concat('o')+'ok'][1-1]"
    ).forEach(expr->{
        Book find = OP.parse(expr).eval(store, Book.class);
        Assert.assertEquals(want,find);
    });
}

```
## Syntax Rules
### Basic Syntax Rules
A valid JSONPath expression consists of two parts: locators and selectors.

There are two locators, which are:

- `@`: Represents the current element, for example: `@.id`, which accesses the id value of the current element and changes according to the parsing context.

    > Suppose we have the following two JSONPath expressions to access the example object given earlier: `@.id` and `@.book[?(@.id like '%a%')]`. Both contain `@.id`, but the former corresponds to the value storeID, while the latter corresponds to the id of **each** element in the book collection, such as book1 and book2.

- `$`: Represents the root element, which always accesses the root object. Suppose in the same two scenarios, we replace `@.id` with `$.id`, the value will **always** be storeID.

Selectors are more flexible. For ease of understanding, I classify selectors as follows:

The most straightforward and commonly used selector is the child element selector, which is responsible for the most basic access operations to internal resources.

| Expression        | Description                                            | Example                   | Example Description                                         | Return Value                          |
| ----------------- | ------------------------------------------------------ | ------------------------- | ----------------------------------------------------------- | ------------------------------------- |
| `.number` \| `.name` | Get child elements by specified index/name           | `$.book`                  | Get the child element named `book` from the root element   | `[]`, omitted                         |
| `..ID`             | Recursive descent, get all child elements with the specified name | `$..id`                  | Get all elements named `id`                               | `["70...215", "39..46e", ...]`        |
| `[ID(,ID)+]`       | Get multiple child elements of the current element and merge into a map | `$.book[0]['id','price']` | Get `id` and `price` data from the first element in `book` array and combine into a map | `{"id":"3993c542-66ba-459d-bcea-97434f8aa46e", "price":300}` |
| `[ID]`             | Get child element by specified name                    | `$[book]`<br />`$.book[0]`| Get resources by specified name                            | `[]` omitted,<br />`{"id":...}`      |
| `[Expression]`     | Support returning text or numeric values via expression and access corresponding elements | `$[book][1-1]`           | Supports computing data for each part using expression    | Equivalent to `$[items][0]`          |
| `[*]`             | Access all elements in the collection                  | `$[book][*]`             | Note the distinction from `$[book]`: `$[book]` accesses the `book` object, while `$[book][*]` accesses each child element |                                       |
| `[start:end?]`     | Slice the collection to get a new collection            | `$[book][0,1]`           | Slice the `book` collection from index 0 to 1 to form a new collection |                                       |
| `[num(,num)+]`     | Select elements at specified positions in the collection and form a new collection | `$[book][0,1,3]`       | Select elements at indices 0, 1, and 3 from `book` collection to form a new collection |                                       |

Besides the child element accessor, another frequently used syntax in jsonpath is the **filter syntax**. It is a special type of child element accessor that is typically applied to collection elements, **used to get a subset of elements from the collection that meet specific conditions**.

The filter syntax is simple: `[?(returning boolean value)]`. For example, in the previously mentioned example `@.book[?(@.id like '%a%')]`, it is used to filter records in the `book` collection where the `id` contains the text `a`.

To make filter syntax effective, we provide many built-in expressions or methods for making judgments. Additionally, when comparing data, we use some literals such as numbers or text. Therefore, we have included support for some literals and provided common logical operations and arithmetic calculations:

| Expression          | Description                                               | Example                                  | Example Description                                                                |
| ------------------- | --------------------------------------------------------- | ---------------------------------------- | ---------------------------------------------------------------------------------- |
| `'text'` \| `"text"` | Text type                                                | `'hello,world'`                         | Provides a string `hello,world`                                                     |
| `-1` \| `0.2` \| `0.2D` | Numeric values                                           | `-1` \| `0.2` \| `0.2D`                 | Provides numeric values, with `L` \| `l` \| `D` \| `d` to force type to `Long` or `Double` |
| `true` \| `TRUE` \| `false` \| `FALSE` | Boolean values                                             | `true`                                  | Provides boolean values for true or false                                          |
| `/regex/`           | Special text: Regular expression                          | `/^j.*?a$/`                             | Provides the regular expression `^j.*?a$`                                          |
| Valid JSON content  | Valid JSON structure can also be used as an expression, and it will be serialized as a collection or map | `{"name":"jpanda","age":18}`            | Provides a JSON object serialized as a map                                         |
| `expr?:expr` \| `expr?expr:expr` | Default value expression                                    | `@.price?:10` \| `@.price?@.costPrice<@price:10` | Use value `10` if `price` is null. Use `10` if `costPrice` is less than `price` |
| `( expr )`          | Grouping to raise priority                                | `(@.price-@.costPrice)*@.quantity`      | Arithmetic operations do not support `*/%` default high priority; use grouping to customize priority |
| `expr ('+' | '-' | '*' | '/' | '%') expr` | Arithmetic operations                                        | `@.price-@.costPrice`                   | Supports basic addition, subtraction, multiplication, division, and modulo operations |
| `expr == expr`      | Logical comparison, equality                             | `@.price==@.costPrice+10`                | Checks if the current element's `price` equals `costPrice` + 10                   |
| `expr != expr`      | Logical comparison, inequality                           | `@.price!=@.costPrice+10`                | Checks if the current element's `price` does not equal `costPrice` + 10           |
| `expr < expr`       | Logical comparison, less than                            | `@.price<@.costPrice+10`                 | Checks if the current element's `price` is less than `costPrice` + 10             |
| `expr <= expr`      | Logical comparison, less than or equal to                | `@.price<=@.costPrice+10`                | Checks if the current element's `price` is less than or equal to `costPrice` + 10 |
| `expr > expr`       | Logical comparison, greater than                         | `@.price>@.costPrice+10`                 | Checks if the current element's `price` is greater than `costPrice` + 10          |
| `expr >= expr`      | Logical comparison, greater than or equal to             | `@.price>=@.costPrice+10`                | Checks if the current element's `price` is greater than or equal to `costPrice` + 10 |
| `expr is null`      | Logical comparison, is null                              | `@.name is null`                        | Checks if the current element's `name` is null                                    |
| `REG '~=' expr`     | Logical comparison, left side must be a regex expression, used to validate the right side value | `/^j.*$/ ~=@.name`                       | Checks if the current element's `name` matches the regex `^j.*`                   |
| `expr '=~' REG`     | Logical comparison, right side must be a regex expression, used to validate the left side value | `@.name =~ /^j.*$/`                      | Checks if the current element's `name` matches the regex `^j.*`                   |
| `expr 'like' expr`  | Logical comparison, pattern matching, like method        | `@.name like '%panda%'`                  | Checks if the current element's `name` contains `panda`                            |
| `expr ('and' | '&&') expr` | Logical AND                                               | `@.price>100 && @.costPrice<50 && @.price-@costPrice>100` | Filters data where cost price is less than 50, selling price is greater than 100, and profit is over 100 |
| `expr ('or' | '||') expr` | Logical OR                                                | `@.price>100 || @.costPrice<50 || @.price-@costPrice>100` | Filters data where cost price is less than 50, or selling price is greater than 100, or profit is over 100 |
| `!expr`            | Logical NOT                                               | `!@.price>100`                          | Checks if `price` is not greater than 100                                         |

In addition, a large number of built-in methods are provided to handle more complex scenarios:

| Method Name   | Description                                                                                                         | Type of Caller              | Return Type      | Number of Arguments | Argument Types                                  |
| ------------- | ------------------------------------------------------------------------------------------------------------------- | --------------------------- | ---------------- | -------------------- | ---------------------------------------------- |
| `min`         | Retrieves the element with the **minimum** value from a specified collection. Users can provide one parameter to filter the collection elements, accessible via `@`. | Collection                 | Collection Element | 1                    | Expression returning a numeric value           |
| `max`         | Retrieves the element with the **maximum** value from a specified collection. Users can provide one parameter to filter the collection elements, accessible via `@`. | Collection                 | Collection Element | 1                    | Expression returning a numeric value           |
| `sum`         | Calculates the total of numeric values in a collection. The method accepts one parameter to provide the value to be computed. | Numeric Collection         | Numeric           | 1                    | Expression returning a numeric value           |
| `avg`         | Calculates the average of numeric values in a collection. The method accepts one parameter to provide the value to be computed. | Numeric Collection         | Numeric           | 1                    | Expression returning a numeric value           |
| `size` \| `count` | Retrieves the number of elements in a collection.                                                                 | Collection                 | Numeric           | 0                    | Numeric                                         |
| `isEmpty`     | Checks if the collection is empty.                                                                                   | Collection                 | Boolean           | 0                    | -                                               |
| `index`       | Retrieves the index of an element in the collection, or the key if the element belongs to a map or object.          | (Collection \| Object \| MAP) Element | Numeric/Text      | 0                    | -                                               |
| `length`      | Retrieves the length of text or collection.                                                                         | Collection \| Text         | Numeric           | 0                    | -                                               |
| `keys`        | Retrieves all the keys of elements under the current object.                                                        | Collection \| Object \| MAP | (Numeric \| Text) Collection | 0                    | Collection                                     |
| `values`      | Retrieves all the values of elements under the current object.                                                      | Collection \| Object \| MAP | Collection of Objects | 0                    | Collection                                     |
| `reverse`     | Reverses the current collection.                                                                                      | Collection                 | Collection        | 0                    | Collection                                     |
| `distinct` \| `dis` | Removes duplicates from the current collection.                                                                      | Collection                 | Collection        | <2                   | Any                                            |
| `sort`        | Sorts the collection. One or more expressions can be provided for sorting, which will affect the sort order sequentially. | Collection                 | Collection        | >1                   | Boolean                                         |
| `rsort`       | Performs reverse sorting on the collection. One or more expressions can be provided for sorting, which will affect the sort order sequentially. | Collection                 | Collection        | >1                   | Boolean                                         |
| `groupBy`     | Groups the collection. An expression can be provided for grouping criteria, which will affect the sorting result sequentially. | Collection                 | Collection        | 1                    | Any                                            |
| `join`        | Joins one or more collections to the current collection.                                                              | Collection                 | Collection        | >1                   | Any                                            |
| `split`       | Splits text based on an expression that returns a string used to split the text.                                       | Text                       | Collection of Text | 1                    | Text                                           |
| `replace`     | Replaces text content. Two expressions must be provided: the first specifies the content to be replaced, and the second specifies the replacement content. | Text                       | Text              | 2                    | Text                                           |
| `substring` \| `sub` | Extracts a substring. Accepts one or two parameters: the first for the start position (inclusive), and the optional second for the end position (exclusive). Defaults to the length of the text. | Text                       | Text              | >1 and <2            | Numeric                                         |
| `toUpper`     | Converts text to uppercase.                                                                                           | Text                       | Text              | 0                    | -                                               |
| `toLower`     | Converts text to lowercase.                                                                                           | Text                       | Text              | 0                    | -                                               |
| `trim`        | Trims whitespace from both ends of the text.                                                                          | Text                       | Text              | 0                    | -                                               |
| `ltrim`       | Trims whitespace from the left side of the text.                                                                      | Text                       | Text              | 0                    | -                                               |
| `rtrim`       | Trims whitespace from the right side of the text.                                                                     | Text                       | Text              | 0                    | -                                               |
| `concat`      | Concatenates two strings. Requires an expression returning text to be used with the `+` operator.                      | Text                       | Text              | 1                    | Text                                           |
| `first`       | Retrieves the first element in the collection.                                                                         | Collection                 | Collection Element | 0                    | -                                               |
| `last`        | Retrieves the last element in the collection.                                                                          | Collection                 | Collection Element | 0                    | -                                               |
| `indexOf`     | Retrieves an element at a specified position in the collection or an element with a specified name in an object.       | Collection                 | Collection Element | 1                    | Numeric/Text                                    |
| `map`         | Converts the current object to another object using an expression.                                                     | Any                        | Any               | 1                    | Any                                            |
| `toMap`       | Converts the current element to a map collection.                                                                       | Collection \| Object \| MAP | MAP               | 0                    | -                                               |
| `isNull`      | Checks if the current element is null. If a parameter is provided, its value is used as the criteria for comparison.    | Any                        | Boolean           | <2                   | Any                                            |
| `notNull`     | Checks if the current element is not null. If a parameter is provided, its value is used as the criteria for comparison. | Any                        | Boolean           | <2                   | Any                                            |
| `and`         | Logical AND. Multiple expressions separated by commas can be provided, each returning a value that can be converted to a boolean. | Any                        | Boolean           | >1                   | Boolean                                         |
| `or`          | Logical OR. Multiple expressions separated by commas can be provided, each returning a value that can be converted to a boolean. | Any                        | Boolean           | >1                   | Boolean                                         |
| `not`         | Logical NOT. Accepts one expression that must return a value that can be converted to a boolean. If a parameter is provided, its value is used as the criteria for comparison. | Any                        | Boolean           | <2                   | Boolean                                         |
| `filter`      | Filtering. Can be replaced with standard `[?(filter)]`. Accepts one or more conditions for filtering elements, with logical AND between multiple conditions. | Collection                 | Collection        | >0                   | Boolean                                         |
| `between`     | Checks if the current element is within a specified numeric range. Accepts two parameters for minimum and maximum values, inclusive. | Numeric                    | Boolean           | 2                    | Numeric                                         |
| `like`        | Supports pattern matching with `%` for SQL-like syntax. Can be replaced with `startWith`, `endWith`, `==`.              | String                     | Boolean           | 1                    | Text                                           |
| `in`          | Checks if the current value exists in the specified list.                                                              | Any                        | Boolean           | >1                   | Any                                            |
| `notIn`       | The opposite of `in`.                                                                                                  | Any                        | Boolean           | >1                   | Any                                            |
| `contains`    | Checks if the current collection contains the specified elements.                                                       | Collection                 | Boolean           | >                    | Any                                            |
| `startWith`   | Checks if the text starts with the specified content.                                                                   | Text                       | Boolean           | 1                    | Text                                           |
| `endWith`     | Checks if the text ends with the specified content.                                                                     | Text                       | Boolean           | 1                    | Text                                           |
| `match`       | Checks if the text matches the specified regular expression.                                                            | Text                       | Boolean           | 1                    | Text                                           |
| `raw`         | Calls a native Java method. Requires at least one parameter: the first must be the Java native method name, with subsequent parameters as method arguments. | Any                        | Any               | >0                   | The first parameter must be text; subsequent parameters can be of any type |
| Custom Function | Calls a custom function.                                                                                             | Custom                     | Any               | >-1                  | Any                                            |

## Advanced Syntax Rules
In addition to the basic syntax rules mentioned earlier, ObjectPath provides more features to help developers quickly implement new functionalities:

##### Type Casting

In JSONPath syntax, due to operations like calculations or functions, the data type parsed might not meet our expectations. For example, you might expect an `int` type value but end up with a `double` type value. To address this issue, type casting is supported in the syntax. The syntax is similar to Java, using `(Type)expression` or `(Type)(expression)` to perform type casting. Adding `[]` after the type indicates a collection of that type.

Supported built-in types include:

| Java Type   | Type Text          | Example        |
| ----------- | ------------------- | -------------- |
| `String`    | `String` \| `string` \| `str` | (str)@.name  |
| `Integer`   | `Integer` \| `I`   | (I)@.price     |
| `int`       | `int` \| `i`       | (i)@.price     |
| `Double`    | `Double` \| `D`    | (D)(@.price)   |
| `double`    | `double` \| `d`    | (d)@.price     |
| `Boolean`   | `Boolean` \| `B`   | (B)@.isAdmin   |
| `boolean`   | `boolean` \| `b`   | (b)@.isAdmin   |
| `Float`     | `Float` \| `F`     | (F)@.price     |
| `float`     | `float` \| `f`     | (f)@.price     |
| `Long`      | `Long` \| `L`      | (L)@.price     |
| `long`      | `long` \| `l`      | (l)@.price     |
| `Short`     | `Short` \| `S`     | (S)@.price     |
| `short`     | `short` \| `s`     | (s)@.price     |
| `Byte`      | `Byte`             | (B)@.price     |
| `byte`      | `byte`             | (b)@.price     |
| `Character` | `Character` \| `C` | (C)@.price     |
| `char`      | `char` \| `c`      | (c)@.price     |

##### Pipe Operations

Similar to the pipe symbol in Unix, we use `|` as the pipe symbol in ObjectPath. By using the pipe symbol, we can split an ObjectPath expression into multiple stages. As data flows through the pipe, it is aggregated back into an object. This is primarily to solve the problem where the `[*]` syntax implicitly splits the context into multiple sub-contexts for execution.

> 📢. Normally, during syntax parsing, the subject object of the right-side expression corresponds to the value of the left-side expression. However, the `[*]` syntax implicitly splits the current subject object from a collection into its internal elements.

For example: `$.book[*].price` aims to retrieve the price values of all book elements in the book collection and then constructs a new collection from the parsed results.

Note that `.price` here is interpreted as each individual book element within the collection, not the entire book array. If we extend this syntax further: `$.book[*].price.min()`, the question arises:

Is the `min` function applied to each `price` or to the `price` array?

Logically, the goal is to find the minimum value in the `price` array, so `min` should apply to the `price` array. But consider this:

> What syntax should we use if we want to perform additional operations on each `price`?

For instance, if we define a custom function `roundUp` that rounds up values to a specified number of decimal places:

* `$.book[*].price.roundUp(2)` calls the `roundUp` function to round the price values and retain two decimal places. Should the `roundUp` function be applied to each `price` element?

This creates ambiguity. To avoid this problem, we parse the expression by applying subsequent expressions to each element of the collection when encountering `[*]`.

Thus, in parsing the expression `$.book[*].price.min()`, starting from `.price`, the subject is no longer the value corresponding to `$.books`, but rather the value of each element within the `$.books` collection.
![example](./docs/imgs/simple01.png)

##### Applying Expressions to the Entire Collection

Based on this design, how can we apply some expressions to the entire collection again after applying them to individual elements? For this purpose, we use the pipe symbol.

With the pipe symbol, we can first retrieve the `price` field from all book elements and then get the minimum value from those prices. The syntax for this is: `$.book[*].price|@.min()`. Alternatively, the same effect can be achieved with: `$.books.min().price`.

##### Custom Functions

ObjectPath supports user-defined functions to handle complex logic, providing a similar experience to built-in functions.

To create a custom function for formatting dates, you need to implement a `FunctionProvider` class.
The system provides `AbstractFunctionProvider` to simplify this process. 
You can then register your custom function using the `FunctionManager` in `ObjectPathConfiguration`. 
Here is the complete code for creating a custom date formatting function:
- Creating a Custom Function Provider
    ```java
    package cc.catman.object.core.function;

    import cc.catman.object.core.ObjectPathParserContext;
    import cc.catman.object.core.exception.FunctionRuntimeException;
    import lombok.extern.slf4j.Slf4j;

    import java.text.SimpleDateFormat;
    import java.util.Collections;
    import java.util.Date;
    import java.util.List;
    /**
     * 日期格式化函数提供者,用于格式化日期
     * 支持的参数有: Date,Timestamp,Long,String
     * @author jpanda
     * @since 0.0.1
     */
    @Slf4j
    public class DateFormatFunctionProvider extends AbstractFunctionProvider{

        public DateFormatFunctionProvider(String name) {
            super(name);
            FunctionArgDesc format = FunctionArgDesc.builder()
                    .type(String.class)
                    .desc("格式")
                    .build();
            this.args().add(Collections.singletonList(
                    format

            ));
        }

        @Override
        protected Object doApply(ObjectPathParserContext context,List<Object> params) {
            if (params.size() != 1) {
                log.error("参数数量不匹配");
                return null;
            }
            Object date = context.currentValue().read();
            Object format = params.get(0);
            if (date instanceof java.util.Date) {
                return new SimpleDateFormat(format.toString()).format((Date) date);
            } else if (date instanceof Long) {
                return new SimpleDateFormat(format.toString()).format(new Date((Long) date));
            } else if (date instanceof String) {
                long time;
                try {
                    time = Long.parseLong((String) date);
                } catch (NumberFormatException e) {
                    throw new FunctionRuntimeException(e, this.name(), params);
                }
                return new SimpleDateFormat(format.toString()).format(new Date(time));
            }
            return null;
        }

    }
    ```
  In the DateFormatFunctionProvider constructor, pay attention to the `this.args().add` related code. 

  We allow custom functions to support different parameter lists to accommodate various parameter types. 
  
  This field is a two-dimensional array, where each set of parameter type descriptions provides a list of supported 
  parameter types.
  ```java
   /**
     * 函数参数描述
     */
    private final List<List<FunctionArgDesc>> args;
  ```
  Additionally, when a custom function is invoked, it implicitly accesses the context of the function call. 
  You can retrieve the triggering object of the function call using the `context.currentValue().read()` method.
- Registering a function with the custom function manager
  ```java
    ObjectPathConfiguration configuration = ObjectPathConfiguration.create();

    FunctionManager fm = configuration.getFunctionManager();
    fm.register(new DateFormatFunctionProvider("format"));
     // 使用自定义函数
    ObjectPath op = ObjectPath.builder()
            .config(configuration)
            .build();
    long now = System.currentTimeMillis();
    Map<String,Long> map=new HashMap<>();
    map.put("time",now);
    ObjectPathAccessor ac = op.parse("$.time.format('yyyy-MM-dd HH:mm:ss')");
    Object res = ac.eval(map);
    Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now),res);
  ```
#### Calling Built-in Methods of Java Objects
ObjectPath supports directly calling methods on Java objects. 

Before making these calls, you need to enable the relevant permissions.

For security reasons, this permission is disabled by default:
```java
@Test
public void  funcForRaw(){
    Order order = OrderMock.mockOrderWithFixedSize(10);
    String expr="$.items.raw('size')";
    ObjectPathConfiguration cfg = ObjectPathConfiguration.create();
    cfg.setAllowExecuteRawMethod(true);
    ObjectPathAccessor as = ObjectPath.parse(expr,cfg);
    Integer res = as.eval(order,Integer.class);
    Assert.assertEquals(10, (int) res);
}
```
Of course, we can also call methods on custom objects. If there are overloaded methods, the system will try to select the most appropriate one to execute. The logic is as follows:

First, it verifies the method name and the number of parameters, and then compares the parameter types. Considering that the actual parameter list may include null values and subclass definitions, it will traverse all methods that meet the basic requirements and then try to find a matching method.

Matching methods are categorized into two types:

* **Best Match**: First, all methods are retrieved, and then the method list is traversed to compare the parameter lists. If the parameter types are an exact match, that method is returned as the best match.
* **Candidate Method**: Handles null values and subclasses. Note that if multiple methods have the same parameter list, it is impossible to determine which method to call, leading to an exception being thrown.

For example, we have the following two class definitions:
```java
public static class A{
    public String hello(){
        return "hello";
    }
    public String hello(String name){
        return "hello," + name;
    }
    public String hello(String name, Number age){
        return "hello," + name + " age: " + age;
    }
}

public static class B extends A{

    public String hello(String name,Object obj){
        return "hello," + name + " age: " + obj;
    }
}
```
The expected result of the method call is as follows:
```java

@Test
public void findSubstringMethod(){
    Object res = ReflectionHelper.invokeMethod("hello world", "substring", Arrays.asList(0, 5));
    assertEquals("hello", res);
}

@Test
public void invokeSameNameMethod(){
    A a = new A();
    Assert.assertEquals("hello", ReflectionHelper.invokeMethod(a,"hello", Collections.emptyList()));
    Assert.assertEquals("hello,world", ReflectionHelper.invokeMethod(a, "hello", Collections.singletonList("world")));
    Assert.assertEquals("hello,world age: 18", ReflectionHelper.invokeMethod(a, "hello", Arrays.asList("world", 18)));

    boolean hasException=false;
    try {
        Assert.assertEquals("hello,world age: 18", ReflectionHelper.invokeMethod(a, "hello", Arrays.asList("world", new Date())));
    } catch (PropertyAccessorRuntimeException e) {
        hasException=true;
    }
    Assert.assertTrue(hasException);
    hasException=false;
    B b = new B();
    try {
        Assert.assertEquals("hello,world age: 18", ReflectionHelper.invokeMethod(b, "hello", Arrays.asList("world", 18)));
    } catch (PropertyAccessorRuntimeException e) {
        hasException=true;
    }
    Assert.assertTrue(hasException);
}
```
##### Call the script engine

In addition to the features mentioned above, objectpath syntax also includes support for scripting languages. 

The system includes a `JavaScriptExecutor` to provide support for JavaScript, but it is not enabled by default and needs to be manually registered with the script language manager.

Here is a simple example:

```java
ObjectPathConfiguration configuration = ObjectPathConfiguration.create();

configuration.getScriptExecutorManager().addExecutor("js",new JavaScriptExecutor());

ObjectPath op = ObjectPath.builder()
        .config(configuration)
        .build();
Map<String,String> map=new HashMap<>();
map.put("name","world");
ObjectPathAccessor ac = op.parse("$.name.[<js name=$.name>return \"hello,\"+name;</js>]");
Object res = ac.eval(map);
Assert.assertEquals("hello,world", res);
```
Please note the `addExecutor` method call in the above code. 
This method takes two parameters: the first value is the name of the scripting language, 
and the second is the scripting language executor instance. 

The scripting language name should match the one used in line 10 of the code.
