package jcrush.io;

public enum ConnectionType {
    GET("GET"),
    POST("POST"),
    HEAD("HEAD"),
    DELETE("DELETE");

    String type;
    ConnectionType(String type) { this.type = type; }
}
