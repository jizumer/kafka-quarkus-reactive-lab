package com.jizumer;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/simulate-error")
public class SimulateErrorResource {

    @Inject
    MyReactiveMessagingApplication myReactiveMessagingApplication;

    @POST
    @Path("/{simulateErrorOnMessage}")
    public void simulateErrorOnMessage(@PathParam("simulateErrorOnMessage") Boolean simulateErrorOnMessage) {
        myReactiveMessagingApplication.setSimulateErrorOnMessage(simulateErrorOnMessage);
    }
}