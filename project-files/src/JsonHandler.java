import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonHandler {
    public List<Room> readRooms(String jsonFilePath) throws IOException {

        List<Room> roomList = new ArrayList<>();
        try {

            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

            JSONArray jsonArray = new JSONArray(jsonContent);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject roomJson = jsonArray.getJSONObject(i);
                Room room = new Room(roomJson.getInt("roomID"), roomJson.getString("roomName"), roomJson.getInt("noOfPersons"), roomJson.getString("area"), roomJson.getInt("stars"), roomJson.getInt("noOfReviews"), roomJson.getInt("price"), roomJson.getString("roomImage"));

                roomList.add(room);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return roomList;
    }
}

