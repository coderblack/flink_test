package cn.doitedu.state.test;

import cn.doitedu.common.Data;
import cn.doitedu.common.SourceUtil;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class RockdbStatebackendTest {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = SourceUtil.getEnv();
        StreamTableEnvironment tenv = SourceUtil.getTenv(env);

        DataStreamSource<Data> dataStream = SourceUtil.getStream(env);
        SourceUtil.registerTable("t1",dataStream,tenv);

        tenv.executeSql(
                " SELECT                                                " +
                        "   window_start,                                       " +
                        " 	window_end,                                         " +
                        " 	count(distinct user_id) as uv,                          " +
                        " 	count(1) as all_pv,                          " +
                        " 	count(1) filter(where event_id <>'a') as notapv,  " +
                        " 	count(1) filter(where event_id <>'b') as notbpv   " +
                        " FROM TABLE(                                           " +
                        "   TUMBLE(TABLE t1,DESCRIPTOR(rt),INTERVAL '1' MINUTE) " +
                        " )                                                     " +
                        " GROUP BY                                              " +
                        "     window_start,                                     " +
                        " 	  window_end,                                       " +
                        " 	  event_id                                          "
        ).print();

        env.execute();

    }



}
