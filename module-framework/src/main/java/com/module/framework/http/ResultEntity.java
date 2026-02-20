package com.module.framework.http;

public class ResultEntity<D, R> {

    private Integer code;
    private D data;
    private R rows;
    private String msg;
    private Integer total;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public R getRows() {
        return rows;
    }

    public void setRows(R rows) {
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public boolean isOk(){
        return code==200;
    }



    @Override
    public String toString() {
        return "ResultEntity{" +
                "code=" + code +
                ", data=" + data +
                ", rows=" + rows +
                ", msg='" + msg + '\'' +
                ", total=" + total +
                '}';
    }
}
