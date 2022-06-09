package Testbench;

import Entity.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试流程：
 * 1. 首先创建Broker,Monitor, Shard, Tenant,Worker的实例，并将Shard分配给 Worker
 * 2. 其次，利用概率来随机尝试任务给broker,这个需要1 sec,一共进行6次
 * 3. broker收集到任务需要1sec,其次参照当前的router(这里可能会触发更新操作，但是不消耗时间，因为考虑到并行的情况）分发到具体的worker需要5 sec
 * 4. 对于worker来讲，它能够处理的时间应该和 borker 时间一致为6s.
 */
public class Test {
    public static int vir_clk = 0; // 用来记录虚拟时间
    public static void main(String[] args) {
        Initiator initiator = new Initiator();
        initiator.initiate();
        List<Tenant> tenants = initiator.tenants;
        List<Shard> shards = initiator.shards;
        List<Worker> workers = initiator.workers;
        Monitor monitor = initiator.monitor;
        Broker broker = initiator.broker;
        while(vir_clk <= 1800){
            for(Tenant tenant : tenants) {
                tenant.sendTask();
            }
            if(vir_clk % 300 == 0) {
                broker.updateRouterTable(); //申请更新路由表
                for (Map.Entry<Integer, List<Monitor.Factor>> entry: monitor.routerTable.entrySet()) {
                    System.out.println(entry.getKey());
                    entry.getValue().forEach(System.out::println);
                }
            }
            broker.sendToWorkers();
            for (Worker worker: workers) {
                worker.handleTask();
            }
            vir_clk += 6;
        }
    }
}
