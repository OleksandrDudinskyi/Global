package org.globalsdb.sample.keyvalue;

import java.util.HashMap;
/**
 * Special format for save data to db. 
 * @author OleksandrDudinskyi
 *
 */

public class KeyValue extends HashMap<String,String> {

    /**
	 * Unique id for class extends from another.
	 */
	private static final long serialVersionUID = 1L;
	private     String          dbID =  null;

    public KeyValue() {}

    String getDBID() {
        return dbID;
    }

    void setDBID(String n, String id) {
        dbID = id;
    }

    public String toJSON(String separator) {
        if (separator == null) {
            separator = "";
        }
        String json = separator + "{" + separator;
        boolean first = true;
        for (String key : keySet()) {
            if (!first) {
                json = json + "," + separator;
            }
            first = false;
            Object doc = get(key);
            if (doc instanceof KeyValue) {
                json = json + key + " : " + ((KeyValue)doc).toJSON(separator+ " ");
            } else {
                json = json + key + " : " + get(key);
            }
        }
        return json + separator + "}";
    }
}
