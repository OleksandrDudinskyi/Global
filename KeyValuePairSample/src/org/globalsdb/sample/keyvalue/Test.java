package org.globalsdb.sample.keyvalue;

import java.util.ArrayList;

import javax.swing.JFrame;

/**
 * <CODE>Test.java</CODE> is the main Globals KeyValue Test routine. Globals
 * KeyValue is a sample application that illustrates a possible use of Globals
 * API. It implements a very basic KeyValue data store. KeyValue class extends
 * HashMap<String,String>. Future possible versions would allow Object values,
 * making it an even more interesting example. The tests generates some sample
 * data, then proceeds to connect to Globals DB, delete any previously stored
 * data, then store and load some data.
 */
public class Test {

	private int count;
	private Connection connection;
	private KeyValueMap customers;
	private ArrayList<KeyValue> customersData;
//	private JFrame mainWindow;
	private Test(int cnt) {
		count = cnt;
		customersData = generateCustomers(count);
		connection = new Connection();
		/**
		 * namespace - the namespace to which to connect.
		 * user - name of user
		 * connecting. password - password of user connecting.
		 */
		connection.connect("USER", "_SYSTEM", "SYS");
		/**
		 * create new KeyValueMap 
		 */
		customers = connection.getKeyValueMap("Customers");
	}

	public static void main(String[] args) {
		checkParameters(args);
		int count = 10;
		if (args.length == 1) {
			count = Integer.parseInt(args[0]);
		}
		Test test = new Test(count);
//		mainWindow = new JFrame();
		test.deleteData();
		test.store();
		test.load();
		test.query();
		test.close();
	}

	private void deleteData() {
		// delete all data
		customers.clear();
	}

	private void store() {
		long start = getTime();
		for (int i = 0; i < count; i++) {
			customers.store("" + i, customersData.get(i));
		}
		report(false, count * 2, start);
	}

	private void load() {
		long start = getTime();
		for (int i = 0; i < count; i++) {
			@SuppressWarnings("unused")
			KeyValue customer = customers.load("" + i, customersData.get(0));
		}
		report(true, count, start);
	}

	private void query() {
		KeyValue customer;
		long start = getTime();
		for (int i = 0; i < count; i++) {
			customer = customers.load("" + i, customersData.get(0));
			String name = (String) customer.get("name");
			if (name.equals("John Smith 4")) {
				System.out.println(customer.toJSON("\r\n "));
				break;
			}
		}
		System.out.println("Query took: " + (getTime() - start) / 1000.0
				+ "\r\n");
	}

	private void close() {
		connection.close();
	}

	private static ArrayList<KeyValue> generateCustomers(int count) {
		ArrayList<KeyValue> customers = new ArrayList<KeyValue>();
//		ArrayList<String> jokes = new ArrayList<String>();
		for (int i = 0; i < count; i++) {
			KeyValue customer = new KeyValue();
			customer.put("name", "John Smith " + i);
			customer.put("ssn", "001-01-0001");
			customer.put("dob", "03-01-1975");
			customer.put("home phone", "(617) 577-3600");
			customers.add(customer);
		}
		return customers;
	}

	private static long getTime() {
		return (new java.util.Date(System.currentTimeMillis())).getTime();
	}

	private static void checkParameters(String args[]) {
		if ((args.length == 1) && args[0].equalsIgnoreCase("-help")) {
			System.out
					.println("Parameters: \r\n  (1) Total number of entries [optional, defaulfs to 100,000]");
			return;
		}
	}

	private static void report(boolean load, long count, long start) {
		long duration = getTime() - start;
		if (load) {
			System.out.println("\r\nNumber of entries loaded = " + count);
		} else {
			System.out.println("\r\nNumber of entries stored = " + count);
		}
		System.out.println("Duration: " + duration / 1000.0 + " seconds");
		if (load) {
			System.out.println("Load rate: "
					+ (int) (count * 1000.0 / duration) + " objects/sec\r\n");
		} else {
			System.out.println("Store rate: "
					+ (int) (count * 1000.0 / duration) + " objects/sec\r\n");
		}
	}

}
