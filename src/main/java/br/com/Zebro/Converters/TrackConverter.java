package br.com.Zebro.Converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.wrapper.spotify.model_objects.specification.Track;

import br.com.Zebro.Utils.SpotifyUtils;

@SuppressWarnings("rawtypes")
@FacesConverter(value="trackConverter", forClass=Track.class)
public class TrackConverter implements Converter{

	private SpotifyUtils utils = new SpotifyUtils();
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String id) {

		Track track = utils.getTrackById(id);
		return track;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object track) {
		Track music = (Track) track;

		return music.getId();
	}

}
