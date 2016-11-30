package controllers;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import model.Hotel;
import model.User;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;
import services.CityService;
import services.UserService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class ApiController extends Controller {

    private final ActorSystem actorSystem;
    private final ExecutionContextExecutor exec;
    private final UserService service;
    private final CityService cityService;

    @Inject
    public ApiController(ActorSystem actorSystem, ExecutionContextExecutor exec, UserService service,
                         CityService cityService) {
        this.actorSystem = actorSystem;
        this.exec = exec;
        this.service = service;
        this.cityService = cityService;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> saveHotel() {
        JsonNode node = request().body().asJson();
        Hotel hotel = new Hotel(node);
        CompletableFuture<Result> future = new CompletableFuture<>();
        actorSystem.scheduler().scheduleOnce(
                Duration.create(1, TimeUnit.MICROSECONDS),
                (Runnable) () -> future.complete(
                        ok(Json.toJson(cityService.saveHotel(hotel)))
                ),
                exec
        );
        return future;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> insertUser() {
        JsonNode node = request().body().asJson();
        final User user = new User(node);
        CompletableFuture<Result> future = new CompletableFuture<>();
        actorSystem.scheduler().scheduleOnce(
                Duration.create(1, TimeUnit.MICROSECONDS),
                (Runnable) () -> future.complete(
                        ok(Json.toJson(service.addUser(user)))
                ),
                exec
        );
        return future;
    }

    public CompletionStage<Result> getUsers() {
        CompletableFuture<Result> future = new CompletableFuture<>();
        actorSystem.scheduler().scheduleOnce(
                Duration.create(1, TimeUnit.MICROSECONDS),
                (Runnable) () -> future.complete(
                        ok(Json.toJson(service.getUsers()))
                ),
                exec
        );
        return future;
    }

    public Result index() {
        final JsonNode jsonResponse = Json.toJson("Your new application is ready.");
        return ok(jsonResponse);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletableFuture<Result> getCity() {
        JsonNode node = request().body().asJson();
        String city = node.get("city").asText();
        CompletableFuture<Result> future = new CompletableFuture<>();
        actorSystem.scheduler().scheduleOnce(
                Duration.create(1, TimeUnit.MICROSECONDS),
                (Runnable) () -> future.complete(
                        ok(Json.toJson(cityService.getHotels(city)))
                ),
                exec
        );
        return future;
    }
}
