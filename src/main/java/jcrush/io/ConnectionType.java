package jcrush.io;

public enum ConnectionType {
    GET("GET"),
    POST("POST"),
    HEAD("HEAD");

    String type;
    ConnectionType(String type) { this.type = type; }
}
