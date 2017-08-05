package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

import net.sf.json.JSONObject;
import util.DateTime;

public class mongoDbSta {
	private Mongo m=null;
	private DB db=null;
	private DBCollection coll=null;
	
	//连接mongoDb数据库
	public void conn2mongoDb(){
		try{
			m = new Mongo("localhost" , 27017);  
			db = m.getDB("classBigData");  
			coll = db.getCollection("log");
	    }catch(Exception e){  
	        e.printStackTrace();              
	    }
	}
	
	//按天统计每天的访问量
	public JSONObject getEverydayAccess() {
		conn2mongoDb();
		JSONObject jo = new JSONObject();
		//connect to MongoDB
		String s_time=DateTime.getCurrentTime();
		Date s_date=new Date();
		long s_int=s_date.getTime();
		DBObject group = (BasicDBObject) JSON.parse("{$group:{_id:\"$date\",num:{$sum:1}}}");
		DBObject sort = (BasicDBObject) JSON.parse("{$sort:{_id:1}}");
		AggregationOutput output = coll.aggregate(group,sort);
		Iterable<DBObject> result=output.results();
		List<String> date = new ArrayList<String>();
		List<String> num = new ArrayList<String>();
		result.forEach(new Consumer<DBObject>() {
		    @Override
		        public void accept(DBObject t) {
		            // TODO Auto-generated method stub
		                date.add(t.get("_id").toString());
		                num.add(t.get("num").toString());
		        }
		    });	
		String e_time=DateTime.getCurrentTime();
		Date e_date=new Date();
		long e_int=e_date.getTime();
		System.out.println("查询每天的访问次数耗时:"+String.valueOf(e_int-s_int)+"ms");
		System.out.println(date);
		System.out.println(num);
		jo.put("x", date);
		jo.put("y", num);
		return jo;
	}
	public static void main(String[] args) {
		mongoDbSta sta=new mongoDbSta();
		System.out.println(sta.getEverydayAccess());
	}
	
}
