# Al-Quest — Project Overview

Al-Quest is a Java Swing adventure game for the UBA course "Algoritmos y Estructuras de Datos" (1C2026). The player navigates a map of interconnected cities, each presenting a different algorithmic challenge. The project doubles as a custom data-structures library and a framework for plugging in new city mini-games.

No build system (no Maven/Gradle). VS Code project with `src/` as source path, output to `out/`.

---

## Architecture at a Glance

```
Main.java                          ← entry point (default package, launches Swing)
│
├── modelos/                       ← domain model (shared by all cities)
│   ├── Partida (abstract)         ← base class every city extends
│   ├── Jugador, Mapa, Celda, Elemento, Mochila, Ciudad
│
├── Juego/
│   ├── PartidaAiQuest             ← campaign orchestrator (holds List<Partida> of cities)
│   └── ciudades/
│       ├── ordenamientos/         ← Sorting city (Bubble, Selection)
│       ├── reinas/                ← N-Queens city (backtracking)
│       └── torresDeHanoi/         ← Towers of Hanoi city (recursion/stacks)
│
├── estructuras/                   ← custom data-structures library
│   ├── nodos, vector, listas, pilas, cola, conjuntos, hashing, arboles, grafos
│   └── grafos/algoritmos/        ← Kruskal, Prim, Floyd, Warshall, Ford-Fulkerson, Hamilton
│
└── utils/                         ← reusable utilities
    ├── ValidacionesUtiles          ← precondition checker (used pervasively)
    ├── Teclado, SistemaUtiles, NumerosUtiles
    └── bitmap/Bitmap, BitmapViewerConMenu
```

---

## Entry Point: `Main.java`

```java
SwingUtilities.invokeLater(() -> {
    new VentanaPrincipal(() -> System.out.println("Ciudad completada"));
});
```

Boots the N-Queens Swing UI. The `VictoriaListener` callback is a placeholder — when a city is won, this lambda fires, and will eventually chain to the next city via `PartidaAiQuest`.

---

## Module: `modelos/` — Domain Models

Shared domain objects used across all cities.

### `Partida` (abstract)

| Field | Type | Visibility |
|---|---|---|
| `nombre` | `String` | private |
| `combatiente` | `Jugador` | private |
| `puntajeActual` | `int` | private |
| `estado` | `EstadoDePartida` | private |

**Key methods:**
- `abstract void iniciar()` — start the game session
- `abstract void finalizar()` — end the game session
- `boolean estaIniciada()` — checks `estado == Iniciado`
- `protected void setEstado(EstadoDePartida)`, `protected void setPuntaje(int)`

**`EstadoDePartida` enum:** `Creado`, `Iniciado`

This is the integration contract. Every city must extend `Partida`.

### `Jugador`

- Single field: `String nombre`. Value-equality by name.
- No gameplay state (no score, no inventory here — that lives in `Partida`).

### `Elemento`

- Single field: `String nombre`. Base class for game items.
- `Caja` (in ordenamientos) extends this, adding `int tamaño` and `Comparable<Caja>`.

### `Celda<T>`

- Wraps a generic `contenido`. Used by `Mapa` to fill grid positions.
- Equality by content value.

### `Mapa`

- Backed by `Celda<?>[][]`. 0-indexed grid.
- `ocuparCelda(Object, int ancho, int alto)` / `vaciarCelda(int, int)` — place or remove content.
- `getCeldasVecinasRespectoPosicion(int, int, int)` — returns a `(2*cant+1)×(2*cant+1)` neighborhood.
- `validarFueraDeRango(int, int)` — public range check (throws `RuntimeException`).
- **Bug:** `getCeldaConContenido` compares the content object against the `Celda` object itself (should be `.getContenido()`), so it always returns null.

### `Mochila`

