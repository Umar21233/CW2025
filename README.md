# CW 2025 – Tetris

[![Build](https://img.shields.io/badge/build-Maven%20Wrapper-3.8.5)]()
[![JDK](https://img.shields.io/badge/JDK-23-blue)]()

**GitHub Repository:**  
<https://github.com/Umar21233/CW2025>

---

## Compilation Instructions

### Requirements

- **JDK:** 23 (ensure your IDE and Maven both use JDK 23)
- **Build tool:** Maven / Maven Wrapper
- **JavaFX:** 21.0.6 (via Maven dependencies & plugin)
- **Internet access** the first time, so Maven can fetch dependencies.

### Using Maven Wrapper (recommended)

From the project root:

```bash
# macOS / Linux
./mvnw -q clean javafx:run

# Windows
mvnw.cmd -q clean javafx:run
```

---

## Key Resources & Assets

The project relies on several key resource files for its UI, styling, and audio. These are located in `src/main/resources/`.

*   **FXML Layouts**: Define the structure of the UI scenes.
    *   `gameLayout.fxml`: The main game screen, including the board, stats display, and next piece preview.
    *   `main_menu.fxml`: The main menu screen with buttons for navigation.
    *   `settings.fxml`, `audio_settings.fxml`: Layouts for the settings screens.
    *   `stats.fxml`: The layout for the player statistics screen.

*   **CSS Stylesheets**: Provide the visual styling for the application.
    *   `window_style.css`: The primary stylesheet defining the dark theme, fonts, and layout for the game window.
    *   `main_menu_style.css`, `stats_style.css`: Specific styles for the main menu and stats screens.
    *   `window_style_light.css`, `window_style_neon.css`, `window_style_retro.css`: Alternative theme examples (not fully implemented).

*   **Audio Files**: Sound effects and music used throughout the game.
    *   `menu_music.wav`, `game_music.wav`: Background music for the menu and game.
    *   `piece_drop.wav`, `line_clear.wav`, `piece_rotate.wav`: Core gameplay sound effects.
    *   `game_over.wav`, `button_click.wav`: UI and event sound effects.

*   **Fonts & Images**:
    *   `digital.ttf`: The custom "digital clock" font used for scores and stats.
    *   `background_image.png`, `tetris4.png`: Background and decorative images.

---

## Documentation

The project's Javadoc documentation can be accessed by opening `javadocs/index.html` directly in your web browser. This provides detailed API information for classes and methods.

---


## Project Features and Implementation Details

### Implemented and Working Properly

#### Core Gameplay Features
*   **Ghost Piece (Shadow Piece)**
    *   Implemented in the engine (`SimpleBoard`, `ViewData`), not just the UI.
    *   Uses `computeGhostY()` in `SimpleBoard` and `ghostYPosition` field in `ViewData`.
    *   Reacts to move, rotate, soft drop, and hard drop.
    *   Rendered on a dedicated ghost panel with lower opacity.
*   **Hard Drop (SPACE)**
    *   Implemented in `GameController.onHardDrop(MoveEvent)` and `GuiController.hardDrop(MoveEvent)`.
    *   Instantly moves the piece to the lowest valid row and merges it.
*   **Next Piece Preview (`NextPieceView`)**
    *   Refactored into a dedicated `NextPieceView` class.
    *   Automatically recenters and resizes the 4x4 preview box.
*   **Score System (Score + Level + Lines)**
    *   `Score` class tracks `score`, `highScore`, `totalLines`, and `level` (1–10).
    *   JavaFX property bindings to UI labels.
*   **Level & Speed System**
    *   Level increases every `LINES_PER_LEVEL` lines.
    *   Game speed (`Constants.LEVEL_SPEED`) adjusts with the level.
*   **Game Over & Restart**
    *   Dedicated game-over overlay.
    *   Reliable reset for new games.
*   **Pause / Resume**
    *   Pause/resume via button and keyboard, including music control.
*   **Line Clear Notification Popup**
    *   Uses a dedicated `NotificationPanel` overlay to display score popups such as `+50`, `+100` when lines are cleared.
    *   Animations are styled via the existing notification CSS (`.notificationText`), preserving the original “digital arcade” look.
    *   Keeps feedback purely visual and non-blocking (no dialogs), so gameplay flow is uninterrupted.

*   **Danger Line Flashing**
    *   A “danger line” near the top of the board flashes when the stack height is close to game over.
    *   Implemented using a JavaFX `Timeline` animation that starts with the game and restarts correctly on new games.
    *   Gives the player a clear visual warning that they are close to losing.

*   **Help / Controls Screen**
    *   Accessible from the **Help** button on the main menu.
    *   Implemented via a custom `HelpDialog` class that wraps a styled JavaFX `Alert`.
    *   Dialog uses a dark theme with clear, monospace text explaining the controls (movement, rotation, hard drop, pause, etc.).

#### Audio System
*   **Central Audio Manager (`AudioManager`)**
    *   Singleton managing menu music, game music, and sound effects.
    *   Global volume controls and mute toggles.
*   **Audio Settings Screen (`AudioSettingsController`)**
    *   UI for controlling music/SFX volume and toggles.
*   **Gameplay Integration**
    *   SFX played for rotate, drop, line clear, and game over.

#### Persistent High Score
*   **File-based persistence (`HighScorePersistence`)**
    *   High score saved in `src/main/resources/HighScore/highscore.dat`.
    *   Loaded at startup and updated immediately when a new high score is set.

#### Settings & Statistics
*   **Ghost Mode Toggle (`GameSettings`, `SettingsController`)**
    *   Uses `java.util.prefs.Preferences` to store user's choice.
    *   Toggle available in the settings screen.
*   **Player Statistics Screen (`PlayerStats`, `StatsController`)**
    *   Tracks total games, total points, highest score, highest combo, and highest level.
    *   Stats are displayed with an animated count-up effect.

#### UI / UX Improvements
*   **`StackPane` Board Architecture**
    *   Layered board for clean rendering: background, landed bricks, ghost piece, active piece.
*   **Grid Background & Alignment**
    *   All visual elements are aligned mathematically based on row/column indices.
*   **Main Menu & Navigation Flow**
    *   Clear and simple main menu with intuitive scene transitions.
*   **Game Layout & Styling**
    *   Cohesive dark theme, styled components, and well-organized layout using CSS (`window_style.css`).
*   **Statistics Screen Styling (`stats_style.css`)**
    *   Custom styling consistent with the main game theme.
*   **Main Menu → Game Transition**
    *   Clean scene switching between `main_menu.fxml` and `gameLayout.fxml` using `FXMLLoader`.
    *   Stage is resized and re-centred appropriately when moving from menu → game and back.
    *   Ensures there is always a clear entry point (menu first) instead of launching straight into gameplay.

### Testing
Comprehensive JUnit tests are located under `src/test/java/com/comp2042/`.

#### Core Logic & Game Mechanics
*   **`GameBoardTest`**: Validates piece movement, placement, and line clearing on the main board.
*   **`SimpleBoardTest`**: Tests the underlying data structure for the game board.
*   **`ScoreTest`**: Ensures scoring, high score, line counting, and level progression logic is correct.
*   **`PieceManagerTest`**: Verifies the logic for spawning new pieces and managing the piece queue.
*   **`GhostCalculatorTest`**: Checks the calculation of the ghost piece's Y position.
*   **`HighScorePersistenceTest`**: Tests saving and loading the high score to/from the `.dat` file.

#### Matrix & Collision
*   **`MatrixOperationsTest`**: Covers fundamental matrix functions like rotation, copying, and merging.
*   **`MatrixOperationsOutOfBoundsTest`**: Specifically tests edge cases for pieces moving or rotating out of the board boundaries.
*   **`MatrixOperationsMergeCollisionTest`**: Tests collision detection when a piece merges with the board.

#### Bricks
*   Tests for each individual brick type (`IBrickTest`, `JBrickTest`, `LBrickTest`, `OBrickTest`, `SBrickTest`, `TBrickTest`, `ZBrickTest`) to ensure their initial shapes and rotation states are correct.
*   **`BrickRotatorTest`**: Validates the brick rotation logic.
*   **`RandomBrickGeneratorTest`**: Ensures the brick generator produces a valid sequence of bricks.

#### Model & Data Structures
*   `ClearRowTest`, `DownDataTest`, `NextShapeInfoTest`, `ViewDataTest`: Validate the integrity of data transfer objects.

---

### Implemented but Not Working Perfectly

*   **Visual Ghost Offset Hack**: A small negative Y-offset is applied to the ghost panel for visual correction instead of a pure mathematical solution.
*   **Audio Resource Dependence**: The game handles missing audio files gracefully but prints warnings to the console.
*   **Game Area Alignment**: The game view does not center itself when the window is maximized.

---

### Features Not Implemented

*   **Full Theme / Skin System**: The architecture supports themes, but only one theme is implemented.
*   **Online / Database Leaderboard**: High score is local only.
*   **Advanced Gameplay Extensions**: No power-ups or special game modes.
*   **Full Javadoc Coverage**: Key classes are documented, but coverage is not exhaustive.
*   **Exhaustive Test Suite**: Core logic is tested, but UI controllers and other components lack unit tests.

---

### New Java Classes

#### UI Architecture & Rendering
*   **`com.comp2042.ui.MainMenuController`**  
    Controller for `main_menu.fxml`.  
    - Handles **Play**, **Help**, **Stats**, **Settings**, and **Exit** buttons.
    - Starts menu music via `AudioManager` and switches to game music on Play.
    - Loads the game view, stats screen, and settings screen using `FXMLLoader` and passes the primary `Stage`.

*   `com.comp2042.audio.AudioManager`
*   `com.comp2042.audio.SoundEffect`
*   `com.comp2042.audio.MusicType`
*   `com.comp2042.logic.HighScorePersistence`
*   `com.comp2042.ui.SettingsController`
*   `com.comp2042.ui.AudioSettingsController`
*   `com.comp2042.ui.GameSettings`
*   `com.comp2042.ui.PlayerStats`
*   `com.comp2042.ui.StatsController`
*   `com.comp2042.ui.GameBoardView`
*   `com.comp2042.ui.PieceRenderer`
*   `com.comp2042.ui.NextPieceView`
*   `com.comp2042.ui.HelpDialog`

---

### Modified Java Classes

#### UI Layer
*   **`com.comp2042.ui.GameOverPanel`**
    *   Updated styling and positioning so that the game-over message is centred over the board area.
    *   Integrated with the new layout (StackPane-based board + right sidebar) so it no longer overlaps UI elements incorrectly.
    *   Works together with `GuiController.gameOver()` to show/hide the overlay cleanly.

*   **`com.comp2042.ui.NotificationPanel`**
    *   Extended to support the new score popup values (`+50`, `+100`, etc.) linked to the updated scoring rules.
    *   Timing and fade animations tuned so they remain readable without blocking gameplay.
    *   Reused across both normal line clears and hard-drop clears.

*   **`com.comp2042.logic.NextShapeInfo`** 
    *   Adjusted to work with the refactored `NextPieceView` and updated preview rendering.
    *   Ensures the correct shape ID and rotation state are passed to the preview logic after each piece spawn.

*   `com.comp2042.logic.GameController`: Added hard drop, level-based speed scaling.
*   `com.comp2042.logic.Score`: Extended to track level, lines and manage high score persistence.
*   `com.comp2042.logic.Constants`: Added constants for level speed and scoring.
*   `com.comp2042.logic.SimpleBoard`: Added ghost piece calculation logic.
*   `com.comp2042.logic.ViewData`: Extended to include ghost piece position.
*   `com.comp2042.logic.DownData`: Modified to prevent null board references.
*   `com.comp2042.ui.GuiController`: Major refactor to delegate responsibilities and integrate new features.
*   `com.comp2042.ui.MainMenuController`: Implemented scene switching and menu logic.
*   `com.comp2042.ui.Main`: Changed entry point to main menu and added resource cleanup on exit.

---

## System Maintenance & Refactoring (Summary)

-   **Package and Responsibility Clean-up**
    -   Separated UI (`com.comp2042.ui`), logic (`com.comp2042.logic`), and audio (`com.comp2042.audio`) concerns.
    -   Moved rendering and preview logic out of `GuiController` into dedicated classes (`GameBoardView`, `PieceRenderer`, `NextPieceView`) to enforce the Single Responsibility Principle.

-   **Encapsulation & Naming**
    -   Replaced public mutable fields with private fields + getters/setters or JavaFX properties where appropriate.
    -   Renamed ambiguous variables and methods to clearer names (e.g. avoiding one-letter identifiers), improving readability.

-   **Removal of Magic Numbers**
    -   Centralised critical constants (drop speeds, lines-per-level, scoring values) in `Constants`.
    -   Removed hard-coded pixel offsets from the layout; positions are now derived from row/column indices and cell sizes.

-   **Stability & Robustness**
    -   Eliminated potential `NullPointerException`s by:
        -   Ensuring `DownData.board` is always initialised.
        -   Adding missing FXML elements (`levelLabel`, `linesClearedLabel`) where bindings are used.
    -   Normalised FXML and resource paths to consistent classpath-based loading to avoid “Location is not set” errors.

### Unexpected Problems & Resolutions

1.  **Alignment Bugs After Grid Rewrite**: Fixed by removing all "magic number" pixel offsets and using a strict mathematical mapping from board coordinates to pixels.
2.  **Hard Drop Crashing (Null `board` in `DownData`)**: Fixed by adding the board matrix to `DownData` and updating all call sites to pass it.
3.  **Next Piece Disappearing**: Fixed by reordering the refresh logic in `GuiController` to ensure the preview is updated last.
4.  **Spawn Location & Bottom Gap Issues**: Fixed by standardizing the coordinate system and handling hidden rows correctly.
5.  **Resource Path Issues (FXML, Images)**: Fixed by using absolute classpath paths for all resource loading.
6.  **Danger Line Not Flashing on First Game**: Fixed by restarting the animation timeline after the game view layout is initialized.
7.  **Immutable `MoveEvent` Misuse**: Fixed by creating a new `MoveEvent` object for each key press instead of reusing one.
8.  **Alert Dialog Showing Game Background**: Fixed by applying custom CSS to the dialog pane to give it a solid background.
9.  **Missing Labels Causing NPEs**: Fixed by adding the required `fx:id`s to the FXML files.

---

## Design Patterns Implemented

The following design patterns have been utilized in the project:

*   **Singleton Pattern**:
    *   `GameSettings.java`: Ensures a single instance for managing game settings.
    *   `AudioManager.java`: Guarantees a single instance for audio management.
    *   `SettingsController.java`: Accesses singleton instances of other components.

*   **Persistence Pattern**:
    *   `GameSettings.java`: Uses Java Preferences API for data persistence.

*   **Observer Pattern**:
    *   `GuiController.java`: Implements the Observer pattern to react to changes in settings.

*   **Strategy Pattern**:
    *   `Theme.java`: Each enum constant represents a different visual strategy for themes.

*   **Enum Pattern**:
    *   `Theme.java`: Provides type-safe theme selection with predefined constants.

---

### New Java Classes