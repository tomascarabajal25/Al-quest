# Ciudad 9 – Batalla de Listas, Pilas y Colas

## 9.1. Descripción General

En esta ciudad, el jugador participa de un sistema de combate por turnos contra múltiples enemigos. El objetivo es aplicar correctamente las estructuras de datos Lista, Cola y Pila dentro de un contexto dinámico.

## 9.2. Objetivo

El jugador deberá derrotar a todos los enemigos presentes en el combate utilizando estrategias basadas en el uso eficiente de estructuras de datos.

## 9.3. Estructuras Obligatorias

### 9.3.1 Lista

Se deberá utilizar una estructura de tipo lista para almacenar los enemigos activos en combate.

Operaciones requeridas:

- Alta de enemigos
- Eliminación de enemigos derrotados
- Recorrido completo de la estructura

### 9.3.2 Cola

Se deberá implementar una cola para gestionar el orden de turnos de los participantes.

Reglas:

- El orden de atención será FIFO
- Todo personaje que finaliza su turno debe reinsertarse al final de la cola
- La cola debe incluir tanto al jugador como a los enemigos

### 9.3.3 Pila

Se deberá utilizar una pila para gestionar las acciones del jugador.

Reglas:

- Las acciones deben apilarse antes de ejecutarse
- La ejecución debe respetar el orden LIFO
- Se valorará la implementación de acciones encadenadas (combos)

## 9.4. Mecánica del Combate

- El combate se desarrolla por turnos
- En cada turno, el personaje puede realizar una acción
- Las acciones pueden incluir:
  - Ataque
  - Defensa
  - Uso de habilidad
- Las acciones del jugador deberán ser procesadas mediante una pila.

## 9.5. Condiciones de Finalización

El combate finaliza cuando:

- El jugador derrota a todos los enemigos (victoria)
- El jugador pierde todos sus puntos de vida (derrota)

## 9.6. Validaciones

El sistema deberá validar:

- Que no existan elementos nulos en las estructuras
- Que los enemigos eliminados sean removidos correctamente de la lista
- Que la cola mantenga el orden correcto de turnos
- Que la pila respete el orden LIFO

## 9.7. Criterios de Evaluación Específicos

Se evaluará:

- Correcta implementación de las estructuras
- Uso adecuado de las operaciones (push, pop, enqueue, dequeue, etc.)
- Claridad en la lógica del combate
- Separación entre lógica y presentación
