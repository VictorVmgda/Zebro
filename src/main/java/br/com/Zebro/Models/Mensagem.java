package br.com.Zebro.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.google.gson.Gson;

public class Mensagem {

	private Integer idmensagem;
	private Integer fkusuario;
	private Integer fkcontato;
	private String msgtexto;
	private String msgdata;
	
	public Integer getIdmensagem() {
		return idmensagem;
	}
	
	public void setIdmensagem(Integer idmensagem) {
		this.idmensagem = idmensagem;
	}
	
	public Integer getFkusuariosend() {
		return fkusuario;
	}
	
	public void setFkusuariosend(Integer fkusuariosend) {
		this.fkusuario = fkusuariosend;
	}
	
	public Integer getFkcontato() {
		return fkcontato;
	}
	
	public void setFkcontato(Integer fkcontato) {
		this.fkcontato = fkcontato;
	}
	
	public String getMsgText() {
		return msgtexto;
	}
	
	public void setMsgText(String msgText) {
		this.msgtexto = msgText;
	}
	
	public String getMsgData() {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(format.parse(msgdata));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return format.format(cal.getTime());
	}
	
	public void setMsgData(String msgData) {
		this.msgdata = msgData;
	}

	@Override
	public String toString() {
		return "Mensagem [idmensagem=" + idmensagem + ", fkusuariosend=" + fkusuario + ", fkcontato=" + fkcontato
				+ ", msgTexto=" + msgtexto + ", msgData=" + msgdata + "]";
	}
	
	public String toJson() {
		return new Gson().toJson(this);
	}
	
}
