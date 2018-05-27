package br.com.Zebro.Beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.Zebro.Models.Mensagem;
import br.com.Zebro.Models.Usuario;
import br.com.Zebro.ServiceApi.ContatoService;
import br.com.Zebro.ServiceApi.MensagemService;
import br.com.Zebro.ServiceApi.SolicitacoesService;
import br.com.Zebro.ServiceApi.UsuarioService;
import br.com.Zebro.Utils.SpotifyUtils;

@Named
@ViewScoped
public class ConversasBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject FacesContext context;
	
	@Inject UsuarioService usuarioService;
	@Inject MensagemService mensagemService;
	@Inject SolicitacoesService solicitacoesService;
	@Inject ContatoService contatoService;
	
	private Usuario mainUser = new Usuario();
	private Usuario selectedUser = new Usuario();
	private List<Usuario> amigos = new ArrayList<>();
	private Integer count = 0;
	private List<Usuario> filterFriends = new ArrayList<>();
	
	private List<Mensagem> messages = new ArrayList<>();
	private Mensagem mensagem = new Mensagem();
	
	@PostConstruct
	public void init() {
		try {
			
			SpotifyUtils.refreshUser();
			
			mainUser = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
			
			amigos.clear();
			setCount(solicitacoesService.getRequestCount(mainUser.getIdusuario()));
			List<Usuario> friends = contatoService.getUserFriendsByMessages(mainUser.getIdusuario());
			if (friends.size() > 0) {
				
				amigos.addAll(friends);
				filterFriends.addAll(friends);
				
				this.selectedUser = amigos.get(0);
				
				messages = mensagemService.getUserMessages(this.selectedUser.getIdcontato());
				mensagem.setFkcontato(selectedUser.getIdcontato());
			}
			
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage("Sessão expirada"));
			context.getExternalContext().getFlash().setKeepMessages(true);
			try {
				context.getExternalContext().redirect("index.xhtml");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void plusCount() {
		this.count += 1;
	}
	
	public void changeOrderList() {

		Usuario user = new Usuario();
		int indexOf = 0;
		Integer id = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("data"));
		
		for (Usuario usuario : amigos) {
			if (usuario.getIdusuario() == id) {
				indexOf = amigos.indexOf(usuario);
				System.out.println("IndexOf user: " + indexOf);
				user = amigos.get(indexOf);
			}
		}
		amigos.remove(indexOf);
		amigos.add(0, user);
		filterFriends.clear();
		filterFriends.addAll(amigos);
	}
	
	public Usuario getMainUser() {
		return mainUser;
	}

	public void setMainUser(Usuario mainUser) {
		this.mainUser = mainUser;
	}

	public Usuario getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(Usuario selectedUser) {
		
		this.selectedUser = selectedUser;
		messages = mensagemService.getUserMessages(this.selectedUser.getIdcontato());
		mensagem.setFkcontato(selectedUser.getIdcontato());
		System.out.println("Trocando usuario");
	}

	public List<Usuario> getAmigos() {
		return amigos;
	}

	public void setAmigos(List<Usuario> amigos) {
		this.amigos = amigos;
	}

	public List<Mensagem> getMessages() {
		return messages;
	}

	public void setMessages(List<Mensagem> messages) {
		this.messages = messages;
	}

	public Mensagem getMensagem() {
		return mensagem;
	}

	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}

	public List<Usuario> getFilterFriends() {
		return filterFriends;
	}

	public void setFilterFriends(List<Usuario> filterFriends) {
		this.filterFriends = filterFriends;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
