## Autokrator
# Contents
* [Description](#description)
   * [What it does?](#what-it-does)
   * [Why?](#why)
   * [How?](#how)
* [Usage](#usage)
   * [Grammar](#grammar)
   * [How to use?](#how-to-use) 
* [Structure of project](#structure-of-project)

# Description
## What it does?
Executes code written in custom program language Autokrator (Autokrator or Autocrator is a Greek epithet applied to an individual who is unrestrained by superiors), grammar of which is described below. Which is similar to Python grammar, but not quite.

## Why?
Mostly to demonstrate the possibilities of creating your own programming language.
   
## How?
Using tokenization of source code into set ot tokens (a single element of a programming language), converting tokens into [syntax tree](https://en.wikipedia.org/wiki/Abstract_syntax_tree) and executing it.

# Usage
## Grammar
file: [statements]

statements: statement+

statement: 

    | VARIABLE_NAME '=' expression NEWLINE
    | expression NEWLINE
    | if_statement
    | while statement
    | function_def
    | class_def

func_name: STR

function_def: 'def' func_name'('[VARIABLE_NAME], [VARIABLE_NAME ',']+')' block

expression: 

    | comparison
    | '('comparison')'

comparison:

    | sum '>' sum
    | sum '==' sum
    | sum '<' sum
    | sum

sum: 

    | sum '+' term 
    | sum '-' term 
    | term

term: 

    | term '*' factor 
    | term '/' factor 
    | factor

factor: 

    | '-'factor
    | '+' factor
    | primary

primary: 

    | atom '.' VARIABLE_NAME
    | atom
    | atom '.' function_call
    
atom: 

    | INT 
    | FLOAT
    | VARIABLE_NAME
    | STR
    | LIST
    
function_call: 

    | func_name(expression [',' expression]*)
    | func_name()

block: [NEWLINE INDENT statement]+ NEWLINE DEDENT



while_stmt:

    | 'while' expression block [elif_stmt] 

if_stmt:

    | 'if' expression  block elif_stmt 
    | 'if' expression  block [else_block] 
elif_stmt:

    | 'elif' expression block elif_stmt 
    | 'elif' expression block [else_block] 
else_block:

    | 'else' block

class_def_raw:
    | 'class' NAME block 

    
VARIABLE_NAME: [_a-zA-Z]{1}[_a-zA-Z0-9]+

INT: [0-9]+ 

STR: "[.]+"

FLOAT: [0-9]+.[0-9]+

LIST: 
    | []
    | '[' atom [',' atom]+ ']'



## How to use?
Write program code in txt file. Pass path to file as command line argument and run it.

# Structure of project
Program divided into three packages: Lexing, Parsing, Executing.  And two classes that can be executing: Main.java and Testing.Testing.java
1. Lexing, main class is src/Lexing.Lexer.java - finds tokens inside program text.
2. Parsing, main class is src/Parsing.Parser.java - builds syntax tree using tokens.
3. Executing, main class is src/Executing.Executor.java - executes code after text of program is parsed. Divided into two subpackages:
   3.1 ExecutorExceptions - represents runtime exceptions.
   3.2 ExecutorTokens - represent all units of executions.
   
4. src/Main.java - receives path to file with program, creates Lexing.Lexer, Parsing.Parser and Executing.Executor, guarantees communication between them.
5. src/Testing.Testing.java - to test if program returns desired output.
