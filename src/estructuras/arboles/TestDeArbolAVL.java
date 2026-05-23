package estructuras.arboles;

public class TestDeArbolAVL {

    public static void main(String[] args) {
        ArbolAVL<String> arbolAVL = new ArbolAVL<String>();

        // Insertar nodos en el árbol AVL
        arbolAVL.insertar( new String("10"));
        arbolAVL.insertar( new String("20"));
        arbolAVL.insertar( new String("30"));
        arbolAVL.insertar( new String("40"));
        arbolAVL.insertar( new String("50"));
        arbolAVL.insertar( new String("25"));

        // Recorridos
        System.out.print("Inorden: ");
        arbolAVL.inorden();
        System.out.println();

        System.out.print("Preorden: ");
        arbolAVL.preorden();
        System.out.println();

        System.out.print("Postorden: ");
        arbolAVL.postorden();
        System.out.println();
    }
}
