package br.com.Zebro.Beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Track;

import br.com.Zebro.Models.Mensagem;
import br.com.Zebro.Models.Usuario;
import br.com.Zebro.ServiceApi.ContatoService;
import br.com.Zebro.ServiceApi.MensagemService;
import br.com.Zebro.ServiceApi.SolicitacoesService;
import br.com.Zebro.ServiceApi.UsuarioService;
import br.com.Zebro.Utils.SpotifyUtils;

@Named
@ViewScoped
public class PerfilBean implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Inject UsuarioService usuarioService;
	@Inject MensagemService mensagemService;
	@Inject ContatoService contatoService;
	@Inject SolicitacoesService solicitacoesService;
	
	@Inject FacesContext context;
	@Inject SpotifyUtils spotifyUtils;
	
	private Usuario usuarioChat = new Usuario();
	private Usuario usuario = new Usuario();
	private Usuario selectedUsuario = new Usuario();
	
	private Track track;
	private Track musicSelectedUsuario;

	private Integer count = 0;
	
	private List<Usuario> lastFriends = new ArrayList<>();
	private List<Usuario> solicitacoes = new ArrayList<>();
	private List<Usuario> amigos = new ArrayList<>();

	private List<String> checkedArtistsString = new ArrayList<>();
	
	private List<Mensagem> friendsMessage = new ArrayList<>();

	private List<Artist> artistsSelectedUsuario = new ArrayList<>();
	private List<Artist> checkedArtists = new ArrayList<>();
	private List<Artist> usuarioArtists = new ArrayList<>();	
	
	private boolean needAutoComplete = false;
	private boolean updateImages = false;
	private boolean wanderlei = true;
	
	private UploadedFile priImage;
	private UploadedFile secImage;
	private UploadedFile terImage;
	private UploadedFile quatImage;
	private UploadedFile quinImage;

	private String logoffmessage = "Logoff efetuado com sucesso!";
	
	@PostConstruct
	public void init() {
		try {
			SpotifyUtils.refreshUser();
			
			this.usuario = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
			this.selectedUsuario = this.usuario;
			
			setTrack(spotifyUtils.getTrackById(usuario.getMusfavusuario()));
			setAmigos(contatoService.getUserFriends(usuario.getIdusuario()));
			setSolicitacoes(solicitacoesService.getUserRequests(usuario.getIdusuario()));

			setCount(solicitacoesService.getRequestCount(usuario.getIdusuario()));			
			setUsuarioArtists(spotifyUtils.getSeveralArtistsById(this.usuario.getFavoriteArtist()));

			List<Usuario> userFriendsByMessages = contatoService.getUserFriendsByMessages(this.usuario.getIdusuario());
			
			if (userFriendsByMessages.size() > 0) {
				lastFriends.addAll(userFriendsByMessages);
				setUsuarioChat(lastFriends.get(0));
			}
			
			if (spotifyUtils.getRelevantArtists().size() < 5) setNeedAutoComplete(true);
			else setNeedAutoComplete(false);
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				context.addMessage(null, new FacesMessage("Sessão expirada"));
				context.getExternalContext().getFlash().setKeepMessages(true);
				context.getExternalContext().redirect("index.xhtml");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public List<Track> completeMusic(String query){
		return spotifyUtils.searchMusics(query);
	}
	
	public List<Artist> completeArtists(String query){
		return spotifyUtils.searchArtists(query);
	}
	
	public List<Artist> getArtistsName(){
		return spotifyUtils.getSeveralArtistsById(usuario.getFavoriteArtist());
	}
	
	public void updateUserData() throws IOException {
		
		try {
			if (this.usuario.getMusfavusuario() != track.getId() && track != null) 
				this.usuario.setMusfavusuario(track.getId());
			
			
			if (!getPriImage().getFileName().isEmpty())
				usuario.setFotousuario("data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(getPriImage().getContents()));
			
			if (!getSecImage().getFileName().isEmpty()) 
				usuario.setFotosecundaria("data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(getSecImage().getContents()));
			
			if (!getTerImage().getFileName().isEmpty()) 
				usuario.setFototerciaria("data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(getTerImage().getContents()));
			
			
			if (!getQuatImage().getFileName().isEmpty()) 
				usuario.setFotoquaternaria("data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(getQuatImage().getContents()));
			
			
			if (!getQuinImage().getFileName().isEmpty()) 
				usuario.setFotoquintenaria("data:image/jpeg;base64,"+Base64.getEncoder().encodeToString(getQuinImage().getContents()));
			
			
			Calendar data = usuario.getCalendarnascusuario();
			usuario.setNascusuario(data.get(Calendar.YEAR) + "-" + (data.get(Calendar.MONTH) + 1) + "-" + data.get(Calendar.DAY_OF_MONTH));
			
			Usuario userData = new Usuario();				
			if (this.getCheckedArtists().size() > 0) {
				
				List<String> artistId = new ArrayList<>();
				
				checkedArtists.forEach((Artist art) -> artistId.add(art.getId()));
				
				this.usuario.setFavoriteArtist(artistId);
				this.usuario.setFavoriteGen(spotifyUtils.addGenresByArtistsId(artistId));
				
				userData = usuarioService.updateUserAndArtistData(this.usuario);
				
			} else if(this.checkedArtistsString.size() > 0){
				
				this.usuario.setFavoriteArtist(checkedArtistsString);
				this.usuario.setFavoriteGen(spotifyUtils.addGenresByArtistsId(checkedArtistsString));
				
				userData = usuarioService.updateUserAndArtistData(this.usuario);
			
			}else userData = usuarioService.updateUserData(usuario);
			
	
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().replace("usuario", userData);
			externalContext.redirect("busca.xhtml");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void plusCount() {
		this.count += 1;
		solicitacoes = solicitacoesService.getUserRequests(usuario.getIdusuario());
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
				quinImage = null;
				usuario.setFotoquintenaria("");
		}
		
	}
	
	public void changeOrderList() {

		Integer id = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("data"));
		
		Usuario user = new Usuario();
		int indexOf = 0;		
		
		
		for (Usuario usuario : lastFriends) {
			if (usuario.getIdusuario() == id) {
				indexOf = lastFriends.indexOf(usuario);
				user = lastFriends.get(indexOf);
			}
		}
		
		lastFriends.remove(indexOf);
		lastFriends.add(0, user);
		
	}
	
	public void chatboxFirst() {
		setUsuarioChat(lastFriends.get(0));
	}
	
	public void changeRequestStatus(Integer status, Usuario u) {
		contatoService.changeRequestStatus(status, u.getIdcontato());
		amigos.add(u);
		this.count -= 1;
		solicitacoes.remove(u);
	}
	
	public void deleteAccount() {
		this.logoffmessage = "Conta deletada com sucesso";
		boolean deleteAccount = this.usuarioService.deleteAccount(this.usuario.getIdusuario());
		
		if (deleteAccount) logoff();
		else context.addMessage(null, new FacesMessage("Não foi possível deletar sua conta.\nTente mais tarde!"));
		
	}
	
	public void removeFriend(Usuario u) {
		amigos.remove(u);
		contatoService.deleteUserFriend(u.getIdcontato());
	}
	
	public void logoff() {
		context.getExternalContext().getSessionMap().remove("usuario");
		try {
			context.addMessage(null, new FacesMessage(this.logoffmessage ));
			context.getExternalContext().getFlash().setKeepMessages(true);
			context.getExternalContext().redirect("index.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Artist> getRelevantArtists() {
		return spotifyUtils.getRelevantArtists();
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isWanderlei() {
		return wanderlei;
	}

	public void setWanderlei(boolean wanderlei) {
		this.wanderlei = wanderlei;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public List<Usuario> getAmigos() {
		return amigos;
	}

	public void setAmigos(List<Usuario> amigos) {
		this.amigos = amigos;
	}

	public List<Usuario> getSolicitacoes() {
		return solicitacoes;
	}

	public void setSolicitacoes(List<Usuario> solicitacoes) {
		this.solicitacoes = solicitacoes;
	}

	public Usuario getSelectedUsuario() {
		return selectedUsuario;
	}

	public void setSelectedUsuario(Usuario selectedUsuario) {
		this.selectedUsuario = usuarioService.getUserById(selectedUsuario.getIdusuario());		
		musicSelectedUsuario = spotifyUtils.getTrackById(this.selectedUsuario.getMusfavusuario());
		artistsSelectedUsuario = spotifyUtils.getSeveralArtistsById(this.selectedUsuario.getFavoriteArtist());
	}

	public Track getMusicSelectedUsuario() {
		return musicSelectedUsuario;
	}

	public void setMusicSelectedUsuario(Track musicSelectedUsuario) {
		this.musicSelectedUsuario = musicSelectedUsuario;
	}

	public List<Artist> getArtistsSelectedUsuario() {
		return artistsSelectedUsuario;
	}

	public void setArtistsSelectedUsuario(List<Artist> artistsSelectedUsuario) {
		this.artistsSelectedUsuario = artistsSelectedUsuario;
	}

	public boolean isUpdateImages() {
		return updateImages;
	}

	public void setUpdateImages(boolean updateImages) {
		System.out.println("Changing to " + updateImages);
		this.updateImages = updateImages;
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

	public UploadedFile getQuinImage() {
		return quinImage;
	}

	public void setQuinImage(UploadedFile quinImage) {
		this.quinImage = quinImage;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<Usuario> getLastFriends() {
		return lastFriends;
	}

	public void setLastFriends(List<Usuario> lastFriends) {
		this.lastFriends = lastFriends;
	}

	public Usuario getUsuarioChat() {
		return usuarioChat;
	}

	public void setUsuarioChat(Usuario usuarioChat) {
		if (!this.usuarioChat.equals(usuarioChat)) {
			setFriendsMessage(mensagemService.getUserMessages(usuarioChat.getIdcontato()));
			this.usuarioChat = usuarioChat;			
		}
	}

	public List<Mensagem> getFriendsMessage() {
		return friendsMessage;
	}

	public void setFriendsMessage(List<Mensagem> friendsMessage) {
		this.friendsMessage = friendsMessage;
	}

	public boolean isNeedAutoComplete() {
		return needAutoComplete;
	}

	public void setNeedAutoComplete(boolean needAutoComplete) {
		this.needAutoComplete = needAutoComplete;
	}

	public List<String> getCheckedArtistsString() {
		return checkedArtistsString;
	}

	public void setCheckedArtistsString(List<String> checkedArtistsString) {
		this.checkedArtistsString = checkedArtistsString;
	}

	public List<Artist> getCheckedArtists() {
		return checkedArtists;
	}

	public void setCheckedArtists(List<Artist> checkedArtists) {
		this.checkedArtists = checkedArtists;
	}

	public List<Artist> getUsuarioArtists() {
		return usuarioArtists;
	}

	public void setUsuarioArtists(List<Artist> usuarioArtists) {
		this.usuarioArtists = usuarioArtists;
	}
}
