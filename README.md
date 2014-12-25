# Stag

A simple language which compiles to Java bytecode.

## Syntax

```stag
state State {
    int value;

    transition ToState {
        // transition rule logic
    }
}
```

```stag
machima Machine initial State {
    int variable;

    someMethod(int param) {
        variable = state.value;
        out("State value %d\n", variable);
        // ...
        transition to(NewState);
    }
}
```

