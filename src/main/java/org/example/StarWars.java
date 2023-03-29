package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class StarWars {
    public static void main(String[] args) {
        String link = "https://swapi.py4e.com/api/films/1/";
        System.out.println(grabTitle(link));

        String[] titles = getAllMovieTitles(10);
        for (String t : titles){
            System.out.println(t);
        }
    }

    //GOAL: Given a personID, return the list of movie titles they were in
    /*
        1. Build the URL
		2. Go to the person (with the API?)
			We use MakeGETRequest
		3. Now we have JSON
		4. We make a DTO
		5. THen we map the JSON to the DTO
		6. Find the Film Links
		7. Click each Link
			Then get the titles
     */
    public static String[] getAllMovieTitles(int personID){
        String URL = "https://swapi.py4e.com/api/people/" + personID + "/";
        String jsonResponse = APIConnector.makeGETRequest(URL);
        ObjectMapper mapper = new ObjectMapper();
        PeopleDTO dto = null;
        try {
            dto = mapper.readValue(jsonResponse, PeopleDTO.class);
        } catch (Exception e){
            return null;
        }
        List<String> filmURLs = dto.getFilms();
        //basket (String[] -> need a size?)
        //how big should basket be? same number as URLS?
        //dt[] name = new dt[size]
        String[] basket = new String[filmURLs.size()];
        //loop
        for (int i = 0; i < filmURLs.size(); i++) {
            //fill up the basket (with titles?)
            String title = grabTitle(filmURLs.get(i));
            basket[i] = title;
        }
        return basket;
    }

    //GOAL: Given a film URL, return the title
    public static String grabTitle(String filmURL){
        /*
        I need: JSON and a DTO
		Where does the JSON come from?
			From the API
				How do we get it?
					URL (Make a GET request with http)
		Once I have the JSON, I can build the DTO
		Then I can use the Mapper
		THEN I can get the title from the DTO
         */
        String response = APIConnector.makeGETRequest(filmURL);
        ObjectMapper mapper = new ObjectMapper();
        try {
            FilmDTO filmDTO = mapper.readValue(response, FilmDTO.class);
            return filmDTO.getTitle();
        } catch (Exception e){
            return "Title Not Found";
        }
    }
}