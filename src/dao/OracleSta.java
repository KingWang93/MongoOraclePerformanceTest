package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import db.DBConnectionCreater;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.ParseDataType;

public class OracleSta {
	private Connection conn = null;
	private PreparedStatement pre = null;
	private ResultSet res = null;
	
	//获取有哪些IP访问
	public List<String> getIPs(){
		List<String> list = new ArrayList<String>();
		try {
			conn=DBConnectionCreater.ConnectionCreaterForTG("LocalDbUsername", "LocalDbPassword", "LocalDb");
			String sql = "select distinct ip from TOMCAT_LOG";//查询有哪些IP访问
			pre=conn.prepareStatement(sql);
			res=pre.executeQuery();
			while(res.next()){
				list.add(res.getString("ip"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			DBConnectionCreater.close(conn, pre, res);
		}
		return list;
	}
	
	//按照IP统计历史累计访问量
	public JSONObject getipAccess(){
		JSONObject o=new JSONObject();
		List<String> x = new ArrayList<String>();
		List<Integer> y = new ArrayList<Integer>();
		try {	
			conn=DBConnectionCreater.ConnectionCreaterForTG("LocalDbUsername", "LocalDbPassword", "LocalDb");
			String sql = "select ip,count(ip) num from TOMCAT_LOG t group by ip order by num desc";//查询有哪些IP访问
			pre=conn.prepareStatement(sql);
			res=pre.executeQuery();
			while(res.next()){
				x.add(res.getString("ip"));
				y.add(res.getInt("num"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			DBConnectionCreater.close(conn, pre, res);
		}
		o.put("x", x);
		o.put("y", y);
		return o;
	}
	
	//按访问类型来统计比例，因为数据里只有get和post方法，因此只需要进行这两者的统计
	public Object getAccesstypeRatio(){
		Object o=null;
		try {	
			conn=DBConnectionCreater.ConnectionCreaterForTG("LocalDbUsername", "LocalDbPassword", "LocalDb");
			String sql = "select accesstype,count(accesstype) count from tomcat_log group by accesstype";//查询有哪些IP访问
			pre=conn.prepareStatement(sql);
			res=pre.executeQuery();
			o=new JSONArray();
			int post = 0;
			int get = 0;
			String postratio="";
			String getratio="";
			int total=0;
			while(res.next()){
				total+=res.getInt("count");
				if(res.getString("accesstype").equals("POST")){
					post=res.getInt("count");
				}else{
					get=res.getInt("count");
				}
			}
			postratio=ParseDataType.parseD2s((double)post/total, 1);
			getratio=ParseDataType.parseD2s((double)get/total, 1);
			o = new JSONObject();
			((JSONObject)o).put("post",postratio);
			((JSONObject)o).put("get",getratio);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			DBConnectionCreater.close(conn, pre, res);
		}
		return o;
	}

	//按天统计访问量
	public JSONObject getAccessCount(){
		JSONObject o=new JSONObject();
		List<String> x = new ArrayList<String>();
		List<Integer> y = new ArrayList<Integer>();
		long s_long = new Date().getTime();
		try {
			conn=DBConnectionCreater.ConnectionCreaterForTG("LocalDbUsername", "LocalDbPassword", "LocalDb");
			String sql = "select * from(select to_char(time,'yyyy/mm/dd') daydate,count(time) count from TOMCAT_LOG group by to_char(time,'yyyy/mm/dd'),'yyyy/mm/dd') order by to_date(daydate,'yyyy/mm/dd') asc";//查询有哪些IP访问
			pre=conn.prepareStatement(sql);
			res=pre.executeQuery();
			while(res.next()){
				x.add(res.getString("daydate"));
				y.add(res.getInt("count"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			DBConnectionCreater.close(conn, pre, res);
		}
		long e_long = new Date().getTime();
		System.out.println("耗时："+String.valueOf(e_long-s_long)+"ms");
		o.put("x", x);
		o.put("y", y);
		return o;
	}

	//获取24小时的访问量数据
	public JSONObject get24Access(){
		JSONObject o=new JSONObject();
		List<String> x = new ArrayList<String>();
		List<Integer> y = new ArrayList<Integer>();
		try {
			conn=DBConnectionCreater.ConnectionCreaterForTG("LocalDbUsername", "LocalDbPassword", "LocalDb");
			String sql = "select a.num,nvl(b.count,0) totalnum from (select level num from dual connect by level <=24) a,(select substr(to_char(time,'yyyymmddhh24'),9)+1 num,count(*) count from tomcat_log group by substr(to_char(time,'yyyymmddhh24'),9)+1) b where a.num=b.num(+) order by 1";//查询有哪些IP访问
			pre=conn.prepareStatement(sql);
			res=pre.executeQuery();
			while(res.next()){
				x.add(res.getString("num"));
				y.add(res.getInt("totalnum"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			DBConnectionCreater.close(conn, pre, res);
		}
		o.put("x", x);
		o.put("y", y);
		return o;
	}
	
	public static void main(String[] args) {
		OracleSta query = new OracleSta();
//		System.out.println("查询各个IP访问的次数：");
//		System.out.println(query.getipAccess());
//		System.out.println("查询访问方法的比例：");
//		System.out.println(query.getAccesstypeRatio());
//		System.out.println("查询24小时时间段内的访问次数：");
//		System.out.println(query.get24Access());
		System.out.println("按天统计的访问次数：");
		System.out.println(query.getAccessCount());
	}
}
