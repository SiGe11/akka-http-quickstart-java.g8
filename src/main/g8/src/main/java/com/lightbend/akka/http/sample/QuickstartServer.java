package com.lightbend.akka.http.sample;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.example.routes.BaseRoutes;
import com.example.routes.SimpleRoutes;

import java.util.concurrent.CompletionStage;

//#main-class
public class QuickstartServer extends AllDirectives {

  // set up ActorSystem and other dependencies here
  //#main-class
  private final SimpleRoutes simpleRoutes;

  public HttpServer() {
        simpleRoutes = new SimpleRoutes();
  }
  //#main-class

  public static void main(String[] args) throws Exception {
    // boot up server using the route as defined below
    ActorSystem system = ActorSystem.create("routes");

    //#server-bootstrapping
    final Http http = Http.get(system);
    final ActorMaterializer materializer = ActorMaterializer.create(system);
    //#server-bootstrapping

    //#http-server
    //In order to access all directives we need an instance where the routes are define.
    HttpServer app = new HttpServer();

    final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
    final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
      ConnectHttp.toHost("localhost", 8080), materializer);

    System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
    System.in.read(); // let it run until user presses return

    binding
      .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
      .thenAccept(unbound -> system.terminate()); // and shutdown when done
    //#http-server
    //#main-class
  }

  /**
   * Here you can define all the different routes you want to have served by this web server
   * Note that routes might be defined in separated classes like the current case
   */
  protected Route createRoute() {
    return route(BaseRoutes.baseRoutes(), simpleRoutes.simpleRoutes());
  }
}
//#main-class
