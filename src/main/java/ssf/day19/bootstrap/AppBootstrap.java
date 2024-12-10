package ssf.day19.bootstrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import ssf.day19.config.Constants;

@Component
public class AppBootstrap implements CommandLineRunner {

    @Value("classpath:static/main/todos.json")
    private Resource todoFile;

    @Value("classpath:static/main/products.json")
    private Resource productsFile;

    @Autowired @Qualifier(Constants.REDIS_TEMPLATE_01)
    private RedisTemplate<String, String> template;

    @Override
    public void run(String... args) {
        StringBuilder sbJson = new StringBuilder();
        String line;

        // Load todos.json -> save as a string -> process and store into redis
        try {
            if (todoFile.exists()) {
                try(InputStreamReader is = new InputStreamReader(todoFile.getInputStream());
                    BufferedReader br = new BufferedReader(is)) {

                    while ((line = br.readLine()) != null) 
                        sbJson.append(line).append("\n");
                }
            } else 
                System.out.println("todos.json file not found.");
            
            processAndSaveTodoFile(sbJson.toString());
        } catch (IOException e) {
            System.err.println("I/O Error in loading todos.json file");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Error in parsing todos.json file");
            e.printStackTrace();
        }

        // Reset sb
        sbJson.setLength(0);

        // Load todos.json -> save as a string -> process and store into redis
        try {
            if (productsFile.exists()) {
                try(InputStreamReader is = new InputStreamReader(productsFile.getInputStream());
                    BufferedReader br = new BufferedReader(is)) {

                    while ((line = br.readLine()) != null) 
                        sbJson.append(line).append("\n");
                }
            } else 
                System.out.println("products.json file not found.");
            
            processAndSaveProductsFile(sbJson.toString());
        } catch (IOException e) {
            System.err.println("I/O Error in loading todos.json file");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Error in parsing todos.json file");
            e.printStackTrace();
        }
    }
    
    private void processAndSaveProductsFile(String json) throws ParseException {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonArray jsonArr = reader.readArray();

        for(int i = 0; i < jsonArr.size(); i++) {
            JsonObject j = jsonArr.getJsonObject(i);

            template.opsForHash().put(Constants.REDIS_KEY_PRODUCTS, Integer.toString(j.getInt("id")), j.toString());
        }
    }

    private void processAndSaveTodoFile(String json) throws ParseException {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonArray jsonArr = reader.readArray();
        for(int i = 0; i < jsonArr.size(); i++) {
            JsonObject j = jsonArr.getJsonObject(i);
            
            // ### Convert all json dates to epoch ms ###
            // Create a mutable copy of the JsonObject
            JsonObjectBuilder job = Json.createObjectBuilder(j);
            
            // Convert from String -> Date -> Long epoch ms
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
            Long epochDateTime = sdf.parse(j.getString("due_date")).getTime();

            // Replace j.due_date
            job.add("due_date", epochDateTime);
            
            // Replace j.created_at
            epochDateTime = sdf.parse(j.getString("created_at")).getTime();
            job.add("created_at", epochDateTime);
            
            // Replace j.updated_at
            epochDateTime = sdf.parse(j.getString("updated_at")).getTime();
            job.add("updated_at", epochDateTime);
            
            JsonObject jUpdated = job.build();
            
            template.opsForHash().put(Constants.REDIS_KEY_TODO, jUpdated.getString("id"), jUpdated.toString());
        }
    }
}
