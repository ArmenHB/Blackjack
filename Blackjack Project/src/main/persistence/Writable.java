package persistence;

import org.json.JSONObject;

//TAKEN FROM THE WORKROOM PROJECT
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
