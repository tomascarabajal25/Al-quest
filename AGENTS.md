# AGENTS.md

## What this project is
Al-Quest is a Java 17 Swing adventure game for the UBA "Algoritmos y Estructuras de Datos" course (1C2026). The player moves between 10 cities, each one a different algorithmic mini-game. The custom data-structures library under `src/estructuras/` is part of the deliverable, not just a dependency.

## Build & run
- `build.sh` — `javac` every `src/**/*.java` to `out/production/Al-quest/` and copies non-Java resources (sprites, maps, dictionaries) preserving package layout.
- Run the game (entry point is `src/Main.java`, in the default package): `java -cp out/production/Al-quest:lab/gson-2.14.0.jar Main`. The gson jar is required at runtime by `persistencia.GestorArchivosJSON`.
- `Main` first shows a `JOptionPane` asking for a player name before any window opens.
- `*.sh`, `*.iml`, `.idea/`, `out/` are all gitignored — the build scripts in the repo root are not tracked and exist as local tooling.

## Test
- Tests use JUnit 5 (5.14.0); jars are read from `~/.m2/repository/...` by `test.sh` (hardcoded paths, see Quirks).
- `test.sh` compiles one subset of tests via `javac` only. To actually run JUnit, use `junit-platform-console-standalone` (not present in this environment) with `--class-path out/production/Al-quest:out/test` and `--scan-class-path`, or wire the suite into IntelliJ via `Al-quest.iml`.
- Test packages are inconsistent: most live under `ciudades.X` (no `tests.` prefix), a few under `tests.ciudades.X`. Match whatever the source file's `package` line says when running.

## Repo layout
- `src/Main.java` — entry point (default package).
- `src/modelos/` — domain. `Partida` (abstract base), `Jugador`, `GrafoCiudades`, `NodoCiudad`, `PartidaGeneral`, `Celda`, `Elemento`, `Mapa`, `Mochila`, `Objeto`, `Entidad`, `Minijuego`.
- `src/juego/ciudades/<ciudad>/` — each mini-game is a `Partida` subclass. The 10 cities: `batalla`, `ciudad5` (búsqueda), `ciudad_3_laberinto`, `complejidad`, `grafos`, `hashing`, `ordenamientos`, `recoleccionEnMatriz`, `reinas`, `torresDeHanoi`. Some are split into `controller/`, `model/`, `view/`, `ui/`, `model.acciones/`.
- `src/juego/configuracion/` — per-city configuration constants.
- `src/modelosVista/` — Swing rendering. `Vista` (JPanel + 60 FPS game loop), `VistaGlobal` (world map), `JugadorVista`, `KeyHandler`, `ManejadorDeConstruccion`, `AdministradorDeObjetos`, `TiendaSkins`, etc.
- `src/estructuras/` — hand-rolled TDAs: `listas`, `pilas`, `cola`, `conjuntos`, `hashing`, `arboles`, `grafos/` (with `algoritmos/` for Kruskal, Prim, Floyd, Warshall, Ford-Fulkerson, cycles, Hamilton), `vector`, `nodos`. Some have a "Basica" variant.
- `src/persistencia/` — Gson-based JSON save/load (`GestorArchivosJSON`, `GestorDeInicio`, `DatosGuardado` DTO).
- `src/utils/` — `ValidacionesUtiles` (pervasive precondition checks), `Teclado`, `SistemaUtiles`, `NumerosUtiles`, `convertidosSprites`, plus `utils.bitmap/`.
- `src/maps/` — tile maps (`mapa_global.txt`, `world01..03.txt`, `world_hashing.txt`, `recoleccion/`). Loaded via classpath, e.g. `/maps/mapa_global.txt`.
- `src/assets/` — sprite bitmaps (`jugador/`, `ciudades/`, `cartas/`, `construcciones/`, `objetos/`).
- `tests/` — JUnit 5. Mirrors `src/` but with the package-inconsistency noted above.
- `lab/gson-2.14.0.jar` — the only third-party runtime dependency.
- `Al-quest.iml` + `.idea/` — IntelliJ module (JDK 17, `out/` as output).
- `mapasBusqueda/` — word lists for the search city (facil/medio/dificil).

