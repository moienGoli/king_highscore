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
import com.king.service.highscore.HighScoreServiceWithLocking;
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
    private static final HighScoreService highscoreService = new HighScoreServiceWithLocking(15);
    private static final AuthenticationService auth = new AuthenticationService(new CryptographyService());

    public static void main(String[] args) throws Exception {

        HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/", new RequestHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private static class RequestHandler implements HttpHandler {

        private final int sessionKeySecondsToLive = 600;

        public void handle(HttpExchange http) throws IOException {

            String uri = http.getRequestURI().toString();
            String response = "";
            int responseCode = 200;

            try {
                HTTPServerURIHelper.ServiceName service = helper.getService(uri);
                if (service.equals(ServiceName.SCORE)) {
                    handleScorePost(http, uri);
                } else if (service.equals(ServiceName.HIGHSCORE)) {
                    response = handleHighScoreRequest(uri);
                } else if (service.equals(ServiceName.LOGIN)) {
                    response = handleLoginRequest(uri);
                }
            } catch (AppException e) {
                Logger.log(e.getMessage());
                responseCode = 400;
                response = e.getMessage();
            } catch (Exception e) {
                Logger.log(e.getMessage());
                responseCode = 500;
                response = e.getMessage();
            }

            http.sendResponseHeaders(responseCode, response.length());
            OutputStream os = http.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String handleLoginRequest(String uri) {

            int userId = helper.getUserIDFromLoginURI(uri);
            return auth.doLogin(userId, sessionKeySecondsToLive);
        }

        private String handleHighScoreRequest(String uri) {

            int levelId = helper.getLevelIDFromHighScoreURI(uri);
            return highscoreService.getHighScoresForLevel(levelId).toString();
        }

        private void handleScorePost(HttpExchange t, String uri) {

            int levelID = helper.getLevelIDFromScorePostURI(uri);
            String sessionKey = helper.getSessionKeyFromScorePostURI(uri);
            int userID = auth.getUserID(sessionKey);
            Scanner s = new Scanner(t.getRequestBody());
            int score = 0;
            if (s.hasNext()) {
                score = s.nextInt();
            }
            highscoreService.addScore(new Score(userID, levelID, score));
        }
    }

}
