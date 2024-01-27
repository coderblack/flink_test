package cn.doitedu.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public  class Data{
    private Long user_id;
    private String event_id;
    private String page_id;
    private Long action_time;
}