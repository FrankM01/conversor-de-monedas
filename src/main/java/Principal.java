import java.util.Scanner;
import java.text.DecimalFormat;

public class Principal {
    public static void menu() {
        System.out.println("=== Conversor de Monedas ===");
        System.out.println("1. Dolar a Euro");
        System.out.println("2. Soles a Dolar");
        System.out.println("3. Dolar a Soles");
        System.out.println("4. Euro a Dolar");
        System.out.println("5. Euro a Soles");
        System.out.println("6. Soles a Euro");
        System.out.println("7. Salir");
        System.out.print("Selecciona una opción: ");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("#0.00");

        ConsultarConversion conversion = new ConsultarConversion();

        boolean running = true;
        while (running) {
            menu();
            String opcion = sc.nextLine().trim();

            String monedaBase;
            String monedaObjetivo;

            switch (opcion) {
                case "1": // Dolar a Euro
                    monedaBase = "USD";
                    monedaObjetivo = "EUR";
                    break;
                case "2": // Soles a Dolar
                    monedaBase = "PEN";
                    monedaObjetivo = "USD";
                    break;
                case "3": // Dolar a Soles
                    monedaBase = "USD";
                    monedaObjetivo = "PEN";
                    break;
                case "4": // Euro a Dolar
                    monedaBase = "EUR";
                    monedaObjetivo = "USD";
                    break;
                case "5": // Euro a Soles
                    monedaBase = "EUR";
                    monedaObjetivo = "PEN";
                    break;
                case "6": // Soles a Euro
                    monedaBase = "PEN";
                    monedaObjetivo = "EUR";
                    break;
                case "7":
                    System.out.println("Saliendo...");
                    running = false;
                    continue;
                default:
                    System.out.println("Opción inválida. Intenta de nuevo.");
                    continue;
            }

            // Pedir monto a convertir
            double monto;
            while (true) {
                try {
                    System.out.print("Ingresa el monto a convertir: ");
                    String linea = sc.nextLine().trim();
                    monto = Double.parseDouble(linea);
                    if (monto <= 0) {
                        System.out.println("El monto debe ser mayor que 0. Intenta de nuevo.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Monto inválido. Ingresa un número válido.");
                }
            }

            try {
                Conversor conversor = conversion.realizarConversion(monedaBase, monedaObjetivo, monto);
                double resultado = conversor.conversion_result();
                double tasa = conversor.conversion_rate();
                System.out.println("Tasa de conversión (" + monedaBase + "->" + monedaObjetivo + "): " + df.format(tasa));
                System.out.println("El valor " + df.format(monto) + " [" + monedaBase + "] corresponde al valor final de =>>> " + df.format(resultado) + " [" + monedaObjetivo + "].");
            } catch (RuntimeException e) {
                System.out.println("Error al realizar la conversión: " + e.getMessage());
                System.out.println("Intenta de nuevo o revisa tu conexión.");
            }

            System.out.println();
        }

        sc.close();
    }
}
