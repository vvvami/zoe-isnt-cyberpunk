package net.vami.zoe.capability;

public class CapabilityContainer<T> {
    private T value;

    CapabilityContainer(T value) {
        this.value = value;
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

}
