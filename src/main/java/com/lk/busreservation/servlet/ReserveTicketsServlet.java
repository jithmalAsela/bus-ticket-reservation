package com.lk.busreservation.servlet;

import com.google.gson.Gson;
import com.lk.busreservation.model.ReservationRequest;
import com.lk.busreservation.model.ReservationResponse;
import com.lk.busreservation.service.BusService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jithmal
 */
@WebServlet("/api/reserveTickets")
public class ReserveTicketsServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final BusService service = BusService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            String body = new String(req.getInputStream().readAllBytes());
            ReservationRequest request = gson.fromJson(body, ReservationRequest.class);

            ReservationResponse responseObj = service.reserve(
                    request.getPassengerCount(),
                    request.getOrigin(),
                    request.getDestination(),
                    request.getPriceConfirmation()
            );
            resp.getWriter().write(gson.toJson(responseObj));
        } catch (Exception e) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
