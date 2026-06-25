# PLAN — Victory/Defeat Modal & Enemy Type Abilities

Two self-contained features for Ciudad 9 (Batalla). Each can land in its own PR.

---

## Feature 1 — End-of-Battle Victory / Defeat Modal

### Problem

When the battle ends, `Batalla.empezar()` returns a `boolean` and `PartidaBatalla.iniciar()` immediately calls `finalizar()` → `ui.cerrar()`. The player never sees whether they won or lost; the window just disappears.

### Design

Draw a semi-transparent overlay on the existing `BattleCanvas` showing:

| Element         | Victoria ✅                     | Derrota ❌                        |
|-----------------|---------------------------------|-----------------------------------|
| Title           | `"¡VICTORIA!"`                  | `"DERROTA"`                       |
| Color accent    | Gold (`#FFD700`)                | Crimson (`#DC143C`)               |
| Stats line 1    | `"Enemigos derrotados: N/M"`    | `"Enemigos derrotados: N/M"`      |
| Stats line 2    | `"Puntaje obtenido: X"`        | `"Puntaje obtenido: 0"`          |
| Action          | `"Click para volver al mapa"`   | `"Click para volver al mapa"`     |

Puntaje por dificultad: `1 → 1000`, `2 → 5000`, `3 → 15000` (matches `idea.md`).

The overlay stays until the player clicks. No `JOptionPane`, no `Thread.sleep` — just a painted overlay consumed by a single mouse click, keeping the UI on the Swing thread.

### Implementation Steps

#### Step 1 — `ResultadoBatalla` record (model)

Create `model/ResultadoBatalla.java`:

```java
package juego.ciudades.batalla.model;

/**
 * Inmutable result snapshot passed from the battle loop to the UI overlay.
 */
public class ResultadoBatalla {

    private final boolean victoria;
    private final int enemigosEliminados;
    private final int enemigosTotales;
    private final int puntaje;

    public ResultadoBatalla(boolean victoria, int enemigosEliminados,
                            int enemigosTotales, int puntaje) {
        this.victoria = victoria;
        this.enemigosEliminados = enemigosEliminados;
        this.enemigosTotales = enemigosTotales;
        this.puntaje = puntaje;
    }

    public boolean esVictoria()        { return victoria; }
    public int getEnemigosEliminados() { return enemigosEliminados; }
    public int getEnemigosTotales()    { return enemigosTotales; }
    public int getPuntaje()            { return puntaje; }
}
```

#### Step 2 — Compute the result in `Batalla`

In `Batalla.empezar()`:

- Store `int totalEnemigos = enemigos.size()` before the loop.
- After the loop, build a `ResultadoBatalla`:
  ```java
  boolean victoria = heroe != null && heroe.estaVivo();
  int eliminados = totalEnemigos - enemigos.size();
  int puntaje = victoria ? puntajePorDificultad(dificultad) : 0;
  ResultadoBatalla resultado = new ResultadoBatalla(victoria, eliminados, totalEnemigos, puntaje);
  ```
- Change the return type from `boolean` to `ResultadoBatalla`.
- Add a private helper:
  ```java
  private static int puntajePorDificultad(int dificultad) {
      switch (dificultad) {
          case 1: return 1000;
          case 2: return 5000;
          case 3: return 15000;
          default: return 0;
      }
  }
  ```

#### Step 3 — Overlay rendering in `BatallaUI`

Add to `BatallaUI`:

- `private volatile ResultadoBatalla resultado;` — set after the battle loop ends.
- `public void mostrarResultado(ResultadoBatalla r)` — sets the field, disables menu input, registers a one-shot click listener that calls a `Runnable onResultadoCerrado` callback.
- In `BattleCanvas.paintComponent`, after all existing draws, if `resultado != null`, paint the overlay:
  1. Semi-transparent black backdrop (`new Color(0, 0, 0, 180)`).
  2. Centered rounded-rect panel (`PANEL_BG` style, ~350×200).
  3. Title text in gold/crimson with `FONT_MENU` scaled up (or a new `FONT_TITLE` at 28pt).
  4. Two stats lines in `PANEL_TEXT` with `FONT_DIALOG`.
  5. "Click para volver al mapa" blinking hint at the bottom.

