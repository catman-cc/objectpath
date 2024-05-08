grammar ObjectPathEnhance;
objectPath: expr (pipe)*;
/* 扩展jsonpath,在未显式提供$/@前导符号时,默认为@,表示当前对象本身 */
expr: (location|ID)? selector* ;
location: '$'                                      #ROOT_NODE
| '@'                                               #CURRENT_NODE
|'@@'                                               #PARENT_NODE
;
selector:
ID                                  #CHILD
|'.' ID                             #CHILD
| '..' ID                           #RECURSIVE_CHILD
| '[' ID ']'                        #INDEX_OR_NAME
| '[' NUMBER ']'                    #INDEX
| '[' '*'? ']'                      #WILDCARD_ALL
| '[' NUMBER ':' NUMBER ']'         #SLICE
|filterExpr                         #FILTER
|func                               #METHOD
|'.' scripts                        #SCRIPT
|'[^'NUMBER?']'                     #UP
;

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
| expr 'like' STRING                        #LIKE
| expr 'in' '(' args ')'                    #IN
| expr 'notIn' '(' args')'                  #NOTIN
| expr 'contains' '(' args ')'              #CONTAINS
| expr 'startWith' '(' args ')'             #STARTSWITH
| expr 'endWith' '(' args ')'               #ENDSWITH
| expr 'matches' '(' STRING ')'             #MATCHES
| expr ('and'|'&&') expr                    #AND
| expr ('or'|'||') expr                     #OR
;

reverseComparExpr: '!' comparExpr;

func: '.' ID '(' (namedArgs|) ')'                   #METHOD_CALL
|'.' 'min' '(' (expr|) ')'                     #MIN
|'.' 'max' '('(expr|) ')'                      #MAX
|'.' 'sum' '(' (expr|) ')'                     #SUM
|'.' 'avg' '(' (expr|) ')'                     #AVG
|'.' 'size' '(' ')'                          #SIZE
|'.' 'isEmpty' '(' ')'                       #ISEMPTY
|'.' 'index' '(' ')'                         #METHOD_INDEX
|'.' 'length' '(' ')'                        #LENGTH
|'.' 'keys' '(' ')'                          #KEYS
|'.' 'values' '(' ')'                        #VALUES
|'.' 'reverse' '(' ')'                       #REVERSE
|'.' 'sort' '(' args ')'                     #SORT
|'.' 'groupBy' '(' (args|scripts) ')'        #GROUPBY
|'.' 'join' '(' args ')'                     #JOIN
|'.' 'split' '(' args ')'                    #SPLIT
|'.' 'replace' '(' args ',' args ')'                  #REPLACE
|'.' 'substring' '(' NUMBER ','NUMBER ')'    #SUBSTRING
|'.' 'toUpper' '(' ')'                       #TOUPPER
|'.' 'toLower' '(' ')'                       #TOLOWER
|'.' 'trim' '(' ')'                          #TRIM
|'.' 'ltrim' '(' ')'                         #LTRIM
|'.' 'rtrim' '(' ')'                         #RTRIM
|'.' 'concat' '(' args ')'                   #CONCAT
|'.' 'first' '(' ')'                         #FIRST
|'.' 'last' '(' ')'                          #LAST
|'.' 'indexOf' '(' NUMBER ')'                #INDEXOF
;

/* 扩展脚本支持,其中ID表示脚本语言 */
scripts: scripts_start SCRIPT_CONTENT  scripts_end;
scripts_start: '[<' ID  namedArgs? ;
scripts_end: ID '>]';

//脚本可以包含任意字符,包括换行符,所以使用非贪婪匹配,直到遇到'}'为止,表示脚本结束,所以此处应该修改.*?为
namedArgs: namedArg ( namedArg)*;
namedArg: ID '=' arg;
args: arg (',' arg)*;
arg: expr|value|complexValue;
complexValue: value|json;
value:STRING|NUMBER|DOUBLE|ID;
json: object|array;
object: '{' pair (',' pair)* '}';
array: '[' value (',' value)* ']';
pair: STRING ':' complexValue;

SCRIPT_CONTENT: '>' (~[<])* '</';
ID: [a-zA-Z_][a-zA-Z_0-9]*|STRING;
DOUBLE: [0-9]+ '.' [0-9]*;
NUMBER: DIGIT+;
STRING: '"' (ESC | ~["\\])* '"'
| '\'' (ESC | ~["\\])* '\''
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
