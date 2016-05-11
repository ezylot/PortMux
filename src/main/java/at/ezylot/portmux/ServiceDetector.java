package at.ezylot.portmux;

import at.ezylot.portmux.services.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceDetector {

    private String ip;

    public ServiceDetector(String serverIP) {
        this.ip = serverIP;
    }

    private static List<Service> registeredServices = new ArrayList<>();

    public static void registerService(String name, Service service) {
        registeredServices.add(service);
    }

    public static Service detect(String message) {
        for(Service service : registeredServices) {
            if(service.CheckInput(message) == true) {
                return service;
            }
        }
        return null;
    }

}
