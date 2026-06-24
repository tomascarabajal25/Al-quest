# TODO — Batalla (Ciudad 9)

Checklist breve para `src/juego/ciudades/batalla/`.

## ✅ Ya resuelto

- [x] `Defender` reduce el próximo daño recibido (no-op eliminado).
- [x] Estados activos con infraestructura de efectos y badges UI.
- [x] Números de daño/curación y animación de barras de vida.
- [x] Log de acciones visible (no solo último mensaje).

## 🔥 Prioridad alta (scope vigente de PLAN.md)

### Feature 1 — Resultado de batalla (overlay)
- [ ] Crear `ResultadoBatalla` (victoria, eliminados/totales, puntaje).
- [ ] Calcular resultado en `Batalla` (con puntaje por dificultad).
- [ ] Mostrar overlay de `Victoria/Derrota` en `BatallaUI` (~1.5s + volver al mapa).
- [ ] Integrar en `PartidaBatalla` y ajustar `finalizar()`.
- [ ] Reemplazar esperas bloqueantes de cierre por flujo UI correcto.

### Feature 2 — Habilidades por tipo de enemigo
- [ ] Extender `HabilidadEspecial` con `getNombreHabilidad()` (default).
- [ ] Implementar habilidades concretas por `TipoEnemigo`.
- [ ] Crear/ajustar estados necesarios para esas habilidades.
- [ ] Agregar `HabilidadFactory` (`TipoEnemigo -> HabilidadEspecial`).
- [ ] Usar factory en `ManagerBatalla.generarEnemigos()`.
- [ ] Mejorar AI enemiga para usar habilidad con heurística (no solo `Atacar`).
- [ ] Crear acción `UsarHabilidad` (`TipoAccion.HABILIDAD_ESPECIAL`).
- [ ] Exponer `getHabilidad()` en `Combatiente`.
- [ ] Agregar `HabilidadUi` con mensaje: `"{nombre} usó {nombreHabilidad}!"`.

### Feature 3 — Completar ciudad requiere 3 dificultades
- [ ] Guardar `dificultadesGanadas` en `PartidaBatalla`.
- [ ] Acumular puntaje por dificultad solo una vez.
- [ ] Ajustar `finalizar()` para otorgar puntaje total al completar 1+2+3.
- [ ] Mostrar progreso de dificultades en overlay de resultado.
- [ ] Persistir/restaurar progreso en guardado (`DatosGuardado` + `PartidaGeneral`).
- [ ] Mantener reingreso permitido tras una sola victoria.

### Refactor transversal — EDT safety (decidido)
- [ ] Localizar seguridad de hilo Swing dentro de `BatallaUI` (`runOnEdt(...)`).
- [ ] Hacer EDT-safe métodos mutantes de UI (`actualizarEstado`, `registrarAnimacion`, `setEstadoMenu`, `mostrarMenuPrincipal`, `mostrarIndicadorAccion`, `cerrar`).
- [ ] Mantener `Batalla` en thread de juego y `PartidaBatalla.finalizar()` en EDT.

## 🧪 Tests mínimos a cubrir

- [ ] `ResultadoBatalla`/overlay: victoria y derrota muestran datos correctos.
- [ ] `HabilidadEspecial`: nombre de habilidad + ejecución por tipo.
- [ ] AI enemiga: usa habilidad/defiende/ataca según estado.
- [ ] Progresión 3 dificultades: score acumulado correcto, sin duplicados.
- [ ] Persistencia de `dificultadesGanadas` en save/load.
- [ ] Threading UI: llamadas desde no-EDT no rompen Swing.

## 🗂 Backlog (opcional, fuera del plan actual)

- [ ] `Curar` + botón `CURAR` (con cap a vida máxima).
- [ ] Combos de acciones encadenadas.
- [ ] Sistema elemental.
- [ ] Menú de settings en batalla.
- [ ] Auto-target inteligente.
- [ ] Event bus completo para reemplazar `Thread.sleep`.
- [ ] Items/objetos durante combate.

## Orden sugerido de PRs

1. `feat/resultado-batalla`
2. `feat/habilidades-tipo-enemigo`
3. `feat/batalla-3-dificultades`
4. `refactor/batalla-ui-edt-safety`

## Decisiones abiertas

- [ ] `Potenciado`: ¿multiplicativo (+50%) o aditivo (+N)?
- [ ] ¿Habilidad especial del héroe ahora o solo enemigos?
- [ ] `CortePreciso (SAMURAI)`: ¿ignora solo armadura o también defensa activa?
- [ ] ¿Marcar dificultades ganadas en el selector (`"Fácil ✓"`)?

## Alcance

- Sin librerías nuevas.
- Sin cambios a scripts locales (`*.sh`).
- **Sí incluye** cambios acotados en persistencia/campaña para batalla: `DatosGuardado` y `PartidaGeneral` (solo progreso de dificultades de Ciudad 9).