#### Step 4 — Wire in `PartidaBatalla`

In `PartidaBatalla.iniciar()`:

```java
new Thread(() -> {
    ResultadoBatalla resultado = new Batalla(ui, turnos, enemigos, dificultad).empezar();
    SwingUtilities.invokeLater(() -> {
        ui.mostrarResultado(resultado, () -> {
            victoria = resultado.esVictoria();
            setPuntaje(resultado.getPuntaje());
            finalizar();
        });
    });
}, "batalla-game-loop").start();
```

This replaces the current direct `finalizar()` call after `empezar()`.

#### Step 5 — Update `PartidaBatalla.finalizar()`

`finalizar()` currently hardcodes `setPuntaje(victoria ? 100 : 0)`. Remove that line — score is now set by the callback above. Keep the rest (cerrar, stop music, notificar).

### Files touched

| File | Change |
|------|--------|
| `model/ResultadoBatalla.java` | **New** |
| `controller/Batalla.java` | Return `ResultadoBatalla`, compute score |
| `view/BatallaUI.java` | `mostrarResultado()`, overlay paint, click-to-dismiss |
| `controller/PartidaBatalla.java` | Wire callback, remove hardcoded score |

### Tests

- `ResultadoBatallaTest` — verify constructor & getters.
- `BatallaTest` — mock UI, run a minimal 1-enemy fight, assert `ResultadoBatalla.esVictoria() == true` and correct score.
- `BatallaDerrrotaTest` — hero with 1 HP vs strong enemy, assert `esVictoria() == false` and `puntaje == 0`.

---

## Feature 2 — Enemy Type Abilities

### Problem

`ManagerBatalla.generarEnemigos()` assigns special abilities **randomly** from a pool of 3 lambdas (`danioBonus`, `veneno`, `roboDeVida`), ignoring the enemy's `TipoEnemigo`. A NINJA should poison; a ROBOT should hit hard — the type should determine the ability.

Also, `elegirAccionEnemigo()` always pushes a single `Atacar`. Enemies never use their special ability.

### Design — Ability Table

Each `TipoEnemigo` maps to a **fixed** `HabilidadEspecial` with a name and behaviour:

| TipoEnemigo | Ability Name          | Effect                                                       |
|-------------|-----------------------|--------------------------------------------------------------|
| `NINJA`     | Golpe Venenoso        | Applies `ENVENENADO` state (3 dmg/turn, 3 turns) to target   |
| `SAMURAI`   | Corte Preciso         | Ignores target's armor entirely — deals raw `fuerza` damage  |
| `VIKINGO`   | Furia Nórdica         | Self-buff: `POTENCIADO` state (+50% fuerza, 2 turns)         |
| `CABALLERO` | Escudo Sagrado        | Self-buff: `DEFENDIENDO` state (2 stacks)                    |
| `BUFON`     | Truco Sucio           | Applies `SANGRADO` state (2 dmg/turn, 4 turns) to target     |
| `DUENDE`    | Robo de Vida          | Steals 4 HP from target, heals self                          |
| `ROBOT`     | Sobrecarga            | Applies `QUEMADO` state (5 dmg/turn, 2 turns) to target      |

### Implementation Steps

#### Step 1 — Add `getNombreHabilidad()` to `HabilidadEspecial`

In `HabilidadEspecial.java`:

```java
public interface HabilidadEspecial {
    void activar(Combatiente personaje, Combatiente objetivo);

    /**
     * Display name for the UI action log.
     * @return ability name, or "Habilidad" if not overridden
     */
    default String getNombreHabilidad() {
        return "Habilidad";
    }
}
```

