package core;

import io.restassured.http.ContentType;

public interface Constantes {
    String APP_BASE_URL = "https://petstore.swagger.io/";
    String APP_BASE_PATH = "v2";

    ContentType APP_CONTENT_TYPE = ContentType.JSON;

    Long MAX_TIMEOUT = 10000L;
}
