# TODO — Batalla (Ciudad 9)

Track the work proposed for `src/juego/ciudades/batalla/`. Check items as they land in their own PRs.

## Priority 1 — Finish what's half-built

- [ ] **`Defender` is a no-op.** `model/acciones/Defender.java:17` has `// TODO: implementar buff temporal de armadura`. Add a real effect: a "next incoming attack takes 50% damage" flag on `Combatiente` (e.g. `int reduccionDanioPendiente`) that `Atacar.ejecutar()` consumes and resets. Add a `Defender` button test.
- [ ] **`HabilidadEspecial` is dead code.** `ManagerBatalla` constructs three lambdas (danioBonus / veneno / roboDeVida) and assigns one to each enemy, but `Batalla.empezar()` never calls `Combatiente.usarHabilidadEspecial()`. Wire it up: new "HABILIDAD" button in the menu that pushes a `HabilidadEspecial` action (typed `TipoAccion.HABILIDAD_ESPECIAL`) into the Pila. Enemy AI uses its assigned ability roughly every 3 turns. Action log message format: `"{nombre} usó {nombreHabilidad}!"`. Add `getNombreHabilidad()` to `HabilidadEspecial` (functional interface change — add default method).
- [ ] **`TipoAccion.CURACION` exists but has no action.** Implement `model/acciones/Curar` with a "CURAR" button that heals 20–30% of max HP. Cap the hero's current HP at max HP (add a `getVidaMaxima` / `setVidaMaxima` to `Combatiente` and clamp in `setVida`).
- [ ] **End-of-battle overlay.** The JFrame just closes. Add a "Victoria / Derrota" screen drawn over the canvas for ~1.5s showing score earned (e.g. 100/200/300 by difficulty), enemies defeated count, and "Volver al mapa". Triggers a `JOptionPane` like `PartidaBusqueda` does, or a `Timer`+`repaint` overlay on the canvas. Replace the `Thread.sleep(ACTION_DELAY_MS)` hack with a proper queued effect on the swing thread.

## Priority 2 — Combat depth

- [ ] **Status effects via a `Cola<EfectoActivo>`.** Define `EfectoActivo` (tipo: VENENO, REGENERACION, PARALIZAR; turnos restantes; tick por turno). Each turn in `Batalla.empezar()` iterates the queue and applies/removes effects. New actions: `Envenenar`, `Regenerar`, `HabilidadParalizar`. UI badge on each combatiente showing active effects (rendered next to the HP bar). Uses the required `Cola<>` from the spec.
- [ ] **Combos / chained actions.** `solicitarAcciones` already allows the user to push multiple actions before `PASAR`. Add a combo bonus: if the same action is queued 2+ times, the second +30% damage, third +60%. Counter on UI showing combo level. Store state on the Pila contents (or wrap actions in `AccionConCombo` decorator).
- [ ] **Elemental/type system.** Map each `TipoEnemigo` to a damage multiplier table (NINJA→VENENO, ROBOT→FUEGO, etc.). Add `Atacar.elemental` and a multiplier lookup in `Atacar.ejecutar()`. UI shows a small icon for the hero's element next to name.
- [ ] **Enemy AI by state.** `ManagerBatalla.elegirAccionEnemigo` is hard-coded to one `Atacar`. Replace with a state-based AI: low HP → `Defender`/`Curar`; full HP → `Atacar`; with Habilidad → use every 3 turns. Heuristic lives in `ManagerBatalla` (not `Batalla`) so it's unit-testable.
- [ ] **Boss enemy.** Add `TipoEnemigo.JEFE` with 3× the normal HP/strength and a unique `HabilidadEspecial` (multi-target AOE). UI renders boss with a 2× sprite size and a "JEFE" tag. Spawn only at dificultad 3, 50% chance to replace the enemy pack.
- [ ] **Items / potions mid-battle.** Add `Item` (PocionVida, PocionDanio) usable once per battle via a fourth button "OBJETO". Items are dropped by defeated enemies with probability `1/N`. Implementation: a small `inventarioCombate` list on the player session.

## Priority 3 — UI reorganization & reactivity

- [ ] **Refactor `BatallaUI` — split into 3 classes.** Currently 885 lines, 1 file, 4 responsibilities. Proposed split:
  - `BatallaUI` — window + wiring (~150 lines)
  - `BattleCanvas` — only the paintComponent (~300 lines, moved out of inner class)
  - `BattleInput` — mouse handling, command queue, menu state (~150 lines)
  - `BattleEffects` — flash/shake/HP interpolation (~150 lines)
  Makes it possible to mock the canvas for testing and shortens every file to <300 lines.