This is a **default method** — all existing lambdas keep working without changes.

#### Step 2 — Create concrete `HabilidadEspecial` implementations per type

New package `model/habilidades/`:

| File | Class | Behaviour |
|------|-------|-----------|
| `GolpeVenenoso.java` | `GolpeVenenoso` | `objetivo.setEstado(new Envenenado(personaje, objetivo))` |
| `CortePreciso.java` | `CortePreciso` | `objetivo.setVida(max(0, vida - personaje.getFuerza()))` |
| `FuriaNordica.java` | `FuriaNordica` | `personaje.setEstado(new Potenciado(personaje))` |
| `EscudoSagrado.java` | `EscudoSagrado` | `personaje.setEstado(new Defendiendo(personaje, 2))` |
| `TrucoSucio.java` | `TrucoSucio` | `objetivo.setEstado(new Sangrando(personaje, objetivo))` |
| `RoboDeVida.java` | `RoboDeVida` | steal 4 HP (existing lambda, now a named class) |
| `Sobrecarga.java` | `Sobrecarga` | `objetivo.setEstado(new Quemado(personaje, objetivo))` |

Each implements `HabilidadEspecial` and overrides `getNombreHabilidad()`.

#### Step 3 — New `EstadoActivo` subclasses (if missing)

Some status effects already exist (`Defendiendo`). Others need new classes in `model/estados/`:

| File | Estado | Damage/turn | Turns | `getUi()` badge color |
|------|--------|-------------|-------|-----------------------|
| `Envenenado.java` | `ENVENENADO` | 3 | 3 | Purple `#8B00FF` |
| `Sangrando.java` | `SANGRADO` | 2 | 4 | Dark red `#8B0000` |
| `Quemado.java` | `QUEMADO` | 5 | 2 | Orange `#FF8C00` |
| `Potenciado.java` | `POTENCIADO` | — (buff) | 2 | Yellow `#FFD700` |

Each overrides `aplicar()` to tick damage or apply the buff, and `getUi()` for the badge.

The `Potenciado` state should temporarily increase `fuerza` by 50% on application and restore it when `terminado()`.

#### Step 4 — `HabilidadFactory` mapping `TipoEnemigo → HabilidadEspecial`

New `model/habilidades/HabilidadFactory.java`:

```java
package juego.ciudades.batalla.model.habilidades;

import juego.ciudades.batalla.model.TipoEnemigo;

public class HabilidadFactory {

	private HabilidadFactory() {
	}

	/**
	 * Returns the canonical special ability for the given enemy type.
	 */
	public static HabilidadEspecial crear(TipoEnemigo tipo) {
		switch (tipo) {
			case NINJA:
				return new GolpeVenenoso();
			case VIKINGO:
				return new CortePreciso();
			case MAGO:
				return new FuriaNordica();
			case CABALLERO:
				return new EscudoSagrado();
			case BUFON:
				return new TrucoSucio();
			case DUENDE:
				return new RoboDeVida();
			case ROBOT:
				return new Sobrecarga();
			default:
				return (p, o) -> {
				};
		}
	}
}
```

#### Step 5 — Update `ManagerBatalla.generarEnemigos()`

In `ManagerBatalla.generarEnemigos()`:

Replace the random ability assignment block (lines 49–73) with:

```java
HabilidadEspecial habilidad = conHabilidad
        ? HabilidadFactory.crear(tipo)
        : ninguna;
```

Remove the three lambda declarations (`danioBonus`, `veneno`, `roboDeVida`) and the `habilidades` array — they are superseded by `HabilidadFactory`.

#### Step 6 — Update `ManagerBatalla.elegirAccionEnemigo()`

In `elegirAccionEnemigo()`:

Replace the single `Atacar` with type-aware AI:

