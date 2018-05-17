package com.uxun.pos.domain.dto;

import java.util.Date;

/**
 * @author Administrator POS机信息
 */
public class Device {

	public String IpAddress;// 收银机IP地扯
	public String OrgCode;// 商场编码
	public String PosNo;// 收银机号
	public String Descr;// 收银机描述
	public String GrdCode;// 收银机所属商铺

	public Byte TradeRound;// 总金额保留几位小数(最大只能保留二位)
	public Byte TradeWay;// 总金额小数的保留方式(1-舍去，2-四舍五入，3-进一)
	public Byte EcardIntegralRound;// 一卡通(会员)积分保留小数位(最大只能保留二位)
	public Byte EcardIntegralWay;// 一卡通(会员)积分保留小数位计算方式(1-舍去，2-四舍五入，3-进一)

	public Byte RecSalesman;// 是否记录营业员：0-不记录，1-记录
	public Byte RecSalesmanType;// 记录营业员方式：1-按整单记录，2-按单品记录
	public Byte RecSalesmanGAtt;// 记录营业员的商品类型：1-仅开单商品记录，2-所有商品都记录
	public Byte ChkSalesman;// 是否校验营业员合法性：0-不校验，1-校验
	public Byte RetrunMode;// 前台退货方式(1-必须输入单号退货，2-可无销售退货)
	public Byte ReturnCheckMem;// 会员消费的单不刷会员卡是否可退货：0-不可退，1-可退
	public Byte ReturnBankMIS;// 退货时原销售单用银行MIS是否必须使用银行MIS退款(0-否，1-是)

	public String TblHead;// 销售分表_单头
	public String TblDetail;// 销售分表_商品明细
	public String TblPay;// 销售分表_付款明细
	public String TblRebate;// 销售分表_促销明细
	public String TblGoodsPay;// 销售分表_商品付款分摊

	public String Version;// 版本号
	public Date VersionDate;// 版本日期
	public String LastUser;// 最后登录用户
	public Date LastTime;// 最后登录时间

	public Byte Status;// 启用标志(0-未启用，1-启用)

	public Byte LoginType;// 登录方式：1-用户密码，2-磁条卡，3-IC卡
	public Byte LogoutCheck;// 退出系统是否验证权限：0-不验证，1-验证
	public Byte DownloadType;// 下载数据的时机：1-每次登录时下载，2-每天只下载一次
	public Byte DataSource;// 收银方式(0-统一收银，1-无线POS机，2-导入，3-租赁收银)
	public Byte PriceModel;// 取价模式(1-后台数据库，2-前台数据库)
	public Byte PopPayWin;// 是否弹出支付窗口:0-不，1-是
	public Short MaxNumSale;// 最大销售记录数

	public Byte MaxNumHang;// 最大允许挂单数
	public Byte NoSaleOpenCashbox;// 是否允许无销售开钱箱：1-不允许，2-收银员本人，3-双密码(收银员及收银主管)，4-有权限无需密码
	public Byte AuthorizeType;// 授权方式：1-用户密码，2-磁条卡，3-IC卡
	public Byte RecMerchantNo;// 是否记录商铺开单票号：0-不记录，1-记录
	public Byte InputGoodsByMer;// 是否按商铺录入商品：0-不，1-是
	public Byte LimitGrdPayType;// 是否限制商铺付款方式(0-不，1-是)
	public Byte RecSaleInfo;// 是否记录录入的商品、付款信息：0-不记录，1-记录
	public Short IntervalCheckkNet;// 空闲时检查网络间隔时间(秒)：间隔为0则不主动检查网络
	public Short IntervalCheckMail;// 取邮件时间间隔(秒)：最小60秒
	public Byte BankMISType;// 银联接口类型：0-无接口，1-深圳瑞柏，2-南京银石，3-杭州新利，4-福建联迪

	public Byte PrintCashier;// 是否打印收银员(0-不，1-是)
	public Byte PrintTradeTime;// 是否打印交易时间(0-不，1-是)
	public Byte PrintTelphone;// 是否打印商场联系电话(0-不，1-是)
	public Byte PrintAddress;// 是否打印商场地址(0-不，1-是)
	public Byte PrintCaps;// 是否打印大写金额(0-不，1-是)
	public Byte PrintTicketNum;// 小票打印联数
	public Byte PirntEcardValue;// 是否打印储值卡余额(0-不，1-是)
	public Byte PrintWholeCancel;// 整单打印时是否打印整单取消商品(0-不，1-是)
	public Byte PrintWholeHang;// 整单打印时是否打印挂单商品(0-不，1-是)
	public Byte PrintCheck;// -盘点是否打印(0-不，1-是)
	public Byte PrintGoodsTest;// 开业验码是否打印(0-不，1-是)
	public Byte PrintSalesName;// 打印营业员(0-不，1-是)
	public Byte PrintPriceType;// 小票单价、金额打印方式(1-打印原价，2-打印实价)
	public Byte PrintSumReturn;// 重印小票是否计算退货数量(0-不计算，1-计算)
	public Byte PrintBankMIS;// 打印银联签购单(0-不，1-是)
	public Byte PrintByGrdCode;// 是否按商铺分单打印(0-不，1-是)

	public Byte SaleDayKeptLDB;// 销售单在前台数据库备份的保留天数
	public Byte LogDayKeptLocal;// 前台日志文件的保留天数
	public String LockShowText;// 前台锁屏时的文字
	public Byte Screen_HScrollBar;// 收银程序销售界面是否显示滚动条
	public Byte DoubleScreen;// 是否双屏：0-否，1-是
	public Byte Keyboard_Show;// 是否显示软键盘：0-否，1-是
	public Byte TaskBarHide;// 是否隐藏任务栏(0-否，1-是)

	public String WebPayWS;// 移动支付WebService地址
	public Byte WebPayGoods;// 移动支付是否发送商品信息(0-否，1-是)

	public Byte FtpVideo;// 是否下载视频(0-不下载，1-下载)
	public String FtpAddress;// 视频FTP地址
	public String FtpUser;// 视频FTP用户
	public String FtpPassword;// 视频FTP密码
	public String FtpPath;// 视频FTP路径
	public Byte MarkType;// 评价是否单独记录(0-不，1-是)
	public Byte OnlineStatus;// 是否在线(0-不在线，1-在线)

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
