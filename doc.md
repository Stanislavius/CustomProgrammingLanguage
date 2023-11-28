# Summary
## Contents
* [Built-in types](#built-in-types) 
  * [int](#int) 
  * [float](#float)
  * [str](#str)
  * [list](#list)
  * [dict](#dict)
* [Supported operations and methods](#supported-operations-and-methods)
    * [Operations](#operations)
      * [Unary](#unary)
      * [Binary](#binary)
    * [Functions](#functions)

## Built-in types

Built-in types are: int, float, str, list, dict.
Each type in fact is an object and has its class as its member. Every object has its type of type 'type'.
### int
Integer with 4 bytes, equals type int in Java. 
### float
Float with 4 bytes, equals type float in Java.
### str

Equals to String in Java. 
### list
Creation:
list = [item1, item2, items3, ...,last_item]

list = []

Operations:

| operation      |             result              |
|----------------|:-------------------------------:|
| list.append(x) |      Add x to list values       |
| list[i] = x    | Make value with index i to be x |
| list[i]        |  ith item of i, starts with 0   |
| len(list)      |         length of list          |
| str(list)      |  string representation of list  |


### dict


## Supported operations and methods
Variables to which operations are applied are called obj1, obj2, and their classes objects are cls1, cls2.
### Operations
Variables to which operations are applied are called obj1, obj2, and their classes objects are cls1, cls2.
#### Unary
| operation | example |                    respective call                     |
|-----------|:-------:|:------------------------------------------------------:|
| Unary '-' |  -obj1  |                 cls1.\_\_neg\_\_(obj1)                 |
| Unary '+' |  +obj1  | return obj1, will be changed to cls1.\_\_pos\_\_(obj1) |

#### Binary

| operation  |  example   |       respective call        |
|------------|:----------:|:----------------------------:|
| Binary '-' | obj1-obj2  | cls1.\_\_sub\_\_(obj1, obj2) |
| Binary '+' | obj1+obj2  | cls1.\_\_add\_\_(obj1, obj2) |
| '/'        | obj1/obj2  | cls1.\_\_div\_\_(obj1, obj2) |
| '*'        | obj1*obj2  | cls1.\_\_mul\_\_(obj1, obj2) |
| '=='       | obj1==obj2 | cls1.\_\_eq\_\_(obj1, obj2)  |
| '<'        | obj1<obj2  | cls1.\_\_lt\_\_(obj1, obj2)  |
| '>'        | obj1/obj2  | cls1.\_\_gt\_\_(obj1, obj2)  |

Comparison by  '==' be changed later to comparison based on types, references and values.

### Functions
1. len. len(obj1) will call cls1.\_\_len\_\_(obj1).
2. abs. abs(obj1) will call cls1.\_\_abs\_\_(obj1).
3. str. str(obj1) will call cls1.\_\_str\_\_(obj1).