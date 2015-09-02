package services;

public class HTTP extends Service {


    @Override
    public Boolean CheckInput(String input) {
        return input.startsWith("GET ");
    }

    @Override
    public Integer getPort() {
        return 80;
    }

    @Override
    public String getIP() {
        return "127.0.0.1";
    }

}
