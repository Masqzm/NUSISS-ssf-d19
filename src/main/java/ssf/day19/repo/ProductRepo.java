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
import ssf.day19.models.Product;

@Repository
public class ProductRepo {
    @Autowired @Qualifier(Constants.REDIS_TEMPLATE_01)
    private RedisTemplate<String, String> template;

    // HGETALL PRODUCTS
    public List<Product> getAllProducts() {
        List<Product> productsList = new ArrayList<>();

        // To sort products by int id
        Map<Integer, Object> sortedMap = new TreeMap<>();

        // key = id of product, value = all properties of product
        for(Map.Entry<Object, Object> entry : template.opsForHash().entries(Constants.REDIS_KEY_PRODUCTS).entrySet())
            sortedMap.put(Integer.parseInt(entry.getKey().toString()), entry.getValue());
        
        for(int key : sortedMap.keySet())
        {
            String json = sortedMap.get(key).toString();
        
            productsList.add(Product.jsonToProduct(json));
        }        

        return productsList;
    }
}
