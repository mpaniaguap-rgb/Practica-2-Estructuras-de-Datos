import java.util.ArrayList;

/**
 * Árbol B+ de orden m.
 * - Cada nodo tiene como máximo m hijos (m - 1 claves).
 * - Todo nodo, excepto la raíz, tiene al menos ceil(m / 2) hijos.
 * - Los datos viven solo en las hojas, que están enlazadas entre sí.
 */
public class ArbolBMas {

    // Atributos
    private final int orden;
    private NodoArbolBMas raiz;

    // Constructor
    public ArbolBMas(int orden) {
        if (orden < 3) {
            throw new IllegalArgumentException("El orden del árbol debe ser al menos 3");
        }
        this.orden = orden;
        raiz = new NodoArbolBMas(true);
    }

    // ------------------------------------------------------------------
    // Inserción
    // ------------------------------------------------------------------

    // Inserción de una clave con su dato. Retorna false si la clave ya existe
    public boolean insertar(int clave, String dato) {
        if (buscar(clave)) {
            return false;
        }

        // Se inserta primero de forma recursiva
        insertarRecursivo(this.raiz, clave, dato);

        // Se verifica si la raíz se desbordó
        if(this.raiz.getClaves().size() == orden) {
            NodoArbolBMas nuevaRaiz = new NodoArbolBMas(false);
            nuevaRaiz.getHijos().add(this.raiz);
            dividirHijo(nuevaRaiz, 0);
            this.raiz = nuevaRaiz;
        }
        return true;
    }

    // Inserción recursiva de una clave
    // Permite que los nodos se desborden temporalmente y los divide al regresar de la recursión
    private void insertarRecursivo(NodoArbolBMas nodo, int clave, String dato) {
        int i = nodo.getClaves().size() - 1;

        if(nodo.esHoja()) {
            while(i >= 0 && clave < nodo.getClaves().get(i)) {
                i--;
            }
            // La clave y su dato se guardan en la misma posición de sus listas
            nodo.getClaves().add(i + 1, clave);
            nodo.getDatos().add(i + 1, dato);
        } else {
            while(i >= 0 && clave < nodo.getClaves().get(i)) {
                i--;
            }
            i++;
            NodoArbolBMas hijo = nodo.getHijos().get(i);

            // Llamada recursiva para insertar la clave primero en el hijo correspondiente
            insertarRecursivo(hijo, clave, dato);

            // Evaluación de desbordamiento al retornar de la recursividad
            if(hijo.getClaves().size() == orden) {
                dividirHijo(nodo, i);
            }
        }
    }

