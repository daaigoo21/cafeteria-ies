package servicio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServicioMeteo {

    private static final String URL_API = "https://api.open-meteo.com/v1/forecast" +
            "?latitude=37.1773&longitude=-3.5986" +
            "&current=temperature_2m,apparent_temperature,weathercode" +
            "&timezone=Europe/Madrid";

    public static String[] obtenerTiempo() {
        try {
            URL url = new URL(URL_API);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null)
                sb.append(linea);
            br.close();
            con.disconnect();

            String json = sb.toString();

            String temp = extraer(json, "temperature_2m");
            String sensacion = extraer(json, "apparent_temperature");
            String codigo = extraer(json, "weathercode");

            String descripcion = interpretarCodigo(Integer.parseInt(codigo));

            return new String[] { temp + "°C", sensacion + "°C", descripcion };

        } catch (Exception e) {
            return new String[] { "--", "--", "Sin conexión" };
        }
    }

    private static String extraer(String json, String clave) {
        String buscar = "\"" + clave + "\":";
        int idx = json.indexOf(buscar);
        if (idx == -1)
            return "--";
        int inicio = idx + buscar.length();
        int fin = json.indexOf(",", inicio);
        if (fin == -1)
            fin = json.indexOf("}", inicio);
        return json.substring(inicio, fin).trim();
    }

    private static String interpretarCodigo(int codigo) {
        if (codigo == 0)
            return "Despejado";
        if (codigo <= 2)
            return "Parcialmente nublado";
        if (codigo == 3)
            return "Nublado";
        if (codigo <= 49)
            return "Niebla";
        if (codigo <= 59)
            return "Llovizna";
        if (codigo <= 69)
            return "Lluvia";
        if (codigo <= 79)
            return "Nieve";
        if (codigo <= 82)
            return "Chubascos";
        if (codigo <= 99)
            return "Tormenta";
        return "Desconocido";
    }
}
