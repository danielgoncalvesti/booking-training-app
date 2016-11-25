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
        List<Row> rows = service.getUsers().all();
        List<String> result = new ArrayList<>();
        for (Row row : rows) {
            result.add("user_name [" + row.getString("user_name") + "] gender [" + row.getString("gender") + "]");
        }
        final JsonNode jsonResponse = Json.toJson(result);
        return ok(jsonResponse);
    }

    public Result index() {
        final JsonNode jsonResponse = Json.toJson("Your new application is ready.");
        return ok(jsonResponse);
    }


}
