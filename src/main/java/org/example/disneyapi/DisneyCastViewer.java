package org.example.disneyapi;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.stream.events.Characters;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DisneyCastViewer {
    final static String ROOT_URL = "https://api.disneyapi.dev/character";

    public static CastDTO convert(String json){
        ObjectMapper mapper = new ObjectMapper();
        try{
            CastDTO dto = mapper.readValue(json, CastDTO.class);
            return dto;
        } catch (Exception e){
            System.out.println("Something went wrong");
            return null;
        }
    }
    public static String getFilm(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your favorite Disney movie title: ");
        String movie = scan.nextLine();
        return movie;
    }
    //GOAL: Take in a movie name and return the formatted URL
    //parameter? --> String movieTitle
    //ret type? --> String URL
    //https://api.disneyapi.dev/character
    public static String formatURL(String URL, String movieTitle){
        movieTitle = movieTitle.replaceAll(" ","%20");
        movieTitle = movieTitle.replaceAll("'","%27");
        String bigBoyURL = URL + "?" + "films=" + movieTitle;
        return bigBoyURL;
    }
    public static String letsGetit(String URL){
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = httpResponse.statusCode();
            if (statusCode == 200) {
                return httpResponse.body();
            } else {
                // String.format is fun! Worth a Google if you're interested
                return String.format("GET request failed: %d status code received", statusCode);
            }
        } catch (IOException | InterruptedException e) {
            return e.getMessage();
        }
    }

    //GOAL:Take a list of characters return a list of urls
    //DATA TYPE: String URL
    //If statement on the movie name

    public static List<String> grabURL (List<Data> characters, String movie){
        ArrayList<String> castURL = new ArrayList<>();
        for(Data c : characters){
         if(c.getFilms().contains(movie)){
             String image = c.getImageUrl();
             castURL.add(image);
         }
        }
        return castURL;
    }

    //GOAL: Write an HTML file that has all the image URLS
    /* HTMLS
    <html>
        <head> </head>
        <body>
     <img src = "link">
        </body>
     </html>
     */
    public static void writeToFile(List<String> imgURL){
        try {
            FileWriter fw = new FileWriter("index.html");
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write("<html>\n");
            writer.write("<head></head>\n");
            writer.write("<body>\n");
            //TODO: insert image links
            for (int i = 0; i< imgURL.size(); i++){
                String currURL = imgURL.get(i);
                writer.write("<img src=\"");
                writer.write(currURL);
                writer.write("\">\n");

            }
            writer.close();
            //DON'T FORGET TO CLOSE IT
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public static void main(String[] args) {
        String movie = getFilm();
        System.out.println(movie);

        String URL = formatURL(ROOT_URL,movie);
        System.out.println(URL);

        String json = letsGetit(URL);
        System.out.println(json);

        CastDTO dto = convert(json);
        List<Data> characters = dto.getData();


        writeToFile(grabURL(characters, movie));
        //System.out.println(grabURL(characters,movie));
        /*
        System.out.println(characters.get(0).getName());
        for(Data character : characters){
            if (character.getFilms().contains(movie)){
                System.out.println(character.getName());
            }

        }

         */
    }
}
