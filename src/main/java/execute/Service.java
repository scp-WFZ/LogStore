package execute;

public class Service {
    private int serverId;
    private int capacity;
    private int occupied;

    public Service(int id, int capacity) {
        this.serverId = id;
        this.capacity = capacity;
        this.occupied = 0;
    }

    public void reset() {
        this.occupied = 0;
    }

    public boolean satisfy(int data) {
        return data + this.occupied <= this.capacity;
    }

    public void serve(int data) throws Exception {
        if (data + this.occupied <= this.capacity) {
            this.occupied += data;
            System.out.println("Server " + this.serverId + " is occupied " + data + ", totally use " + this.occupied + "/" + this.capacity);
        } else {
            throw new Exception("Server " + this.serverId + " can't take on " + data + " more, totally use " + this.occupied + "/" + this.capacity);
        }
    }

    public void release(int data) throws Exception {
        if (this.occupied - data >= 0) {
            this.occupied -= data;
            System.out.println("Server " + this.serverId + " releases " + data + ", totally use " + this.occupied + "/" + this.capacity);
        } else {
            throw new Exception("Server " + this.serverId + " doesn't have " + data);
        }
    }
}
