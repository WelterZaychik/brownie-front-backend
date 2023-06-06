package xyz.Brownie.commonutils;

import lombok.Data;

import java.util.Map;

@Data
public class Result {
    private int code;//状态码200 成功  400 失败
    private String msg; //成功?失败
    private Map data;//数据

    public static Result result(int code, String msg, Map data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
    //失败,原因在data中可以展示
    public static Result fail(int code,Map data){//data代表具体原因
        return result(code,"失败",data);
    }
    //成功带参数
    public static Result suc(int code,Map data){
        return result(code,"成功",data);
    }
    //成功不带参数
    public static Result suc(int code){
        return result(code,"成功",null);
    }
}
