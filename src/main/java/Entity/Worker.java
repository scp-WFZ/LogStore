package Entity;

import java.util.List;

public class Worker{
    int id;
    List<Shard> allocated_shards;
    double high_watermark; // 用来表示它能处理
    double max_size; // 最大的存储量
    double process_ability; // per second to handle task in shard
    int interval; // the same means in tenant

    public Worker(int id, List<Shard> shards, double high_watermark, double max_size, double process_ability){
        this.id = id;
        this.allocated_shards = shards;
        this.high_watermark = high_watermark;
        this.max_size = max_size;
        this.process_ability = process_ability;
        this.interval = 6;
    }
    // 使用priority的策略来分配处理时间
    public void handleTask(){
        double total_process = interval * process_ability;
        double[] ratio = new double[allocated_shards.size()];
        double sum = 0;
        for (int i = 0; i < ratio.length; i++) {
            Shard temp = allocated_shards.get(i);
            ratio[i] = temp.cur_processing_size / temp.max_size; // 用当前积累的任务量除以最大容量
            sum += ratio[i];
        }
        for (int i = 0; i < ratio.length; i++) {
            Shard temp = allocated_shards.get(i);
            temp.processTask(total_process * (ratio[i] / sum));
        }
    }
}
