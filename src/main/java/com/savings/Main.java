package com.savings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	public static final List<Holder> HOLDERS = new ArrayList<>();
	
	public static void main(String[] args) {
	
		Holder holder = new Holder("test");
		holder.bills.add(new Bill("Car Payment", 5, 500));
		HOLDERS.add(holder);

		saveHolders();
		
		refreshHolders();
		print(HOLDERS.isEmpty() + "");
		
	}
	
	private static File getBills() {
		try {
			URL resource = ClassLoader.getSystemClassLoader().getResource("bills.json");
			assert resource != null;
			return new File(resource.toURI());
		} catch (Exception ignored) {
			print("null");
		}
		
		return null;
	}
	
	public static void saveHolders() {
	
		JSONArray arr = new JSONArray();
		HOLDERS.forEach(o -> arr.put(o.toJSON()));
		
		try (FileWriter writer = new FileWriter(getBills())) {
			
			writer.write(arr.toString());
			
		} catch (Exception ignored) {}
	
	}
	
	private static void refreshHolders() {
		
		HOLDERS.clear();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(getBills()))) {
			
			String json;
			
			try {
				StringBuilder sb = new StringBuilder();
				String line = reader.readLine();
				
				while (line != null) {
					sb.append(line);
					sb.append("\n");
					line = reader.readLine();
				}
				json = sb.toString();
			} finally {
				reader.close();
			}
			
			print(json);
			
			JSONArray array = new JSONArray(json);
			
			for (Object raw : array) {
				JSONObject o = (JSONObject) raw;
				
				Holder holder = new Holder(o.getString("name"));
				
				JSONArray arr = o.getJSONArray("bills");
				if (!arr.isEmpty()) {
					arr.forEach(obj -> holder.bills.add(Bill.parseBill((JSONObject) obj)));
				}
				
				HOLDERS.add(holder);
			}
			
		} catch (Exception ignored) {}
	}
	
	public static void print(String message) {
		System.out.print(message);
	}
}