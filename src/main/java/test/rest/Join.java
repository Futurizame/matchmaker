package test.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import redis.clients.jedis.UnifiedJedis;

@Path("/join")
public class Join {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Anything hello() {

        UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379");

        String value = jedis.get("key");

        jedis.close();


        Anything anything = new Anything();

        anything.name = "John Doe";
        anything.description = "This is a john doe";

        anything.value = value;

        return anything;
    }
}
