package at.ezylot.portmux.services;

public class SSH_2_0_OPENSSH extends Service {

    public SSH_2_0_OPENSSH(String targetIP) {
        super(targetIP, 22);
    }

    public SSH_2_0_OPENSSH(String targetIP, Integer port) {
        super(targetIP, port);
    }

    @Override
    public Boolean CheckInput(String input) {
        return input.startsWith("SSH-2.0-OpenSSH");
    }

}
