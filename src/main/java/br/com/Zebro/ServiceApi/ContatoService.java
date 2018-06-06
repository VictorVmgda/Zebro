package br.com.Zebro.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import br.com.Zebro.Models.Usuario;

@RequestScoped
public class ContatoService extends ServiceApi {

	public List<Usuario> getUserFriends(Integer id) {

		List<Usuario> list = new ArrayList<>();

		Client c = Client.create();
		WebResource resource = c.resource(URL_SERVICE + "me/friends/" + id);
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

	public List<Usuario> getUserFriendsByMessages(Integer id) {

		List<Usuario> list = new ArrayList<>();

		Client c = Client.create();
		WebResource resource = c.resource(URL_SERVICE + "me/friends/message/" + id);
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

	// POST METHODS

	public boolean sendFriendRequest(Integer idUsuarioSend, Integer idUsuarioReceive, Integer status) {
		Client c = Client.create();
		WebResource resource = c.resource(URL_SERVICE + "me/requests");

		MultivaluedMap<String, String> formData = new MultivaluedMapImpl<>();
		formData.add("idUsuario", idUsuarioSend.toString());
		formData.add("idAmigo", idUsuarioReceive.toString());
		formData.add("constatus", String.valueOf(status));

		ClientResponse post = resource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);

		return post.getStatus() == 200;

	}

	// PUT METHODS

	public void changeRequestStatus(Integer status, Integer idcontato) {

		Client c = Client.create();
		WebResource resource = c.resource(URL_SERVICE + "me/requests");

		MultivaluedMap<String, String> map = new MultivaluedMapImpl<String, String>();
		map.add("idcontato", String.valueOf(idcontato));
		map.add("constatus", String.valueOf(status));
		ClientResponse put = resource.accept("application/x-www-form-urlencoded").put(ClientResponse.class, map);
		System.out.println(put.getStatus());

	}
	
	//DELETE METHODS
	
	public int deleteUserFriend(Integer idcontato) {
		Client c = Client.create();
		WebResource resource = c.resource(URL_SERVICE + "me/friends");
		MultivaluedMap<String, String> map = new MultivaluedMapImpl<String, String>();
		map.add("idcontato", String.valueOf(idcontato));
		
		ClientResponse delete = resource.accept("application/x-www-form-urlencoded").delete(ClientResponse.class, map);
		return delete.getStatus();
	}

}
