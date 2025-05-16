package test.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import redis.clients.jedis.UnifiedJedis;

@Path("set")
public class Another {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response set() {
        UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379");

        String value = jedis.set("key", "value");

        jedis.close();

        Response response = new Response();

        response.ok = value;

        return response;
    }
}
