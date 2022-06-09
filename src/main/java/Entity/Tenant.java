package Entity;

import java.util.*;

public class Tenant {
    int id; // the same as the index in List
    int send_task_num; // 用来记录累积发送的任务数目
    double cur_wait_to_process; // 用来记录当前发送的任务量
    double have_processed; // 用来记录累积发送的任务
    public Broker broker;
    int busyRatio; // from 1 to 100, busyRatio 越大越有可能发出任务
    double task_size_max; // 一次最大可能产生的任务量
    Random random;
    int interval; // 用来定义产生数据的间隔
    public Tenant(int id, Broker broker, int busyRatio, double task_size_max){
        this.id = id;
        this.broker = broker;
        this.busyRatio = busyRatio;
        this.task_size_max = task_size_max;
        this.send_task_num = 0;
        this.cur_wait_to_process = 0;
        this.have_processed = 0;
        this.random = new Random();
        this.interval = 6;
    }
    public boolean canProduceTask(){
        int temp = random.nextInt(100);
        return busyRatio > temp;
    }
    public double produceOneTask(){
        return  task_size_max * random.nextDouble();
    }
    public void sendTask(){
        double task = 0;
        for (int i = 0; i < interval; i++) {
            if (canProduceTask()) {
                task += produceOneTask();
            }
        }
        cur_wait_to_process = task;
        have_processed += cur_wait_to_process;
        if(task > 0) {
            broker.sendToBroker(task, id);
            send_task_num += 1;
        }
    }
}
