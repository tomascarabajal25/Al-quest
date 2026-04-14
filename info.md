# Tener en cuenta durante el desarrollo

1. Para cada modificación hacer un pull request. Algún otro miembro del grupo debe aceptar los cambios antes de mergear.
2. Al crear un método explicar en el informe y también agregar su javadoc.
3. Crear constantes, enums, etc. Intentar no usar valores hardcodeados.

## Programación a alto nivel

1. Resolución del problema
2. Diseño
3. Abstracción de la realidad
4. Encapsulamiento
5. Convención de nomenclatura de la cátedra
6. Conjunto completo de operaciones
7. Pre y post condición. Comentarios
8. Objetos estáticos vs objetos dinámicos
9. Robustez
10. Desarrollo modularizado
11. Testeo. Main de prueba independiente
12. Desarrollo mantenible, extensible y optimizado
13. Campos estáticos
14. Sobrecarga de métodos
15. Herencia
16. Polimorfismo

## _Conventional Branch_ (nombres de las branch)

Es una especificación para nombrar ramas en Git de forma estructurada, legible tanto por humanos como por máquinas. Su objetivo es facilitar la comunicación, automatización y organización en equipos de desarrollo.

**Nombres de ramas orientados al propósito**:
Cada nombre de rama debe indicar claramente su propósito, para que cualquier desarrollador entienda “para qué es” sin tener que abrir muchas cosas.

## Especificaciones

### Prefijos de nombres de ramas

La convención sugiere usar este formato general:

```
<tipo>/<descripción>
```

Algunos de los prefijos propuestos:

-   `feat/`: para nuevas funcionalidades
    Ej.: `feat/add-login-page`
-   `fix/`: para correcciones de errores
    Ej.: `fix/fix-header-bug`
-   `hotfix/`: para arreglos urgentes
    Ej.: `hotfix/security-patch`
-   `chore/`: para tareas que no son código directamente, actualizaciones de dependencias, documentación, etc.
    Ej.: `chore/update-dependencies`

### Reglas básicas de formato

-   Usar **minúsculas**, números, guiones (`-`) y puntos (`.`). Evitar caracteres especiales, espacios o guiones bajos (`_`).
-   No poner guiones o puntos al principio ni al final, ni consecutivos. Ejemplo incorrecto: `feature/-new-login`, `release/v1.-2.0`, `feature/new--login`.
-   Mantener los nombres claros y concisos.
-   Si hay un número de ticket (de issue tracking), incluirlo. Ejemplo: `feature/issue-123-new-login`.

---

## _Conventional Commits_ (nombres de los commits)

Es una convención ligera para estructurar los mensajes de commit, facilitando la automatización de tareas como la generación de changelogs, la determinación de versiones y la integración con herramientas de CI/CD. Se alinea con [Semantic Versioning (SemVer)](https://semver.org/), permitiendo que el tipo de commit indique el impacto en la versión del software.

---

## Estructura del mensaje de commit

Cada mensaje debe seguir el siguiente formato:

```
<tipo>[alcance opcional]: <descripción>

[cuerpo opcional]

[pie opcional]
```

-   **Tipo**: Indica la naturaleza del cambio (obligatorio).
-   **Alcance**: Sección del código afectada (opcional).
-   **Descripción**: Resumen breve del cambio (obligatorio).
-   **Cuerpo**: Detalles adicionales sobre el cambio (opcional).
-   **Pie**: Notas adicionales, como referencias a problemas o cambios incompatibles (opcional).

---

## Tipos de commit recomendados

Algunos tipos comunes incluyen:

-   `feat`: Nueva funcionalidad
-   `fix`: Corrección de un error
-   `docs`: Cambios en la documentación
-   `style`: Formato, sin afectar el significado del código
-   `refactor`: Cambio en el código que no corrige errores ni añade funcionalidades
-   `test`: Añadir o corregir pruebas
-   `chore`: Tareas rutinarias y mantenimiento

Además, se puede usar un signo de exclamación (`!`) después del tipo o alcance para indicar un cambio que rompe la compatibilidad (breaking change).

---

Estas convenciones fueron sacadas de [conventional branch](https://conventional-branch.github.io/) y [conventional commits](https://www.conventionalcommits.org/en/v1.0.0/)
