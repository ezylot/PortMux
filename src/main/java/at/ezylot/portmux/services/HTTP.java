package at.ezylot.portmux.services;

import java.util.regex.Pattern;

public class HTTP extends Service {

    public HTTP(String targetIP) {
        super(targetIP, 80);
    }

    public HTTP(String targetIP, Integer port) {
        super(targetIP, port);
    }

    @Override
    public Boolean CheckInput(String input) {
        return Pattern.compile("(GET|POST|HEAD|PUT|DELETE|TRACE|OPTIONS|CONNECT).*")
                .matcher(input)
                .find();
    }
}
