package br.com.Zebro.Models;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class Usuario {

	@Expose private Integer idusuario;
	@Expose private Integer sexousuario;
	@Expose private String nomeusuario;
	@Expose private String descusuario;
	
	@Expose private String fotousuario;
	@Expose private String fotosecundaria;
	@Expose private String fototerciaria;
	@Expose private String fotoquaternaria;
	@Expose private String fotoquintenaria;
	
	@Expose private String latusuario;
	@Expose private String lonusuario;
	@Expose private String spotifyid;
	@Expose private String emailusuario;
	
	@Expose private String nascusuario;
	@Expose private String musfavusuario;
	
	@Expose private List<String> favoriteArtist;
	@Expose private Set<String> favoriteGen;

	private Integer messageCount = 0;
	private Integer idcontato;

	private String distance;

	private Calendar calendarnascusuario = Calendar.getInstance();
	

	public String getNome() {
		return nomeusuario;
	}

	public String getPrincipalName() {
		String[] split = nomeusuario.split(" ");
		
		return split[0];
	}
	
	public void setNome(String nome) {
		this.nomeusuario = nome;
	}

	public String getDesc() {
		return descusuario;
	}

	public void setDesc(String desc) {
		this.descusuario = desc;
	}

	public void plusMessageCount() {
		this.messageCount += 1;
	}
	
	public void emptyMessageCount() {
		this.messageCount = 0;
	}

	public Integer getIdade() {
		try {
			Date date = Date.valueOf(nascusuario);			
			calendarnascusuario.setTime(date);
			Integer idade = Calendar.getInstance().get(Calendar.YEAR) - calendarnascusuario.get(Calendar.YEAR);
			if (calendarnascusuario.get(Calendar.MONTH) - Calendar.getInstance().get(Calendar.MONTH) <= 0 && 
					calendarnascusuario.get(Calendar.DAY_OF_MONTH) - Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 0) {
				idade -= 1;
			}
			return idade;
		} catch (Exception e) {
			return null;
		}
	}

	public String getSpotifyid() {
		return spotifyid;
	}

	public void setSpotifyid(String spotifyid) {
		this.spotifyid = spotifyid;
	}

	public String getEmail() {
		return emailusuario;
	}

	public void setEmail(String email) {
		this.emailusuario = email;
	}

	public String getMusfavusuario() {
		return musfavusuario;
	}

	public void setMusfavusuario(String favoriteMusic) {
		this.musfavusuario = favoriteMusic;
	}

	public Set<String> getFavoriteGen() {
		return favoriteGen;
	}

	public void setFavoriteGen(Set<String> favoriteGen) {
		this.favoriteGen = favoriteGen;
	}

	public List<String> getFavoriteArtist() {
		return favoriteArtist;
	}

	public void setFavoriteArtist(List<String> favoriteArtist) {
		this.favoriteArtist = favoriteArtist;
	}



	public String getLatusuario() {
		return latusuario;
	}

	public void setLatusuario(String latusuario) {
		this.latusuario = latusuario;
	}

	public String getLonusuario() {
		return lonusuario;
	}

	public void setLonusuario(String lonusuario) {
		this.lonusuario = lonusuario;
	}
	
	public String toJson() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}

	public Integer getSexousuario() {
		return sexousuario;
	}

	public void setSexousuario(Integer sexousuario) {
		this.sexousuario = sexousuario;
	}

	public String getStringSexo() {
		String sexo = "";
		switch(this.sexousuario) {
			
			case 0:
				sexo = "Masculino";
				break;
			case 1:
				sexo = "Feminino";
				break;
				
		}
		return sexo;
	}

	public String getFotousuario() {
		System.out.println(this.fotousuario);
		if (this.fotousuario != null) {
			return fotousuario;			
		}else {
			return "http://res.cloudinary.com/dkpphc5ei/image/upload/v1524159765/sf85eco0zbzuibv7ebhm.jpg";
		}
	}

	public void setFotousuario(String fotousuario) {
		this.fotousuario = fotousuario;
	}

	public Integer getIdcontato() {
		return idcontato;
	}

	public void setIdcontato(Integer idcontato) {
		this.idcontato = idcontato;
	}

	public Integer getIdusuario() {
		return idusuario;
	}

	public void setIdusuario(Integer idusuario) {
		this.idusuario = idusuario;
	}

	public String getFotosecundaria() {
		return fotosecundaria;
	}

	public void setFotosecundaria(String fotosecundaria) {
		this.fotosecundaria = fotosecundaria;
	}

	public String getFototerciaria() {
		return fototerciaria;
	}

	public void setFototerciaria(String fototerciaria) {
		this.fototerciaria = fototerciaria;
	}

	public String getFotoquaternaria() {
		return fotoquaternaria;
	}

	public void setFotoquaternaria(String fotoquaternaria) {
		this.fotoquaternaria = fotoquaternaria;
	}

	public String getFotoquintenaria() {
		return fotoquintenaria;
	}

	public void setFotoquintenaria(String fotoquintenaria) {
		this.fotoquintenaria = fotoquintenaria;
	}

	public String getNascusuario() {
		return nascusuario;
	}

	public void setNascusuario(String nascusuario) {
		this.nascusuario = nascusuario;
	}

	public Calendar getCalendarnascusuario() {
		return calendarnascusuario;
	}

	public void setCalendarnascusuario(Calendar calendarnascusuario) {
		this.calendarnascusuario = calendarnascusuario;
	}
	
	public Integer getDistance() {
		try {
			return Double.valueOf(distance).intValue();			
		} catch (Exception e) {
			return null;
		}
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idusuario == null) ? 0 : idusuario.hashCode());
		result = prime * result + ((spotifyid == null) ? 0 : spotifyid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (idusuario == null) {
			if (other.idusuario != null)
				return false;
		} else if (!idusuario.equals(other.idusuario))
			return false;
		if (spotifyid == null) {
			if (other.spotifyid != null)
				return false;
		} else if (!spotifyid.equals(other.spotifyid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Usuario [idusuario=" + getIdusuario() + ", nomeusuario=" + nomeusuario + ", descusuario=" + descusuario
				+ ", fotousuario=" + fotousuario + ", latusuario=" + latusuario
				+ ", lonusuario=" + lonusuario + ", spotifyid=" + spotifyid + ", emailusuario="
				+ emailusuario + ", sexousuario=" + sexousuario + ", idcontato=" + idcontato  + ", musfavusuario=" + musfavusuario + ", favoriteGen=" + favoriteGen + ", favoriteArtist="
				+ favoriteArtist + "]";
	}

	public Integer getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(Integer messageCount) {
		this.messageCount = messageCount;
	}
	
}
