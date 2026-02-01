package com.module.framework.http;

public class ResultEntity<T> {

    private Integer code;
    private T data;
    private String msg;
    private boolean hasNext;
    private int count;
    private String promptType;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isOk(){
        return code==0;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPromptType() {
        return promptType;
    }

    public void setPromptType(String promptType) {
        this.promptType = promptType;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", hasNext=" + hasNext +
                ", count=" + count +
                ", promptType='" + promptType + '\'' +
                '}';
    }
}