- Player inventory. Backed by `ListaSimplementeEnlazada<Elemento>`.
- Has `cantidadMaxima` capacity. `agregarElemento(Elemento)` refuses when full.
- **Bug:** `validarEstaLlena()` returns `true` when NOT full and `false` when full — inverted semantics. The caller in `agregarElemento` throws when the bag is NOT full.

### `Ciudad`

Empty stub — placeholder for future city entity logic.

---

## Module: `Juego/PartidaAiQuest` — Campaign Orchestrator

Extends `Partida`. Holds `List<Partida> ciudades` and `int indiceCiudadActual`.

```java
public PartidaAiQuest(Jugador combatiente) {
    super("Campaña Global AIQUEST", combatiente);
    ciudades.add(new PartidaOrdenamientos<>("Wilde", getJugador(), ..., new OrdenadorBubble<>("Burbuja")));
    ciudades.add(new PartidaOrdenamientos<>("Tokio", getJugador(), ..., new OrdenadorSelection<>("Selección")));
}
```

- `getPartidaCiudadActual()` — returns the active city's `Partida`.
- `avanzarCiudad()` — increments index; score aggregation is planned but incomplete.
- `iniciar()` and `finalizar()` are stubs.

**To add a new city:** add a new `Partida` subclass instance in `inicializarCiudades()`.

---

## Module: City — Ordenamientos (Sorting)

**Package:** `Juego.ciudades.ordenamientos`

The player watches a sorting algorithm animate step-by-step, then must recall the array state at a randomly chosen step.

### `EstadoDePartida` (enum)

`Creado`, `Iniciado` — shared across ALL city Partidas (referenced by `modelos.Partida`).

### `Caja` extends `Elemento`

- Fields: `int tamaño` (from `Elemento`: `String nombre`).
- `implements Comparable<Caja>` — compares by `tamaño`.
- `equals` compares by `tamaño` only (not name).

### `Ordenador<T extends Comparable<T>>` (abstract)

Strategy pattern for sort algorithms.

| Method | Description |
|---|---|
| `protected abstract void ordenar(List<T> elementos)` | Sort in-place, no recording |
| `protected abstract void ordenar(List<T> elementos, AdministradorDePasos<T> historial)` | Sort + record each swap as a `PasoOrdenamiento` |

**Implementations:**
- `OrdenadorBubble<T>` — Bubble Sort. Records a step on each swap.
- `OrdenadorSelection<T>` — Selection Sort. Records a step on each swap.

### `PasoOrdenamiento<T>`

Immutable snapshot of a single sort step.

| Field | Type | Description |
|---|---|---|
| `estadoCopia` | `List<T>` | Defensive copy of the array at this step |
| `indice1` | `int` | First swapped index (-1 if no swap) |
| `indice2` | `int` | Second swapped index (-1 if no swap) |
| `mensaje` | `String` | Description ("Intercambiando elementos", "Inicio del ordenamiento", etc.) |

### `AdministradorDePasos<T>`

Accumulates `List<PasoOrdenamiento<T>>`.

- `guardarPaso(PasoOrdenamiento<T>)` — appends.
- `getPasos()` — returns a **defensive copy** (`new ArrayList<>(historialDePasos)`).

### `PartidaOrdenamientos<T extends Comparable<T>>` extends `Partida`

| Field | Type |
|---|---|
| `elementosIniciales` | `List<T>` |
| `ordenador` | `Ordenador<T>` |
| `administradorPasos` | `AdministradorDePasos<T>` |

**Key methods:**
- `iniciar()` — validates not already started, makes a working copy, calls `ordenador.ordenar(copia, admin)`. Sets `EstadoDePartida.Iniciado`.
- `finalizar()` — validates is started, sets `EstadoDePartida.Creado`.
- `seleccionarPasoDesafioAleatorio()` — picks a random step index from the historial for the memory challenge.
- `verificarEstadosDePasos(List<T> elementos, int nroPaso)` — checks if the player's recall matches step `nroPaso` element-by-element. Returns `boolean`.
- `getHistorialDePasos()` — returns admin's list.
- `getNombreAlgoritmo()` — returns `ordenador.getNombre()`.

