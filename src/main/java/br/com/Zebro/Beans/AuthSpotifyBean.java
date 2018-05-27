package br.com.Zebro.Beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

import com.google.gson.Gson;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.User;

import br.com.Zebro.Models.Usuario;
import br.com.Zebro.ServiceApi.UsuarioService;
import br.com.Zebro.Utils.SpotifyUtils;

@ViewScoped
@Named
public class AuthSpotifyBean implements Serializable {

	@Inject UsuarioService zebroApi;
	@Inject SpotifyUtils spotifyUtil;
	@Inject FacesContext context;

	private static final long serialVersionUID = 1L;

	private Usuario usuario = new Usuario();
	private boolean newAccount = false;
	private List<Artist> relevantArtists = new ArrayList<>();
	private List<Artist> selectedArtists = new ArrayList<>();

	private String code;

	private Track musfavusuario;

	private UploadedFile priImage;
	private UploadedFile secImage;
	private UploadedFile terImage;
	private UploadedFile quatImage;
	private UploadedFile fifthImage;

	public void authenticateURI() {
		spotifyUtil.authorizationCodeUri();
	}

	public void authorizationCode() throws InterruptedException, ExecutionException {

		this.code = context.getExternalContext().getRequestParameterMap().get("code");
		spotifyUtil.authenticateUser(code);

		// Get user data
		User user = spotifyUtil.getAuthenticateUserData();
		try {
			Usuario data = zebroApi.getUserData(user.getId());
			if (data != null) {

				Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
				sessionMap.put("usuario", data);
				context.getExternalContext().redirect("/Zebro/pessoas");
			}

			usuario.setEmail(user.getEmail());
			usuario.setNome(user.getDisplayName());
			usuario.setSpotifyid(user.getId());
			Date date = Date.valueOf(user.getBirthdate());

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			usuario.setCalendarnascusuario(cal);

			if (user.getImages().length == 0) {
				usuario.setFotousuario("http://localhost:8080/Zebro/resources/img/undefined.png");
			} else {
				usuario.setFotousuario(user.getImages()[0].getUrl());
			}

			// Get relevant artist from user
			setRelevantArtists(spotifyUtil.getRelevantArtists());

			if (getRelevantArtists().size() < 5) {
				setNewAccount(true);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void finishForm() {

		if (!priImage.getFileName().isEmpty()) {
			usuario.setFotousuario(
					"data:image/jpeg;base64," + Base64.getEncoder().encodeToString(priImage.getContents()));
		} else if (!usuario.getFotousuario().equals("http://localhost:8080/Zebro/resources/img/undefined.png")) {
			try {
				URL url = new URL(usuario.getFotousuario());

				InputStream inputStream = url.openConnection().getInputStream();
				byte[] byteArray = IOUtils.toByteArray(inputStream);
				System.out.println(byteArray);
				usuario.setFotousuario("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(byteArray));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			usuario.setFotousuario(
					"http://res.cloudinary.com/dkpphc5ei/image/upload/v1524174366/gheedahrkszsiil8yhli.png");
		}

		if (!secImage.getFileName().isEmpty()) {
			usuario.setFotosecundaria(
					"data:image/jpeg;base64," + Base64.getEncoder().encodeToString(secImage.getContents()));
		}
		if (!terImage.getFileName().isEmpty()) {
			usuario.setFototerciaria(
					"data:image/jpeg;base64," + Base64.getEncoder().encodeToString(terImage.getContents()));
		}
		if (!quatImage.getFileName().isEmpty()) {
			usuario.setFotoquaternaria(
					"data:image/jpeg;base64," + Base64.getEncoder().encodeToString(quatImage.getContents()));
		}
		if (!fifthImage.getFileName().isEmpty()) {
			usuario.setFotoquintenaria(
					"data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fifthImage.getContents()));
		}

		usuario.setMusfavusuario(musfavusuario.getId());

		Calendar data = usuario.getCalendarnascusuario();
		usuario.setNascusuario(
				data.get(Calendar.YEAR) + "-" + (data.get(Calendar.MONTH) + 1) + "-" + data.get(Calendar.DAY_OF_MONTH));

		List<String> artistId = new ArrayList<>();
		for (Artist artist : selectedArtists) {
			artistId.add(artist.getId());
		}

		if (usuario.getFavoriteArtist().size() == 0) {
			usuario.setFavoriteArtist(artistId);
			usuario.setFavoriteGen(spotifyUtil.addGenresIntoUser(this.selectedArtists));
		} else {
			Set<String> addGenresByArtistsId = spotifyUtil.addGenresByArtistsId(usuario.getFavoriteArtist());
			usuario.setFavoriteGen(addGenresByArtistsId);
		}
		System.out.println(usuario.toJson());
		String postUser = zebroApi.postUser(usuario.toJson());

		Usuario fromJson = new Gson().fromJson(postUser, Usuario.class);
		Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
		sessionMap.put("usuario", fromJson);

		try {
			context.getExternalContext().redirect("/Zebro/perfil");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public List<Track> completeMusic(String query) {
		return spotifyUtil.searchMusics(query);
	}

	public List<Artist> completeArtists(String query) {
		return spotifyUtil.searchArtists(query);
	}

	public void removePhoto(Integer num) {

		switch (num) {
		case 0:
			secImage = null;
			usuario.setFotosecundaria("");
			break;
		case 1:
			terImage = null;
			usuario.setFototerciaria("");
			break;
		case 2:
			quatImage = null;
			usuario.setFotoquaternaria("");
		case 3:
			fifthImage = null;
			usuario.setFotoquintenaria("");
		}

	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isNewAccount() {
		return newAccount;
	}

	public void setNewAccount(boolean newAccount) {
		this.newAccount = newAccount;
	}

	public List<Artist> getRelevantArtists() {
		return relevantArtists;
	}

	public void setRelevantArtists(List<Artist> relevantArtists) {
		this.relevantArtists = relevantArtists;
	}

	public UploadedFile getPriImage() {
		return priImage;
	}

	public void setPriImage(UploadedFile priImage) {
		this.priImage = priImage;
	}

	public UploadedFile getSecImage() {
		return secImage;
	}

	public void setSecImage(UploadedFile secImage) {
		this.secImage = secImage;
	}

	public UploadedFile getTerImage() {
		return terImage;
	}

	public void setTerImage(UploadedFile terImage) {
		this.terImage = terImage;
	}

	public UploadedFile getQuatImage() {
		return quatImage;
	}

	public void setQuatImage(UploadedFile quatImage) {
		this.quatImage = quatImage;
	}

	public UploadedFile getFifthImage() {
		return fifthImage;
	}

	public void setFifthImage(UploadedFile fifthImage) {
		this.fifthImage = fifthImage;
	}

	public Track getMusfavusuario() {
		return musfavusuario;
	}

	public void setMusfavusuario(Track musfavusuario) {
		this.musfavusuario = musfavusuario;
	}

	public List<Artist> getSelectedArtists() {
		return selectedArtists;
	}

	public void setSelectedArtists(List<Artist> selectedArtists) {
		this.selectedArtists = selectedArtists;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
