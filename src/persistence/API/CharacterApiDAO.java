package persistence.API;

import java.io.IOException;

public class CharacterApiDAO {
    private final ApiHelper apiHelper = new ApiHelper();

    public CharacterApiDAO() throws IOException {
    }

    public String getAllCharacters() throws IOException {
        String url = "https://balandrau.salle.url.edu/dpoo/S1-Project_ICE42/characters";
        System.out.println(apiHelper.getFromUrl(url));
        return apiHelper.getFromUrl(url);
    }
    public String getCharactersByName(String name) throws IOException {
        String url = "https://balandrau.salle.url.edu/dpoo/S1-Project_ICE42/characters?player="+name;
        System.out.println(apiHelper.getFromUrl(url));
        return apiHelper.getFromUrl(url);
    }

    public String getCharactersById(int id) throws IOException {
        String url = "https://balandrau.salle.url.edu/dpoo/S1-Project_ICE42/characters?player="+id;
        System.out.println(apiHelper.getFromUrl(url));
        return apiHelper.getFromUrl(url);
    }


    public String createCharacter(Character character) throws IOException {
        String url = "https://balandrau.salle.url.edu/dpoo/S1-Project_ICE42/characters";
        String body = character.toString(); // convert to Json format
        return apiHelper.postToUrl(url,body);
    }

    public String deleteCharacterByName(String name) throws IOException {
        String url = "https://balandrau.salle.url.edu/dpoo/S1-Project_ICE42/characters?name="+name;
        System.out.println(apiHelper.getFromUrl(url));
        return apiHelper.deleteFromUrl(url);
    }

    public String deleteCharacterById(int id) throws IOException {
        String url = "https://balandrau.salle.url.edu/dpoo/S1-Project_ICE42/characters?name="+id;
        System.out.println(apiHelper.getFromUrl(url));
        return apiHelper.deleteFromUrl(url);
    }

}
