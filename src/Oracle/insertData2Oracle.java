package Oracle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import db.DBConnectionCreater;
import util.DateTime;

public class insertData2Oracle {
	public static void main(String[] args) {
		Connection conn=null;
		PreparedStatement pre=null;
		Matcher matcher=null;
		try {
			conn = DBConnectionCreater.ConnectionCreaterForTG("LocalDbUsername", "LocalDbPassword", "LocalDb");
			conn.setAutoCommit(false);
			String starttime="";
			starttime = DateTime.getCurrentTime();
			Date s_date = new Date();
			long s_long=s_date.getTime();
			String sql = "insert into tomcat_log values(to_date(?,'yyyy/mm/dd hh24:mi:ss'),?,?,?,?,?,?)";
			pre=conn.prepareStatement(sql);
             String encoding="GBK";
             File root=new File("logs");
             File[] files = root.listFiles();
             int database_colls=0;
             for(int i=0;i<20;i++){
            	 for(File file:files){
                	 if(file.isFile() && file.exists()&&file.getName().contains("localhost_access_log")){ //判断文件是否存在
                		 String[] s = file.getName().split("\\.");
                         InputStreamReader read = new InputStreamReader(
                         new FileInputStream(file),encoding);//考虑到编码格式
                         BufferedReader bufferedReader = new BufferedReader(read);
                         String lineTxt = null;
                         while((lineTxt = bufferedReader.readLine()) != null){
                 			Pattern p = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\s-\\s-\\s\\[.*2016:(\\d{2}:\\d{2}:\\d{2}).*\\]\\s\\\"(GET|POST)\\s([^?]+)\\??.*\\s(.*)\\\"\\s(\\d*)\\s(\\d*)");
                 			matcher = p.matcher(lineTxt);
                 	        if(matcher.find())
                 	        {
                 	        	if(matcher.group(4).length()>100){
                 	        		break;
                 	        	}
                 		        
                 		        pre.setString(1, s[1]+" "+matcher.group(2));//时间
                 		        pre.setString(2, matcher.group(1));//IP
                 		        pre.setString(3, matcher.group(3));//get/post
                 		        pre.setString(4, matcher.group(4));//地址
                 		        pre.setString(5, matcher.group(5));//HTTP/1.1
                 		        pre.setString(6, matcher.group(6));//状态  200
                 		        pre.setString(7, matcher.group(7));//3737
                 		        pre.execute();
                 		        database_colls++;
                 	        }
                 	       if(database_colls==10000000){
                	        	read.close();
                	        	conn.commit();
                	        	String endtime="";
                	             endtime = DateTime.getCurrentTime();
                	             Date e_date = new Date();
                	             long e_long = e_date.getTime();
                	             System.out.println("耗时:"+String.valueOf(e_long-s_long));
                	             System.out.println(e_date.getTime());
                	             System.out.println("插入开始时间："+starttime);
                	             System.out.println("插入结束时间："+endtime);
                	        	System.exit(0);
                	        }
                         }
                         read.close();
                         conn.commit();
                     }else{
                    	 System.out.println("找不到指定的文件");
                     }
                 }
             }
             String endtime=DateTime.getCurrentTime();
             System.out.println("开始时间："+starttime);
             System.out.println("结束时间："+endtime);
     } catch (Exception e) {
         e.printStackTrace();
         try {
			conn.rollback();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
     }finally {
		DBConnectionCreater.close(conn, pre, null);
	}
	}
}
