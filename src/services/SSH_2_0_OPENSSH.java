package services;

public class SSH_2_0_OPENSSH extends Service {
    @Override
    public Boolean CheckInput(String input) {
        return input.startsWith("SSH-2.0-OpenSSH");
    }

    @Override
    public Integer getPort() {
        return 17033;
    }

    @Override
    public String getIP() {
        return "192.168.1.201";
    }
}
