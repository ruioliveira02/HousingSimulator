# Housing Simulator
Project for Object-Oriented Programming

Assessed as a 20 / 20.

## Overview

The goal of this project was to develop an application in Java which would allow a user to simulate the energy consumption of multiple houses with different energy suppliers.

The main features were

- Implementation of the simulation and its entities (suppliers, houses and devices)
- Ability to load simulation data from file (using Java's ```Serializable``` interface)
- Ability to automate the simulation using scripts

All of these features were implemented in our project.

## Team

This project was developed by

- [Rui Oliveira](https://github.com/ruioliveira02)
- [Luís Pereira](https://github.com/lumafepe)
- [Tiago Bacelar](https://github.com/tiago-bacelar)

## Notable Features

Here are some of the more notable features of this project which set it apart from others.

### Formula processing

Each energy supplier has a price formula to calculate the cost of electricity for its customers. It was intended to be done using inheritance, meaning there would be a fixed, programmer defined number of different suppliers to choose from.

In order to improve from this, we allowed the formula to be input as a string. This string would then be processed by the [EvalEx](https://github.com/uklimaschewski/EvalEx) library which would output the result.

This allows for as many suppliers as the end user desires, and results in the ability to process far more complex formulas than required.

### Annotations

The user interface is a CLI, in which the user enters a command and the application parses it. In other to minimize the boilerplate code needed to do the command parsing, an annotation processor was developed.

In each method corresponding to a command the user could input, an annotation with the regex syntax of the command would be put on top of it. The processor would then add it to the method responsible for command parsing, and would automatically generate the method call code.

This would allow for more advances features like scripting to be implemented trivially later on.

### Event-based simulation

We implemented the simulation as a series of events, in a completely separate package with no knowledge of anything else in the application. This makes this code reusable for other scenarios, as well as allowing for more advanced features, like moving back in time and creating parallel branches in the simulation easier.

## Running

This project uses maven to control the build flow and dependencies. Make sure Maven and Java (17+) are installed in your system. 

To run the annotation processor run

```
cd Annotations
mvn clean install
cd ..
```

To compile the project run

```mvn clean install```

And to run it

```mvn exec:java −Dexec . mainClass=”com.housingsimulator.Main”```

## Future improvements

There are a lot of ways this project could be improved, mainly, and to name but a few:

- A graphical user interface could have been developed using Java Swing. We attempted to do it, however, time constraints meant we could not have it ready with the quality needed to meet our high standards, meaning we ended up not submitting it in the final build

- The code for the annotation processor is not particularly clean or adaptable. A code generation library should have been used and the whole processor refactored

- The unit tests should have been more and better. However, time again meant we could not do them in time and, as it was not a requirement, we ended up not maintaining them properly (some may be out of date and failing as a result)

- Allow the simulation to have different branches, meaning one could more easily compare the consumption of different house configurations, suppliers, .etc

## Thoughts and Conclusion

Time was the main constraint with this project. I feel that, if given just one extra week to work on it, we could have delivered an even better application.

Despite not being our favourite project to work in (mainly because it was not very technically challenging), we believe we delivered a really solid piece of software, which could still have been improved further.