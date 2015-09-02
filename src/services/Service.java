package services;

import java.net.Socket;

public abstract class Service {

    public abstract Boolean CheckInput(String input);
    public abstract Integer getPort();
    public abstract String getIP();
}