## Architecture — things you only learn by reading code
- `modelos.Partida` is the abstract base for every mini-game. Subclasses implement `iniciar()` and `finalizar()`. A `Runnable onFinalizadoCallback` is wired by `PartidaGeneral.crearNodo` at `modelos/PartidaGeneral.java:150` and invoked by `Partida.notificarFinalizacion()`.
- `PartidaGeneral` is the campaign orchestrator: it holds a `GrafoCiudades` with the 10 cities connected 1→2→…→10. The accessibility rule (in `GrafoCiudades`): a city is reachable iff it's #1 OR has at least one neighbor in `completada=true` pointing to it. Constants: `GrafoCiudades.MAX_CIUDADES = 10`, `GrafoCiudades.ID_CIUDAD_INICIAL = 1`.
- The `EstadoDePartida` enum lives in `juego.ciudades.ordenamientos` (NOT in `modelos`, despite being used everywhere via `Partida.setEstado`). Don't move it.
- Persistence: `saves/{nombre}.json`, written by `persistencia.GestorDeInicio.guardarSesion(...)`. `GestorArchivosJSON` resolves the saves folder by walking up from the compiled classpath looking for a sibling `Al-quest/saves/`, falling back to relative `saves/`. For this to resolve, the JVM working directory should be a descendant of the `Al-quest` repo root.
- The skin system: `Partida.rutaSprites` defaults to `/assets/jugador/boy`; `PartidaGeneral` tracks purchased skins and calls `setRutaSprites(skinActual)` on the sub-partida in `entrarACiudad` (`modelos/PartidaGeneral.java:283`) so a city picks up the equipped skin.
- `PartidaGeneral.alTerminarCiudad` (modelos/PartidaGeneral.java:303) is the bridge that aggregates score, marks the city completed, and persists the session — called via the callback.

## Workflow conventions (from `info.md`)
- Every change goes through a PR; at least one teammate must approve before merge.
- New methods require a javadoc and a mention in the report (`informe`).
- Prefer constants / enums over hardcoded values.
- Branches: `feat/`, `fix/`, `hotfix/`, `chore/` — kebab-case, no leading or trailing `-` or `.`, no underscores.
- Commits: Conventional Commits — `feat:`, `fix:`, `docs:`, `style:`, `refactor:`, `test:`, `chore:` with optional scope and `!` for breaking changes.

## Quirks
- `*.sh` in `.gitignore` means the build scripts are local-only. If you change them, treat them as part of the workflow but don't expect a PR review to cover them.
- The only third-party runtime dependency is `lab/gson-2.14.0.jar`. It is not on any auto-configured classpath — add it manually on the command line, or wire it into the IntelliJ module.
- `test.sh` has hardcoded paths under `/home/fballerio/.m2/repository/...`. Adapt to your own m2 cache, or just point IntelliJ at the JUnit 5 libraries via `Al-quest.iml`.
- `out/production/Al-quest/com/aiquest/...` is a stale build artifact from an older `com.aiquest` package layout. The current source has no `com.aiquest` prefix. It's harmless because `out/` is gitignored; a fresh `build.sh` overwrites `out/production/Al-quest/` from scratch.
- `saves/` is created on demand by the game at runtime and is gitignored. Player saves are not committed.
- Not every class has a test. Some test directories exist only for the city currently being developed (e.g. city 9 batalla on `feat/batalla`). Don't assume a test file exists for a given class — check first.
- `Ciudad 9 (batalla)` requires the hand-rolled `estructuras.listas.ListaSimplementeEnlazada`, `estructuras.pilas.Pila`, and `estructuras.cola.Cola` per `src/juego/ciudades/batalla/batalla.md`; don't substitute `java.util` collections there.
