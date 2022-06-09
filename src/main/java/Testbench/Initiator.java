package Testbench;

import Entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 测试流程：
 * 1. 首先创建Broker,Monitor, Shard, Tenant,Worker的实例，并将Shard分配给 Worker
 * 2. 其次，利用概率来随机尝试任务给broker,这个需要1 sec,一共进行6次，所以总共需要6s
 * 3. broker收集到任务需要1sec,其次参照当前的router(这里可能会触发更新操作，但是不消耗时间，因为考虑到并行的情况）分发到具体的worker需要5 sec
 * 4. 对于worker来讲，它能够处理的时间应该和 broker 时间一致为6s.
 * 5. 其中周期性的在300s的时候调用scheduler 来router table.
 */
public class Initiator {
    static final int initTenant = 10;
    static final int initShard = 30;
    static final int initWorker = 5;
    static final int interval = 6;
    static final int max_size_shard = 500;
    static final int max_size_worker = 3000;
    static final double process_ability = 200;
   // 为了体现来自租户的不同，其余的shard 和 worker都选择使用相同的属性
    public int[] busyRatios = {100, 99, 98, 50, 40, 30, 20 ,15, 10, 5};
    public double[] task_size_max = {100, 99, 98, 50, 40, 30, 20, 15, 10, 5};
    public List<Tenant> tenants;
    public List<Shard> shards;
    public  List<Worker> workers;
    public Monitor monitor;
    public Broker broker;
    public void initiate(){
        tenants = new ArrayList<>();
        //这个时候的broker是个null pointer
        for (int i = 0; i < initTenant; i++) {
            tenants.add(new Tenant(i,broker, busyRatios[i], task_size_max[i]));
        }
        shards = new ArrayList<>();
        for (int i = 0; i < initShard; i++) {
            shards.add(new Shard(i, max_size_shard));
        }
        workers = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < initWorker; i++) {
            List<Shard> temp = new ArrayList<>();
            for (int j = 0; j < (initShard / initWorker); j++) {
                temp.add(shards.get(index));
                index ++;
            }
            workers.add(new Worker(i, temp, 1, max_size_worker, process_ability));
        }
        index = 0;
        HashMap<Integer, List<Monitor.Factor>> routerTable = new HashMap<>();
        for (int i = 0; i < initTenant; i++) {
            List<Monitor.Factor> factors = new ArrayList<>();
            for (int j = 0; j < (initShard / initTenant); j++) {
                factors.add(new Monitor.Factor(index, (double)(1.0 / (initShard / initTenant))));
                index++;
            }
            routerTable.put(i, factors);
        }
        monitor = new Monitor(tenants, shards, workers, routerTable);
        broker = new Broker(monitor);
        // 更新tenants里面的broker
        for (int i = 0; i < initTenant; i++) {
            tenants.get(i).broker = broker;
        }
    }
}

