package Json;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;



public class JsonManager {
	
	public boolean CreateNewJson(String nome, String time, String lights, String position) {
		File filejson = new File("res/"+nome+".json");
        if (filejson.exists()) {
        	return false;
        }
		
        JSONArray jsonArray = new JSONArray();		
		JSONObject obj = new JSONObject();
		obj.put("time", time);
		obj.put("lights", lights);
		obj.put("position", position);
        // Aggiunta del nuovo oggetto all'array JSON
        jsonArray.put(obj);
		// Stampa dell'oggetto JSON
		System.out.println(obj);
        try (FileWriter file = new FileWriter("res/"+nome+".json")) {
            file.write(jsonArray.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public boolean AddRowToJSON(String nome, String time, String lights, String position) {
		try {
            // Lettura del contenuto del file JSON
            FileReader fileReader = new FileReader("res/"+nome+".json");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            String jsonString = stringBuilder.toString();
            
            // Conversione del contenuto in JSONArray
            JSONArray jsonArray = new JSONArray(jsonString);
            // Creazione di un nuovo oggetto JSON
            JSONObject newJsonObject = new JSONObject();
            newJsonObject.put("time", time);
            newJsonObject.put("lights", lights);
            newJsonObject.put("position", position);
            // Aggiunta del nuovo oggetto all'array JSON
            jsonArray.put(newJsonObject);
            // Scrittura del nuovo array JSON su file
            FileWriter fileWriter = new FileWriter("res/"+nome+".json");
            fileWriter.write(jsonArray.toString());
            fileWriter.close();
            return true;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
	
}
