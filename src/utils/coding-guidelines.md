# UBASOFT Java Guidelines

## General Rules

- Use Java 21.
- Prioritize readability over optimization.
- Prefer maintainability over clever solutions.
- Follow Object-Oriented Design principles.
- One responsibility per method.
- Avoid duplicated code.
- Use meaningful names.
- Prefer explicit and easy-to-read code.
- Prefer modular solutions over large methods.
- Keep methods short and cohesive.

---

## Educational Rules

- Target audience: first-year Computer Science students.
- Prefer educational and explicit solutions.
- Explain the reasoning behind the solution when appropriate.
- Avoid advanced Java features unless explicitly requested.
- Prefer clarity over optimization.
- Respect the complexity level requested by the exercise.

---

## Exercise Constraints

- Always respect the constraints explicitly stated in the exercise.
- If an exercise requires Vector, do not replace it with List, ArrayList or arrays.
- If an exercise forbids Stream API, do not use Stream API.
- If an exercise forbids Map, do not use Map.
- If an exercise requests a Stream API solution, prefer streams.
- If an exercise requests a traditional solution, prefer loops.
- If an exercise requests recursion, do not replace it with loops.
- If an exercise requires a specific data structure, use that data structure.

---

## Data Structure Rules

- Respect the structures allowed by the exercise.
- Prefer Vector<T> over arrays ([]).
- Use arrays ([]) only when explicitly required by the exercise.
- When a collection of objects is needed, use Vector<T>.
- Never replace a Vector<T> with an array unless requested.
- Always use Stream API unless the exercise explicitly requires another approach.
- Do not use Map unless explicitly requested.
- Do not use external libraries unless explicitly requested.
- Show algorithmic complexity when relevant.

Preferred:

```java
private Vector<Empleado> empleados;
```

Avoid:

```java
private Empleado[] empleados;
```

---

## Stream API

- Stream API is allowed and encouraged when it improves readability and maintainability.
- Prefer Stream API for filtering, mapping, sorting and aggregation operations.
- Avoid overly complex stream chains.
- Prefer readable streams over clever streams.
- For educational solutions, provide a traditional loop-based alternative when useful.

Preferred:

```java
return empleados.stream()
        .filter(Empleado::estaActivo)
        .sorted(
                Comparator.comparing(
                        Empleado::getNombre))
        .toList();
```

---

## Validation Rules

- All validations must be centralized in ValidacionesUtiles.
- Reuse existing methods from ValidacionesUtiles whenever possible.
- Do not implement validation logic directly in business methods if an equivalent validation belongs in ValidacionesUtiles.
- If a required validation does not exist, create a new reusable validation method in ValidacionesUtiles.
- Keep validations consistent across the project.

Preferred:

```java
ValidacionesUtiles.esDistintoDeNull(
        empleado,
        "empleado");

ValidacionesUtiles.validarMayorACero(
        sueldo,
        "sueldo");
```

Avoid:

```java
if (empleado == null) {
    throw new RuntimeException();
}
```

---

## TDA Rules

- Include invariants when applicable.
- Include preconditions.
- Include postconditions.
- Use JavaDoc for all public methods.
- Validate all public method parameters.
- Protect internal state.
- Return defensive copies when necessary.
- Implement equals(), compareTo(), hashCode() and toString() when appropriate.
- Do not expose internal collections directly.
- Prefer encapsulation over direct access.

---

## Language Rules

- Use Spanish for JavaDoc.
- Use Spanish for comments.
- Use Spanish for variable names.
- Use Spanish for method names.
- Use Spanish for class names unless the exercise explicitly requires English.

---

## JavaDoc Rules

- All public methods must have JavaDoc.
- JavaDoc must describe the behavior, not the implementation.
- Include preconditions.
- Include postconditions.
- Include @param for every parameter.
- Include @return when applicable.
- Leave one blank line between logical sections.
- Use complete sentences.

Example:

```java
/**
 * Aprieta una tecla del teclado.
 *
 * Si la tecla corresponde al carácter SHIFT,
 * activa el modo mayúsculas para la próxima letra.
 *
 * Si la tecla corresponde a un carácter,
 * agrega dicho carácter al texto ingresado.
 *
 * Post:
 * - Incrementa la cantidad de veces presionada.
 * - Puede modificar el texto ingresado.
 *
 * @param letra letra a presionar
 */
public void apretarTecla(char letra) {

}
```

---

## Parameter Formatting

- If a method has one parameter, keep it on the same line.

Example:

```java
public void agregar(
        Empleado empleado) {

}
```

- If a method has two or more parameters, each parameter must be written on its own line.

Example:

```java
public void asignarEmpleadoAProyecto(
        Empleado empleado,
        Proyecto proyecto) {

}
```

---

## Control Structures

- Always use braces.
- Never omit braces.
- One condition per line when expressions become long.

Preferred:

```java
if ((empleado != null) &&
    (empleado.estaActivo()) &&
    (empleado.getSueldo() > 0)) {

}
```

Avoid:

```java
if (empleado != null)
    total++;
```

---

## Variable Rules

- One variable declaration per line.
- Never declare multiple variables on the same line.
- Use descriptive names.
- Avoid abbreviations unless they are universally accepted.

Preferred:

```java
double total = 0;
int cantidadDeEmpleados = 0;
```

Avoid:

```java
double total = 0, promedio = 0;
```

---

## Loop Rules

- Prefer foreach when the index is not required.
- Use indexed loops only when the position is needed.
- Avoid duplicated traversals when possible.
- Prefer modularization over deeply nested loops.

Preferred:

```java
for (Empleado empleado : empleados) {

}
```

---

## Class Structure

Use the following section order:

```java
// CONSTANTES
// ATRIBUTOS DE CLASE
// ATRIBUTOS
// CONSTRUCTORES
// METODOS DE CLASE
// METODOS GENERALES
// METODOS DE COMPORTAMIENTO
// GETTERS
// SETTERS
```

Keep the structure consistent across all TDAs.

---

## Testing Rules

- Use JUnit 5.
- Follow Arrange / Act / Assert.
- Create one test per behavior.
- Use descriptive test names.
- Document the purpose of each test.
- Validate both successful and error scenarios.
- Prefer assertEquals, assertTrue, assertFalse, assertThrows and assertNotNull when appropriate.

---

## Complexity Rules

- Mention algorithmic complexity when relevant.
- Prefer solutions with lower complexity when readability is not affected.
- Explicitly state the complexity of key methods.