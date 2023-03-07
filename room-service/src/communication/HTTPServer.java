package communication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import controller.Controller;

public class HTTPServer {

    public HTTPServer(Controller controller) {
        super();
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/IoT", new handler(controller));
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static class handler implements HttpHandler {

        private Controller controller;

        public handler(Controller controller) {
            super();
            this.controller = controller;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

            if(t.getRequestMethod().equals("OPTIONS")){
                t.sendResponseHeaders(200, 0);
                return;
            }

            if(t.getRequestMethod().equals("POST")){
                System.out.println("POST"); //Da eliminare
                InputStream is = t.getRequestBody();
                JSONObject json = new JSONObject(new BufferedReader(new InputStreamReader(is)).readLine());
                System.out.println(json.toString()); //Da eliminare

                System.out.println(json.getString("lights"));
                System.out.println(json.getInt("position"));

                this.controller.updateLights(json.getString("lights").equals("ON"));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("OK"); //Da eliminare
                this.controller.updateRollerBlinds(json.getInt("position"));
                System.out.println("OK"); //Da eliminare
                t.sendResponseHeaders(200, 0);
                OutputStream os = t.getResponseBody();
                os.write("".getBytes());
                os.close();
                return;
            }

            if(t.getRequestMethod().equals("GET")){
                System.out.println("GET"); //Da eliminare
                String query = t.getRequestURI().toString();
                query = query.split("\\=")[1];
                System.out.println(query); //Da eliminare

                File file = new File("res/"+query);
                if(!file.exists()){
                    String response = "File not found";
                    t.sendResponseHeaders(404, response.length());
                    OutputStream os = t.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return;
                }

                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }
                String jsonString = stringBuilder.toString();
                System.out.println(jsonString); //Da eliminare
                bufferedReader.close();
                t.sendResponseHeaders(200, jsonString.length());
                OutputStream os = t.getResponseBody();
                os.write(jsonString.getBytes());
                os.close();
                fileReader.close();
            }
        }
    }
}