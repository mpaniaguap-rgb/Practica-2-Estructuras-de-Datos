import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Programa de consola para interactuar con un árbol B+.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Árbol B+ ===");

        // Se pide el orden del árbol hasta recibir un valor válido (mínimo 3)
        int orden;
        do {
            orden = leerEntero("Ingrese el orden del árbol (mínimo 3): ");
        } while (orden < 3);

        Arbolbmas arbol = new Arbolbmas(orden);
        menu(arbol);

        System.out.println("¡Hasta luego!");
    }

    // Menú principal: se repite hasta que el usuario elija salir
    private static void menu(Arbolbmas arbol) {
        int opcion;
        do {
            System.out.println();
            System.out.println("------ MENÚ ------");
            System.out.println("1. Insertar clave y dato");
            System.out.println("2. Buscar clave");
            System.out.println("3. Eliminar clave");
            System.out.println("4. Recorrer rango");
            System.out.println("5. Mostrar árbol");
            System.out.println("0. Salir");
            opcion = leerEntero("Seleccione una opción: ");
            System.out.println();

            switch (opcion) {
                case 1 -> insertar(arbol);
                case 2 -> buscar(arbol);
                case 3 -> eliminar(arbol);
                case 4 -> recorrerRango(arbol);
                case 5 -> arbol.imprimirArbol();
                case 0 -> { }
                default -> System.out.println("Opción inválida, intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private static void insertar(Arbolbmas arbol) {
        int clave = leerEntero("Clave (número entero): ");
        System.out.print("Dato asociado: ");
        String dato = scanner.nextLine();

        if (arbol.insertar(clave, dato)) {
            System.out.println("Clave " + clave + " insertada correctamente.");
        } else {
            System.out.println("La clave " + clave + " ya existe en el árbol.");
        }
    }

    private static void buscar(Arbolbmas arbol) {
        int clave = leerEntero("Clave a buscar: ");
        String dato = arbol.obtenerDato(clave);

        if (dato != null) {
            System.out.println("Encontrada: " + clave + " -> " + dato);
        } else {
            System.out.println("La clave " + clave + " no existe en el árbol.");
        }
    }

    private static void eliminar(Arbolbmas arbol) {
        int clave = leerEntero("Clave a eliminar: ");

        if (arbol.eliminar(clave)) {
            System.out.println("Clave " + clave + " eliminada correctamente.");
        } else {
            System.out.println("La clave " + clave + " no existe en el árbol.");
        }
    }

    private static void recorrerRango(Arbolbmas arbol) {
        int clave = leerEntero("Clave de inicio: ");
        int n = leerEntero("Cantidad de elementos a mostrar: ");

        System.out.println("Elementos desde la clave " + clave + ":");
        arbol.recorrerRango(clave, n);
    }

    // Lee un entero de la consola; si el usuario escribe otra cosa, vuelve a pedirlo
    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();   // Limpia el salto de línea pendiente
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine();   // Descarta la entrada inválida
                System.out.println("Entrada inválida, ingrese un número entero.");
            }
        }
    }
}