package br.com.Zebro.ServiceApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.MediaType;

import org.primefaces.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import br.com.Zebro.Models.Usuario;

@RequestScoped
public class UsuarioService extends ServiceApi {

	public Usuario getUserById(Integer id) {

		Client c = Client.create();
		WebResource resource = c.resource(URL_SERVICE + "users/" + id);

		String jsonUsuario = resource.get(String.class);

		JSONObject jsonObject = new JSONObject(jsonUsuario);
		String favoriteart = jsonObject.getString("favoriteartist");
		String[] artArray = favoriteart.substring(1, favoriteart.length() - 1).split(",");

		Usuario usuario = getGson().fromJson(jsonUsuario, Usuario.class);
		usuario.setFavoriteArtist(Arrays.asList(artArray));

		return usuario;
	}

	public Usuario getUserData(String sId) {

		Usuario usuario = new Usuario();
		Client c = Client.create();

		WebResource resource = c.resource(URL_SERVICE + "me?id=" + sId);
		String jsonString = resource.get(String.class);

		try {
			JSONObject u = new JSONObject(jsonString);
			String artists = u.getString("favoriteartist");
			String[] artArray = artists.substring(1, artists.length() - 1).split(",");

			usuario = getGson().fromJson(jsonString, Usuario.class);
			usuario.setFavoriteArtist(Arrays.asList(artArray));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return usuario;
	}

	public List<Usuario> getShowUsuarios(Usuario u, String distance) {

		List<Usuario> list = new ArrayList<>();
		Client c = Client.create();
		WebResource resource = c
				.resource(URL_SERVICE + "users?idusuario=" + u.getIdusuario() + "&lat=" + u.getLatusuario() + "&lon="
						+ u.getLonusuario() + "&dist=" + Double.parseDouble(distance) + "&limit=100");

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

	public String postUser(String json) {

		Client c = Client.create();
		WebResource resource = c.resource(URL_SERVICE + "me");

		String post = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(String.class,
				json);
		return post;

	}
	
	//PUT METHODS
	
	public Usuario updateUserData(Usuario u) {
		Client c = Client.create();
		WebResource resource = c.resource(URL_SERVICE + "me");
		
		String json = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
					.put(String.class, u.toJson());
		Usuario usuario = getGson().fromJson(json, Usuario.class);
		return usuario;
	}
	
	
	public Usuario updateUserAndArtistData(Usuario u) {
		Client c = Client.create();
		WebResource resource = c.resource(URL_SERVICE + "me/complete");
		
		String json = resource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
					.put(String.class, u.toJson());
		
		Usuario usuario = getGson().fromJson(json, Usuario.class);
		return usuario;
	}

}
