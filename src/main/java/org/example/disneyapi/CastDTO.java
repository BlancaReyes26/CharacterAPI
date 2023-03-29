package org.example.disneyapi;

import java.util.List;

public class CastDTO {
    @com.fasterxml.jackson.annotation.JsonProperty("data")
    private List<Data> data;
    @com.fasterxml.jackson.annotation.JsonProperty("count")
    private int count;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