```java
public static Pila<Accion> elegirAccionEnemigo(Enemigo enemigo, Combatiente heroe) {
    Pila<Accion> acciones = new Pila<>();
    Random rand = new Random();

    // Use special ability ~30% of the time
    boolean usarHabilidad = rand.nextInt(100) < 30;

    if (usarHabilidad) {
        acciones.push(new UsarHabilidad(enemigo, heroe));
    } else {
        acciones.push(new Atacar(enemigo, heroe));
    }
    return acciones;
}
```

#### Step 7 — New `Accion` subclass: `UsarHabilidad`

Create `model/acciones/UsarHabilidad.java`:

```java
package juego.ciudades.batalla.model.acciones;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.view.accion.ActionUi;
import juego.ciudades.batalla.view.accion.HabilidadUi;

public class UsarHabilidad extends Accion {

    public UsarHabilidad(Combatiente combatiente, Combatiente objetivo) {
        super(combatiente, objetivo, TipoAccion.HABILIDAD_ESPECIAL);
    }

    @Override
    public void ejecutar() {
        combatiente.usarHabilidadEspecial(objetivo);
    }

    @Override
    public ActionUi getUi() {
        String nombre = combatiente.getHabilidad().getNombreHabilidad();
        return new HabilidadUi(combatiente.getNombre(), nombre);
    }
}
```

#### Step 8 — Add `getHabilidad()` getter to `Combatiente`

In `Combatiente.java` add:

```java
public HabilidadEspecial getHabilidad() { return habilidad; }
```

#### Step 9 — New `ActionUi`: `HabilidadUi`

Create `view/accion/HabilidadUi.java` following the same pattern as `AtacarUi` and `DefenderUi`:

```java
public class HabilidadUi implements ActionUi {
    // Message: "{nombre} usó {nombreHabilidad}!"
    // Animation: GlowAnimacion with a purple/ability-specific color
}
```

#### Step 10 — UI badge for active status effects

`BattleCanvas.drawEnemyStatus()` and `drawHeroStatus()` should render small colored circles (badges) next to the HP bar for each active `EstadoCombatiente` in the combatant's `getEstados()` map. The color comes from `EstadoActivo.getUi().getBadgeColor()`.

### Files touched

| File | Change |
|------|--------|
| `model/HabilidadEspecial.java` | Add `default getNombreHabilidad()` |
| `model/Combatiente.java` | Add `getHabilidad()` getter |
| `model/habilidades/*.java` | **New** — 7 ability classes + `HabilidadFactory` |
| `model/estados/Envenenado.java` | **New** |
| `model/estados/Sangrando.java` | **New** |
| `model/estados/Quemado.java` | **New** |
| `model/estados/Potenciado.java` | **New** |
| `model/acciones/UsarHabilidad.java` | **New** |
| `controller/ManagerBatalla.java` | Use `HabilidadFactory`, update AI |
| `view/accion/HabilidadUi.java` | **New** |
| `view/BatallaUI.java` | Status badges next to HP bars |

### Tests

- `HabilidadFactoryTest` — assert each `TipoEnemigo` maps to the expected class.
- `GolpeVenenosoTest` — apply to a combatant, tick 3 turns, verify 9 total damage.
- `CortePrecisoTest` — verify armor is ignored (combatant with 10 armor takes full `fuerza`).
- `PotenciadoTest` — verify fuerza increases for 2 turns, then reverts.
- `ElegirAccionEnemigoTest` — over many calls, verify `UsarHabilidad` appears ~30% of the time.

---

## Feature 3 — City Completion Requires All 3 Difficulties

### Problem

Today, beating batalla **once** at any difficulty sets `puntaje > 0`, which triggers `alTerminarCiudad()` → `nodo.setCompletada(true)`. The city is marked done and the next city in the graph unlocks. But batalla's spec defines 3 difficulty tiers (Fácil / Normal / Difícil) — the city should only count as completed when the player has won **all three**.

### Design Principle — Keep it Local

