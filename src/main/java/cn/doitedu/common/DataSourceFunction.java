package cn.doitedu.common;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class DataSourceFunction implements SourceFunction<Data> {

    @Override
    public void run(SourceContext<Data> ctx) throws Exception {
        new Thread(new DataGenRunnalbe(ctx)).start();
        //new Thread(new DataGenRunnalbe(ctx)).start();
        //new Thread(new DataGenRunnalbe(ctx)).start();

        Thread.sleep(Long.MAX_VALUE);

    }

    @Override
    public void cancel() {

    }
}
