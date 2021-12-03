package com.qifeng.will.base.warrior.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhw
 * @since 2021-07-22
 */
@TableName("sys_config")
public class SysConfig extends Model<SysConfig> {

    private static final long serialVersionUID = 1L;

	private String variable;
	private String value;
	@TableField("set_time")
	private Date setTime;
	@TableField("set_by")
	private String setBy;


	public String getVariable() {
		return variable;
	}

	public SysConfig setVariable(String variable) {
		this.variable = variable;
		return this;
	}

	public String getValue() {
		return value;
	}

	public SysConfig setValue(String value) {
		this.value = value;
		return this;
	}

	public Date getSetTime() {
		return setTime;
	}

	public SysConfig setSetTime(Date setTime) {
		this.setTime = setTime;
		return this;
	}

	public String getSetBy() {
		return setBy;
	}

	public SysConfig setSetBy(String setBy) {
		this.setBy = setBy;
		return this;
	}

	@Override
	protected Serializable pkVal() {
		return this.variable;
	}

}
