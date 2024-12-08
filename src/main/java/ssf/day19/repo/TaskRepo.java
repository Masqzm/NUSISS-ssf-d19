package ssf.day19.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ssf.day19.config.Constants;
import ssf.day19.models.Task;

@Repository
public class TaskRepo {
    @Autowired @Qualifier(Constants.REDIS_TEMPLATE_01)
    private RedisTemplate<String, String> template;

    public List<Task> getAllTasks() {
        List<Task> tasksList = new ArrayList<>();

        Map<Object, Object> hashMap = new TreeMap<>(template.opsForHash().entries(Constants.REDIS_KEY_TODO));

        // key = id of task, value = all properties of task
        for(Object key : hashMap.keySet())
        {
            String json = hashMap.get(key).toString();
            
            tasksList.add(Task.jsonToTask(json));
        }

        return tasksList;
    }
}
