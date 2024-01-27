package lookup.test;

import cn.doitedu.common.Data;
import cn.doitedu.common.SourceUtil;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class PreFilterTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = SourceUtil.getEnv();
        StreamTableEnvironment tenv = SourceUtil.getTenv(env);

        DataStreamSource<Data> stream = SourceUtil.getStream(env);
        SourceUtil.registerTable("a",stream,tenv);

        tenv.executeSql("CREATE TABLE b (                          \n" +
                "  dt date,                                         \n" +
                "  user_id bigint,                                  \n" +
                "  member_level  int,                               \n" +
                "  user_name  string                                \n" +
                ") WITH (                                           \n" +
                "   'connector' = 'jdbc',                           \n" +
                "   'url' = 'jdbc:mysql://doitedu:9030/flinktest',  \n" +
                "   'table-name' = 'dim_user_info',                 \n" +
                "   'username' = 'root',                            \n" +
                "   'password' = 'root',                            \n" +
                //"   'lookup.extra_filter' = 'dt=\"2024-01-27\"'     \n" +
                "   'lookup.extra_filter' = 'member_level>0'     \n" +
                ")");

        tenv.executeSql("create temporary view vx as select * from b where dt='2024-01-27' ");


        tenv.executeSql(
                "explain select \n" +
                "a.*,\n" +
                "vx.* \n" +
                "from a join vx   \n" +
                "on a.user_id=vx.user_id ").print();

    }
}
