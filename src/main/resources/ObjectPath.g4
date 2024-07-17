grammar ObjectPath;
objectPath: expr (pipe)*;
/* 扩展jsonpath,在未显式提供$/@前导符号时,默认为@,表示当前对象本身 */
/* 新增计算方法后,表达式,就变成了两部分,计算表达式和标准表达式 */
expr:
(location|ID|selector) selector*        #PATH_EXPR
|(DOUBLE|NUMBER|STRING|BOOL)            #VALUE_EXPR
| expr ('+'|'-'|'*'|'/'|'%') expr       #CALCULATE_EXPR
|'(' expr ')'                           #GROUP_EXPR
|expr ('?'|filterExpr) ':' expr         #DEFAULT_EXPR
;

location: '$'                                       #ROOT_NODE
| '@'                                               #CURRENT_NODE
|'@@'                                               #PARENT_NODE
;
selector:
ID                                  #CHILD
|'.' ID                             #CHILD
| '..' ID                           #RECURSIVE_CHILD
| '[' ID ']'                        #INDEX_OR_NAME
| '[' ID (','ID)+ ']'               #INDEX_OR_NAME_LIST   // 逗号分隔的多个id标志,该操作会从当前节点中提取对应的属性,并将其转换为map对象
| '[' NUMBER ']'                    #INDEX
| '[' '*'? ']'                      #WILDCARD_ALL
| '[' NUMBER ':' NUMBER ']'         #SLICE
| '[' NUMBER (',' NUMBER )* ']'     #SLICE_PICK
|filterExpr                         #FILTER
|func                               #METHOD
|'.' scripts                        #SCRIPT
|'[^'NUMBER?']'                     #UP
|'.*'                               #ALL
;

/* 管道表达式 */
pipe: '|'expr ;

/* 过滤表达式 */
filterExpr: '[' '?' '('( comparExpr|reverseComparExpr|scripts) ')' ']';

/* 比较表达式 */
comparExpr: (expr|value) '=='  (expr|value) #EQ
|  (expr|value) '!=' (expr|value)           #NEQ
|  (expr|value) '<'  (expr|value)           #LT
|  (expr|value) '<='  (expr|value)          #LE
|  (expr|value) '>'  (expr|value)           #GT
|  (expr|value) '>='  (expr|value)          #GE
|  (expr|value) 'is' 'null'                 #ISNULL
| REG '~=' expr                          #LREGEX    //正则表达式,~的位置表示正则表达式的位置,此处左侧为正则表达式
| expr '=~'REG                          #RREGEX   //正则表达式,=~的位置表示正则表达式的位置,此处右侧为表达式
| expr 'between' '(' NUMBER ',' NUMBER ')'  #BETWEEN
| expr 'like' ID                        #LIKE
| expr 'in' '(' args ')'                    #IN
| expr 'notIn' '(' args')'                  #NOTIN
| expr 'contains' '(' args ')'              #CONTAINS
| expr 'startWith' '(' args ')'             #STARTSWITH
| expr 'endWith' '(' args ')'               #ENDSWITH
| expr 'matches' '(' ID ')'             #MATCHES
| expr ('and'|'&&') expr                    #AND
| expr ('or'|'||') expr                     #OR
;

reverseComparExpr: '!' comparExpr;



func: '.' ID '(' (args|) ')'                   #METHOD_CALL
|'.' 'min' '(' (expr|) (',' (DOUBLE|NUMBER))? ')'                     #MIN
|'.' 'max' '('(expr|)(',' (DOUBLE|NUMBER))?')'                      #MAX
|'.' 'sum' '(' (expr|)(',' (DOUBLE|NUMBER))? ')'                     #SUM
|'.' 'avg' '(' (expr|)(',' (DOUBLE|NUMBER))?')'                     #AVG
|'.' ('size'|'count') '(' ')'                          #SIZE
|'.' 'isEmpty' '(' ')'                       #ISEMPTY
|'.' 'index' '(' ')'                         #METHOD_INDEX
|'.' 'length' '(' ')'                        #LENGTH
|'.' 'keys' '(' ')'                          #KEYS
|'.' 'values' '(' ')'                        #VALUES
|'.' 'reverse' '(' ')'                       #REVERSE
|'.' ('distinct'|'dis') '(' (expr|)')'         #DISTINCT
|'.' 'sort' '(' expr (',' expr)* ')'          #SORT
|'.' 'rsort' '(' expr (',' expr)* ')'         #RSORT
|'.' 'groupBy' '(' expr ')'        #GROUPBY
|'.' 'join' '(' args ')'                     #JOIN
|'.' 'split' '(' ID ')'                    #SPLIT
|'.' 'replace' '(' ID ',' ID ')'                  #REPLACE
|'.' ('substring'|'sub') '(' NUMBER? ','NUMBER? ')'    #SUBSTRING
|'.' 'toUpper' '(' ')'                       #TOUPPER
|'.' 'toLower' '(' ')'                       #TOLOWER
|'.' 'trim' '(' ')'                          #TRIM
|'.' 'ltrim' '(' ')'                         #LTRIM
|'.' 'rtrim' '(' ')'                         #RTRIM
|'.' 'concat' '(' ID ')'                   #CONCAT
|'.' 'first' '(' ')'                         #FIRST
|'.' 'last' '(' ')'                          #LAST
|'.' 'indexOf' '(' NUMBER ')'                #INDEXOF
|'.' 'map' '(' expr ')'                      #MAP
;

/* 扩展脚本支持,其中ID表示脚本语言 */
scripts: scripts_start SCRIPT_CONTENT  scripts_end;
scripts_start: '[<' ID  namedArgs? ;
scripts_end: ID '>]';

//脚本可以包含任意字符,包括换行符,所以使用非贪婪匹配,直到遇到'}'为止,表示脚本结束,所以此处应该修改.*?为
namedArgs: namedArg ( namedArg)*;
namedArg: ID '=' arg;
args: arg (',' arg)*;
arg:  value|complexValue| expr;
complexValue: value|json;
calcValue: expr|DOUBLE|NUMBER;
value:STRING|NUMBER|DOUBLE|BOOL|ID;
json: object|array;
object: '{' pair (',' pair)* '}';
array: '[' value (',' value)* ']';
pair: STRING ':' complexValue;

SCRIPT_CONTENT: '>' (~[<])* '</';
BOOL: 'true' | 'false'|'TRUE'|'FALSE';
NUMBER: DIGIT+ ('l'|'L')?;
DOUBLE: (NUMBER ( '.' NUMBER)('d'|'D')?|NUMBER ('d'|'D')?) ;
CACLUATE: '+'|'-'|'*'|'/'|'%';
ID: [a-zA-Z_][a-zA-Z_0-9]*|STRING;
STRING: '"' (.*?) '"'
| '\'' .*? '\''
;
INT: '0' | [1-9] [0-9]*;
DIGIT: [0-9];
fragment
OPTINAL: '?';
REG: ('/' (~[/])* '/'('i')?('g')?);
ESC: '\\' (["\\/bfnrt] | UNICODE);
UNICODE: 'u' HEX HEX HEX HEX;
HEX: [0-9a-fA-F];
WS: (' ' | '\t' | '\r' | '\n')+ -> skip;
