package data_management;

import com.cardio_generator.outputs.TcpOutputStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TcpOutputStrategy class.
 */
public class TcpOutputStrategyTest {
    private static final int TEST_PORT = 12345;
    private TcpOutputStrategy tcpOutputStrategy;
    private Socket clientSocket;
    private BufferedReader in;
    
    @BeforeEach
    public void setup() throws IOException, InterruptedException {
        // Create a CountDownLatch to wait for the server to start
        CountDownLatch serverStartLatch = new CountDownLatch(1);
        
        // Start the TCP output strategy in a separate thread
        Thread serverThread = new Thread(() -> {
            tcpOutputStrategy = new TcpOutputStrategy(TEST_PORT);
            serverStartLatch.countDown();
        });
        serverThread.start();
        
        // Wait for the server to start
        assertTrue(serverStartLatch.await(5, TimeUnit.SECONDS), "Server did not start within timeout");
        
        // Give the server a moment to initialize
        Thread.sleep(500);
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        // Close client resources if they exist
        if (in != null) {
            in.close();
        }
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();
        }
        
        // Close server resources by creating a new instance
        // This is a workaround since TcpOutputStrategy doesn't have a close method
        tcpOutputStrategy = null;
        System.gc(); // Encourage garbage collection to close resources
    }
    
    @Test
    public void testOutputWithConnectedClient() throws IOException, InterruptedException {
        // Connect a client to the server
        clientSocket = new Socket("localhost", TEST_PORT);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        // Give the server time to accept the connection
        Thread.sleep(500);
        
        // Send data through the output strategy
        int patientId = 42;
        long timestamp = 1234567890L;
        String label = "HeartRate";
        String data = "72.5";
        tcpOutputStrategy.output(patientId, timestamp, label, data);
        
        // Read the data from the client socket
        String receivedMessage = in.readLine();
        
        // Verify the received message
        assertNotNull(receivedMessage, "Message should not be null");
        assertEquals(patientId + "," + timestamp + "," + label + "," + data, receivedMessage,
                "Received message should match the expected format");
    }
    
    @Test
    public void testOutputWithNoConnectedClient() {
        // No client is connected in this test
        
        // This should not throw an exception
        assertDoesNotThrow(() -> {
            tcpOutputStrategy.output(1, 1000L, "Test", "Data");
        }, "Output method should not throw when no client is connected");
    }
    
    @Test
    public void testMultipleOutputs() throws IOException, InterruptedException {
        // Connect a client to the server
        clientSocket = new Socket("localhost", TEST_PORT);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        // Give the server time to accept the connection
        Thread.sleep(500);
        
        // Send multiple data points
        tcpOutputStrategy.output(1, 1000L, "HeartRate", "70.0");
        tcpOutputStrategy.output(1, 2000L, "BloodPressure", "120/80");
        
        // Read and verify the first message
        String message1 = in.readLine();
        assertNotNull(message1, "First message should not be null");
        assertEquals("1,1000,HeartRate,70.0", message1, "First message should match expected format");
        
        // Read and verify the second message
        String message2 = in.readLine();
        assertNotNull(message2, "Second message should not be null");
        assertEquals("1,2000,BloodPressure,120/80", message2, "Second message should match expected format");
    }
}