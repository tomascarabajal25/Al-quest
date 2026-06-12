package utils.bitmap;

import utils.bitmap.BitmapViewerConMenu.MenuAction;

import java.util.ArrayList;
import java.util.List;

/**
 * TDA Test para BitmapViewerConMenu.
 *
 * Cubre:
 *   - Constructor con lista de bitmaps válida (con y sin acciones)
 *   - showBitmaps (factory sin menú)
 *   - showBitmapsWithMenu (factory con acciones)
 *   - setMenuActions: reemplazo, lista vacía y null
 *   - MenuAction: getLabel / getRunnable / ejecución del runnable
 *   - close(): cierra el frame sin excepción
 *   - Precondiciones: bitmaps null o lista vacía deben lanzar excepción
 */
public class TestBitmapViewerConMenu {

    // ------------------------------------------------------------------ //
    //  Helpers
    // ------------------------------------------------------------------ //

    /** Crea un Bitmap de N×N píxeles negro (válido para instanciar el viewer). */
    private static Bitmap crearBitmapValido(int lado) {
        // Se asume que Bitmap expone un constructor Bitmap(int ancho, int alto)
        // o similar; ajustar según la firma real de la clase.
        return new Bitmap(lado, lado);
    }

    /** Imprime resultado de un caso de prueba. */
    private static void reportar(String nombre, boolean ok) {
        System.out.println((ok ? "[OK]  " : "[FAIL]") + " " + nombre);
    }

    // ------------------------------------------------------------------ //
    //  Test: MenuAction
    // ------------------------------------------------------------------ //

    /** getLabel devuelve la etiqueta con la que fue creada la acción. */
    private static void testMenuActionGetLabel() {
        MenuAction accion = new MenuAction("Exportar", () -> {});
        reportar("MenuAction.getLabel devuelve la etiqueta correcta",
                "Exportar".equals(accion.getLabel()));
    }

    /** getRunnable devuelve un Runnable no null. */
    private static void testMenuActionGetRunnableNoNull() {
        MenuAction accion = new MenuAction("Acción", () -> {});
        reportar("MenuAction.getRunnable no es null",
                accion.getRunnable() != null);
    }

    /** El Runnable asociado a MenuAction se ejecuta sin excepción. */
    private static void testMenuActionRunnableEjecuta() {
        boolean[] ejecutado = {false};
        MenuAction accion = new MenuAction("Click", () -> ejecutado[0] = true);
        accion.getRunnable().run();
        reportar("MenuAction: el runnable se ejecuta correctamente",
                ejecutado[0]);
    }

    // ------------------------------------------------------------------ //
    //  Test: Constructor directo
    // ------------------------------------------------------------------ //

