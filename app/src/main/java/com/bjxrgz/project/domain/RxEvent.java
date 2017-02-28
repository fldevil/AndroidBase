package com.bjxrgz.project.domain;

import java.io.Serializable;

/**
 * Created by JiangZhiGuo on 2016-11-25.
 * describe 用来RxBUs传输的实体类
 */
public class RxEvent<T> implements Serializable {

    /* 相当于频道 */
    public enum ID {
        common
    }

    private ID id; // 订阅的id (可以在上面的枚举ID中定义)
    private T object; // 要传输的数据

    public RxEvent(ID id, T object) {
        this.id = id;
        this.object = object;
    }

    public RxEvent.ID getId() {
        return id;
    }

    public void setId(RxEvent.ID id) {
        this.id = id;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "RxEvent{" +
                "id=" + id +
                ", object=" + object +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RxEvent)) return false;

        RxEvent<?> rxEvent = (RxEvent<?>) o;

        return id == rxEvent.id && (object != null ? object.equals(rxEvent.object) : rxEvent.object == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }
}
