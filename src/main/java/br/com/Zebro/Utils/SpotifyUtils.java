package br.com.Zebro.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import com.wrapper.spotify.requests.data.artists.GetSeveralArtistsRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

@SessionScoped
public class SpotifyUtils implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private final static String clientId = "2980bea5dc444f0480fd08e5ed426a85";
	private final static String clientSecret = "b2c26811fa3b4051a480df9624865609";
	private final static URI redirectUri = URI.create("http://localhost:8080/Zebro/cadastro.xhtml");
	
	private final static SpotifyApi api = SpotifyApi.builder().setClientId(clientId).setClientSecret(clientSecret)
			.setRedirectUri(redirectUri).build();
	
	private static final AuthorizationCodeUriRequest authorizationCodeUriRequest = api.authorizationCodeUri()
			.scope("user-top-read,user-read-private,user-read-email,user-read-birthdate,user-library-read,streaming").build();
	
	private static String refreshCode;
	private static String accessCode;
	
	//Generate Login URI 
	public void authorizationCodeUri() {
		final URI uri = authorizationCodeUriRequest.execute();
		System.out.println("URI: " + uri.toString());

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(uri.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void refreshUser() {
		System.out.println("Dando refresh: " + refreshCode);
		
		AuthorizationCodeRefreshRequest authCodeRefresh = 
				api.authorizationCodeRefresh(clientId, clientSecret, refreshCode).build();
		Future<AuthorizationCodeCredentials> execute = authCodeRefresh.executeAsync();
		AuthorizationCodeCredentials authorizationCodeCredentials;
		try {
			authorizationCodeCredentials = execute.get();
			
			String accessToken = authorizationCodeCredentials.getAccessToken();
			api.setAccessToken(accessToken);
			setAccessCode(accessToken);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public void authenticateUser(String code) {
		AuthorizationCodeRequest authorizationCodeRequest = api.authorizationCode(code).build();
		
		try {
			final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

			api.setAccessToken(authorizationCodeCredentials.getAccessToken());
			api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

			setAccessCode(authorizationCodeCredentials.getAccessToken());
			setRefreshCode(authorizationCodeCredentials.getRefreshToken());

			System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn() + "\nAccess Token: "
					+ api.getAccessToken() + "\nRefresh Token: " + api.getRefreshToken());

		} catch (IOException | SpotifyWebApiException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public User getAuthenticateUserData() {
		GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = api.getCurrentUsersProfile().build();
		try {
			User user = getCurrentUsersProfileRequest.execute();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Track> searchMusics(String query) {
		List<Track> result = new ArrayList<>();
		SearchTracksRequest searchTracksRequest = api.searchTracks(query)
		          .limit(5).offset(0).build();
		
		Paging<Track> trackPaging;
		try {
			trackPaging = searchTracksRequest.execute();
			Track[] items = trackPaging.getItems();
			for (Track item : items) {
				result.add(item);
			}
		} catch (SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Track getTrackById(String id){
		
		GetTrackRequest request = api.getTrack(id).build();
		Track track;
		try {
			track = request.execute();
			return track;
		} catch (SpotifyWebApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public Artist getArtistById(String id) {
		
		GetArtistRequest request = api.getArtist(id).build();
		
		try {
			Artist execute = request.execute();
			return execute;
		} catch (SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public List<Artist> getSeveralArtistsById(List<String> ids){
		
		List<Artist> artistsList = new ArrayList<>();
		System.out.println(ids.size());
		String[] idsArray = new String[ids.size()];
		idsArray = ids.toArray(idsArray);
		
		GetSeveralArtistsRequest several = api.getSeveralArtists(idsArray).build();
		
		try {
			Artist[] artists = several.execute();
			
			for (Artist artist : artists) {
				artistsList.add(artist);
			}
		
		} catch (SpotifyWebApiException | IOException e1) {
			e1.printStackTrace();
		}
		
		return artistsList;
	}
	
	public Set<String> addGenresIntoUser(List<Artist> artistas) {
		
		Set<String> generes = new HashSet<>();
	
		for (Artist artist : artistas) {
			String[] genres = artist.getGenres();
			for (String string : genres) {
				generes.add(string);				
			}
		}
		
		return generes;
	}
	
	public Set<String> addGenresByArtistsId(List<String> artistas){
		Set<String> generes = new HashSet<>();
		
		GetSeveralArtistsRequest request = api.getSeveralArtists(artistas.toArray(new String[artistas.size()])).build();
		
		Artist[] artists;
		try {
			artists = request.execute();
			
			for (Artist artist : artists) {
				String[] genres = artist.getGenres();
				for (String string : genres) {
					generes.add(string);				
				}
			}
			
		} catch (SpotifyWebApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return generes;
	}
	
	public List<Artist> searchArtists(String query){
		
		List<Artist> result = new ArrayList<>();
		SearchArtistsRequest artistReq = api.searchArtists(query)
				.limit(10).offset(0).build();
		
		try {
			Paging<Artist> execute = artistReq.execute();
			for (Artist artist : execute.getItems()) {
				result.add(artist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<Artist> getRelevantArtists(){
		List<Artist> artistas = new ArrayList<>();
		
		GetUsersTopArtistsRequest topuser = api.getUsersTopArtists().time_range("medium_term").limit(5).offset(0).build();		
		try {
			Artist[] artists = topuser.execute().getItems();
			for (Artist artist : artists) {
				artistas.add(artist);
			}
		} catch (SpotifyWebApiException | IOException e) {
			e.printStackTrace();
		}

		return artistas;
		
	}

	public String getRefreshCode() {
		return refreshCode;
	}

	public static void setRefreshCode(String refreshCode) {
		SpotifyUtils.refreshCode = refreshCode;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public static void setAccessCode(String accessCode) {
		SpotifyUtils.accessCode = accessCode;
	}
	
}
