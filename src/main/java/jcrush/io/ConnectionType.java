package jcrush.io;

public enum ConnectionType {
    GET("GET"),
    POST("POST");

    String type;
    ConnectionType(String type) { this.type = type; }
}
