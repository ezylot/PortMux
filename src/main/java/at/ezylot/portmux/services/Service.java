package at.ezylot.portmux.services;

public abstract class Service {

    protected String targetIp;
    protected Integer port;

    public Service(String targetIP, Integer port) {
        this.targetIp = targetIP;
        this.port = port;
    }

    public abstract Boolean CheckInput(String input);

    public Integer getPort() { return this.port; }
    public void setPort(Integer port) { this.port = port; }

    public String getTargetIP() { return this.targetIp; }
    public void setTargetIP(Integer port) { this.port = port; }
}
