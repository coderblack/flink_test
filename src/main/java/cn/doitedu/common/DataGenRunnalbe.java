package cn.doitedu.common;

import cn.doitedu.common.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class DataGenRunnalbe implements Runnable{
    SourceFunction.SourceContext<Data> ctx;
    Data data;

    public DataGenRunnalbe(SourceFunction.SourceContext<Data> ctx) {
        this.ctx = ctx;
        data = new Data();
    }

    @Override
    public void run() {

        while(true){
        //for(long i=1;i<=4;i++){
            data.setUser_id(RandomUtils.nextLong(1,4));
            //data.setUser_id(i);
            data.setPage_id(RandomStringUtils.randomAlphabetic(1,10));
            data.setEvent_id(RandomStringUtils.randomAlphabetic(8,8));
            data.setAction_time(System.currentTimeMillis());
            ctx.collect(data);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
