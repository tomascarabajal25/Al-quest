# Ciudad Batalla

Debemos plantear una batalla entre el combatiente y X enemigos por turnos
Los enemigos estaran en una lista, los turnos en una cola, y las acciones del combatiente se apilan (pila) antes de ejecutarse

Tendra 3 dificultades:
- Facil - 1 enemigo - 1000 puntos
- Media - 3 enemigos - 5000 puntos
- Dificil - 5 enemigos - 15000 puntos

## Clases / Objetos  
- Enemigo (Por ahi herencia de combatiente)
- Accion
- Objeto magico random (Durante una batalla hay probabilidades de que salga)
- Agregar alguna evolucion o algo por el estilo

### A resolver
- Formato pelea de pokemon clasico (Personaje vs enemigo/s enfrentados, turno x turno)
- herramientas especiales random que el combatiente ira recolectando a lo largo de las distintas cuidades
- Contador de turnos (para envenenamiento por ejemplo)
- Manager para la batalla (hara los chequeos necesarios) --> evitamos el loop de juego
- Manejo de nuevas excepciones

## Ideas
- BattleManager recibe personaje, pila de enemigos y UiManager -> primero lo hacemos por consola para debug y despues hacemos con bitmap y swing
- crear un sleep personalizado para dar tiempo a la ui a renderizar
- Para las acciones del personaje usaremos un map y estrategias (strategy pattern). Entonces el battle manager recibira el map de acciones, y la ui devuelve un string o algo asi que el la key de la accion -> el value sera la estrategia que se encarga de la logica de la accion
- Enemigos y sus acciones son generados de manera random