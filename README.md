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
Executes code written in program language Autokrator (Autokrator or Autocrator is a Greek epithet applied to an individual who is unrestrained by superiors), grammar of which is described below.

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
    | if_statement NEWLINE
    | while statement NEWLINE
    | function_def NEWLINE

func_name: STR

function_def: 'def' func_name'('[VARIABLE_NAME], [VARIABLE_NAME ',']+')' block

expression: 

    | comparison
    | '(comparison)'

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

    | INT 
    | '-'factor
    | FLOAT
    | VARIABLE_NAME
    | '+' factor
    | function_call

function_call: 

    | func_name(expression [',' expression]*)
    | func_name()

block: [NEWLINE INDENT statement]+



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

    
VARIABLE_NAME: STR

INT: [0-9]+ 

STR: [a-zA-Z]+

FLOAT: [0-9]+.[0-9]+

## How to use?
Write program code in txt file. Pass path to file as command line argument and run it.

# Structure of project
Program divided into three packages: Lexing, Parsing, Executing.  And two classes that can be executing: Main.java and Testing.java
1. Lexing, main class is src/Lexing.Lexer.java - finds tokens inside program text.
2. Parsing, main class is src/Parsing.Parser.java - builds syntax tree using tokens.
3. Executing, main class is src/Executing.Executor.java - executes code after text of program is parsed. Divided into two subpackages:
   3.1 ExecutorExceptions - represents runtime exceptions.
   3.2 ExecutorTokens - represent all units of executions.
   
4. src/Main.java - receives path to file with program, creates Lexing.Lexer, Parsing.Parser and Executing.Executor, guarantees communication between them.
5. src/Testing.java - to test if program returns desired output.
