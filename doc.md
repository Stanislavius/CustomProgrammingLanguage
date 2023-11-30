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
* [Supported constructions and key features](#supported-operations-and-methods)
    * [Function definition](#function-definition)
    * [Scopes](#scopes)
    * [Class definition](#class-definition)
    * [Comparison](#comparison)
    * [Conditional statements and loops](#conditional-statements-and-loops)

## Built-in types

Built-in types are: int, float, str, list, dict.
Each type in fact is an object and has its class as its member. Every object has its type of type 'type'.
### int
Integer with 4 bytes, equals type int in Java. 
Allow summing, subtracting, multiplying, division it by another int or float.
Cannot be changed. Each int is a different object.
### float
Float with 4 bytes, equals type float in Java.
Allow summing, subtracting, multiplying, division it by another int or float.
Cannot be changed. Each float is a different object.
### str
Equals to String in Java. 
Allow summing it with another str or value which has `__str__` method defined.
Cannot be changed. Each str is a different object.
### list
List of heterogeneous values. Can be changed.
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
Dictionary (key, value) of of heterogeneous values. Can be changed.

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

## Supported constructions and key features

Body of all constructions like function definition, class definition, conditional and loops statements starts and ends after indent and dedent.
### Function definition
    def example_name(arg1, arg2, ... ,last_arg) <br>
        some code

or 

    def example_name() <br>
        some code

This will lead to creation of a variable with name 'example_name'.
It is possible to define functions inside of the functions:

    def example_name(arg1, arg2, ... ,last_arg)
        def example_name2(arg1, arg2..., last_arg)
            some code

This function will be available only inside of the function example_name.
But can be returned:

    def example_name(arg1, arg2, ... ,last_arg)
        def example_name2(arg1, arg2..., last_arg)
            some code
        example_name2

Program returns the result of the last execution:
    
    5 #returns 5

And so as function: 
    
    def func()
        5
    func() #program will return 5


### Scopes
From lesser scope you can have access to all greater scopes variables, but only for reading.

    a = 12
    def example_name()
        #a can be read hear
        a = 4 # but it will create local variable a
        def example_name2(arg1, arg2..., last_arg)
            a = a + 1 #read a from greater scope a = 4, create local a = 4 + 1 = 5
        a #return 4
    example_name()
    a #return 12

You can have infinitive number of scopes for that matter.

### Class definition

    class example_name
        def __init__(self, a)
            self.a = a

Class is a variable, so after execution of class definition new variable named example_name is created.

To create object based on class you need to call variable with class, after creation of new object it will call `__init__` method and pass arguments from calling class to it:

    class example_name
        def __init__(self, a)
            self.a = a
    obj = example_name(5)
    obj.a #returns 5

You can define [magic methods](#operations)  to define objects behaviour in some situations:

For example:

    class example_name
        def __init__(self, a)
            self.a = a
        def __add__(self, other)
            example_name(self.a+other.a) #will return new object
    obj1 = example_name(5)
    obj2 = example_name(6)
    (obj1 + obj2).a # will call __add__ method and return a = 11


### Comparison
There are [3 comparison operations](#binary) for now: "==", '<', '>'. Result of the comparison
Result of the comparison is of type int. 0 - false. Anything else is true.

### Conditional statements and loops

If-clause will execute if condition is not zero. It can be followed by elif and\or else.

Example:

    a = 2
    if a > 5
        if a > 6
            0
        elif a == 6
            1
        else
            2

While-loops work the same way:

    a = 2
    while a < 10
        a = a + 1
    elif a == 10
        a = 15
    else
        a = a + 5

Elif part will execute if condition in while is not true and condition in elif is true.
Else will always execute.