### `Principal` (test driver)

Standalone `main()` that creates a `PartidaOrdenamientos` with `Caja` elements, calls `iniciar()`, launches the `RenderOrdenamiento` animation, then simulates a memory challenge via console. Not used in production.

### `ui/RenderOrdenamiento`

Static utility that creates a `Bitmap` canvas and animates the sort steps.

- `visualizarSimulacion(PartidaOrdenamientos<Caja>)` — loads images, draws step 0, opens a `BitmapViewerConMenu` with an "Iniciar Animación" button.
- Animation runs on a background `Thread`, sleeping 800ms between steps.
- Bar heights are proportional to `Caja.getTamaño()`. Bars at `indice1`/`indice2` are rendered in red.

### `ui/RecursosOrdenamiento`

Loads BMP images from `src/imagenesDeOrdenamiento/`. Falls back to solid-color rectangles (`Color.BLUE` / `Color.RED`) if files are missing.

### Game Flow (Sorting City)

```
1. Create PartidaOrdenamientos with Cajas + Ordenador
2. partida.iniciar()  →  ordenador.ordenar(copia, admin)  →  fills historial
3. RenderOrdenamiento.visualizarSimulacion(partida)  →  animated bitmap window
4. partida.seleccionarPasoDesafioAleatorio()  →  picks step X
5. Player recalls the order of Cajas at step X
6. partida.verificarEstadosDePasos(playerAnswer, X)  →  true/false
```

---

## Module: City — Reinas (N-Queens)

**Package:** `Juego.ciudades.reinas`

The player places N queens on an NxN board so no two attack each other. A backtracking solver can show its steps.

**Note:** This city does NOT extend `Partida`. It's currently launched directly from `Main.java`. This is a gap — if you want it integrated into the campaign, you'll need to wrap it in a `PartidaReinas` class.

### `Tablero`

- Backed by `int[] reinas` where `reinas[fila] = columna` (or -1 for empty).
- `setTamanio(int)` — resizes and fills with -1.
- `colocarReina(fila, col)` / `quitarReina(fila)` — place or remove.
- `esValido(fila, col)` — checks column and diagonal conflicts against all placed queens.
- `copiar()` — deep copy for solver use.
- `getTodasLasReinas()` — returns `Arrays.copyOf(reinas, tamanio)`.

### `Accion` (enum)

`COLOCAR`, `QUITAR` — tags for solver step recording.

### `Paso`

Immutable record of one solver step: `int fila`, `int columna`, `Accion accion`.

### `SolverReinas`

Backtracking solver with three modes:

| Method | Returns |
|---|---|
| `resolver(Tablero, int fila)` | `boolean` — whether a solution exists |
| `obtenerSolucion(Tablero)` | `int[]` — queen positions, or `null` |
| `grabarPasos(Tablero)` | `List<Paso>` — every COLOCAR/QUITAR action during solving |

### `CiudadReinas`

Facade managing a `Tablero` and `SolverReinas`.

- `iniciarCiudad(tamanio, fila, col)` — sets board size, places the player's locked first queen, checks solvability. Returns `boolean`.
- `validarTableroJugador(int[][] tableroJugador)` — checks exactly 1 queen per row, no column/diagonal conflicts. Returns `boolean`.
- `actualizarTableroJugador(int[][], filaInicial, colInicial)` — syncs the internal `Tablero` from the player's 2D array, preserving the locked queen.
- `obtenerPasos()` — returns `List<Paso>` from `solver.grabarPasos()`, or `null` if no solution exists.

### `VictoriaListener` (interface)

Single method: `void onVictoria()` — callback for when the city is won.

### `ui/VentanaPrincipal` (JFrame)

- Shows a board-size selector (4-8), then creates a `TableroPanel`.
- `crearPanelDerecho(tamanio)` — rules panel + size-change combo box.
- On size change, confirms with dialog, then calls `iniciarConTamanio()` which creates a fresh `CiudadReinas`.

