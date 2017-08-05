package mongodb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import util.DateTime;

public class insertData2Mongodb {
		static Matcher matcher=null;
		
		@SuppressWarnings("resource")
		public static void main(String[] args) {
			DBCollection coll=null;
			//connect to MongoDB
			try{
				Mongo m = new Mongo("localhost" , 27017);  
				DB db = m.getDB("classBigData");  
				coll = db.getCollection("log");
		    }catch(Exception e){  
		        e.printStackTrace();              
		    }
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				String starttime="";
				starttime = DateTime.getCurrentTime();
				Date s_date = new Date();
				long s_long=s_date.getTime();
				
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
                 	        	BasicDBObject DBObject = new BasicDBObject();             				
                 	        	
                 				DBObject.put("time", sdf.parse(s[1]+" "+matcher.group(2)));//时间
                 				DBObject.put("date", s[1]);//日期
                 				DBObject.put("ip", matcher.group(1));//IP
                 				DBObject.put("accesstype", matcher.group(3));//get/post
                 				DBObject.put("accesspath", matcher.group(4));//地址
                 				DBObject.put("protocol", matcher.group(5));//HTTP/1.1
                 				DBObject.put("status1", matcher.group(6));//状态  200
                 				DBObject.put("status2", matcher.group(7));//3737
                 				coll.insert(DBObject);
                 				database_colls++;
                 	        }
                 	        
                 	        if(database_colls==10000000){
                 	        	read.close();
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
                     }else{
                    	 System.out.println("找不到指定的文件");
                     }
                 	}
             }
             String endtime="";
             endtime = DateTime.getCurrentTime();
             System.out.println("插入开始时间："+starttime);
             System.out.println("插入结束时间："+endtime);
             } catch (Exception e) {
 				// TODO: handle exception
            	 e.printStackTrace();
 			}
		}
}