The completion chain in `PartidaGeneral.alTerminarCiudad()` is simple and correct: it checks `puntaje > 0` to decide whether to flip `completada`. **We don't touch that logic.** Instead, `PartidaBatalla` itself controls when it reports a positive score:

- It tracks a `Set<Integer> dificultadesGanadas` (values 1, 2, 3).
- Each victory adds the difficulty to the set and accumulates partial score.
- `getPuntaje()` only returns a positive value once the set contains all three.
- Until then, `alTerminarCiudad` sees `puntaje == 0` and leaves `completada = false`.

The player can re-enter the city freely (it stays "accessible" but not "completed"), pick a different difficulty, and progress is preserved across re-entries because the `PartidaBatalla` instance lives for the entire session inside `construirGrafo()`.

### Score Accumulation

| Difficulty | Score on win |
|------------|-------------|
| 1 (Fácil)  | 1000        |
| 2 (Normal) | 5000        |
| 3 (Difícil)| 15000       |

Replaying a difficulty you already won: allowed (for fun), but the score for that tier is **not** added again. Total reported score = sum of each unique difficulty won (max 21000).

### Implementation Steps

#### Step 1 — Add `dificultadesGanadas` to `PartidaBatalla`

In `controller/PartidaBatalla.java`:

```java
import java.util.HashSet;
import java.util.Set;

public class PartidaBatalla extends Partida {

    private BatallaUI ui;
    private boolean victoria;
    private final Set<Integer> dificultadesGanadas = new HashSet<>();
    private int puntajeAcumulado = 0;

    // ...
}
```

#### Step 2 — Update `iniciar()` to accumulate on victory

After `Batalla.empezar()` returns, instead of immediately calling `finalizar()`:

```java
new Thread(() -> {
    ResultadoBatalla resultado = new Batalla(ui, turnos, enemigos, dificultad).empezar();
    SwingUtilities.invokeLater(() -> {
        if (resultado.esVictoria() && !dificultadesGanadas.contains(dificultad)) {
            dificultadesGanadas.add(dificultad);
            puntajeAcumulado += resultado.getPuntaje();
        }
        victoria = resultado.esVictoria();

        ui.mostrarResultado(resultado, dificultadesGanadas, () -> {
            finalizar();
        });
    });
}, "batalla-game-loop").start();
```

#### Step 3 — Override `getPuntaje()` semantics in `finalizar()`

In `finalizar()`, only report a positive score when all 3 are done:

```java
@Override
public void finalizar() {
    boolean todasCompletadas = dificultadesGanadas.size() == 3;
    setPuntaje(todasCompletadas ? puntajeAcumulado : 0);

    if (ui != null) ui.cerrar();
    setEstado(EstadoDePartida.Creado);
    if (this.sonido != null) {
        this.sonido.stopMusica();
        this.sonido.playMusica(ConstantesSonido.GLOBAL_AVENTURA);
    }
    notificarFinalizacion();
}
```

This is the key trick: `alTerminarCiudad` checks `getPuntaje() > 0` to decide `setCompletada(true)`. By returning 0 until all 3 difficulties are won, the existing chain works untouched.

#### Step 4 — Show progress on the result overlay

Update `BatallaUI.mostrarResultado()` (from Feature 1) to also receive the `Set<Integer> dificultadesGanadas` and render a progress indicator:

```
Progreso: ★ ☆ ★     (1/3 completadas)
           Fácil  Normal  Difícil
```

Stars are filled (★) for completed difficulties, empty (☆) for pending. This gives the player clear feedback on what's left.

#### Step 5 — Persist `dificultadesGanadas` across saves

The `PartidaBatalla` instance lives in memory for the whole session, so in-session re-entries are fine. But if the player quits and reloads, the progress is lost.

**5a. Add a field to `DatosGuardado`:**

```java
/** Dificultades ganadas en la ciudad de batalla (1, 2, 3). Null for old saves. */
private Vector<Integer> dificultadesBatallaGanadas;
```