### `ui/TableroPanel` (JPanel)

- Renders an NxN chessboard with BMP tile images from `RecursosGraficos`.
- **First left-click** places the locked initial queen and calls `ciudad.iniciarCiudad(tamanio, fila, col)`.
- Subsequent left-clicks place queens; right-clicks remove them (except the locked queen).
- **"Listo" button** → calls `ciudad.validarTableroJugador(tableroJugador)`. On success: `victoriaListener.onVictoria()`.
- **"Mostrar solucion" button** → calls `ciudad.obtenerPasos()`. Animates steps via `javax.swing.Timer` (300ms per step). Sets `solucionRevelada = true` (disqualifies winning).
- **"Reiniciar" button** → resets all state.

### `ui/RecursosGraficos`

Loads BMP images from `src/Juego/ciudades/reinas/resources/imagenes/`: `casilla-clara.bmp`, `casilla-oscura.bmp`, `reina-fondoClaro.bmp`, `reina-fondoOscuro.bmp`.

### Game Flow (Queens City)

```
1. Player selects board size (4-8).
2. First click places locked queen → ciudad.iniciarCiudad(size, row, col).
3. Player places/removes queens.
4. "Listo" → ciudad.validarTableroJugador(tableroJugador).
   → true: victoriaListener.onVictoria() called.
   → false: game over, "Ver solucion" replaces "Mostrar solucion".
5. "Mostrar solucion" → animates solver backtracking steps.
```

---

## Module: City — Torres de Hanoi

**Package:** `Juego.ciudades.torresDeHanoi`

Classic Towers of Hanoi with manual play and automatic solver. Follows MVC strictly.

### `EstadoHanoi`

Immutable DTO (snapshot) of game state.

| Field | Type | Description |
|---|---|---|
| `torreA`, `torreB`, `torreC` | `String[]` | Disk arrays (bottom to top), cloned on get |
| `movimientos` | `int` | Move count |
| `minMovimientos` | `double` | 2^n - 1 |

### `Nodo<T>` (game-specific)

Doubly-linked node for the tower stacks.

- Fields: `T dato`, `Nodo<T> arriba`, `Nodo<T> abajo`.
- `setDato(T)`, `setArriba(T)`, `setAbajo(T)`, getters.

**Important:** This is NOT `estructuras.nodos.Nodo`. The Hanoi city has its own `Nodo<T>`.

### `Pila<T>` (game-specific)

LIFO stack using `Nodo<T>` with `arriba`/`abajo` links.

- `push(Nodo<T>)` — inserts at head.
- `pop()` — removes head.
- `peek()` — returns `cabeza.getDato()`.
- `getContNodo()` — element count.
- `getCabeza()` — returns the head `Nodo<T>`.

**Important:** This is NOT `estructuras.pilas.Pila`. The Hanoi city has its own `Pila<T>`.

### `CiudadHanoi`

Core game logic. Three `Pila<String>` towers (A, B, C). Disks are `"###"` strings (length = size).

- `CiudadHanoi(int discos)` — validates 3-10 discs, initializes all on tower A.
- `iniciar()` — resets towers, places all discs on A.
- `reiniciar(int nuevoObjetivo)` — resets with new disc count.
- `mover(Pila<String> origen, Pila<String> destino)` — validates no larger-on-smaller, increments `movimientos`. Returns `boolean`.
- `haGanado()` — checks if tower C has `objetivo` discs.
- `esPerfecto()` — `haGanado() && movimientos == 2^objetivo - 1`.
- `getMinMovimientos()` — returns `2^objetivo - 1` as `double`.
- `getDiscosDeTorre(Pila<String>)` — returns `String[]` of disks (bottom to top), null-padded.

### `PartidaDeHanoi` extends `Partida`

