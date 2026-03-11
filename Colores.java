package BotLechonk;

public class Colores {

    // Reset
    public static final String RESET = "\u001B[0m";

    // Colores básicos
    public static final String NEGRO = "\u001B[30m";
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BLANCO = "\u001B[37m";

    // Colores brillantes
    public static final String ROJO_BRILLANTE = "\u001B[91m";
    public static final String VERDE_BRILLANTE = "\u001B[92m";
    public static final String AMARILLO_BRILLANTE = "\u001B[93m";
    public static final String AZUL_BRILLANTE = "\u001B[94m";
    public static final String MAGENTA_BRILLANTE = "\u001B[95m";
    public static final String CYAN_BRILLANTE = "\u001B[96m";
    public static final String BLANCO_BRILLANTE = "\u001B[97m";

    // Estilos
    public static final String NEGRITA = "\u001B[1m";
    public static final String SUBRAYADO = "\u001B[4m";

    // Fondos
    public static final String FONDO_ROJO = "\u001B[41m";
    public static final String FONDO_VERDE = "\u001B[42m";
    public static final String FONDO_AMARILLO = "\u001B[43m";
    public static final String FONDO_AZUL = "\u001B[44m";

    public static void main(String[] args) {

        System.out.println(ROJO + "Texto en rojo" + RESET);
        System.out.println(VERDE + "Texto en verde" + RESET);
        System.out.println(AMARILLO + "Texto en amarillo" + RESET);
        System.out.println(AZUL + "Texto en azul" + RESET);

        System.out.println(MAGENTA + "Texto en magenta" + RESET);
        System.out.println(CYAN + "Texto en cyan" + RESET);

        System.out.println(ROJO_BRILLANTE + "Rojo brillante" + RESET);
        System.out.println(VERDE_BRILLANTE + "Verde brillante" + RESET);

        System.out.println(NEGRITA + "Texto en negrita" + RESET);
        System.out.println(SUBRAYADO + "Texto subrayado" + RESET);

        System.out.println(FONDO_ROJO + "Texto con fondo rojo" + RESET);
    }
}