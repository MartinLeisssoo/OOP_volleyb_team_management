# Volleyball Coach Application

**Authors: Martin Leissoo, Lauri Mark Kontson**

## Project Overview

This is a management tool designed to help volleyball coaches evaluate and develop players, simulate training sessions, and generate personalized skill improvement recommendations using a custom ELO-like rating system and skill progression mechanics.

## Project Description

The Volleyball Coach Application is software designed to assist volleyball coaches with:

*   Managing multiple team rosters and player profiles.
*   Adding new teams and selecting the active team to work with.
*   Simulating training sessions (serving, attacking, blocking, defending).
*   Analyzing player skills and identifying weaknesses (based on both overall level and recent training performance).
*   Generating personalized training recommendations based on a player's overall weakest skills.
*   Tracking player development through ELO rating changes and skill level increases.

## Installation and Execution

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/MartinLeisssoo/OOP_volleyb_team_management.git
    ```
2.  **Navigate to the project directory:**
    ```bash
    cd src
    ```
3.  **Compile the project**:
    ```bash
    javac *.java
    ```
4.  **Run the application:**
    ```bash
    java TreenerRakendus
    ```

## Usage Guide

After launching the application, a menu will be displayed where you can choose the following actions (using `JOptionPane` dialog boxes):

1.  **Add Player:** Add a new player to the *active* team with their skill levels.
2.  **Simulate Training:** Run a training simulation for a selected player in the *active* team. Displays results, skill changes, and ELO change.
3.  **View Training Recommendations:** Get personalized recommendations for a selected player's development in the *active* team (based on the player's overall weakest skills).
4.  **Show All Players:** View the statistics (including ELO and skills) for all players in the *active* team.
5.  **Add New Team:** Create a new team, specifying its name and maximum player capacity.
6.  **Select Active Team:** Change which team you are currently working with.
7.  **Show All Teams:** Display a list of all teams in the system and their current player count/capacity.
8.  **Exit:** Terminate the application.

## Project Architecture

### Classes

*   `Mängija.java`: Player data model (name, skills: serve, attack, block, defense; ELO rating). Includes logic for calculating initial ELO and finding the overall weakest skill.
*   `Võistkond.java`: Manages a single team, containing an array of players and methods for adding/finding players.
*   `TreeninguTulemus.java`: Stores and analyzes training results (successes/attempts per skill), calculating overall and individual success rates, and identifying the weakest performance *in that specific training session*.
*   `TreeninguSimulaator.java`: Performs the randomness-based training simulation, calculates ELO changes, and is responsible for updating player skills based on performance.
*   `TreeninguSoovitaja.java`: Generates training recommendations based on the player's overall weakest skills (not just the last training session).
*   `TreenerRakendus.java`: The main application class that manages the user interface (`JOptionPane`), application flow, and holds all teams and the currently active team.

## Performance Rating System (Custom ELO)

The application uses an ELO-inspired rating system to track individual development. Unlike traditional ELO, which compares two competing entities, this adaptation measures a player's performance in a simulation against an *expectation* derived from their own skill level.

*   **Expected Performance:** For each player, an expected success rate (`arvutaOodatavSooritus`) is calculated based on their average skill level. Higher-skilled players are expected to perform better. (Uses formula `0.95 * (1 - Math.exp(-averageSkill / 45.0))`).
*   **Actual Performance:** During the simulation, the player's actual average success rate (`õnnestumismäär`) is calculated.
*   **ELO Change:** The change is calculated based on the difference between actual and expected performance (`relativeSuccess = actualRate - expectedRate`), multiplied by a K-factor (currently **K=28**).
*   **Beginner Protection:** For players with a lower average skill level, ELO loss is reduced if their performance falls below expectations.
*   **Asymmetry:** Negative ELO changes (losses) are further mitigated (`NEGATIVE_CHANGE_MITIGATION = 0.7`), making it proportionally harder to lose ELO than to gain it for the same performance difference.

This approach creates a metric reflecting individual development trends and whether a player is over- or under-performing relative to their potential, rather than just their absolute success.

## Technical Implementation

*   **Simulation:** The system simulates player performance using `java.util.Random`. Each training session:
    *   Runs **10** simulated attempts for each core skill (serve, attack, block, defense).
    *   Uses a non-linear formula (`simuleeriTegevus`) to determine the success chance for each action, making it harder to achieve 90%+ success rates even with top skills (uses formula `97.0 * (1.0 - Math.exp(- (double)skillLevel / 50.0))`).
    *   Calculates success rates per skill (`TreeninguTulemus`).
    *   Identifies the skill with the weakest performance *during that training*.
*   **Skill Progression:** Player skills can improve (+1 point) if their success rate in that skill during training exceeds a dynamic threshold (`lävend` in `uuendaOskusi`). This threshold is calculated using the formula (`0.3 + 0.7 * Math.pow(skillLevel / 100.0, 5)`), which rises very steeply as the skill approaches 100, making progression at elite levels more difficult.
*   **User Interface:** User interaction is handled via `javax.swing.JOptionPane` dialog boxes, providing a simple text-based interface.
*   **Data Structures:** For simplicity, player and team management uses basic Java arrays (`Mängija[]`, `Võistkond[]`) with counters, rather than more complex Collections.
