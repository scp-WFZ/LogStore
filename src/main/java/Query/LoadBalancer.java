package Query;

import execute.Service;
import execute.ServiceFactory;

import java.util.ArrayList;

public class LoadBalancer {
    private ServiceFactory serviceFactory;
    public ArrayList<Service> services;
    public ArrayList<Boolean> lcu;

    public LoadBalancer() {
        this.serviceFactory = new ServiceFactory();
        services = new ArrayList<>();
        lcu = new ArrayList<>();
    }

    public void addService(int data) {
        services.add(serviceFactory.createService(data));
        lcu.add(true);
    }

    public Service clock(int data) throws Exception {
        boolean find = false;
        Service result = null;
        while (!find) {
            for (int i = 0; i < services.size(); i++) {
                if (lcu.get(i)) {
                    if (services.get(i).satisfy(data)) {
                        result = services.get(i);
                        find = true;
                        lcu.set(i, false);
                        break;
                    }
                    lcu.set(i, false);
                } else {
                    lcu.set(i, true);
                }
            }
        }
        return result;
    }
}
