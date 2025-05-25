package com.cardio_generator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;
import java.util.Collection;
/**
 * An {@link OutputStrategy} implementation that broadcasts patient data to connected WebSocket clients.
 * <p>The message format follows: "patientId,timestamp,label,data" where:
 * <ul>
 *   <li><b>patientId</b> - unique patient identifier (integer)</li>
 *   <li><b>timestamp</b> - measurement time in milliseconds (long)</li>
 *   <li><b>label</b> - type of measurement</li>
 * </ul>
 */
public class WebSocketOutputStrategy implements OutputStrategy {

    private static WebSocketServer server;
    /**
     * Creates a WebSocket output strategy on the specified port.
     * @param port the TCP port to listen for connections
     */
    public WebSocketOutputStrategy(int port) {
        this(createServer(port));
    }
    /**
     * Allows injection of a custom WebSocketServer instance.
     * @param server the WebSocket server instance to use
     */
    WebSocketOutputStrategy(WebSocketServer server) {
        this.server = server;
        System.out.println("WebSocket server created on port: " + server.getPort() + ", listening for connections...");
        server.start();
    }

    /**
     * Creates a default WebSocket server configuration.
     *
     * @param port the port to bind the server to
     * @return a new WebSocketServer instance
     */
    public static WebSocketServer createServer(int port) {
        return new SimpleWebSocketServer(new InetSocketAddress(port));
    }
    /**
     * Broadcasts patient data to all connected WebSocket clients.
     * @param patientId the patient identifier
     * @param timestamp the measurement timestamp
     * @param label the type of measurement
     * @param data the measurement value
     * @throws IllegalStateException if the server is not running
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        String message = formatMessage(patientId, timestamp, label, data);
        if (message != null) {
            // Broadcast the message to all connected clients
            for (WebSocket conn : server.getConnections()) {
                conn.send(message);
            }
        }
    }

    public static String formatMessage(int patientId, long timestamp, String label, String data) {
        // Validate the message fields
        if (patientId <= 0 || label == null || label.isEmpty() || timestamp <= 0 || data == null || data.isEmpty()) {
            System.err.println("Invalid message fields: patientId=" + patientId + ", timestamp=" + timestamp + ", label=" + label + ", data=" + data);
            return null;
        }

        // Return formatted message
        return String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
    }
    /**
     * Internal WebSocket server implementation for handling connections.
     * Provides basic connection lifecycle logging and message broadcasting capabilities.
     */
    private static class SimpleWebSocketServer extends WebSocketServer {
        /**
         * Creates a new WebSocket server bound to the specified address.
         * @param address the network address to bind to
         */
        public SimpleWebSocketServer(InetSocketAddress address) {
            super(address);
        }
        /**
         * Handles new WebSocket connections.
         * Making logs according new connections
         * @param conn the new WebSocket connection
         * @param handshake the handshake data
         */
        @Override
        public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
            System.out.println("New connection: " + conn.getRemoteSocketAddress());
        }
        /**
         * Handles connection closes.
         * @param conn the closed connection
         * @param code the closure code
         * @param reason the closure reason
         * @param remote whether closure was initiated remotely
         */
        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        }
        /**
         * Handles incoming messages.
         * @param conn the source connection
         * @param message the received message
         */
        @Override
        public void onMessage(WebSocket conn, String message) {
            // Sometimes can be unused
        }
        /**
         * Handles server errors.
         * @param conn the connection where error occurred (may be null)
         * @param ex the exception
         */
        @Override
        public void onError(WebSocket conn, Exception ex) {
            ex.printStackTrace();
        }
        /**
         * Server starts successfully.
         * Logs server startup.
         */
        @Override
        public void onStart() {
            System.out.println("Server started successfully");
        }
    }
}