package br.com.Zebro.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.primefaces.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import br.com.Zebro.Models.Usuario;

@RequestScoped
public class SolicitacoesService extends ServiceApi {

	public List<Usuario> getUserRequests(Integer usuarioid) {

		List<Usuario> list = new ArrayList<>();

		Client c = Client.create();
		WebResource resource = c.resource(URL_SERVICE + "me/requests/" + usuarioid);
		String jsonString = resource.get(String.class);

		Gson json = getGson();
		JsonParser parser = new JsonParser();
		JsonArray data = parser.parse(jsonString).getAsJsonArray();

		for (JsonElement objeto : data) {
			Usuario usuario = json.fromJson(objeto, Usuario.class);
			list.add(usuario);
		}

		return list;

	}

	public Integer getRequestCount(Integer idusuario) {

		Client c = Client.create();
		WebResource resource = c.resource(URL_SERVICE + "me/requests/count/" + idusuario);
		String jsonString = resource.get(String.class);

		JSONObject jsonObject = new JSONObject(jsonString);
		return jsonObject.getInt("count");
	}

	
	
}
