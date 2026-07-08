package com.vehicularcloud.network;

import com.vehicularcloud.model.OwnerInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class VehicleOwnerClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6000;

    /**
     * Sends a vehicle owner request to the VC Controller server.
     *
     * @param owner The OwnerInput object containing owner ID, vehicle details, and residency hours
     * @return Server response: "ACCEPTED", "REJECTED", or error message
     */
    public String sendOwnerToServer(OwnerInput owner) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // build request line
            String request = String.format(
                    "VEHICLE_OWNER,%s,%s,%s,%d,%s,%.2f",
                    owner.ownerId(),
                    owner.vehicleMake(),
                    owner.vehicleModel(),
                    owner.vehicleYear(),
                    owner.licensePlate(),
                    owner.residencyHours());

            // send request
            out.println(request);
            System.out.println("Sent to server: " + request);

            // read ack
            String ack = in.readLine();
            if (ack == null) {
                return "Error: No acknowledgment received from VC Controller.";
            }
            System.out.println("Server ACK: " + ack);

            // read final decision
            String decision = in.readLine();
            if (decision == null) {
                return "Error: No final decision received from VC Controller.";
            }

            decision = decision.trim();
            System.out.println("Server decision: " + decision);

            return decision;

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Could not connect to VC Controller server.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unexpected error while talking to VC Controller.";
        }
    }
}
