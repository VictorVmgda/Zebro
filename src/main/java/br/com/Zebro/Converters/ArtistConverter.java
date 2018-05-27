package br.com.Zebro.Converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.wrapper.spotify.model_objects.specification.Artist;

import br.com.Zebro.Utils.SpotifyUtils;

@SuppressWarnings("rawtypes")
@FacesConverter("artistConverter")
public class ArtistConverter implements Converter{

	private SpotifyUtils spotifyUtils = new SpotifyUtils();
	
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent comp, String id) {
		return spotifyUtils.getArtistById(id);
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
		Artist artist = (Artist) value;
		return artist.getId();
	}

}