- Fields: `CiudadHanoi juego`, `int cantidadDiscos`.
- `iniciar()` — validates not started, creates `CiudadHanoi`, sets `EstadoDePartida.Iniciado`.
- `finalizar()` — validates is started, sets `EstadoDePartida.Creado`.
- `actualizarPuntaje(int puntos)` — **protected**, bridge for `ControllerHanoi`.
- `getJuego()` — returns `CiudadHanoi`.
- `getCantidadDeDiscos()` — returns disc count.

**Scoring formula** (in `ControllerHanoi`):
- Perfect (minimum moves): `150 × discos`
- Normal win: `100 × discos`

### `HanoiSolver<T>`

Recursive Hanoi solver with observer pattern.

- `resolverHanoi(int n, Pila<T> ori, Pila<T> aux, Pila<T> des)` — classic recursion.
- After each move, calls `observador.onMovimiento(paso)`.
- If observer returns `false`, sets `stop = true` and halts.

### `ObservadorHanoi` (interface)

```java
boolean onMovimiento(int paso);
```

Returns `true` to continue, `false` to stop the solver.

### `ControllerHanoi` implements `ObservadorHanoi`

MVC controller bridging `PartidaDeHanoi`/`CiudadHanoi` (model) and `VentanaPrincipalHanoi` (view).

- Directional move methods: `moverA_B()`, `moverA_C()`, `moverB_A()`, `moverB_C()`, `moverC_A()`, `moverC_B()`.
- Each calls `juego.mover()`, then `actualizarVista()`, then optionally `preguntarSiGano()`.
- `resolver()` — creates a new `HanoiSolver`, calls `resolverHanoi()` with towers from `partida.getJuego()`.
- `actualizarVista()` → `vista.actualizar(getEstado())`.
- `onMovimiento(int paso)` — implements `ObservadorHanoi`: updates view and asks "Continue?" via dialog.
- `getEstado()` — creates immutable `EstadoHanoi` snapshot from current towers.

### `VentanaPrincipalHanoi` (JFrame)

Swing UI with three `JTable`s for towers, directional move buttons, disc count combo (3-10), Init/Reset/Solve buttons.

- `actualizar(EstadoHanoi)` — refreshes all three tower tables and move count labels.
- `presentarTorre(JTable, String[])` — fills table model from disk array.
- `preguntarContinuar(int paso)` — `JOptionPane` Yes/No dialog.
- `mostrarVictoria()` / `mostrarVictoriaPerfecta()` — victory dialogs.
- `iniciar()` creates a new `PartidaDeHanoi` and `ControllerHanoi` on each init.

### Game Flow (Hanoi City)

```
1. Player selects disc count (3-10) and clicks "iniciar".
2. Controller creates PartidaDeHanoi + CiudadHanoi.
3. Player moves discs via directional buttons → controller.moverX_Y().
4. After moves to tower C: controller.preguntarSiGano() → victoria dialog.
5. "resolver" → HanoiSolver.resolverHanoi() with ControllerHanoi as observer.
   → Each step: controller.onMovimiento() → vista.actualizar() + continue dialog.
6. Win scoring: perfect = 150×discs, normal = 100×discs.
```

---

## Module: `estructuras/` — Custom Data Structures Library

Academic implementations from scratch. Two API styles per structure: a full Java Collections-compatible version and a simpler Spanish-named version.

### `nodos/`

| Class | Extends | Key |
|---|---|---|
| `Nodo<T>` (abstract) | — | `dato`, `tieneDato()`, `getDato()`/`setDato()` |
| `NodoSimplementeEnlazado<T>` | `Nodo<T>` | Adds `siguiente` pointer |
| `NodoDoblementeEnlazado<T>` | `NodoSimplementeEnlazado<T>` | Adds `anterior` pointer |

### `vector/`

**`Vector<T>`** — 1-based indexed, auto-expanding array.

