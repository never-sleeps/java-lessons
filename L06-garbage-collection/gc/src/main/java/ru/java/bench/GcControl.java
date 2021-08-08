package ru.java.bench;


class GcControl implements GcControlMBean {
    private final GcDemoApplication gcDemo;

    public GcControl(GcDemoApplication gcDemo) {
        this.gcDemo = gcDemo;
    }


    @Override
    public int getObjectArraySize() {
        int size = gcDemo.getObjectArraySize();
        System.out.println("current size:" + size);
        return size;
    }

    @Override
    public void setObjectArraySize(int size) {
        System.out.println("setting size:" + size);
        gcDemo.setObjectArraySize(size);
    }
}
