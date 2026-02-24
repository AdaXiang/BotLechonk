package BotLechonk;

public class Colores {

    // Reset
    public static final String RESET = "\u001B[0m";

    // Colores
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";

    public static void main(String[] args) {

        System.out.println(ROJO + "Texto en rojo" + RESET);
        System.out.println(VERDE + "Texto en verde" + RESET);
        System.out.println(AMARILLO + "Texto en amarillo" + RESET);
        System.out.println(AZUL + "Texto en azul" + RESET);

    }
}