package com.vehicularcloud.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.vehicularcloud.gui.VCControllerGUI;
import com.vehicularcloud.storage.ServerStorageAdapter;

/**
 * Member 3: Sadia Sultana
 * Milestone 5 – VC Controller Server
 *
 * This server waits for Task Owner and Vehicle Owner clients.
 * It:
 *  - Reads their requests
 *  - Sends ACK
 *  - Shows request in GUI
 *  - Waits for Accept/Reject
 *  - Sends final result back to the client
 */
public class VCControllerServer {

    public static final int PORT = 6000;

    public static void main(String[] args) {

        // Start GUI on separate thread
        javax.swing.SwingUtilities.invokeLater(() -> VCControllerGUI.createAndShowGUI());

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("VC Controller Server running on port " + PORT + "...");

            while (true) {
                // Accept client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected: " + clientSocket.getInetAddress());

                // Handle client in separate thread
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Handles ONE client request at a time
class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    // Simple format check so bad data does not go to the GUI
    private boolean isValidRequest(RequestType type, String request) {
        if (request == null || request.isBlank()) {
            return false;
        }

        String[] parts = request.split(",");
        if (type == RequestType.TASK_OWNER) {
            // TASK_OWNER,clientId,jobName,duration,deadline
            return parts.length == 5;
        } else if (type == RequestType.VEHICLE_OWNER) {
            // VEHICLE_OWNER,ownerId,make,model,year,plate,residency
            return parts.length == 7;
        }
        return false;
    }

    @Override
    public void run() {

        try {
            // Step 1 — Read raw request
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String request = reader.readLine();

            if (request == null) {
                // Client closed connection before sending anything
                socket.close();
                return;
            }

            request = request.trim();
            System.out.println("Received: " + request);

            // Step 2 — Send acknowledgment right away
            writer.println("ACK");

            // Step 3 — Decide which type of request and send to GUI
            RequestType type;
            if (request.startsWith("TASK_OWNER")) {
                type = RequestType.TASK_OWNER;
            } else if (request.startsWith("VEHICLE_OWNER")) {
                type = RequestType.VEHICLE_OWNER;
            } else {
                writer.println("INVALID TYPE");
                socket.close();
                return;
            }

            // Basic server-side check so bad or short messages are rejected
            if (!isValidRequest(type, request)) {
                System.out.println("Rejected malformed request: " + request);
                writer.println("REJECTED");
                socket.close();
                return;
            }

            // Wait for Accept or Reject (controller clicks button)
            boolean accepted = VCControllerGUI.waitForDecision(type, request);

            // Step 4 — Respond to client
            if (accepted) {
                writer.println("ACCEPTED");
                ServerStorageAdapter.saveData(type, request); // Member 4 storage call
            } else {
                writer.println("REJECTED");
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