    /** El constructor acepta una lista de bitmaps válida sin menú (null en acciones). */
    private static void testConstructorBitmapsValidosSinAcciones() {
        boolean ok = false;
        try {
            Bitmap bmp = crearBitmapValido(10);
            List<Bitmap> lista = List.of(bmp);
            BitmapViewerConMenu viewer = new BitmapViewerConMenu(lista, null);
            viewer.close();
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("Constructor: bitmaps válidos, acciones null → sin excepción", ok);
    }

    /** El constructor acepta una lista de bitmaps válida con acciones. */
    private static void testConstructorBitmapsValidosConAcciones() {
        boolean ok = false;
        try {
            Bitmap bmp = crearBitmapValido(10);
            List<MenuAction> acciones = List.of(new MenuAction("A1", () -> {}));
            BitmapViewerConMenu viewer = new BitmapViewerConMenu(List.of(bmp), acciones);
            viewer.close();
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("Constructor: bitmaps válidos con acciones → sin excepción", ok);
    }

    /** El constructor debe lanzar excepción si la lista de bitmaps es null. */
    private static void testConstructorBitmapsNull() {
        boolean lanzó = false;
        try {
            new BitmapViewerConMenu(null, List.of());
        } catch (Exception e) {
            lanzó = true;
        }
        reportar("Constructor: bitmaps null → lanza excepción", lanzó);
    }

    /** El constructor debe lanzar excepción si la lista de bitmaps está vacía. */
    private static void testConstructorBitmapsVacio() {
        boolean lanzó = false;
        try {
            new BitmapViewerConMenu(new ArrayList<>(), List.of());
        } catch (Exception e) {
            lanzó = true;
        }
        reportar("Constructor: lista de bitmaps vacía → lanza excepción", lanzó);
    }

    // ------------------------------------------------------------------ //
    //  Test: showBitmaps (factory sin menú)
    // ------------------------------------------------------------------ //

    /** showBitmaps crea el viewer sin acciones y sin excepción. */
    private static void testShowBitmaps() {
        boolean ok = false;
        try {
            Bitmap bmp = crearBitmapValido(10);
            BitmapViewerConMenu.showBitmaps(bmp);
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("showBitmaps: crea viewer sin acciones → sin excepción", ok);
    }

    /** showBitmaps acepta múltiples bitmaps. */
    private static void testShowBitmapsMultiples() {
        boolean ok = false;
        try {
            Bitmap b1 = crearBitmapValido(10);
            Bitmap b2 = crearBitmapValido(20);
            Bitmap b3 = crearBitmapValido(30);
            BitmapViewerConMenu.showBitmaps(b1, b2, b3);
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("showBitmaps: múltiples bitmaps → sin excepción", ok);
    }

    // ------------------------------------------------------------------ //
    //  Test: showBitmapsWithMenu (factory con menú)
    // ------------------------------------------------------------------ //

    /** showBitmapsWithMenu crea el viewer con acciones y sin excepción. */
    private static void testShowBitmapsWithMenu() {
        boolean ok = false;
        try {
            Bitmap bmp = crearBitmapValido(10);
            List<MenuAction> acciones = List.of(
                    new MenuAction("Guardar", () -> {}),
                    new MenuAction("Cancelar", () -> {})
            );
            BitmapViewerConMenu.showBitmapsWithMenu(acciones, bmp);
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("showBitmapsWithMenu: acciones + bitmap → sin excepción", ok);
    }

    /** showBitmapsWithMenu acepta lista de acciones vacía. */
    private static void testShowBitmapsWithMenuAccionesVacias() {
        boolean ok = false;
        try {
            Bitmap bmp = crearBitmapValido(10);
            BitmapViewerConMenu.showBitmapsWithMenu(List.of(), bmp);
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("showBitmapsWithMenu: lista de acciones vacía → sin excepción", ok);
    }

    // ------------------------------------------------------------------ //
    //  Test: setMenuActions
    // ------------------------------------------------------------------ //

    /** setMenuActions reemplaza las acciones existentes sin excepción. */
    private static void testSetMenuActionsReemplaza() {
        boolean ok = false;
        try {
            Bitmap bmp = crearBitmapValido(10);
            BitmapViewerConMenu viewer = new BitmapViewerConMenu(
                    List.of(bmp),
                    List.of(new MenuAction("Original", () -> {}))
            );
            List<MenuAction> nuevas = List.of(
                    new MenuAction("Nueva1", () -> {}),
                    new MenuAction("Nueva2", () -> {})
            );
            viewer.setMenuActions(nuevas);
            viewer.close();
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("setMenuActions: reemplaza acciones existentes → sin excepción", ok);
    }

    /** setMenuActions con lista vacía elimina todos los botones sin excepción. */
    private static void testSetMenuActionsListaVacia() {
        boolean ok = false;
        try {
            Bitmap bmp = crearBitmapValido(10);
            BitmapViewerConMenu viewer = new BitmapViewerConMenu(
                    List.of(bmp),
                    List.of(new MenuAction("Borrar", () -> {}))
            );
            viewer.setMenuActions(List.of());
            viewer.close();
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("setMenuActions: lista vacía → sin excepción", ok);
    }

    /** setMenuActions con null es equivalente a lista vacía y no lanza excepción. */
    private static void testSetMenuActionsNull() {
        boolean ok = false;
        try {
            Bitmap bmp = crearBitmapValido(10);
            BitmapViewerConMenu viewer = new BitmapViewerConMenu(List.of(bmp), null);
            viewer.setMenuActions(null);
            viewer.close();
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("setMenuActions: null → sin excepción (equivale a lista vacía)", ok);
    }

    // ------------------------------------------------------------------ //
    //  Test: close
    // ------------------------------------------------------------------ //

    /** close() no lanza excepción cuando el frame fue creado. */
    private static void testCloseNoLanzaExcepcion() {
        boolean ok = false;
        try {
            Bitmap bmp = crearBitmapValido(10);
            BitmapViewerConMenu viewer = new BitmapViewerConMenu(List.of(bmp), null);
            viewer.close();
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("close(): cierra el frame sin excepción", ok);
    }

    /** close() puede llamarse varias veces sin excepción (idempotencia). */
    private static void testCloseIdempotente() {
        boolean ok = false;
        try {
            Bitmap bmp = crearBitmapValido(10);
            BitmapViewerConMenu viewer = new BitmapViewerConMenu(List.of(bmp), null);
            viewer.close();
            viewer.close(); // segunda llamada
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("close(): llamada múltiple → sin excepción (idempotente)", ok);
    }

    // ------------------------------------------------------------------ //
    //  Punto de entrada
    // ------------------------------------------------------------------ //

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Tests BitmapViewerConMenu ===\n");

        // MenuAction
        System.out.println("-- MenuAction --");
        testMenuActionGetLabel();
        testMenuActionGetRunnableNoNull();
        testMenuActionRunnableEjecuta();

        // Constructor
        System.out.println("\n-- Constructor --");
        testConstructorBitmapsValidosSinAcciones();
        testConstructorBitmapsValidosConAcciones();
        testConstructorBitmapsNull();
        testConstructorBitmapsVacio();

        // Factories
        System.out.println("\n-- showBitmaps --");
        testShowBitmaps();
        testShowBitmapsMultiples();

        System.out.println("\n-- showBitmapsWithMenu --");
        testShowBitmapsWithMenu();
        testShowBitmapsWithMenuAccionesVacias();

        // setMenuActions
        System.out.println("\n-- setMenuActions --");
        testSetMenuActionsReemplaza();
        testSetMenuActionsListaVacia();
        testSetMenuActionsNull();

        // close
        System.out.println("\n-- close --");
        testCloseNoLanzaExcepcion();
        testCloseIdempotente();

        System.out.println("\n=== Fin de tests ===");

        // Pequeña pausa para que Swing tenga tiempo de procesar los eventos
        // pendientes antes de que el proceso termine.
        Thread.sleep(1000);
        System.exit(0);
    }
}