- [ ] **Reactive event bus.** Replace the `BlockingQueue<String> colaComandos` and the `Thread.sleep` polling in `Batalla.empezar()` with an event-driven model. Concrete plan:
  - Define `interface EventoBatalla { void aceptar(VisitanteEvento v); }` with `AtaqueEvent`, `DefensaEvent`, `CurarEvent`, `HabilidadEvent`, `PasarEvent`, `TickAnimacionEvent`.
  - `Batalla` posts events to a `Cola<EventoBatalla>`. A `HiloAnimacion` consumes them and triggers `BatallaUI.reproducir(...)` (flash, shake, HP interpolation, sound) via `SwingUtilities.invokeLater`. When animations finish, the consumer pushes back a "listo" event.
  - Kills all `Thread.sleep(N)` calls and lets the UI run at its own pace.
  - Side benefit: tests can drive `Batalla` synchronously without any Swing.
- [ ] **Damage numbers / health bar animations.** When HP drops, render a floating "-N" number that rises and fades (200ms) at the target's position. Regeneration renders a green "+N". Combo counter "x2!"/"x3!" pops on chained actions. All driven by the `HiloAnimacion` from the previous item.
- [ ] **Settings menu (gear icon, top-right).** Open a small dialog mid-battle to: toggle animations off, change text speed, abandon battle (returns to PartidaBatalla with puntaje 0). Better than the current "X cierra la ventana" implicit behavior.
- [ ] **Smarter enemy targeting (auto-select).** The UI's `enemigoActivoIdx` only changes when the user clicks the LUCHAR submenu. Default-select the lowest-HP enemy. On enemy death, the auto-select jumps to the next living one (`primerVivo()` already exists at `BatallaUI.java:355` but isn't called from `actualizarEstado`).
- [ ] **Live state in the action log.** The `actionLog` only shows the most recent message. Expose a scrollable log panel on the right side of the canvas (`JScrollPane` + `JList<String>` updated in `actualizarEstado`). Players can review what happened without taking screenshots.

## Priority 4 — Tests

- [ ] **Add to `tests/ciudades/testsCiudadDeBatalla/`:**
  - `EstadoCombateTest` — Defend halves next attack, Curar caps at maxHP, buff expires after 1 hit
  - `HabilidadEspecialTest` — both branches of `usarHabilidadEspecial` and the `getNombreHabilidad` default
  - `StatusEffectTest` — poison ticks 3× then expires; regen +5/turn
  - `EnemyAITest` — at HP<30% the enemy picks Defender; otherwise Atacar
  - `ComboTest` — 2 same attacks = +30% damage on the 2nd
  - `BatallaAsincronaTest` — using the new event bus, no `Thread.sleep`
- [ ] **Migrate the integration test from 7 files to use mocks for the UI.** Today `Batalla.empezar()` calls `ui.solicitarAcciones(...)` which blocks on a `BlockingQueue`. With the new event bus, `Batalla` can run in a JUnit test without a JFrame.

## Suggested PR order

1. (Refactor) Split `BatallaUI` into 4 files — pure refactor, no behavior change, easiest to review.
2. (Polish) Finish Defender, wire HabilidadEspecial, implement Curar.
3. (Polish) Auto-select + settings dialog. Small, isolated.
4. (Refactor) Event-bus refactor. The biggest behavioral change; land separately.
5. (Depth) Combat-depth features on top of the event bus.
6. (Depth) Boss + items, polish features.
7. (Polish) Animation polish + test coverage.

## Open questions (resolve before coding)

- Reactivity refactor (event bus) — same PR as features, or separate "refactor" PR first?
- `estructuras.cola.Cola` does not implement `BlockingQueue` — add `peekNoBloqueante` / `pollNoBloqueante` so the event bus can reuse the required `Cola<>`, or keep `java.util.concurrent.LinkedBlockingQueue` for events and `estructuras.cola.Cola` only for the turn queue?
- `HabilidadEspecial` change to add `getNombreHabilidad()` is a tiny breaking change for the 3 lambdas in `ManagerBatalla.java:50-57`. Trivial fix, just confirm we want the default method.

## Out of scope

- No changes to `persistencia/`, `DatosGuardado`, `PartidaGeneral`, or the world map. Battle is its own scope.
- No changes to `run_tests.sh` / `test.sh` — tracked separately.
- No new third-party libraries.
- No "campaign progression" / unlocks.
