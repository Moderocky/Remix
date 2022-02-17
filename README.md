RMX
=====

### Opus #16

A lightweight JVM language designed for frameworks and small-scale serverside applications.

## Description

RMX (Remix) is a JVM language, but is designed to have minimal interaction with other JVM languages or resources.
It is designed for writing command-line or background programs that handle data. As a JVM language, Remix programs can
be deployed on any Java-supporting machine or operating system and can be run through Java's packaging tools.

## Core Features

Below are details for some of the language's most important features.
This is not an exhaustive list.

### 1. Safety

Unlike in other languages, errors are not failure conditions for a Remix program.
The program is designed to continue past an undetected error where possible, although programs can opt to halt at
critical error points.

Remix does not feature a `null` empty value.
All Remix objects act like Java primitives and can be allocated in a memory-zero 'default' state. This prevents
null-pointer exceptions from ever occurring and allows programs to continue even in an unexpected error state.

```rmx
house house {}
system.Print {
    house.GetName
}
```

Remix has no global state.
There is no equivalent for the `static` modifier in Remix. All runnable code exists in objects, which removes a common
cause for memory leaks and allows more adaptable design.

### 2. Fluidity

The RMX code-style is fluid, giving developers the freedom to adopt any structure or style.
Inside an instruction area (e.g. a function body) all statements map directly to bytecode operations.

This allows advanced developers to write more efficient code that would be unachievable in other JVM languages.

```rmx
system dup.Print { "hello" }.Print {"there" }
exit {
    number x { 100 }
    x + 10
}

if var = true {
}
var if = true {
}
var = true if {
}
```

Please note this is not available for all branches: the `while` branch uses the keyword as a head marker.

### 3. Configuration

All RMX operators are backed by special operator functions (using the `oper` keyword.)
These operators can be overwritten with type-specific versions.
Binary operators can have multiple overloaded versions for specific arguments.

| Symbol   | Function               | Description                            |
|----------|------------------------|----------------------------------------|
| `+`      | `Add object object`    | Defines the addition behaviour.        |
| `-`      | `Sub object object`    | Defines the subtraction behaviour.     |
| `*`      | `Mul object object`    | Defines the multiplication behaviour.  |
| `/`      | `Div object object`    | Defines the division behaviour.        |
| `=`      | `Equals object object` | Defines the equality relation.         |
| `<<`     | `Push object object`   | Defines the push (l-shift) relation.   |
| `>>`     | `Pull object object`   | Defines the pull (r-shift) relation.   |
| `&`      | `And object object`    | Defines the and relation.              |
| `&#124;` | `Or object object`     | Defines the or relation.               |
| `?`      | `Bool`                 | Defines the zero-value of this object. |

### 4. Storage

RMX programs are designed to be easy to store and load. All objects can be converted to a binary format for easy saving
and resuming of a program state.
This is useful for applications that need to recover data and resume their progress once restarted.

### 5. Power

Developers have access to low-level tools that are inaccessible in other JVM languages.

These are designed to allow advanced developers to write more efficient code by reducing the need for variable
assignments, repeat evaluation, etc.

At the compiler stage:

| Operation | Description                                                                    | Example         |
|-----------|--------------------------------------------------------------------------------|-----------------|
| Duplicate | Copy the previous stack value. (Useful for duplicating arguments.)             | `dup`           |
| Pop       | Discard the previous stack value. (Useful for discarding unwanted results.)    | `pop`           |
| Swap      | Swap the order of the previous two values. (Useful for re-ordering arguments.) | `swap`          |

At the virtual machine stage:

| Operation  | Description                                                          | Example          |
|------------|----------------------------------------------------------------------|------------------|
| Allocation | Directly allocate zero-instance objects, skipping their constructor. | `alloc integer`  |
| Cloning    | Create direct memory clones of any object.                           | `object.Clone`   |
| Pointers   | Access/retrieve native memory pointers of an object.                 | `object.Pointer` |
| Freezing   | Mark an object as frozen, preventing it being altered.               | `object.Freeze`  |

## Keywords

A list of control keywords available in the language.
RMX keywords are not reserved words, but will always take preference over other usages in valid situations.
For example, the first `house` usage in `this house house { }` would be identified as a type (for a variable assignment)
but if it were replaced with `cast` (see `this cast house { }`) then the `cast` keyword would take priority.

| Keyword | Description                                              |
|---------|----------------------------------------------------------|
| `type`  | Declare a new object-type.                               |
| `is`    | Declare or check for type inheritance.                   |
| `trans` | Declare a field as being transient.                      |
| `func`  | Begin a function.                                        |
| `oper`  | Begin an operator.                                       |
| `if`    | Run a branch conditionally.                              |
| `while` | Run a repeating branch conditionally.                    |
| `break` | Jump to the end of the current branch.                   |
| `new`   | Create a new object. Can be overloaded like an operator. |
| `alloc` | Create a zero-instance of an object.                     |
| `cast`  | Convert an object to another (compatible) type.          |
| `dup`   | Duplicate an object reference.                           |
| `pop`   | Discard an object reference.                             |
| `swap`  | Swap the most recent two object references.              |
| `exit`  | Exit the current function.                               |
