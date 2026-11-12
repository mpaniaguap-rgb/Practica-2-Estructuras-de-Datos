# Árbol B+ en Java

Implementación de un árbol B+ en Java con menú de consola, desarrollada para la Segunda Práctica de Implementación de Estructuras de Datos.

- **Universidad:** CENFOTEC, Escuela de Software
- **Curso:** SOFT-10 Estructuras de Datos, sección SCV4, periodo C3-2026
- **Docente:** Romario Salas Cerdas
- **Estudiante:** Mariana Paniagua Porras

## Descripción

El programa simula de forma sencilla cómo las bases de datos y los sistemas de archivos guardan la información. El árbol se mantiene siempre balanceado: cuando una inserción o una eliminación lo desbalancea, se reorganiza automáticamente.

Reglas del árbol B+:

- Cada nodo puede tener como máximo `m` hijos, donde `m` es el orden del árbol.
- Todos los nodos, excepto la raíz, tienen al menos `m/2` hijos (redondeado hacia arriba). La raíz puede tener entre 2 y `m` hijos.
- Los datos se guardan solo en las hojas. Los nodos internos, incluida la raíz, solo contienen claves.
- Todas las hojas están enlazadas entre sí, por lo que, al encontrar una hoja, se pueden recorrer los datos siguientes sin volver a bajar desde la raíz.

## Operaciones del árbol

| Método | Descripción |
|---|---|
| `insertar(clave, dato)` | Agrega una clave con su dato. Si un nodo se llena, se divide y se enlazan las hojas nuevas. Si la clave ya existe, no se inserta y retorna `false`. |
| `buscar(clave)` | Indica si la clave existe en alguna hoja. |
| `obtenerDato(clave)` | Devuelve el dato de una clave, o `null` si la clave no existe. |
| `eliminar(clave)` | Borra la clave y su dato. Si el nodo queda con muy pocas claves, toma una de un hermano o se fusiona con él. Si la raíz queda con un solo hijo, ese hijo pasa a ser la nueva raíz. Retorna `false` si la clave no existe. |
| `recorrerRango(claveInicio, n)` | Busca la clave de inicio y muestra los siguientes `n` elementos, empezando por esa clave, siguiendo el enlace entre hojas. Si la clave no existe, empieza desde la siguiente clave mayor. |
| `imprimirArbol()` | Muestra el árbol completo en consola, con sangría para distinguir los niveles. |

## Compilación y ejecución

Desde la carpeta principal del proyecto:

```
javac -d out src/*.java
java -cp out Main
```

También se puede abrir el proyecto en IntelliJ IDEA y ejecutar la clase `Main`.

## Uso

Al iniciar, el programa pide el orden del árbol (mínimo 3) y luego muestra este menú:

```
------ MENÚ ------
1. Insertar clave y dato
2. Buscar clave
3. Eliminar clave
4. Recorrer rango
5. Mostrar árbol
0. Salir
```

Las claves son números enteros y los datos son texto. Si se escribe algo que no es un número donde se pide uno, el programa avisa y vuelve a preguntar.

### Ejemplo

Con orden 4, al insertar las claves `10, 20, 5, 6, 15`, el árbol queda así:

```
+- Interno > [10]
   +- Hoja > [5, 6]
   +- Hoja > [10, 15, 20]
```

Al recorrer el rango desde la clave `6` con `n = 3`, se muestra:

```
  6 -> d6
  10 -> d10
  15 -> d15
```

## Notas de diseño

- Al eliminar una clave, puede quedar una copia de ella en un nodo interno como separador. Esto es normal en un árbol B+. Por eso `buscar` siempre baja hasta la hoja y no depende de lo que haya en los nodos internos.
- Al insertar, un nodo puede llenarse un momento de más y se divide cuando la recursión regresa.