package controllers;

import akka.actor.ActorSystem;
import com.datastax.driver.core.Row;
import com.fasterxml.jackson.databind.JsonNode;
import model.User;
import play.libs.Json;
import play.mvc.BodyParser;
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

    @BodyParser.Of(BodyParser.Json.class)
    public Result insertCity() {
        JsonNode node = request().body().asJson();
        User user = new User(node);
        System.out.println(user);
        try {
            service.addUser(user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ok(Json.toJson(new Object[] {user, node}));
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
