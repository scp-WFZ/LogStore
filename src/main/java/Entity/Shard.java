package Entity;

public class Shard {
    int id; // 和在list的index相同
    double max_size; // 所能处理的最大任务量
    double cur_processing_size; // 用来表示现在接收到的任务量
    double have_processed_size; // 用来统计处理完的任务量

    public Shard(int id, double max_size){
        this.id = id;
        this.max_size = max_size;
        cur_processing_size = 0;
        have_processed_size = 0;
    }
    public boolean getTaskFromBroker(double task_size){
        cur_processing_size += task_size;
        return true;
    }
    public boolean processTask(double processNum){
        cur_processing_size -= processNum;
        have_processed_size += processNum;
        return true;
    }
}