Add a getter:

```java
public Vector<Integer> getDificultadesBatallaGanadas() {
    return dificultadesBatallaGanadas;
}
```

Update the constructor to accept the new parameter. Old saves loaded by Gson will have `null` for this field (Gson handles missing fields gracefully), so all consumers must null-check.

**5b. Update `PartidaGeneral.generarDatosGuardado()`:**

`PartidaGeneral` needs access to batalla's difficulty progress. Since it already holds the `NodoCiudad` for city 9 in the graph, it can cast:

```java
// In generarDatosGuardado():
NodoCiudad nodoBatalla = mapaMundi.obtenerCiudad(9);
Vector<Integer> difsBatalla = new Vector<>();
if (nodoBatalla != null && nodoBatalla.getPartidaAsociada() instanceof PartidaBatalla) {
    PartidaBatalla pb = (PartidaBatalla) nodoBatalla.getPartidaAsociada();
    difsBatalla.addAll(pb.getDificultadesGanadas());
}
```

Pass `difsBatalla` to the `DatosGuardado` constructor.

**5c. Update `PartidaGeneral.aplicarDatosGuardado()`:**

```java
if (datos.getDificultadesBatallaGanadas() != null) {
    NodoCiudad nodoBatalla = mapaMundi.obtenerCiudad(9);
    if (nodoBatalla != null && nodoBatalla.getPartidaAsociada() instanceof PartidaBatalla) {
        PartidaBatalla pb = (PartidaBatalla) nodoBatalla.getPartidaAsociada();
        pb.restaurarDificultades(datos.getDificultadesBatallaGanadas());
    }
}
```

**5d. Add restore method to `PartidaBatalla`:**

```java
/**
 * Restores difficulty progress from a save file.
 * @param dificultades previously won difficulties
 */
public void restaurarDificultades(Vector<Integer> dificultades) {
    for (int d : dificultades) {
        if (d >= 1 && d <= 3 && !dificultadesGanadas.contains(d)) {
            dificultadesGanadas.add(d);
            puntajeAcumulado += puntajePorDificultad(d);
        }
    }
}

private static int puntajePorDificultad(int dificultad) {
    switch (dificultad) {
        case 1: return 1000;
        case 2: return 5000;
        case 3: return 15000;
        default: return 0;
    }
}
```

#### Step 6 — Allow re-entry after a single win

Currently `alTerminarCiudad` does nothing special for re-entry — `entrarACiudad` just checks `esCiudadAccesible`, which is always true for a lone city or one whose predecessor is completed. The player can always re-enter batalla and pick a new difficulty. No changes needed here.

### Files touched

| File | Change |
|------|--------|
| `controller/PartidaBatalla.java` | `dificultadesGanadas` set, accumulate score, conditional `setPuntaje`, `restaurarDificultades()`, `getDificultadesGanadas()` |
| `view/BatallaUI.java` | Difficulty progress stars on the result overlay |
| `persistencia/DatosGuardado.java` | New `dificultadesBatallaGanadas` field + getter |
| `modelos/PartidaGeneral.java` | Save/load batalla difficulty progress in `generarDatosGuardado()` / `aplicarDatosGuardado()` |

### Tests

- `PartidaBatallaCompletionTest` — win difficulty 1 → `getPuntaje() == 0`, win 2 → still 0, win 3 → `getPuntaje() == 21000`.
- `PartidaBatallaReplayTest` — win difficulty 1 twice → score only counted once (1000, not 2000).
- `DatosGuardadoBatallaTest` — serialize with `dificultadesBatallaGanadas = [1, 3]`, deserialize, verify field. Also test null for old saves.
- `RestaurarDificultadesTest` — call `restaurarDificultades([1, 2])`, then win difficulty 3 in-game → `getPuntaje() == 21000`.

---

