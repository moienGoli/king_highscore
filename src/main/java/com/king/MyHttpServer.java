package com.king;

/**
 * Starts a http server using com.sun.net.httpserver.HttpServer.
 * valid URI examples and their responses are :
 * - GET http://localhost:8081/#{levelID}/highscorelist --> 1212=34322,1234=33000,1001=23103
 * - POST http://localhost:8081/#{levelID}/score?sessionKey=#{sessionKey}
 * - GET http://localhost:8081/#{userID}/login --> #{Base64URIFriendlySessionKey}
 * <p>
 * Created by moien on 9/8/17.
 */

import com.king.exception.AppException;
import com.king.service.highscore.HighScoreService;
import com.king.service.highscore.HighScoreServiceFactory;
import com.king.service.highscore.Score;
import com.king.service.logger.Logger;
import com.king.service.login.AuthenticationService;
import com.king.service.login.CryptographyService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Scanner;

import static com.king.HTTPServerURIHelper.ServiceName;

public class MyHttpServer {


    private static final HTTPServerURIHelper helper = new HTTPServerURIHelper();
    private static final HighScoreService highscoreService = HighScoreServiceFactory.getInstance().getHighScoreService(15, 10);
    private static final AuthenticationService auth = new AuthenticationService(new CryptographyService());

    public static void main(String[] args) throws Exception {

        HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/", new RequestHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private static class RequestHandler implements HttpHandler {

        private static final int SESSION_TTL_SECONDS = 600;
        private static final String MALFORMED_URI = "Malformed URI";


        public void handle(HttpExchange http) throws IOException {

            String uri = http.getRequestURI().toString();
            String response = "";
            int responseCode = 200;

            try {
                HTTPServerURIHelper.ServiceName service = helper.getService(uri);
                if (service == null) {
                    throw new AppException(MALFORMED_URI);
                }
                if (service.equals(ServiceName.SCORE)) {
                    handleScorePost(http, uri);
                } else if (service.equals(ServiceName.HIGHSCORE)) {
                    response = handleHighScoreRequest(uri);
                } else if (service.equals(ServiceName.LOGIN)) {
                    response = handleLoginRequest(uri);
                } else {
                    throw new AppException(MALFORMED_URI);
                }
            } catch (Exception e) {
                String msg = e.getMessage() + " Request: " + uri;
                Logger.log(msg);
                response = msg;
                responseCode = 400;
            }

            http.sendResponseHeaders(responseCode, response.length());
            OutputStream os = http.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String handleLoginRequest(String uri) {

            int userId = helper.getUserIDFromLoginURI(uri);
            return auth.doLogin(userId, SESSION_TTL_SECONDS);
        }

        private String handleHighScoreRequest(String uri) {

            int levelId = helper.getLevelIDFromHighScoreURI(uri);
            String response = highscoreService.getHighScoresForLevel(levelId).toString();
            return response.replace("[", "").replace("]", "");
        }

        private void handleScorePost(HttpExchange t, String uri) {

            int levelID = helper.getLevelIDFromScorePostURI(uri);
            String sessionKey = helper.getSessionKeyFromScorePostURI(uri);
            int userID = auth.getUserID(sessionKey);
            Scanner s = new Scanner(t.getRequestBody());
            int point;
            if (s.hasNext()) {
                point = s.nextInt();
            } else {
                throw new AppException("No Payload");
            }
            if (point > 0) {
                highscoreService.addScore(new Score(userID, levelID, point));
            }
        }
    }

}
