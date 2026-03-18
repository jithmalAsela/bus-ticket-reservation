package com.lk.busreservation.servlet;

import com.google.gson.Gson;
import com.lk.busreservation.model.AvailabilityRequest;
import com.lk.busreservation.model.AvailabilityResponse;
import com.lk.busreservation.service.BusService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jithmal
 */
@WebServlet("/api/checkAvailability")
public class CheckAvailabilityServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final BusService service = BusService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            String body = new String(req.getInputStream().readAllBytes());
            AvailabilityRequest request = gson.fromJson(body, AvailabilityRequest.class);

            if (request.getPassengerCount() <= 0) {
                throw new IllegalArgumentException("Passenger count must be positive.");
            }

            int available = service.getAvailableSeats(request.getOrigin(), request.getDestination());
            double price = service.calculatePrice(request.getOrigin(), request.getDestination())
                    * request.getPassengerCount();

            AvailabilityResponse responseObj = new AvailabilityResponse(available, price);
            resp.getWriter().write(gson.toJson(responseObj));
        } catch (Exception e) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

}