## Cross-cutting Refactor — Swing EDT Safety (Cleaner Option)

### Problem

`Batalla` runs in a background thread (good), but still invokes several `BatallaUI` methods directly. Swing requires all UI mutation on the Event Dispatch Thread (EDT); otherwise we get flaky repaint bugs and race conditions.

### Design Principle

Keep responsibilities explicit:

- `Batalla` thread: turn loop, game rules, action resolution.
- `BatallaUI`: internally marshals every Swing mutation to EDT.

This avoids scattering `SwingUtilities.invokeLater/invokeAndWait` throughout controller code.

### Implementation Steps

1. **Add an EDT helper in `BatallaUI`:**
   - `private void runOnEdt(Runnable r)`
   - If already on EDT, run immediately; otherwise `SwingUtilities.invokeLater(r)`.

2. **Make `BatallaUI` mutating methods EDT-safe internally:**
   - `actualizarEstado(String)`
   - `actualizarEstado(String, Combatiente)`
   - `registrarAnimacion(Animacion)`
   - `setEstadoMenu(EstadoMenu)`
   - `mostrarMenuPrincipal()`
   - `mostrarIndicadorAccion(int)`
   - `cerrar()`

3. **Keep `solicitarAccion()` contract explicit:**
   - It may block waiting for queue input (off-EDT is fine).
   - Any Swing state prep needed before waiting must happen via EDT-safe methods from step 2.

4. **Keep orchestration as-is in `PartidaBatalla`:**
   - `Batalla.empezar()` remains on background thread.
   - `finalizar()` remains scheduled on EDT (`SwingUtilities.invokeLater(this::finalizar)`).

5. **Optional debug guardrails:**
   - Add lightweight thread assertions/logging in sensitive UI methods during development.

### Files touched

| File | Change |
|------|--------|
| `view/BatallaUI.java` | Add `runOnEdt(...)` helper and make UI mutation methods EDT-safe internally |
| `controller/Batalla.java` | No behavioral change required; keep controller UI calls simple |
| `controller/PartidaBatalla.java` | Keep existing background-thread start + EDT finalize pattern |

### Tests

- `BatallaUIThreadingTest` — invoke UI mutating methods from a non-EDT thread and verify no exception + eventual state update.
- Integration sanity test — run `Batalla.empezar()` and confirm UI continues repainting/responding while game thread advances turns.

## Suggested PR Order

1. **`feat/resultado-batalla`** — Feature 1 (modal overlay). Self-contained, no model changes outside `batalla/`.
2. **`feat/habilidades-tipo-enemigo`** — Feature 2 (enemy abilities). Builds on existing `EstadoActivo` infrastructure. Can land independently of Feature 1.
3. **`feat/batalla-3-dificultades`** — Feature 3 (multi-difficulty completion). Depends on Feature 1 (uses `ResultadoBatalla` and the overlay to show progress). Touches `DatosGuardado` and `PartidaGeneral` — smallest possible footprint outside `batalla/`.

## Open Questions

- ✅ **Decided:** EDT safety will be localized in `BatallaUI` (internal `runOnEdt(...)` in mutating UI methods). `Batalla` remains a background-thread game loop and `PartidaBatalla.finalizar()` stays on EDT.
- Should the `Potenciado` buff be a flat `+50%` multiplicative or additive (e.g., `+5 fuerza`)? Multiplicative feels more impactful for strong enemies but could one-shot the hero on difficulty 3.
- Should the hero also be able to use `UsarHabilidad` via the menu, or is this enemies-only for now? (The TODO already tracks a "HABILIDAD" button separately.)
- Should `CortePreciso` (SAMURAI) also bypass the `DEFENDIENDO` damage reduction, or just armor?
- Should the difficulty selector (`pedirDificultad`) visually indicate which difficulties are already won (e.g., `"1 - Fácil ✓"`)? Easy UX win — just read `dificultadesGanadas` when building the `JOptionPane` options array.
