package br.com.Zebro.ServiceApi;

import com.google.gson.Gson;

public class ServiceApi {

	protected final static String URL_SERVICE = "https://projectzebro.herokuapp.com/";

	protected Gson gson = new Gson();		


	protected Gson getGson() {
		return gson;
	}
	
	
}
