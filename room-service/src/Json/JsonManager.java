package json;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.FileReader;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class JsonManager {

    public void updateJSON(final int light, final int rollerBlinds){
        String Date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String Time = LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond();
        if(checkFileExisting(Date)){
            addRowToJSON(Date, Time, light, rollerBlinds);
        }else{
            String path = "res";
            File directory = new File(path);
            if (!directory.exists()) {
                createDir(path);
            }
            createNewJson(Date, Time, light, rollerBlinds);
        }
    }

	private boolean createNewJson(final String filename, final String time, final int lights, final int position) {
        JSONArray jsonArray = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("time", time);
		obj.put("lights", lights);
		obj.put("position", position);
        jsonArray.put(obj);
        try (FileWriter file = new FileWriter("res/"+filename+".json")) {
            file.write(jsonArray.toString());
            return true;
        } catch (IOException e) {
            return false;
        }
	}

	private boolean addRowToJSON(final String filename, final String time, final int lights, final int position) {
		try {
            FileReader fileReader = new FileReader("res/"+filename+".json");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            String jsonString = stringBuilder.toString();
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject newJsonObject = new JSONObject();
            newJsonObject.put("time", time);
            newJsonObject.put("lights", lights);
            newJsonObject.put("position", position);
            jsonArray.put(newJsonObject);
            FileWriter fileWriter = new FileWriter("res/"+filename+".json");
            fileWriter.write(jsonArray.toString());
            fileWriter.close();
            bufferedReader.close();
            return true;
        } catch (IOException | JSONException e) {
            return false;
        }
    }

    private boolean checkFileExisting(final String filename) {
		File filejson = new File("res/"+filename+".json");
        return filejson.exists();
    }

    private boolean createDir(String path) {
        return new File(path).mkdir();
    }
}
