grammar ObjectPath;
objectPath: typeCast? ('('expr (pipe)*')'|expr (pipe)*);

/* 扩展jsonpath,在未显式提供$/@前导符号时,默认为@,表示当前对象本身 */
/* 新增计算方法后,表达式,就变成了两部分,计算表达式和标准表达式 */
expr:
typeCast ('('expr')'|expr)                           #TYPE_CAST_EXPR
|value             #VALUE_EXPR
|(location  selector*)                  #PATH_EXPR
| expr ('+'|'-'|'*'|'/'|'%') expr       #CALCULATE_EXPR
|'(' expr ')'                           #GROUP_EXPR
|expr ('?'expr?) ':' expr                #DEFAULT_EXPR
| expr '==' expr #EQ
| expr '!='expr           #NEQ
| expr '<' expr           #LT
| expr '<=' expr          #LE
| expr '>' expr           #GT
| expr '>=' expr          #GE
| expr 'is' 'null'                 #ISNULL
| REG '~=' expr                          #LREGEX    //正则表达式,~的位置表示正则表达式的位置,此处左侧为正则表达式
| expr '=~'REG                          #RREGEX   //正则表达式,=~的位置表示正则表达式的位置,此处右侧为表达式
| expr 'between' '('  (expr|value)  ','  (expr|value)  ')'  #BETWEEN
| expr 'like'  (expr|value)                        #LIKE
| expr 'in' '(' args ')'                    #IN
| expr 'notIn' '(' args')'                  #NOTIN
| expr 'contains' '(' args ')'              #CONTAINS
| expr 'startWith' '(' arg ')'             #STARTSWITH
| expr 'endWith' '(' arg ')'               #ENDSWITH
| expr 'match' '(' arg ')'                  #MATCHES
| expr ('and'|'&&') expr                    #AND
| expr ('or'|'||') expr                     #OR
|'!'expr #REVERT_EXPR
;

location: '$'                                       #ROOT_NODE
| '@'                                               #CURRENT_NODE
|'@@'                                               #PARENT_NODE
;
selector:
func                               #METHOD
|(ID |STRING  )                                       #CHILD
|'.' (ID |STRING  )                                  #CHILD
| '..' ID                                            #RECURSIVE_CHILD
| '[' (ID |STRING  )  ']'                            #INDEX_OR_NAME
| '[' (ID |STRING  )  (','(ID |STRING  ) )+ ']'      #INDEX_OR_NAME_LIST   // 逗号分隔的多个id标志,该操作会从当前节点中提取对应的属性,并将其转换为map对象
| '[' expr ']'                    #INDEX
| '[' '*'? ']'                      #WILDCARD_ALL
| '[' expr ':' expr ']'         #SLICE
| '[' expr (',' expr )* ']'     #SLICE_PICK
|filterExpr                         #FILTER
|'.' scripts                        #SCRIPT
|'[^'expr?']'                     #UP
|'.*'                               #ALL

;

/* 管道表达式 */
pipe: '|'expr ;
typeCast: '('
          (
          'string'|'String'|'str'
          |'Integer'|'int'|'i'|'I'
          |'Double'|'double'|'d'|'D'
          |'Boolean'|'b'|'boolean'|'bool'|'B'
          |'float'|'Float'|'f'
          |'long'|'Long'|'l'|'L'
          |'short'|'Short'|'s'|'S'
          |'byte'|'Byte'
          |'char'|'Character'|'c'|'C'
          )('['']')?')';

/* 过滤表达式 */
filterExpr: '[' '?' '('( expr) ')' ']';

func:
'.' 'min' '('  (expr|)  ')'                     #MIN
|'.' 'max' '(' (expr|)  ')'                      #MAX
|'.' 'sum' '(' (expr|) ')'                     #SUM
|'.' 'avg' '(' (expr|) ')'                     #AVG
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
|'.' 'split' '(' expr ')'                    #SPLIT
|'.' 'replace' '(' expr ',' expr ')'                  #REPLACE
|'.' ('substring'|'sub') '(' (expr (','expr)?)? ')'    #SUBSTRING
|'.' 'toUpper' '(' ')'                       #TOUPPER
|'.' 'toLower' '(' ')'                       #TOLOWER
|'.' 'trim' '(' ')'                          #TRIM
|'.' 'ltrim' '(' ')'                         #LTRIM
|'.' 'rtrim' '(' ')'                         #RTRIM
|'.' 'concat' '(' expr ')'                   #CONCAT
|'.' 'first' '(' ')'                         #FIRST
|'.' 'last' '(' ')'                          #LAST
|'.' 'indexOf' '(' expr ')'                #INDEXOF
|'.' 'map' '(' expr ')'                      #MAP
|'.' 'isNull' '(' expr? ')'                   #IS_NULL_METHOD
|'.' 'notNull' '(' expr? ')'                  #NOT_NULL_METHOD
|'.' 'and' '(' expr (',' expr)* ')'   #AND_METHOD
|'.' 'or' '(' expr (',' expr)* ')'    #OR_METHOD
|'.' 'not' '(' expr? ')'                      #NOT_METHOD
|'.' 'filter' '(' expr (',' expr)* ')'       #FILTER_METHOD
|'.' ID '(' (args|) ')'                   #METHOD_CALL
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
value:STRING|NUMBER|DOUBLE|BOOL|ID;
json: object|array;
object: '{' pair (',' pair)* '}';
array: '[' expr (',' expr)* ']';
pair: STRING ':' expr;

SCRIPT_CONTENT: '>' (~[<])* '</';
BOOL: 'true' | 'false'|'TRUE'|'FALSE';
NUMBER: DIGIT+ ('l'|'L')?;
DOUBLE: (NUMBER ( '.' NUMBER)('d'|'D')?|NUMBER ('d'|'D')?) ;
CACLUATE: '+'|'-'|'*'|'/'|'%';
STRING: '"' .*? '"'
| '\'' .*? '\''
;
ID: ([a-zA-Z_][a-zA-Z_0-9]*)|STRING;

INT: '0' | [1-9] [0-9]*;
DIGIT: [0-9];
fragment
OPTINAL: '?';
REG: ('/' (~[/])* '/'('i')?('g')?);
ESC: '\\' (["\\/bfnrt] | UNICODE);
UNICODE: 'u' HEX HEX HEX HEX;
HEX: [0-9a-fA-F];
WS: (' ' | '\t' | '\r' | '\n')+ -> skip;
