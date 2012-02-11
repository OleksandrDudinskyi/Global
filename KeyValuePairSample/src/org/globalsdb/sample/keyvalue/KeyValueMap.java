package org.globalsdb.sample.keyvalue;

import com.intersys.globals.NodeReference;
import com.intersys.globals.ValueList;

public class KeyValueMap {

	private NodeReference global;
	private String name;
	private ValueList valueList;
	private Connection connection;

	KeyValueMap(String n, Connection conn) {
		connection = conn;
		name = n;
		// Creates a NodeReference instance with the name specified in the
		// parameter.
		global = connection.createNodeReference(name);
		// Creates an empty ValueList.
		valueList = connection.createList();
	}

	public boolean isEmpty() {
		// true if the node contains data, else false.
		return (global.exists());
	}

	// saves the key value pair and removes the keyValue from the memory
	public void store(String key, KeyValue value) {
		valueList.clear();
		// create node
		global.appendSubscript(key);
		// write new value to valueList for further add to globals db.
		write(value, valueList);
		value.setDBID(name, key);
		global.set(valueList);
//		int qw = global.getInt();
		// Sets number of subscripts to a smaller number (discarding excess
		// trailing subscripts)
		global.setSubscriptCount(0);
		value.remove(key);
	}

	public boolean containsKey(String key) {
		global.appendSubscript(key);
		return (global.exists());
	}

	public void remove(String key) {
		global.appendSubscript(key);
		global.killNode();
	}

	public void clear() {
		global.kill();
	}

	public KeyValue load(String key) {
		// in default
		global.setSubscriptCount(0);
		global.appendSubscript(key);
		valueList = global.getList(valueList);
		KeyValue keyValue = new KeyValue();
		read(keyValue, valueList);
		keyValue.setDBID(name, key);
		return keyValue;
	}
    /**
     * Load date from DB 
     * @param key
     * @param keyValue
     * @return date in special format
     */
	public KeyValue load(String key, KeyValue keyValue) {
		if (keyValue == null) {
			throw new RuntimeException(
					"Cannot reconstruct object, no schema supplied");
		}
		global.setSubscriptCount(0);
		global.appendSubscript(key);
		valueList = global.getList(valueList);
		read(keyValue, valueList);
		keyValue.setDBID(name, key);
		return keyValue;
	}

	/**
	 * Preset key and values of nodes to valueList structure to save our dates
	 * 
	 * @param keyValue
	 * @param valueList
	 */

	private void write(KeyValue keyValue, ValueList valueList) {
		for (String key : keyValue.keySet()) {
			String val = keyValue.get(key);
			valueList.append(key);
			valueList.append(val);
		}
	}

	/**
	 * Put this value from BD in keyValue initially key, than value.
	 * @param keyValue
	 * @param valueList
	 */
	
	private void read(KeyValue keyValue, ValueList valueList) {
		for (int i = 0; i < valueList.length() / 2; i++) {
			// relocated on one
			String key = valueList.getNextString();
			// relocated on two
			keyValue.put(key, valueList.getNextString());
		}
	}
}