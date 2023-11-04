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

function_call: func_name(statement)

func_name: str

statement: |sum 
           |(sum)

sum: | sum '+' term 
     | sum '-' term 
     | term

term: | term '*' factor 
      | term '/' factor 
      | factor

factor: | INT 
        | -factor

INT : [0-9]+ 
STR : [a-zA-Z]+

## How to use?
Write program code in txt file. Pass path to file as command line argument and run it.

# Structure of project
1. src/Lexer.java - finds tokens inside program text.
2. src/Parser.java - builds syntax tree using tokens.
3. src/Executor.java - executes code after text of program is parsed.
4. src/Main.java - receives path to file with program, creates Lexer, Parser and Executor, guarantees communication between them.
5. src/Testing.java - to test if program returns desired output.
