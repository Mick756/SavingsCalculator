package com.savings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Holder {
	
	public final String name;
	public List<Bill> bills;
	
	public Holder(String name) {
		this.name = name;
		this.bills = new ArrayList<>();
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		
		obj.put("name", this.name);
		
		JSONArray array = new JSONArray();
		for (Bill bill : this.bills) {
			array.put(bill.toJSON());
		}
		obj.put("bills", array);
	
		return obj;
	}
}
