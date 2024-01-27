package cn.doitedu.other.test;

import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class StateAnalysis {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.enableCheckpointing(10000);
        env.getCheckpointConfig().setCheckpointStorage("file:///d:/ckpt");

        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);

        DataStreamSource<String> ds = env.socketTextStream("localhost", 9999);

        SingleOutputStreamOperator<Integer> res = ds.keyBy(integer -> 1).map(new RichMapFunction<String, Integer>() {
            ValueState<Integer> state;
            @Override
            public void open(Configuration parameters) throws Exception {
                state = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("v", Integer.class));
            }

            @Override
            public Integer map(String data) throws Exception {


                if(data.equals("4")  && RandomUtils.nextInt(1,10) %3== 0 ){
                    throw new RuntimeException("hahaha");
                }

                int old = state.value() == null? 0 : state.value();
                state.update(old + Integer.parseInt(data));



                return state.value();
            }
        });

        res.print();

        env.execute();
    }
}