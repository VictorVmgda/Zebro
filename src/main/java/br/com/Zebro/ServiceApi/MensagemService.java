package br.com.Zebro.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import br.com.Zebro.Models.Mensagem;

@RequestScoped
public class MensagemService extends ServiceApi{

	
	public List<Mensagem> getUserMessages(Integer idusuario){
		List<Mensagem> list = new ArrayList<>();
		
		Client c = Client.create();
		WebResource resource = c.resource(URL_SERVICE + "me/message/" + idusuario);
		String jsonString = resource.get(String.class);
		
		Gson json = getGson();
		JsonParser parser = new JsonParser();
		JsonArray data = parser.parse(jsonString).getAsJsonArray();
		
		for (JsonElement objeto : data) {
			Mensagem mensagem = json.fromJson(objeto, Mensagem.class);
			list.add(mensagem);
		}
	
		return list;
	}
	
}
