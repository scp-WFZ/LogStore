package execute;

public class ServiceFactory {
    private int number;

    public ServiceFactory() {
        this.number = 0;
    }

    public Service createService(int capacity) {
        return new Service(this.number++, capacity);
    }

    public int getSize() {
        return this.number;
    }
}
