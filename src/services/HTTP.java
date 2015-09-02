package services;

public class HTTP extends Service {


    @Override
    public Boolean CheckInput(String input) {
        if(input.startsWith("GET ")) return true;
        if(input.startsWith("POST ")) return true;
        if(input.startsWith("HEAD ")) return true;
        if(input.startsWith("PUT ")) return true;
        if(input.startsWith("DELETE ")) return true;
        if(input.startsWith("TRACE ")) return true;
        if(input.startsWith("OPTIONS ")) return true;
        if(input.startsWith("CONNECT ")) return true;
        return false;
    }

    @Override
    public Integer getPort() {
        return 17034;
    }

    @Override
    public String getIP() {
        return "192.168.1.201";
    }

}
