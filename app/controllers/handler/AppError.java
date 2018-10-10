package controllers.handler;

import com.typesafe.config.Config;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import play.mvc.Results;
import views.html.error.error;


public class AppError extends DefaultHttpErrorHandler {

    @Inject
    public AppError(Config config, Environment environment, OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(config, environment, sourceMapper, routes);
    }

    @Override
    public CompletionStage<Result> onClientError(Http.RequestHeader request, int statusCode, String message) {
        return CompletableFuture.completedFuture(Results.ok(error.render(Http.Context.current().messages().at("app.details"), Http.Context.current().messages().at("app.status") + " " + statusCode + ((message == null || message.trim().isEmpty()) ? " - " + Http.Context.current().messages().at("app.no.details") : " - " + message))));
    }

    @Override
    public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
        return CompletableFuture.completedFuture(Results.ok(error.render(Http.Context.current().messages().at("app.details"), exception.getMessage())));
    }
}
