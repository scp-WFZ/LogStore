package Entity;

import java.util.HashMap;
import java.util.List;

/**
 * 这里Monitor的作用有两个：一个作为接口来观察虚拟环境下所有设备的工作情况，另一个如果添加新的设备，使用这种方式使得其它设备能够知道
 */
public class Monitor {
  public  List<Tenant> tenants;
  public List<Shard> shards;
  public  List<Worker> workers;
  public  HashMap<Integer, List<Factor>> routerTable;
  public Monitor(List<Tenant> tenants, List<Shard> shards, List<Worker> workers, HashMap<Integer, List<Factor>> routerTable){
      this.shards = shards;
      this.tenants = tenants;
      this.workers = workers;
      this.routerTable = routerTable;
  }
    public static class Factor{
        int shard_ID;
        double factor;
        public Factor(int id, double factor){
            this.shard_ID = id;
            this.factor = factor;
        }
        public String toString() {
            return "shard" + shard_ID +" " + factor;
        }
    }
}
