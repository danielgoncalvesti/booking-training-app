package controllers;

import akka.actor.ActorSystem;
import com.datastax.driver.core.Row;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;
import services.BookingService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ApiController extends Controller {

    private final ActorSystem actorSystem;
    private final ExecutionContextExecutor exec;
    private final BookingService service;

    @Inject
    public ApiController(ActorSystem actorSystem, ExecutionContextExecutor exec, BookingService service) {
        this.actorSystem = actorSystem;
        this.exec = exec;
        this.service = service;
    }

    public Result getCity() {
        final JsonNode jsonResponse = Json.toJson(service.getUsers());
        return ok(jsonResponse);
    }

    public Result index() {
        final JsonNode jsonResponse = Json.toJson("Your new application is ready.");
        return ok(jsonResponse);
    }


}