    // Dividir un nodo que está lleno
    private void dividirHijo(NodoArbolBMas padre, int indice) {
        NodoArbolBMas nodoLleno = padre.getHijos().get(indice);
        NodoArbolBMas nuevoNodo = new NodoArbolBMas(nodoLleno.esHoja());
        int mitad = orden / 2;

        if (nodoLleno.esHoja()) {
            // En nodos hoja, la clave media se mantiene en la hoja derecha (y se copia al padre)
            nuevoNodo.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(mitad, nodoLleno.getClaves().size())));
            nuevoNodo.setDatos(new ArrayList<>(nodoLleno.getDatos().subList(mitad, nodoLleno.getDatos().size())));
            nodoLleno.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(0, mitad)));
            nodoLleno.setDatos(new ArrayList<>(nodoLleno.getDatos().subList(0, mitad)));

            // Se enlaza la nueva hoja justo después de la hoja dividida
            nuevoNodo.setSiguiente(nodoLleno.getSiguiente());
            nodoLleno.setSiguiente(nuevoNodo);

            // El padre recibe como separador la primera clave del nuevo nodo
            int claveMedia = nuevoNodo.getClaves().get(0);
            padre.getClaves().add(indice, claveMedia);
            padre.getHijos().add(indice + 1, nuevoNodo);
        }
        else {
            // En nodos internos, la clave media sube y se elimina del hijo
            int claveMedia = nodoLleno.getClaves().get(mitad);
            nuevoNodo.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(mitad + 1, nodoLleno.getClaves().size())));
            nodoLleno.setClaves(new ArrayList<>(nodoLleno.getClaves().subList(0, mitad)));

            // Mover los hijos del nodo dividido
            nuevoNodo.setHijos(new ArrayList<>(nodoLleno.getHijos().subList(mitad + 1, nodoLleno.getHijos().size())));
            nodoLleno.setHijos(new ArrayList<>(nodoLleno.getHijos().subList(0, mitad + 1)));

            padre.getClaves().add(indice, claveMedia);
            padre.getHijos().add(indice + 1, nuevoNodo);
        }
    }

    // ------------------------------------------------------------------
    // Impresión
    // ------------------------------------------------------------------

    // Impresión del árbol como parte de la interfaz pública del árbol
    public void imprimirArbol() {
        imprimirNodo(raiz, "", true);
    }

    // Impresión recursiva privada de los nodos a partir de uno inicial
    private void imprimirNodo(NodoArbolBMas nodo, String indentacion, boolean esUltimo) {
        System.out.println(indentacion + "+- " + (nodo.esHoja() ? "Hoja > " : "Interno > ") + nodo.getClaves());
        indentacion += esUltimo ? "   " : "|  ";
        for(int i = 0; i < nodo.getHijos().size(); i++) {
            imprimirNodo(nodo.getHijos().get(i), indentacion, i == (nodo.getHijos().size() - 1));
        }
    }

    // ------------------------------------------------------------------
    // Búsqueda
    // ------------------------------------------------------------------

    // Búsqueda de una clave como parte de la interfaz pública del árbol
    public boolean buscar(int clave) {
        return encontrarHoja(clave).getClaves().contains(clave);
    }

    // Retorna el dato asociado a una clave, o null si la clave no existe
    public String obtenerDato(int clave) {
        NodoArbolBMas hoja = encontrarHoja(clave);
        int i = hoja.getClaves().indexOf(clave);
        return (i == -1) ? null : hoja.getDatos().get(i);
    }

    // Desciende desde la raíz hasta la hoja donde está (o debería estar) la clave
    private NodoArbolBMas encontrarHoja(int clave) {
        NodoArbolBMas nodo = raiz;
        while (!nodo.esHoja()) {
            nodo = nodo.getHijos().get(indiceHijo(nodo, clave));
        }
        return nodo;
    }

    // Índice del hijo por el que se debe descender para una clave dada
    // (si la clave es igual a un separador, se desciende a la derecha)
    private int indiceHijo(NodoArbolBMas nodo, int clave) {
        int i = 0;
        while (i < nodo.getClaves().size() && clave >= nodo.getClaves().get(i)) {
            i++;
        }
        return i;
    }

    // ------------------------------------------------------------------
    // Eliminación
    // ------------------------------------------------------------------

    // Elimina una clave (y su dato) del árbol. Retorna false si la clave no existe
    public boolean eliminar(int clave) {
        boolean eliminado = eliminarRecursivo(raiz, clave);

        // Si la raíz interna se quedó con un solo hijo, ese hijo pasa a ser la nueva raíz
        if (!raiz.esHoja() && raiz.getHijos().size() == 1) {
            raiz = raiz.getHijos().get(0);
        }
        return eliminado;
    }

    // Eliminación recursiva: elimina en la hoja y rebalancea al regresar de la recursión
    private boolean eliminarRecursivo(NodoArbolBMas nodo, int clave) {
        if (nodo.esHoja()) {
            int i = nodo.getClaves().indexOf(clave);
            if (i == -1) {
                return false;
            }
            nodo.getClaves().remove(i);
            nodo.getDatos().remove(i);
            return true;
        }

        int i = indiceHijo(nodo, clave);
        NodoArbolBMas hijo = nodo.getHijos().get(i);
        boolean eliminado = eliminarRecursivo(hijo, clave);

        // Si el hijo quedó con menos claves del mínimo, se rebalancea
        if (eliminado && hijo.getClaves().size() < minimoClaves(hijo)) {
            rebalancear(nodo, i);
        }
        return eliminado;
    }

    // Mínimo de claves permitido en un nodo (que no sea la raíz)
    private int minimoClaves(NodoArbolBMas nodo) {
        return nodo.esHoja() ? orden / 2 : (orden + 1) / 2 - 1;
    }

    // Soluciona el subdesbordamiento del hijo en la posición i: presta o fusiona
    private void rebalancear(NodoArbolBMas padre, int i) {
        NodoArbolBMas izquierdo = (i > 0) ? padre.getHijos().get(i - 1) : null;
        NodoArbolBMas derecho = (i < padre.getHijos().size() - 1) ? padre.getHijos().get(i + 1) : null;

        if (izquierdo != null && izquierdo.getClaves().size() > minimoClaves(izquierdo)) {
            prestarDeIzquierdo(padre, i);
        } else if (derecho != null && derecho.getClaves().size() > minimoClaves(derecho)) {
            prestarDeDerecho(padre, i);
        } else if (izquierdo != null) {
            fusionar(padre, i - 1);
        } else {
            fusionar(padre, i);
        }
    }

    // El hijo i toma una clave del hermano izquierdo
    private void prestarDeIzquierdo(NodoArbolBMas padre, int i) {
        NodoArbolBMas hijo = padre.getHijos().get(i);
        NodoArbolBMas izquierdo = padre.getHijos().get(i - 1);
        int ultima = izquierdo.getClaves().size() - 1;

        if (hijo.esHoja()) {
            // La última clave y dato del hermano pasan al inicio del hijo
            hijo.getClaves().add(0, izquierdo.getClaves().remove(ultima));
            hijo.getDatos().add(0, izquierdo.getDatos().remove(ultima));
            padre.getClaves().set(i - 1, hijo.getClaves().get(0));
        } else {
            // La clave separadora baja al hijo y la última del hermano sube al padre
            hijo.getClaves().add(0, padre.getClaves().get(i - 1));
            padre.getClaves().set(i - 1, izquierdo.getClaves().remove(ultima));
            hijo.getHijos().add(0, izquierdo.getHijos().remove(izquierdo.getHijos().size() - 1));
        }
    }

    // El hijo i toma una clave del hermano derecho
    private void prestarDeDerecho(NodoArbolBMas padre, int i) {
        NodoArbolBMas hijo = padre.getHijos().get(i);
        NodoArbolBMas derecho = padre.getHijos().get(i + 1);

        if (hijo.esHoja()) {
            // La primera clave y dato del hermano pasan al final del hijo
            hijo.getClaves().add(derecho.getClaves().remove(0));
            hijo.getDatos().add(derecho.getDatos().remove(0));
            padre.getClaves().set(i, derecho.getClaves().get(0));
        } else {
            // La clave separadora baja al hijo y la primera del hermano sube al padre
            hijo.getClaves().add(padre.getClaves().get(i));
            padre.getClaves().set(i, derecho.getClaves().remove(0));
            hijo.getHijos().add(derecho.getHijos().remove(0));
        }
    }

    // Fusiona el hijo i con el hijo i + 1 (el derecho se absorbe en el izquierdo)
    private void fusionar(NodoArbolBMas padre, int i) {
        NodoArbolBMas izquierdo = padre.getHijos().get(i);
        NodoArbolBMas derecho = padre.getHijos().get(i + 1);

        if (izquierdo.esHoja()) {
            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getDatos().addAll(derecho.getDatos());
            izquierdo.setSiguiente(derecho.getSiguiente());   // Se mantiene el enlace entre hojas
        } else {
            // La clave separadora del padre baja al nodo fusionado
            izquierdo.getClaves().add(padre.getClaves().get(i));
            izquierdo.getClaves().addAll(derecho.getClaves());
            izquierdo.getHijos().addAll(derecho.getHijos());
        }

        padre.getClaves().remove(i);
        padre.getHijos().remove(i + 1);
    }

    // ------------------------------------------------------------------
    // Recorrido por rango
    // ------------------------------------------------------------------

    // Busca la clave de inicio e imprime los datos de los siguientes n elementos
    // (incluyendo el de la clave de inicio) recorriendo las hojas enlazadas.
    // Si la clave no existe, el recorrido inicia en la primera clave mayor.
    public void recorrerRango(int claveInicio, int n) {
        NodoArbolBMas hoja = encontrarHoja(claveInicio);

        // Posición de la primera clave >= claveInicio dentro de la hoja
        int i = 0;
        while (i < hoja.getClaves().size() && hoja.getClaves().get(i) < claveInicio) {
            i++;
        }

        int impresos = 0;
        while (hoja != null && impresos < n) {
            if (i >= hoja.getClaves().size()) {
                hoja = hoja.getSiguiente();   // Se pasa a la siguiente hoja
                i = 0;
            } else {
                System.out.println("  " + hoja.getClaves().get(i) + " -> " + hoja.getDatos().get(i));
                i++;
                impresos++;
            }
        }

        if (impresos < n) {
            System.out.println("  (Solo se encontraron " + impresos + " elementos)");
        }
    }
}