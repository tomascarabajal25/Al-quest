# Ciudad Batalla

Debemos plantear una batalla entre el jugador y X enemigos por turnos
Los enemigos estaran en una lista, los turnos en una cola, y las acciones del jugador se apilan (pila) antes de ejecutarse

Tendra 3 dificultades:
- Facil - 1 enemigo - 1000 puntos
- Media - 3 enemigos - 5000 puntos
- Dificil - 5 enemigos - 15000 puntos

## Clases / Objetos  
- Enemigo (Por ahi herencia de jugador)
- Accion
- Objeto magico random (Durante una batalla hay probabilidades de que salga)
- Agregar alguna evolucion o algo por el estilo

### A resolver
- Formato pelea de pokemon clasico (Personaje vs enemigo/s enfrentados, turno x turno)
- herramientas especiales random que el jugador ira recolectando a lo largo de las distintas cuidades
- Contador de turnos (para envenenamiento por ejemplo)
- Manager para la batalla (hara los chequeos necesarios) --> evitamos el loop de juego
- Manejo de nuevas excepciones
