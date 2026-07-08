package com.vehicularcloud.network;

import java.io.*;
import java.net.*;

/**
 * Client for sending Task Owner (job) requests to the VC Controller server.
 *
 * Member 1: Christopher
 * Milestone 5 - Client Module
 */
public class JobClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6000;

    /**
     * Sends a job request to the VC Controller server.
     *
     * @param clientID The client/task owner ID
     * @param jobName The name of the job
     * @param duration The job duration in hours
     * @param deadline The job deadline (format: yyyy-MM-dd HH:mm)
     * @return Server response: "ACCEPTED", "REJECTED", or error message
     */
    public String sendJobToServer(String clientID, String jobName, String duration, String deadline) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Build request: TASK_OWNER,clientID,jobName,duration,deadline
                String jobData = String.format("TASK_OWNER,%s,%s,%s,%s",
                    clientID, jobName, duration, deadline);

                // Send job data to the server
                out.println(jobData);
                System.out.println("Sent to server: " + jobData);

                // Read acknowledgment
                String ack = in.readLine();
                if (ack == null) {
                    return "Error: No acknowledgment received from VC Controller.";
                }
                System.out.println("Server ACK: " + ack);

                // Read decision (ACCEPTED/REJECTED)
                String decision = in.readLine();
                if (decision == null) {
                    return "Error: No final decision received from VC Controller.";
                }

                decision = decision.trim();
                System.out.println("Server decision: " + decision);

                return decision; // return to display in GUI

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Could not connect to VC Controller server.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unexpected error while talking to VC Controller.";
        }
    }
}
