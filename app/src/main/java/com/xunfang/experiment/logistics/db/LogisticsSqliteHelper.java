package com.xunfang.experiment.logistics.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * <p>Title：物流管理系统</p>
 * <p>Description：数据库操作帮助类</p>

 * <p>Copyright: Copyright (c) 2012</p> 
 * @version 1.0.0.0
 * @author sas 
 */
public class LogisticsSqliteHelper extends SQLiteOpenHelper {

	//数据库名称
	private static String DATABASES_NAME = "logistics.db";
	//数据库版本
	private static int VERSION = 1;
	
	private static LogisticsSqliteHelper instance = null;
	
    public static LogisticsSqliteHelper getInstance(Context context){
        if(instance == null){
            instance = new LogisticsSqliteHelper(context);
        }
        return instance;
    }
    private LogisticsSqliteHelper(Context context){
		this(context, VERSION);
	}
    private LogisticsSqliteHelper(Context context,int version){
		this(context, DATABASES_NAME , null, version);
	}
    private LogisticsSqliteHelper(Context context, String name,CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 创建数据库时调用，包含表的创建和初始数据的插入
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("-----------------------------database onCreate----------------------");
		db.beginTransaction();//事务开始
		try {
			db.execSQL("create table province(id integer primary key autoincrement,code integer not null,name varchar(10) not null," +
					"unique(code),unique(name))");
			db.execSQL("create table city(id integer primary key autoincrement,code integer not null,name varchar(10) not null," +
					"p_code integer not null,foreign key(p_code) references province(code),unique(code))");
			db.execSQL("create table goodstype(id integer primary key autoincrement,code integer not null,name varchar(100) not null," +
					"unique(code),unique(name))");
			db.execSQL("create table goodsinfo(id integer primary key autoincrement,typeid integer not null,name varchar(100) not null," +
					"unique(name),foreign key(typeid) references goodstype(code))");
			db.execSQL("create table station(id integer primary key autoincrement,name varchar(100) not null,addr integer not null," +
					"unique(name),foreign key(addr) references city(code))");
			db.execSQL("create table user(id integer primary key autoincrement,loginname varchar(20) not null," +
					"password varchar(20) not null,username varchar(20) not null,type integer not null,stationid integer not null," +
					"foreign key(stationid) references station(id),unique(loginname))");
			db.execSQL("create table cardinfo(cardsn char(8) primary key,cardid8bit char(8) not null," +
					"cardtype integer not null,state integer not null)");
			db.execSQL("create table orderform(id integer primary key autoincrement,cardid char(8) not null,ordernumber char(20) not null," +
					"goodsinfo varchar(200) not null,sendaddr integer not null,receiveaddr integer not null,state integer not null," +
					"foreign key(cardid) references cardinfo(cardsn),foreign key(sendaddr) references station(id)," +
					"foreign key(receiveaddr) references station(id),unique(ordernumber))");
			db.execSQL("create table goodsfollow(id integer primary key autoincrement,ordernumber char(20) not null,currentstation integer not null," +
					"information varchar(200) not null,handletime char(14) not null,operater integer not null,state integer not null," +
					"foreign key(ordernumber) references orderform(ordernumber),foreign key(currentstation) references station(id)," +
					"foreign key(operater) references user(id))");
			String[] provinceSql = {"insert into province(code,name) values(0,'北京')",
					"insert into province(code,name) values(1,'上海')",
					"insert into province(code,name) values(2,'天津')",
					"insert into province(code,name) values(3,'重庆')",
					"insert into province(code,name) values(4,'云南')",
					"insert into province(code,name) values(5,'贵州')",
					"insert into province(code,name) values(6,'四川')",
					"insert into province(code,name) values(7,'广西')",
					"insert into province(code,name) values(8,'广东')",
					"insert into province(code,name) values(9,'福建')",
					"insert into province(code,name) values(10,'湖南')",
					"insert into province(code,name) values(11,'江西')",
					"insert into province(code,name) values(12,'安徽')",
					"insert into province(code,name) values(13,'江苏')",
					"insert into province(code,name) values(14,'湖北')",
					"insert into province(code,name) values(15,'辽宁')",
					"insert into province(code,name) values(16,'陕西')",
					"insert into province(code,name) values(17,'浙江')",
					"insert into province(code,name) values(18,'山东')",
					"insert into province(code,name) values(19,'黑龙江')",
					"insert into province(code,name) values(20,'吉林')",
					"insert into province(code,name) values(21,'河南')",
					"insert into province(code,name) values(22,'新疆')",
					"insert into province(code,name) values(23,'甘肃')",
					"insert into province(code,name) values(24,'山西')",
					"insert into province(code,name) values(25,'河北')",
					"insert into province(code,name) values(26,'内蒙古')",
					"insert into province(code,name) values(27,'宁夏')",
					"insert into province(code,name) values(28,'青海')",
					"insert into province(code,name) values(29,'海南')",
					"insert into province(code,name) values(30,'西藏')"
					};
			for(String sql:provinceSql){ 
				db.execSQL(sql);
			}
			String[] citySql = {"insert into city(code,name,p_code) values(0,'北京',0)",
				"insert into city(code,name,p_code) values(1,'上海',1)",
				"insert into city(code,name,p_code) values(4,'天津',2)",
				"insert into city(code,name,p_code) values(10,'重庆',3)",
				"insert into city(code,name,p_code) values(23,'昆明',4)",
				"insert into city(code,name,p_code) values(147,'大理',4)",
				"insert into city(code,name,p_code) values(28,'贵阳',5)",
				"insert into city(code,name,p_code) values(146,'遵义',5)",
				"insert into city(code,name,p_code) values(9,'成都',6)",
				"insert into city(code,name,p_code) values(96,'绵阳',6)",
				"insert into city(code,name,p_code) values(144,'德阳',6)",
				"insert into city(code,name,p_code) values(145,'宜宾',6)",
				"insert into city(code,name,p_code) values(29,'南宁',7)",
				"insert into city(code,name,p_code) values(58,'柳州',7)",
				"insert into city(code,name,p_code) values(79,'桂林',7)",
				"insert into city(code,name,p_code) values(142,'玉林',7)",
				"insert into city(code,name,p_code) values(143,'北海',7)",
				"insert into city(code,name,p_code) values(2,'广州',8)",
				"insert into city(code,name,p_code) values(3,'深圳',8)",
				"insert into city(code,name,p_code) values(34,'佛山',8)",
				"insert into city(code,name,p_code) values(35,'东莞',8)",
				"insert into city(code,name,p_code) values(54,'中山',8)",
				"insert into city(code,name,p_code) values(55,'珠海',8)",
				"insert into city(code,name,p_code) values(56,'汕头',8)",
				"insert into city(code,name,p_code) values(80,'惠州',8)",
				"insert into city(code,name,p_code) values(81,'湛江',8)",
				"insert into city(code,name,p_code) values(82,'江门',8)",
				"insert into city(code,name,p_code) values(83,'茂名',8)",
				"insert into city(code,name,p_code) values(138,'清远',8)",
				"insert into city(code,name,p_code) values(139,'揭阳',8)",
				"insert into city(code,name,p_code) values(140,'梅州',8)",
				"insert into city(code,name,p_code) values(141,'肇庆',8)",
				"insert into city(code,name,p_code) values(18,'厦门',9)",
				"insert into city(code,name,p_code) values(21,'福州',9)",
				"insert into city(code,name,p_code) values(38,'泉州',9)",
				"insert into city(code,name,p_code) values(137,'漳州',9)",
				"insert into city(code,name,p_code) values(20,'长沙',10)",
				"insert into city(code,name,p_code) values(84,'株洲',10)",
				"insert into city(code,name,p_code) values(85,'岳阳',10)",
				"insert into city(code,name,p_code) values(86,'衡阳',10)",
				"insert into city(code,name,p_code) values(134,'湘潭',10)",
				"insert into city(code,name,p_code) values(135,'常德',10)",
				"insert into city(code,name,p_code) values(136,'郴州',10)",
				"insert into city(code,name,p_code) values(27,'南昌',11)",
				"insert into city(code,name,p_code) values(93,'赣州',11)",
				"insert into city(code,name,p_code) values(94,'九江',11)",
				"insert into city(code,name,p_code) values(132,'景德镇',11)",
				"insert into city(code,name,p_code) values(133,'新余',11)",
				"insert into city(code,name,p_code) values(30,'合肥',12)",
				"insert into city(code,name,p_code) values(95,'芜湖',12)",
				"insert into city(code,name,p_code) values(123,'蚌埠',12)",
				"insert into city(code,name,p_code) values(124,'淮南',12)",
				"insert into city(code,name,p_code) values(125,'马鞍山',12)",
				"insert into city(code,name,p_code) values(131,'安庆',12)",
				"insert into city(code,name,p_code) values(5,'南京',13)",
				"insert into city(code,name,p_code) values(25,'苏州',13)",
				"insert into city(code,name,p_code) values(26,'无锡',13)",
				"insert into city(code,name,p_code) values(44,'南通',13)",
				"insert into city(code,name,p_code) values(45,'常州',13)",
				"insert into city(code,name,p_code) values(46,'徐州',13)",
				"insert into city(code,name,p_code) values(75,'台州',13)",
				"insert into city(code,name,p_code) values(76,'镇江',13)",
				"insert into city(code,name,p_code) values(77,'盐城',13)",
				"insert into city(code,name,p_code) values(78,'扬州',13)",
				"insert into city(code,name,p_code) values(126,'连云港',13)",
				"insert into city(code,name,p_code) values(127,'淮安',13)",
				"insert into city(code,name,p_code) values(6,'武汉',14)",
				"insert into city(code,name,p_code) values(89,'襄樊',14)",
				"insert into city(code,name,p_code) values(130,'荆州',14)",
				"insert into city(code,name,p_code) values(7,'沈阳',15)",
				"insert into city(code,name,p_code) values(13,'大连',15)",
				"insert into city(code,name,p_code) values(53,'鞍山',15)",
				"insert into city(code,name,p_code) values(99,'本溪',15)",
				"insert into city(code,name,p_code) values(100,'抚顺',15)",
				"insert into city(code,name,p_code) values(101,'丹东',15)",
				"insert into city(code,name,p_code) values(102,'辽阳',15)",
				"insert into city(code,name,p_code) values(103,'锦州',15)",
				"insert into city(code,name,p_code) values(104,'营口',15)",
				"insert into city(code,name,p_code) values(8,'西安',16)",
				"insert into city(code,name,p_code) values(87,'宝鸡',16)",
				"insert into city(code,name,p_code) values(88,'宜昌',16)",
				"insert into city(code,name,p_code) values(109,'榆林',16)",
				"insert into city(code,name,p_code) values(110,'延安',16)",
				"insert into city(code,name,p_code) values(11,'杭州',17)",
				"insert into city(code,name,p_code) values(14,'宁波',17)",
				"insert into city(code,name,p_code) values(49,'绍兴',17)",
				"insert into city(code,name,p_code) values(50,'温州',17)",
				"insert into city(code,name,p_code) values(51,'台州',17)",
				"insert into city(code,name,p_code) values(72,'湖州',17)",
				"insert into city(code,name,p_code) values(73,'嘉兴',17)",
				"insert into city(code,name,p_code) values(74,'金华',17)",
				"insert into city(code,name,p_code) values(128,'丽水',17)",
				"insert into city(code,name,p_code) values(129,'衢州',17)",
				"insert into city(code,name,p_code) values(12,'青岛',18)",
				"insert into city(code,name,p_code) values(15,'济南',18)",
				"insert into city(code,name,p_code) values(37,'烟台',18)",
				"insert into city(code,name,p_code) values(47,'潍坊',18)",
				"insert into city(code,name,p_code) values(48,'淄博',18)",
				"insert into city(code,name,p_code) values(65,'东营',18)",
				"insert into city(code,name,p_code) values(66,'威海',18)",
				"insert into city(code,name,p_code) values(67,'济宁',18)",
				"insert into city(code,name,p_code) values(68,'临沂',18)",
				"insert into city(code,name,p_code) values(69,'德州',18)",
				"insert into city(code,name,p_code) values(70,'滨州',18)",
				"insert into city(code,name,p_code) values(71,'泰安',18)",
				"insert into city(code,name,p_code) values(120,'日照',18)",
				"insert into city(code,name,p_code) values(121,'聊城',18)",
				"insert into city(code,name,p_code) values(122,'枣庄',18)",
				"insert into city(code,name,p_code) values(16,'哈尔滨',19)",
				"insert into city(code,name,p_code) values(52,'大庆',19)",
				"insert into city(code,name,p_code) values(97,'齐齐哈尔',19)",
				"insert into city(code,name,p_code) values(98,'牡丹江',19)",
				"insert into city(code,name,p_code) values(17,'长春',20)",
				"insert into city(code,name,p_code) values(57,'吉林',20)",
				"insert into city(code,name,p_code) values(19,'郑州',21)",
				"insert into city(code,name,p_code) values(43,'洛阳',21)",
				"insert into city(code,name,p_code) values(90,'开封',21)",
				"insert into city(code,name,p_code) values(91,'许昌',21)",
				"insert into city(code,name,p_code) values(92,'平顶山',21)",
				"insert into city(code,name,p_code) values(115,'南阳',21)",
				"insert into city(code,name,p_code) values(116,'濮阳',21)",
				"insert into city(code,name,p_code) values(117,'安阳',21)",
				"insert into city(code,name,p_code) values(118,'焦作',21)",
				"insert into city(code,name,p_code) values(119,'新乡',21)",
				"insert into city(code,name,p_code) values(22,'乌鲁木齐',22)",
				"insert into city(code,name,p_code) values(112,'克拉玛依',22)",
				"insert into city(code,name,p_code) values(113,'喀什',22)",
				"insert into city(code,name,p_code) values(114,'石河子',22)",
				"insert into city(code,name,p_code) values(24,'兰州',23)",
				"insert into city(code,name,p_code) values(111,'天水',23)",
				"insert into city(code,name,p_code) values(31,'太原',24)",
				"insert into city(code,name,p_code) values(108,'大同',24)",
				"insert into city(code,name,p_code) values(32,'石家庄',25)",
				"insert into city(code,name,p_code) values(36,'唐山',25)",
				"insert into city(code,name,p_code) values(60,'保定',25)",
				"insert into city(code,name,p_code) values(61,'邯郸',25)",
				"insert into city(code,name,p_code) values(62,'秦皇岛',25)",
				"insert into city(code,name,p_code) values(63,'沧州',25)",
				"insert into city(code,name,p_code) values(105,'承德',25)",
				"insert into city(code,name,p_code) values(106,'廊坊',25)",
				"insert into city(code,name,p_code) values(107,'邢台',25)",
				"insert into city(code,name,p_code) values(33,'呼和浩特',26)",
				"insert into city(code,name,p_code) values(39,'包头',26)",
				"insert into city(code,name,p_code) values(64,'鄂尔多斯',26)",
				"insert into city(code,name,p_code) values(40,'银川',27)",
				"insert into city(code,name,p_code) values(41,'西宁',28)",
				"insert into city(code,name,p_code) values(42,'海口',29)",
				"insert into city(code,name,p_code) values(59,'拉萨',30)",
				};
			for(String sql:citySql)
			db.execSQL(sql);
			db.execSQL("insert into station(name,addr) values('深圳总站',3)");
			db.execSQL("insert into station(name,addr) values('深圳南山区蛇口站',3)");
			db.execSQL("insert into station(name,addr) values('深圳罗湖站',3)");
			db.execSQL("insert into station(name,addr) values('广州站',2)");
			
			db.execSQL("insert into user(loginname,password,username,type,stationid) values('admin','admin','超级管理员',0,1)");
			db.execSQL("insert into user(loginname,password,username,type,stationid) values('shekou','shekou','蛇口站管理员',0,2)");
			db.execSQL("insert into user(loginname,password,username,type,stationid) values('luohu','luohu','罗湖站管理员',0,3)");
			db.execSQL("insert into user(loginname,password,username,type,stationid) values('guangzhou','guangzhou','广州站管理员',0,4)");
			db.execSQL("insert into user(loginname,password,username,type,stationid) values('gzps','gzps','广州站派件员1',1,4)");
			
			
			
			db.execSQL("insert into goodstype(code,name) values(1,'手机')");
			db.execSQL("insert into goodstype(code,name) values(2,'书')");
			
			db.execSQL("insert into goodsinfo(typeid,name) values(1,'小米1s')");
			db.execSQL("insert into goodsinfo(typeid,name) values(1,'小米2')");
			db.execSQL("insert into goodsinfo(typeid,name) values(1,'小米1')");
			db.execSQL("insert into goodsinfo(typeid,name) values(2,'《Android疯狂讲义》')");
			db.execSQL("insert into goodsinfo(typeid,name) values(2,'《Java疯狂讲义》')");
			
			db.setTransactionSuccessful();//设置事务处理成功，不设置会自动回滚不提交
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}finally{
			db.endTransaction();//处理完成
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("-----------------------------database onUpgrade----------------------");
	}
}

