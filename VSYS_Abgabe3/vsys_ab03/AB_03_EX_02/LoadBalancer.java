package AB_03_EX_02;

import rm.requestResponse.Component;
import rm.requestResponse.Message;
import rm.serverAdmin.ServerAdmin;
import rm.serverAdmin.ServerConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

class LoadBalancer {
    private static final Logger LOGGER = Logger.getLogger("LoadBalancer");
    private final ExecutorService executorService;
    private final ServerAdmin serverAdmin;
    private final Component communication;
    private final int receivePort;

    public LoadBalancer(String configFileName, int receivePort) throws IOException {
        System.out.println("--------------------------" + configFileName);
        this.executorService = Executors.newCachedThreadPool();
        this.serverAdmin = new ServerAdmin(configFileName);
        this.communication = new Component();
        this.receivePort = receivePort;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //URI relative = new File("resources/load-balancer.conf").toURI(); -> geht nicht, weil nicht im out ordner???
        URL resource = LoadBalancer.class.getResource("resources/load-balancer.conf");
        if (resource == null)
            throw new FileNotFoundException();

        new LoadBalancer(resource.getFile(), 1234).listen();
    }

    public void listen() throws IOException, ClassNotFoundException {
        while (true) {
            //get requests from client
            Message msgFromClient = communication.receive(receivePort, true, true);
            System.out.println("got new message");

            //-------------------------------------------------------------------

            executorService.submit(() -> {
                RequestPair requestPair = (RequestPair) msgFromClient.getContent();
                int answerToClientPort = requestPair.sendPort;

                ServerConfig serverConfig = serverAdmin.bind();
                try {
                    Component serverCommunication = new Component();

                    // send request to server
                    System.out.println("send request to server");
                    requestPair.sendPort = serverConfig.getReceivePort();
                    msgFromClient.setContent(requestPair);
                    serverCommunication.send(msgFromClient, serverConfig.getReceivePort(), true);

                    // get answer from server and send answer to client
                    System.out.println("get answer from server and send answer to client");
                    Message msgFromServer = communication.receive(serverConfig.getSendPort(), true, true);
                    serverCommunication.send(msgFromServer, answerToClientPort, true);

                    serverCommunication.cleanup();

                } catch (IOException | ClassNotFoundException e) {
                    LOGGER.severe("error: %s".formatted(e));
                } finally {
                    serverAdmin.release(serverConfig);
                }
            });
        }
    }
}
