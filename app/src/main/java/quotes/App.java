/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package quotes;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class App {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public String getGreeting() {
        return "Hello World!";
    }

    public static String getFilePath(){
        String userPath = System.getProperty("user.dir");
        if(userPath.endsWith("app")){
            return userPath + "/src/main/resources/";
        }
        else{
            return userPath + "/app/src/main/resources/";
        }
    }

    public static FileReader readerFile(String path) throws FileNotFoundException {
        return new FileReader(path);
    }

    public static Quote[] getArray(FileReader filereader){
        Quote[] quotes;
        Gson gson = new Gson();
        quotes = gson.fromJson(filereader, Quote[].class);
        if(quotes != null) {
            return quotes;
        }
        else{
            throw new IllegalArgumentException("Empty json file");
        }
    }

    public static int randomNumber(Quote[] quotes){
        int size = quotes.length;
        Random num = new Random();
        return num.nextInt(0, size);
    }

    public static int randomNumber(){
        int size = 151;
        Random num = new Random();
        return num.nextInt(1, size);
    }

    public static void randomQuote(String fileName) throws IOException{
        Quote[] quotes = getArray(readerFile(getFilePath() + fileName));
        int num = randomNumber(quotes);
        String text = quotes[num].text;
        String author = quotes[num].author;
        System.out.println(author + "\n" + text);
    }

    public static void authorQuote(String author) throws IOException{
        Quote[] quotes = getArray(readerFile(getFilePath() + "recentquotes.json"));
        for ( Quote quote : quotes){
            if ( quote.author.contains(author)) {
                System.out.println(quote.author + "\n" + quote.text);
                break;
            }
        }
    }

    public static void wordQuote(String word) throws IOException{
        Quote[] quotes = getArray(readerFile(getFilePath() + "recentquotes.json"));
        for ( Quote quote : quotes){
                if (quote.text.contains(word)) {
                    System.out.println(quote.author + "\n" + quote.text);
                    break;
                }
        }
        System.out.println("The quote database does not include " + word);
    }

    public static void switchBoard(String[] args) throws IOException{
        switch (args[0]){
            case ("author"):{
                String name = args[1];
                for ( int i = 2; i < args.length; i++){
                    name = name + " " + args[i];
                }
                authorQuote(name);
                break;
                }
            case("contains"):{
                wordQuote(args[1]);
                break;
            }
            case("random"):{
                randomQuote("recentquotes.json");
                break;
            }
            case ("pokemon"):{
               String description = convertToQuotes(readFromConnection(createConnection("https://pokeapi.co/api/v2/pokemon-species/" + randomNumber())));
                System.out.println(description);
                break;
            }
            default:{
                System.out.println("Please type either author or contains before search query. Or use random for a surprise");
            }
        }
    }

    public static HttpURLConnection createConnection(String api) throws MalformedURLException, IOException {
        URL createdURL = new URL(api);
        HttpURLConnection URLConnection = (HttpURLConnection) createdURL.openConnection();
        URLConnection.setRequestMethod("GET");
        return URLConnection;
    }

    public static  String readFromConnection(HttpURLConnection connection) throws IOException {
        InputStreamReader fileInputStreamReader = new InputStreamReader(connection.getInputStream());
        BufferedReader fileBufferedReader = new BufferedReader(fileInputStreamReader);
        String quoteData = fileBufferedReader.readLine();
        return quoteData;
    }

    public static String convertToQuotes(String quoteData) {
        Pokemon quote = gson.fromJson(quoteData, Pokemon.class);
        String description = quote.flavor_text_entries[0].getFlavor_text();
        return description;
    }

    static public void writeToFile(Pokemon pokemon) throws IOException {
       File pokeFile = new File("./pokemon.json");
        try(FileWriter pokeFileWriter = new FileWriter(pokeFile)){
            gson.toJson(pokemon, pokeFileWriter);
            System.out.println("File was created successfully");
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        switchBoard(args);
    }
}
