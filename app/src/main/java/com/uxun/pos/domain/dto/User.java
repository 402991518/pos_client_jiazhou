package com.uxun.pos.domain.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Administrator 收银前台用户
 */
public class User {
	public String UserId;// 收银员号
	public String UserName;// 收银员姓名
	public String OrgCode;// 商场编码
	public String UserPass;// 收银员密码
	public String UserPost;// 收银员岗位(0-收银员,1-收银主管,2-营业员)
	public String GrdCode;// 所属商铺
	public BigDecimal LicValue;// 最高打折率(0-100)

	public Byte Discount;// 打折权限
	public Byte Returned;// 退货权限
	public Byte Change;// 变价权限
	public Byte Reduce;// 减数权限
	public Byte Stat;// 统计权限
	public Byte Hangup;// 挂单权限
	public Byte Reprint;// 重打权限

	public Byte Cancel;// 单品取消权限
	public Byte AllCancel;// 整单取消权限
	public Byte Training;// 练习权限
	public Byte ChkGoods;// 开业验码权限
	public Byte Unlock;// 解锁权限
	public Byte CashDrawer;// 无销售开钱箱权限
	public Byte ExitProg;// 退出收银程序
	public Byte LoginOther;// 允许同时登录多台机
	public Byte Other1;// 保留权限1
	public Byte Other2;// 保留权限2
	public Byte Other3;// 保留权限3
	public Byte Status;// 有效状态(0-离职,1-在职)

	public String LicCard;// 权限卡或上岗卡
	public String LicCardID;// IC权限卡内卡号
	public Date LastTime;// 最后登录时间
	public String LastPosNo;// 最后登录收银机
	public Byte StarLevel;// 员工星级

	public String Remark;// 备注
	public String ExtCol1;// 扩展字段
	public String ExtCol2;// 扩展字段
	public String ExtCol3;// 扩展字段
	public String ExtCol4;// 扩展字段
	public String ExtCol5;// 扩展字段
	public String ExtCol6;// 扩展字段
	public String ExtCol7;// 扩展字段
	public String ExtCol8;// 扩展字段
	public String ExtCol9;// 扩展字段
	public String ExtCol10;// 扩展字段
}
