# Deterministic Finite Automaton

## Overview
This project provides a robust and flexible library for working with Deterministic Finite Automaton in Java. A DFA is a mathematical model used to recognize and validate strings based on a predefined set of states and transition rules.

## Features
- Create custom DFAs with configurable states and transitions
- Validate input strings against a DFA
- Visualize DFA structure and transitions
- Support for different acceptance criteria (e.g., accepting on final state, rejecting on final state)
- Comprehensive error handling and input validation

## Installation
1. Clone the repository:
   ```
   git clone https://github.com/ErnestGeorgeMaxim/DeterministicFiniteAutomaton.git
   ```
2. Navigate to the project directory:
   ```
   cd DeterministicFiniteAutomaton
   ```
3. Build the project using Maven:
   ```
   mvn clean install
   ```

## Project Structure
```
DeterministicFiniteAutomaton/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   ├── DeterministicFiniteAutomaton.java
│                   ├── LambdaDFA.java
│                   ├── Main.java
|                   ├── State.java
|                   └── PostFixConverter.java
|
├── pom.xml
└── README.md
```

## Testing
Run the unit tests using JUnit:
```
mvn test
```

## Contributing
1. Fork the repository
2. Create a new branch (`git checkout -b feature/amazing-feature`)
3. Implement your changes
4. Run the tests to ensure everything is working as expected
5. Commit your changes and push the branch
6. Submit a pull request

## License
This project is licensed under the [MIT License](LICENSE).
