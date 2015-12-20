How to build
----
`gradlew distZip` or `gradlew distTar` or `gradlew installApp`, then see `distribution` or `install` dirs in `build/`.
If you just want to run the program and play around, open the project in IntelliJ IDEA and run Main (MainKt class).

How to run
----
The program expects two files in its working directory:

**machine.in**

Contains a Turing machine description in the following format

    start: <name of the start state, word>
    accept: <name of the accepting state, word>
    reject: <name of the rejecting state, word>
    blank: <word that will be used for blank space on the tape>
    <state> <tape symbol> -> <state> <tape symbol> <movement, one of '>', '<', '^'>
    ...
    <state> <tape symbol> -> <state> <tape symbol> <movement, one of '>', '<', '^'>
    
Example:

    start: s
    accept: ac
    reject: rj
    blank: _
    s _ -> ac _ ^
    s 0 -> n _ >
    n 0 -> s _ >
    n _ -> rj _ >
    
**tape.in**

Contains the initial state of the tape, words separated with whitespaces.

Example:

    1 1 * 1 0
