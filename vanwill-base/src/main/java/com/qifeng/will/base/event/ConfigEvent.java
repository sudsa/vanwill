package com.qifeng.will.base.event;

import org.springframework.context.ApplicationEvent;

public class ConfigEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String msg ;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ConfigEvent(Object source,String msg) {
        super(source);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
