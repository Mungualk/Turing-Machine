import java.util.*;

public class Main
{
    static String SIGMA = "ABCDEF";
    static char BLANCO = '_';
    static String[] ESTADOS = {"q0", "q1", "q2"};
    static String[] SIGUIENTE = {"q1", "q2", "q0"};
    static String[] CIFRADO = {"BCDEFA", "CDEFAB", "DEFABC"};
    static String[] DESCIFRADO = {"FABCDE", "EFABCD", "DEFABC"};

    static Map<String, String[]> ct(String[] permutaciones) // Tabla de transicionsssss
    {
        Map<String, String[]> delta = new HashMap<>();
        for (int e = 0; e < ESTADOS.length; e++)
        {
            for (int i = 0; i < SIGMA.length(); i++)
            {
                String clave = ESTADOS[e] + "," + SIGMA.charAt(i);
                String[] valor = {SIGUIENTE[e], String.valueOf(permutaciones[e].charAt(i)), "R"};
                delta.put(clave, valor);
            }
            delta.put(ESTADOS[e] + "," + BLANCO, new String[]{"qf", String.valueOf(BLANCO), "S"});
        }
        return delta;
    }

    // Impresion de la informacion de cada movimiento
    static void imConf(List<Character> cinta, int cabezal, String estado, int paso)
    {
        StringBuilder prefijo = new StringBuilder();
        for (int i = 0; i < cabezal; i++) {
            prefijo.append(cinta.get(i));
        }
        StringBuilder sufijo = new StringBuilder();
        for (int i = cabezal; i < cinta.size(); i++)
        {
            sufijo.append(cinta.get(i));
        }
        System.out.println("Paso " + paso + ": [" + prefijo + "] (" + estado + ") [" + sufijo + "]");
    }

    // Ejecucion del cifrado
    static void cifrado(String entrada, Map<String, String[]> delta)
    {
        List<Character> cinta = new ArrayList<>();
        for (char c : entrada.toCharArray())
        {
            cinta.add(c);
        }
        if (cinta.isEmpty())
        {
            cinta.add(BLANCO);
        }
        int cabezal = 0;
        String estado = "q0";
        int paso = 0;

        imConf(cinta, cabezal, estado, paso);

        while (!estado.equals("qf"))
        {
            String[] transicion = delta.get(estado + "," + cinta.get(cabezal));
            if (transicion == null)
            {
                break;
            }
            estado = transicion[0];
            cinta.set(cabezal, transicion[1].charAt(0));
            if (transicion[2].equals("R"))
            {
                cabezal++;
            }

            else if (transicion[2].equals("L"))
            {
                cabezal--;
            }

            if (cabezal < 0)
            {
                cinta.add(0, BLANCO);
                cabezal = 0;
            }

            if (cabezal >= cinta.size())
            {
                cinta.add(BLANCO);
            }
            paso++;
            imConf(cinta, cabezal, estado, paso);
        }

        StringBuilder contenido = new StringBuilder();
        for (char c : cinta)
        {
            contenido.append(c);
        }

        if (estado.equals("qf")) {
            System.out.println("Dictamen: ACEPTADA (alcanzo qf)");
        }
        else

        {
            System.out.println("Dictamen: RECHAZADA (transicion indefinida en " + estado
                    + " con el simbolo '" + cinta.get(cabezal) + "')");
        }
        System.out.println("Cinta final: " + contenido);
    }

    static void main(String[] args)
    {
        Map<String, String[]> tablaCifrar = ct(CIFRADO);
        Map<String, String[]> tablaDescifrar = ct(DESCIFRADO);
        Scanner teclado = new Scanner(System.in);
        boolean flag = true;

        while (flag)
        {
            System.out.println();
            System.out.println("------------- Turing Changarro ------------- ");
            System.out.println("1. CIFRAR");
            System.out.println("2. DESCIFRAR");
            System.out.println("3. SALIR");
            System.out.print("Opcion: ");
            String opcion = teclado.nextLine().trim();

            if (opcion.equals("1") || opcion.equals("2"))
            {
                System.out.print("Cadena (alfabeto A-F): ");
                String entrada = teclado.nextLine().trim();
                Map<String, String[]> tabla = opcion.equals("1") ? tablaCifrar : tablaDescifrar;
                System.out.println();
                cifrado(entrada, tabla);
            } else if (opcion.equals("3"))
            {
                flag = false;
            } else
            {
                System.out.println("Opcion no valida.");
            }
        }
        teclado.close();
    }
}