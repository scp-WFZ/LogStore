package Entity;

import java.util.*;

public class  Broker {
    Monitor monitor;
    HashMap<Integer, List<Monitor.Factor>> routerTable; // tenant_id -> [(shard_id, factor)]
    Queue<Task> tasks = new LinkedList<>(); // task queue

    public Broker(Monitor monitor){
        this.monitor = monitor;
        routerTable = monitor.routerTable;
    }
   public  void sendToBroker(double taskSize, int tenantId){
        tasks.add(new Task(tenantId, taskSize));
    }
   public void updateRouterTable(){
        Scheduler.updateRouter(monitor);
    }
   public void sendToWorkers(){
        List<Shard> shards = monitor.shards;
        Iterator<Task> iterator = tasks.iterator();
        while(iterator.hasNext()){// 这里做了简化，在已经超出了shard上限的时候还是可以发送成功
            Task task = iterator.next();
            List<Monitor.Factor> temp = routerTable.get(task.tenant_id);
            double task_size = task.task;
            for (Monitor.Factor factor: temp) {
                Shard shard = shards.get(factor.shard_ID);
                shard.getTaskFromBroker(task_size * factor.factor);
            }
            iterator.remove();
        }
    }
    static class Task{
        int tenant_id;
        double task;
        public Task(int t_id, double t){
            tenant_id = t_id;
            task = t;
        }
    }
}
