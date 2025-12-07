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

### Testing
*   **JUnit Tests** under `src/test/java/com/comp2042/logic/`:
    *   `ScoreTest`: Validates scoring, high score, lines, and reset logic.
    *   `MatrixOperationsTest`: Tests core matrix manipulation functions.
    *   `TBrickTest`: Ensures correct rotation and deep-copying for bricks.

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