- `agregar(int posicion, T dato)`, `obtener(int posicion)`, `remover(int posicion)` — 1-indexed.
- `agregar(T dato)` — appends or doubles capacity.
- `remove(T dato)`, `removeFirst(T dato)` — remove by value.
- `contains(T)`, `getCantidadDeDatos()`, `getLongitud()`.
- `implements Iterable<T>` via inner `VectorIterator`.

### `listas/`

| Class | Extends/Implements | Notes |
|---|---|---|
| `ListaSimplementeEnlazada<T>` | `implements List<T>` | Full List impl, cursor methods, `addSorted()`, `ListIterator` |
| `ListaDoblementeEnlazada<T>` | `implements List<T>` | Head+tail, O(1) tail access, `addSorted()` |
| `ListaConCursor<T>` | standalone | 1-based, Spanish API: `agregar`, `remover`, `obtener`, `existe`, `contarOcurrencias`, cursor traversal |
| `ListaCircularSimplementeEnlazada<T>` | extends `ListaSimplementeEnlazada<T>` | Override `add`, `remove`, `iterator` for circular links |
| `ListaCircularDoblementeEnlazada<T>` | extends `ListaDoblementeEnlazada<T>` | `cerrarCirculo()` helper after mutations |

### `pilas/`

| Class | Implements | Key Methods |
|---|---|---|
| `Pila<T>` | `implements Deque<T>` | `push`, `pop`, `peek`, `addFirst`, `addLast`, full Deque |
| `PilaBasica<T>` | standalone | `apilar(T)`, `apilar(List<T>)`, `desapilar()`, `obtener()`, `contarElementos()` |

### `cola/`

| Class | Implements | Key Methods |
|---|---|---|
| `Cola<T>` | `implements Queue<T>` | `offer`, `add`, `poll`, `remove`, `peek`, `element`, full Collection |
| `ColaBasica<T>` | standalone | `acolar`, `acolarAll`, `desacolar`, `obtener`, `contarElementos` |
| `ColaConPrioridad<E>` | standalone | `enqueue(E, int prioridad)`, `dequeue()`, `peek()`, `isEmpty()`. Lower index = higher priority. |

### `conjuntos/`

| Class | Implements | Key Methods |
|---|---|---|
| `Conjunto<E>` | `implements Set<E>` | Full Set impl backed by `ArrayList<E>` |
| `ConjuntoBasico<E>` | standalone | `agregar`, `quitar`, `union`, `interseccion`, `diferencia`, `elementos` |

### `hashing/`

**`HashTable`** — String-only hash table (no generics). No collision handling (overwrites).

- `put(String key, String value)`, `get(String key)`, `displayTable()`
- Hash: `Math.abs(key.hashCode() % capacity)`

### `arboles/`

| Class | Description |
|---|---|
| `ArbolBinarioDeBusqueda<T extends Comparable<T>>` | BST. `insertar`, `buscar`, `eliminar` (3-case), `inorden`/`preorden`/`postorden`, `contarCantidadDeHojas`, `contarCantidadDeNodosConDosHijos`, `calcularAltura`. Uses `ListaConCursor`. |
| `ArbolAVL<T extends Comparable<T>>` | Self-balancing BST extending `ArbolBinarioDeBusqueda`. Overrides `insertar`/`eliminar` with 4 rotation cases (LL, RR, LR, RL). |
| `ArbolBinarioHeap<E>` | Binary heap (min/max via `Comparator<E>`). Array-backed. `insert` with sift-up, `extract` with sift-down, `peek`, `isEmpty`, `size`. Auto-resizes. |
| `ArbolBinario` | **Actually a B-tree** (misleading name). `insertar(int clave)`, `imprimir()`. Uses `NodoB` with split logic. |

### `grafos/`

**`Grafo<T, U>`** — Directed weighted graph using adjacency lists (`Vertice<T,U>` + `Arista<T,U>`).

**Mutation:** `agregarVertice(T)`, `agregarArista(T origen, T destino, U peso)`

