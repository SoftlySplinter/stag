# Stag

A simple language which compiles to Java bytecode.

A personal project to explore how Java bytecode works and a foray into lower level programming, as well as compiler theory.

## Syntax

The syntax of Stag is Java-like, but stripped down with several macros to speed up code.

1. Classes are inferred from the filename
2. Visibility defaults to public. The Java default is specified through the keyword `package`
3. Method selection based on a `when` clause in the method definition
4. `=` signifies equality, `==` signifies shallow equality (`.equals(Object)`). `:` signifies assignment.

## Examples

### Hello World

```stag
void main(String[] args) {
    out("Hello World!\n");
}
```

### Fibbonachi

```stag
void main(String[] args) {
    int f: args[0];
    out("fib(%d): %d", f, fib(f));
}

int fib(int f) when f > 1 {
    return fib(f - 1) + fib(f - 2);
}

int fib(int f) when f = 0 or f = 1 {
    return f;
}
```
