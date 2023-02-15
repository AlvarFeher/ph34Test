package persistence.API;

import java.io.IOException;

public class CharacterApiDAO {

    public String getCharacters() throws IOException {
        ApiHelper apiHelper = new ApiHelper();
        String url = "https://balandrau.salle.url.edu/dpoo/S1-Project_ICE42/characters";
        System.out.println(apiHelper.getFromUrl(url));
        return apiHelper.getFromUrl(url);
    }

}
