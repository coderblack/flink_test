package cn.doitedu.common;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.contrib.streaming.state.EmbeddedRocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class SourceUtil {

    public static StreamExecutionEnvironment getEnv() {
        Configuration conf = new Configuration();
        conf.setBoolean("classloader.check-leaked-classloader", false);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);

        EmbeddedRocksDBStateBackend embeddedRocksDBStateBackend = new EmbeddedRocksDBStateBackend(true);
        embeddedRocksDBStateBackend.setDbStoragePath("file:///d:/rocksdb");
        env.setStateBackend(embeddedRocksDBStateBackend);

        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointStorage("file:///d:/ckpt");
        env.setParallelism(1);

        return env;
    }


    public static StreamTableEnvironment getTenv(StreamExecutionEnvironment env) {
        StreamTableEnvironment tenv = StreamTableEnvironment.create(env);
        return tenv;
    }


    public static DataStreamSource<Data> getStream(StreamExecutionEnvironment env) {
        return env.addSource(new DataSourceFunction());
    }


    public static void registerTable(String viewName, DataStreamSource<Data> dataStream, StreamTableEnvironment tenv) {
        tenv.createTemporaryView(viewName, dataStream, Schema.newBuilder()
                .column("user_id", DataTypes.BIGINT())
                .column("event_id", DataTypes.STRING())
                .column("page_id", DataTypes.STRING())
                .column("action_time", DataTypes.BIGINT())
                .columnByExpression("pt", "proctime()")
                //.columnByExpression("rt", "to_timestamp_ltz(action_time,3)")
                //.watermark("rt", "rt")
                .build());
    }


}
