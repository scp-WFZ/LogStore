package test;

import Query.LoadBalancer;
import execute.Service;

public class LoadBalancerTest {
   // @Test
    public void test1(){
        LoadBalancer loadBalancer = new LoadBalancer();
        loadBalancer.addService(5);
        loadBalancer.addService(5);
        loadBalancer.addService(5);
        loadBalancer.addService(5);
        loadBalancer.addService(5);
        try {
            int data = 3;
            Service service = loadBalancer.clock(data);
            service.serve(data);

            data = 4;
            service = loadBalancer.clock(data);
            service.serve(data);

            data = 1;
            service = loadBalancer.clock(data);
            service.serve(data);

            data = 4;
            Service service1 = loadBalancer.clock(data);
            service1.serve(data);

            data = 5;
            service = loadBalancer.clock(data);
            service.serve(data);

            data = 4;
            service = loadBalancer.clock(data);
            service.serve(data);

            service1.release(4);

            data = 2;
            service = loadBalancer.clock(data);
            service.serve(data);

            data = 1;
            service = loadBalancer.clock(data);
            service.serve(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
