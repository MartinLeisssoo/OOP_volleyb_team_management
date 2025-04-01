# Volleyball Coach Application

A management tool that helps coaches evaluate and develop volleyball players, simulate training sessions, and generate personalized skill improvement recommendations based on an ELO rating system.

## Project Description

The Volleyball Coach Application is software designed to help volleyball coaches:
- Manage team rosters and player profiles
- Simulate training sessions (serving, attacking, blocking, defending)
- Analyze player skills and identify weaknesses
- Generate personalized training recommendations
- Track player development through ELO rating changes

## Installation and Execution

1. Clone the repository:
```
git clone https://github.com/username/volleyball-coach.git
```

2. Navigate to the project directory:
```
cd volleyball-coach
```

3. Compile the project:
```
javac *.java
```

4. Run the application:
```
java TreenerRakendus
```

## Usage Guide

After launching the application, you can use the following functions:

1. **Add Player** - Add a new player to your team with their skill levels
2. **Simulate Training** - Run a training simulation for a selected player and view results
3. **View Training Recommendations** - Get personalized recommendations for player development
4. **Show All Players** - View statistics for all players in the team

## Project Architecture

### Classes

- **Mängija.java** - Player data model (name, skills, ELO rating)
- **Võistkond.java** - Team management and player collection
- **TreeninguTulemus.java** - Training result tracking and analysis
- **TreeninguSimulaator.java** - Randomized training simulation
- **TreeninguSoovitaja.java** - Training recommendation engine
- **TreenerRakendus.java** - Main class with UI and program flow

### ELO System

The application uses an ELO rating system with the formula: `newElo = oldElo + K * (successRate - 0.5)`, where:
- K = 32 (constant)
- Success rate ranges from 0.0 to 1.0
- 50% success rate results in no ELO change
- Above 50% success rate increases ELO
- Below 50% success rate decreases ELO

## Technical Implementation

The system simulates player performance using Java's Random class to generate outcomes based on skill probabilities. Each training session:

1. Runs 10 simulated attempts for each skill
2. Calculates success rates for each skill area
3. Identifies the weakest skill based on performance
4. Generates targeted recommendations
5. Adjusts the player's ELO rating

User interaction is handled through JOptionPane dialogs for a simple, accessible interface. Player data is maintained in memory through an array-based implementation rather than collections for simplicity.

## Authors

- Martin Leissoo
- Lauri Mark Kontson

## Project Context

This project was created for the Object-Oriented Programming course's first team assignment at the University of Tartu, Spring Semester 2025.

