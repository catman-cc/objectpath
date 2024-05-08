
# ObjectPath
> ObjectPath是一个用于访问java对象的路径解析器,类似于jsonpath,但是ObjectPath用于访问java对象,而不是json对象.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=catman-cc_objectpath&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=catman-cc_objectpath)

---
## 快速开始
### 1. 引入依赖
```xml
<dependency>
    <groupId>cc.catman</groupId>
    <artifactId>objectpath</artifactId>
    <version>0.0.1</version>
</dependency>
```

## 1. 语法规则
### 1.1. 语法规则
objectpath的语法是jsonpath的超集,所以jsonpath的语法规则在objectpath中同样适用,同时objectpath还支持一些额外的语法规则.
#### 1.1.1. 基础语法
为了更好的展示objectpath的语法规则,下面的示例中使用的java对象信息如下:
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


| 语法 | 说明             |
|----|----------------|
| $  | 根节点            |
| @  | 当前节点           |
| @@ | 父节点            |
|ID| 当前节点名为ID的子元素   |
|.ID| 当前节点名为ID的子元素   |
|..ID| 递归获取所有名为ID的子元素 |
|[ID]| 当前节点名为ID的子元素   |
|[NUMBER]|数组指定下标的元素|
|[*]|数组中的所有元素|
|[]|数组中的所有元素|
|[?(表达式)]|过滤当前数组中的元素|

#### 1.1.2 内置简单函数支持
|函数名称|参数列表|说明|
|-------|------|----|
|isNull| -|是否为null|
|between|Number,Number|是否在两个数值之间|
| like|-|参考sql的like,使用%%作为前后模式匹配|
|in|expre|是否在集合中|
|notIn|expr|不在指定集合中|
|contains|expr|是否包含指定元素/文本|
|startWith|expr|是否以指定文本开头|
|endWith|expr|是否以指定文本结尾|
|matchs|expr|正则表达式匹配|

#### 1.1.3 内置高阶函数支持
|函数名称| 参数列表 |说明|
|-------|------|----|
|min| expr |获取数组中的最小值|
|max| expr |获取数组中的最大值|
|sum| expr |获取数组中的和|
|avg| expr |获取数组中的平均值|
|size| -    |获取数组的大小|
|isEmpty| -    |判断数组是否为空|
|length| -    |获取字符串的长度|
|keys| -    |获取对象的所有key|
|values| -    |获取对象的所有value|
|reverse| -    |数组反转|
|sort| expr |数组排序|
|groupBy| expr |数组分组|
|substring| start,end |截取字符串|
|replace| old,new |替换字符串|
|trim| - |去除字符串两端空格|
|ltrim| - |去除字符串左端空格|
|rtrim| - |去除字符串右端空格|
|toUpper| - |转换为大写|
|toLower| - |转换为小写|
|concat| expr |字符串拼接|
|first| - |获取数组的第一个元素|
|last| - |获取数组的最后一个元素|

#### 1.1.4. 内置脚本支持
除了内置的简单函数和高阶函数之外,objectpath还支持内置脚本的方式,脚本的语法和java的lambda表达式类似,但是objectpath的脚本是一个字符串,而不是一个java对象.
示例脚本:
```xml
<js name="a" age="18">
    return {
        name: a,
        age: age
    };
</js>
```
脚本中的js标签用于表示这是一个javaScript脚本,name和age是脚本的参数,js标签中的内容是脚本的主体,示例脚本的返回值是一个json对象.
其类似于js代码:
```javascript
    function x(name,age){
        return {
            name: a,
            age: age
        };
    x("a",18);
}
```
脚本的参数支持objectpath的语法,所以可以使用objectpath的语法来获取参数的值,示例脚本:
```xml
<js name="$.name" age="$.age">
    return {
        name: name,
        age: age
    };
</js>
```
### 管道操作符
objectpath支持管道操作符,管道操作符用于将前一个操作的结果作为后一个操作的输入,示例:
```
$.store.book[?(@.price > 10)] | $.title
```
上面的示例中,首先获取了所有价格大于10的书籍,然后获取这些书籍的title属性,最终返回的是一个title的数组.

除此之外,针对管道的使用场景,还定义了一个特殊的操作符,用于将数组展开,示例:
```
$.store.book[?(@.price > 10)] | $.title | [^]
```
上述结尾的[^]操作符用于将数组展开,将数组中的所有元素展开为一个一维数组.
同时[^]操作符还支持参数,用于指定展开的深度,示例:
```
$.store.book[?(@.price > 10)] | $.title | [^2]
```
上述示例中,展开的深度为2,表示展开两层.

### 开发指南
#### 自定义函数
objectpath支持自定义函数,在语法解析器层面提供了自定义函数的支持,自定义函数的语法如下:
```xml
<fn name="函数名" args="参数列表">
    函数体
</fn>
```



