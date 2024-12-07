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
import jakarta.json.JsonValue;
import ssf.day19.models.Task;

@Component
public class AppBootstrap implements CommandLineRunner {

    @Value("classpath:static/main/todos.json")
    private Resource todoFile;

    @Autowired @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    public static final String TODO_KEY = "TASKS";
    
    @Override
    public void run(String... args) throws ParseException {
        StringBuilder sbJson = new StringBuilder();
        String line;

        try {
            if (todoFile.exists()) {
                try(InputStreamReader is = new InputStreamReader(todoFile.getInputStream());
                    BufferedReader br = new BufferedReader(is)) {

                    while ((line = br.readLine()) != null) 
                        sbJson.append(line).append("\n");
                }
            } else 
                System.out.println("Resource not found.");
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonReader reader = Json.createReader(new StringReader(sbJson.toString()));
        JsonArray jsonArr = reader.readArray();
        for(int i = 0; i < jsonArr.size(); i++) {
            JsonObject j = jsonArr.getJsonObject(i);
            
            // ### Convert all json dates to epoch ms ###
            // Create a mutable copy of the JsonObject
            JsonObjectBuilder job = Json.createObjectBuilder(j);
            
            // Convert from String -> Date -> Long epoch ms
            SimpleDateFormat sdf = new SimpleDateFormat(Task.DATE_FORMAT);
            Long epochDateTime = sdf.parse(j.getString("due_date")).getTime();

            // Replace j.due_date
            job.add("due_date", String.valueOf(epochDateTime));
            
            // Replace j.created_at
            epochDateTime = sdf.parse(j.getString("created_at")).getTime();
            job.add("created_at", String.valueOf(epochDateTime));
            
            // Replace j.updated_at
            epochDateTime = sdf.parse(j.getString("updated_at")).getTime();
            job.add("updated_at", String.valueOf(epochDateTime));
            
            JsonObject jUpdated = job.build();
            
            template.opsForHash().put(TODO_KEY, jUpdated.getString("id"), jUpdated.toString());
        }
    }
}
