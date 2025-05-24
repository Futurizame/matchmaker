package creceqor.retosubmarino.model;

import lombok.Data;

import java.util.Map;

@Data
public class Event {
    private String type;
    private Map<String, Object> data;
}
