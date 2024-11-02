package resources;

import Serialization.Location;
import Serialization.AddPlaceSerialization;


import java.util.ArrayList;
import java.util.List;

public class TestDataBuild {

    public AddPlaceSerialization addPlacePayload(String name, String language, String address) {


        AddPlaceSerialization place = new AddPlaceSerialization();
        place.setAccuracy(50);
        place.setAddress(address);
        place.setLanguage(language);
        place.setName(name);
        place.setPhone_number("6360980036");
        place.setWebsite("https://rahulshettyacademy.com");
        List<String> types = new ArrayList<>();
        types.add("shoe park");
        types.add("shop");
        place.setTypes(types);
        Location location = new Location();
        location.setLat(-38.383494);
        location.setLng(33.427362);
        place.setLocation(location);

        return place;
    }
    public String deletePlacePayload(String place_id){
        return "{\r\n    \"place_id\":\""+place_id+"\"\r\n}\r\n";
    }

}
