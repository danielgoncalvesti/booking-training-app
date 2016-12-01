package controllers;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import model.Booking;
import model.FreeRoomsRequest;
import model.Hotel;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;
import services.BookingService;
import services.CityService;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class ApiController extends Controller {

    private final ActorSystem actorSystem;
    private final ExecutionContextExecutor exec;
    private final CityService cityService;
    private final BookingService bookingService;

    @Inject
    public ApiController(ActorSystem actorSystem, ExecutionContextExecutor exec,
                         CityService cityService, BookingService bookingService) {
        this.actorSystem = actorSystem;
        this.exec = exec;
        this.cityService = cityService;
        this.bookingService = bookingService;
    }

    public Result addGuest() {
        return play.mvc.Results.TODO;
    }

    public Result addRoom() {
        return play.mvc.Results.TODO;
    }

    public Result roomByGuest() {
        return play.mvc.Results.TODO;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> freeRooms() throws ParseException {
        JsonNode node = request().body().asJson();
        FreeRoomsRequest request = new FreeRoomsRequest(node);
        CompletableFuture<Result> future = new CompletableFuture<>();
        actorSystem.scheduler().scheduleOnce(
                Duration.create(1, TimeUnit.MICROSECONDS),
                (Runnable) () -> future.complete(
                        ok(Json.toJson(bookingService.getFreeRooms(request)))
                ),
                exec
        );
        return future;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> addBooking() throws ParseException {
        JsonNode node = request().body().asJson();
        List<Booking> bookingList = Booking.parse(node);
        CompletableFuture<Result> future = new CompletableFuture<>();
        actorSystem.scheduler().scheduleOnce(
                Duration.create(1, TimeUnit.MICROSECONDS),
                (Runnable) () -> future.complete(
                        ok(Json.toJson(bookingService.addBookings(bookingList)))
                ),
                exec
        );
        return future;
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
