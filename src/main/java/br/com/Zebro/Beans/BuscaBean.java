package br.com.Zebro.Beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SlideEndEvent;

import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Track;

import br.com.Zebro.Models.Mensagem;
import br.com.Zebro.Models.Usuario;
import br.com.Zebro.ServiceApi.ContatoService;
import br.com.Zebro.ServiceApi.MensagemService;
import br.com.Zebro.ServiceApi.SolicitacoesService;
import br.com.Zebro.ServiceApi.UsuarioService;
import br.com.Zebro.Utils.SpotifyUtils;

@ViewScoped
@Named
public class BuscaBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Artist> artists = new ArrayList<>();
	private Track music;
	
	private Integer count = 0;
	private List<Usuario> usuarios = new ArrayList<>();
	private Usuario u = new Usuario();
	private Usuario usuario = new Usuario();
	private List<Usuario> lastFriends = new ArrayList<>();
	private Usuario usuarioChat = new Usuario();
	
	private List<Mensagem> friendsMessage = new ArrayList<>();
	
	private double distance = 50.0;
	
	@Inject UsuarioService usuarioService;
	@Inject ContatoService contatoService;
	@Inject SolicitacoesService solicitacoesService;
	@Inject MensagemService mensagensService;
	
	@Inject FacesContext context;
	@Inject SpotifyUtils spotifyUtils;
	
	@PostConstruct
	public void init() {
		try {
			
			SpotifyUtils.refreshUser();
			ExternalContext externalContext = context.getExternalContext();
			setU((Usuario) externalContext.getSessionMap().get("usuario"));
			
			if (u.getIdusuario() == null) {
				externalContext.getSessionMap().replace("usuario", this.u);
				this.u.setIdusuario(usuarioService.getUserData(this.u.getSpotifyid()).getIdusuario());
			}
			
			setCount(solicitacoesService.getRequestCount(u.getIdusuario()));
			getUsers();
			
			List<Usuario> userFriendsByMessages = contatoService.getUserFriendsByMessages(this.u.getIdusuario());
			
			if (userFriendsByMessages.size() > 0) {
				lastFriends.addAll(userFriendsByMessages);
				setUsuarioChat(lastFriends.get(0));
			}
			
			if (usuarios.size() != 0) {
				setUsuario(usuarios.get(0));			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null, new FacesMessage("Sessão expirada"));
			context.getExternalContext().getFlash().setKeepMessages(true);
			try {
				context.getExternalContext().redirect("/Zebro/home");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

	public void plusCount() {
		this.count += 1;
		getUsers();
		setUsuario(usuarios.get(0));
	}
	
	public void getUsers() {
		setUsuarios(usuarioService.getShowUsuarios(u, String.valueOf(getDistance())));
	}
	
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getU() {
		return u;
	}

	public void setU(Usuario u) {
		this.u = u;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void sendRequest(Usuario user) {
		usuarios.remove(user);
		try {
			contatoService.sendFriendRequest(u.getIdusuario(), user.getIdusuario(),0);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			setUsuario(usuarios.get(0));			
		}
	}
	
	public void setUsuario(Usuario usuario) {
		if (!this.usuario.equals(usuario)) {			
			this.usuario = usuario;
			Usuario userById = usuarioService.getUserById(this.usuario.getIdusuario());
			setArtists(spotifyUtils.getSeveralArtistsById(userById.getFavoriteArtist()));
			setMusic(spotifyUtils.getTrackById(usuario.getMusfavusuario()));
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
	
	public void onSlideEnd(SlideEndEvent ev) {
		setDistance((double) ev.getValue());
		System.out.println(getDistance());
		getUsers();
		try {
			setUsuario(getUsuarios().get(0));
		}catch (Exception e) {
			setUsuario(new Usuario());
		}
	}
	
	public void chatboxFirst() {
		setUsuarioChat(lastFriends.get(0));
	}
	
	public void ignoreUser(Usuario usuario) {
		usuarios.remove(usuario);
		try {
			contatoService.sendFriendRequest(this.u.getIdusuario(), usuario.getIdusuario(),2);			
		} finally {
			setUsuario(usuarios.get(0));
		}
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public List<Artist> getArtists() {
		return artists;
	}

	public void setArtists(List<Artist> artists) {
		this.artists = artists;
	}

	public Track getMusic() {
		return music;
	}

	public void setMusic(Track music) {
		this.music = music;
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
			setFriendsMessage(mensagensService.getUserMessages(usuarioChat.getIdcontato()));
			this.usuarioChat = usuarioChat;			
		}
	}

	public List<Mensagem> getFriendsMessage() {
		return friendsMessage;
	}

	public void setFriendsMessage(List<Mensagem> friendsMessage) {
		this.friendsMessage = friendsMessage;
	}

}
