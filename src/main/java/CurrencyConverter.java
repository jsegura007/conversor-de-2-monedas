import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {
    private static final String API_KEY = "1fdbc6a4bdc6b09ddf1a4da8"; // MI llave
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    //metodo principal
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
    //Mamejo de errores
        try {
            boolean salir = false;
            while (!salir) {
                mostrarMenu();
                int opcion = obtenerOpcion(scanner);

                switch (opcion) {
                    case 1:
                        convertirEurosAXPF(scanner);
                        break;
                    case 2:
                        convertirDolaresAEuros(scanner);
                        break;
                    case 3:
                        salir = true;
                        System.out.println("Gracias por usar el conversor de moneda.");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, elige entre 1 y 3.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    // se despliega el menu en consola
    private static void mostrarMenu() {
        System.out.println("Bienvenido al conversor de moneda:");
        System.out.println("1. Convertir de Euros a XPF (Francos CFP)");
        System.out.println("2. Convertir de Dólares a Euros");
        System.out.println("3. Salir");
        System.out.print("Elige una opción: ");
    }
    //metodo para pedir que ingrese un valor valido
    private static int obtenerOpcion(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Por favor, introduce un número válido: ");
            scanner.next(); // Consumir entrada no válida
        }
        return scanner.nextInt();
    }

    public static void convertirEurosAXPF(Scanner scanner) throws IOException {
        //variables
        double amount;
        String fromCurrency = "EUR";
        String toCurrency = "XPF";
        //se asigna el valor ingresado por usuario a amount
        System.out.print("Introduce la cantidad en Euros que quieres convertir a XPF: ");
        amount = scanner.nextDouble();
        //euros a francos
        double convertedAmount = convertCurrency(amount, fromCurrency, toCurrency);
        System.out.printf("%.2f %s es igual a %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);
    }

    public static void convertirDolaresAEuros(Scanner scanner) throws IOException {
        double amount;
        String fromCurrency = "USD";
        String toCurrency = "EUR";

        System.out.print("Introduce la cantidad en Dólares que quieres convertir a Euros: ");
        amount = scanner.nextDouble();
        // Dolares a euros
        double convertedAmount = convertCurrency(amount, fromCurrency, toCurrency);
        System.out.printf("%.2f %s es igual a %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);
    }
    // se crea un url  concatenando la llave y el url base
    public static double convertCurrency(double amount, String fromCurrency, String toCurrency) throws IOException {
        String urlStr = BASE_URL + API_KEY + "/pair/" + fromCurrency + "/" + toCurrency;
        URL url = new URL(urlStr);
        // metodo get para solicitar los rates
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            // Parsear la respuesta JSON
            JSONObject response = new JSONObject(readResponse(reader));
            double rate = response.getDouble("conversion_rate");

            return amount * rate;// se multiplica rate por el monto para calcular el valor del cambio.
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String readResponse(InputStreamReader reader) throws IOException {
        StringBuilder response = new StringBuilder();
        char[] buffer = new char[4096];
        int bytesRead;
        while ((bytesRead = reader.read(buffer)) != -1) {
            response.append(buffer, 0, bytesRead);
        }
        return response.toString();
    }
}