**Query:** `getVertices()`, `getVertice(T)`, `existeVertice(T)`, `getAdyacentes(T)`, `imprimirGrafo()`

**Traversals:** `recorridoAnchura(T)`, `recorridoProfundidad(T)`

**Algorithms built into `Grafo`:**
- `tieneCiclo()`
- `getPuntosArticulacion()` (Tarjan's)
- `getComponentesFuertementeConexas()` (Kosaraju's)
- `recorridoTopologicoDFS()`, `recorridoTopologicoBFS()` (Kahn's)
- `caminoMinimoBFS(T, T)`
- `dijkstra(T, T)`

**Static factory:** `Grafo.build(String vertices, String aristas)` — parses literal strings.

**Standalone algorithm classes in `grafos/algoritmos/`:**

| Class | Algorithm | Input | Method |
|---|---|---|---|
| `Kruskal` | MST | adj. matrix, V count | `kruskalMST(int[][], int)` |
| `Edge` | Kruskal helper | — | `Comparable<Edge>` by weight |
| `Prim` | MST | adj. matrix, V count | `primMST(int[][], int)` |
| `WarshallAlgorithm` | Transitive closure | adj. matrix | `warshall(int[][])` |
| `AlgoritmoFloydMarshall` | All-pairs shortest paths | adj. matrix | `floydWarshall(int[][])` |
| `FordFulkerson` | Maximum flow | adj. matrix, source, sink | `fordFulkerson(int[][], int, int)` |
| `CicloHamiltoniano` | Hamiltonian cycle | adj. matrix | `buscarCicloHamiltoniano()` |

---

## Module: `utils/` — Utilities

### `ValidacionesUtiles` (static methods, all throw `RuntimeException`)

| Method | Validates |
|---|---|
| `validarMayorACero(double, String)` | value > 0 |
| `validarMayorAUno(double, String)` | value > 1 |
| `validarMayorOIgualACero(double, String)` | value >= 0 |
| `validarLongitudDeTexto(String, int desde, Integer hasta, String)` | string length in [desde, hasta] |
| `validarCaracteresAlfabeticos(String, String)` | matches `[a-zA-Z ]+` |
| `validarFalso(boolean, String)` | throws if true |
| `validarVerdadero(boolean, String)` | throws if false |
| `esDistintoDeNull(Object, String)` | not null |
| `validarRangoNumerico(int, int desde, int hasta, String)` | int in [desde, hasta] |
| `validarRango(double, double, double, String)` | double in range |
| `validarRangoDeEnum(E, E...)` | enum value in allowed set |

**Used pervasively** — every model, controller, and data structure class uses this for precondition checks.

### `Teclado`

Static `Scanner` wrapper: `inicializar()`, `finalizar()`, `leerEntero()`. Used only in test/console code.

### `SistemaUtiles`

- `esperar(long milisegundos)` — wraps `Thread.sleep()`.
- `generarRutaAbsoluta(String rutaRelativa)` — resolves relative paths using classloader location.

### `NumerosUtiles`

- `toInt(Double)` — converts to int.
- `limitarRango(int min, int max, int valor)` — clamps to [min, max].

### `Bitmap`

Custom pixel canvas built on `BufferedImage`.

**Drawing primitives:** `drawPixel`, `drawLine` (Bresenham), `drawRectangle`, `drawCircle` (midpoint), `rellenar` (flood fill), `drawText`, `dibujarTablero`, `pasteBitmap`.

**3D helpers:** `drawLine3D`, `drawCube` (perspective projection with configurable `distanceToCamera`).

**File I/O:** `saveToFile(String path)`, `static loadFromFile(String path)`.

**Display:** `show()` — opens a JFrame with the image.

### `BitmapViewerConMenu`

Displays one or more `Bitmap` objects in a grid with optional button menu.

- `static showBitmaps(Bitmap...)` — display only.
- `static showBitmapsWithMenu(List<MenuAction>, Bitmap...)` — display + action buttons.
- Auto-refreshes every 500ms.

**`MenuAction`** (inner class): pairs a `String label` with a `Runnable`.

---

## Cross-Module Dependencies

```
modelos.Partida
  └── uses: EstadoDePartida (from ordenamientos), ValidacionesUtiles
  └── extended by: PartidaAiQuest, PartidaOrdenamientos, PartidaDeHanoi

modelos.Mochila
  └── uses: ListaSimplementeEnlazada (from estructuras)

modelos.Mapa
  └── uses: Celda<?>, ValidacionesUtiles

Juego.ciudades.ordenamientos
  └── uses: modelos.Partida, modelos.Jugador, modelos.Elemento (via Caja), ValidacionesUtiles, Bitmap

Juego.ciudades.reinas
  └── standalone (does NOT extend Partida — integration gap)

Juego.ciudades.torresDeHanoi
  └── uses: modelos.Partida, modelos.Jugador, EstadoDePartida (from ordenamientos), ValidacionesUtiles
  └── has own Pila<Nodo> (NOT estructuras.pilas.Pila)

Juego.PartidaAiQuest
  └── composes: PartidaOrdenamientos (more cities planned)
```

**Key observation:** All cities import `EstadoDePartida` from the `ordenamientos` package. This enum should probably live in `modelos/` or a shared `Juego` package, but currently it's cross-referenced.

---

## Integration Guide: Adding a New City

Based on the patterns established by the three existing cities:

### 1. Create your city package

```
src/Juego/ciudades/tuCiudad/
```

### 2. Extend `Partida`

```java
package Juego.ciudades.tuCiudad;

import modelos.Partida;
import modelos.Jugador;
import Juego.ciudades.ordenamientos.EstadoDePartida;

public class PartidaTuCiudad extends Partida {
    private TuLogica logica;

    public PartidaTuCiudad(String nombre, Jugador combatiente, /* params */) {
        super(nombre, combatiente);
        // init fields
    }

    @Override
    public void iniciar() {
        ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya fue iniciada");
        setEstado(EstadoDePartida.Iniciado);
        // initialize game logic
    }

    @Override
    public void finalizar() {
        ValidacionesUtiles.validarVerdadero(estaIniciada(), "La partida no está iniciada");
        setEstado(EstadoDePartida.Creado);
    }
}
```

### 3. Create your game logic class

Separate from `Partida` (which is the session manager), similar to how `CiudadHanoi` is separate from `PartidaDeHanoi`, and `CiudadReinas` is separate from any Partida at all.

### 4. Create your UI

Use Swing (JFrame/JPanel). Follow the existing patterns:
- **Reinas:** `VentanaPrincipal` + `TableroPanel`, callback via `VictoriaListener`.
- **Hanoi:** MVC with `ControllerHanoi` as observer (`ObservadorHanoi`).

### 5. Register in `PartidaAiQuest`

In `PartidaAiQuest.inicializarCiudades()`, add:

```java
ciudades.add(new PartidaTuCiudad("CityName", getJugador(), /* params */));
```

### 6. Wire into `Main.java`

Currently `Main.java` only launches the Reinas UI. To support city navigation, `PartidaAiQuest` needs to be used as the orchestrator, and each city's victory callback should call `partida.avanzarCiudad()` + launch the next city's UI.

### 7. Add tests

Create `tests/tuCiudad/TuCiudadTest.java` following the JUnit 5 pattern in `tests/`.

### Contract checklist for a new city:

- [ ] Extends `modelos.Partida`
- [ ] Uses `EstadoDePartida.Iniciado` / `Creado` for state management
- [ ] Calls `ValidacionesUtiles` for preconditions (matches project style)
- [ ] Javadoc with PRE/POST on public methods
- [ ] Protected setters for fields that controller needs to update
- [ ] Victory callback mechanism (implement `VictoriaListener` or create your own observer)
- [ ] No hardcoded values — use constants/enums
- [ ] Spanish naming where consistent with existing code