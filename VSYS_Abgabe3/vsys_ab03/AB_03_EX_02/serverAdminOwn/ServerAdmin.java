package AB_03_EX_02.serverAdminOwn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class ServerAdmin {
    private final LinkedList<ServerConfig> serverList;

    public ServerAdmin(String configFileName) throws IOException {
        serverList = readConfigFile(configFileName);
    }

    private static LinkedList<ServerConfig> readConfigFile(String configFileName) throws IOException {
        LinkedList<ServerConfig> servers = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader(configFileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] serverConfigFileLine = line.split(" ");
            servers.push(new ServerConfig(serverConfigFileLine[0], Integer.parseInt(serverConfigFileLine[1]), Integer.parseInt(serverConfigFileLine[2])));
        }
        return servers;
    }

    public ServerConfig bind() {
        while (true) {
            synchronized (serverList) {
                if (serverList.isEmpty()) {
                    try {
                        serverList.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                return serverList.pop();
            }
        }
    }

    public void release(ServerConfig serverConfig) {
        synchronized (serverList) {
            serverList.push(serverConfig);
            serverList.notify();
        }
    }
}
