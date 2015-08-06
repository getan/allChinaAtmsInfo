import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.lang.Exception;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import net.sf.json.JSONObject;
public class Query {
    
	Map<String,Double> map;
    public Query(){
        map=new HashMap<String, Double>();
    }
	public static void main( String[] args ) throws Exception{
	    Map<String,Double> map = new Query().getLngAndLat("天安门");
	    System.out.println("经度："+map.get("lng")+"---纬度："+map.get("lat"));
        String region = "北京";
        int atmCount = Query.getPoiCount(region, "ATM");
        int bankCount = Query.getPoiCount(region, "银行");
        System.out.println(region + " atmcount = "+ atmCount + " bankCount = " + bankCount);

    }
    public static int getPoiCount(String region, String POI) throws Exception{
        int count = 0;
        String url = "http://api.map.baidu.com/place/v2/search?q="+POI+"&region="+
                      region+"&page_num=0&output=json&ak=fvFz2VvvNxePX1xY4QblStYl";
        String json = null;
        JSONObject obj = null;
        while(true){
            try{
                json = loadJSON(url);
                obj = JSONObject.fromObject(json);

            }catch(net.sf.json.JSONException e){
                continue;
            }
            break;
        }
        if(obj.get("status").toString().equals("0")){
            count = Integer.parseInt(obj.get("total").toString());
        }
        else{
            throw new Exception("未找到匹配地点信息");
        }
        return count;
    }
	public Map<String,Double> getLngAndLat(String address){
		 String url = "http://api.map.baidu.com/geocoder/v2/?address="+address+
                       "&output=json&ak=fvFz2VvvNxePX1xY4QblStYl";
	        String json = loadJSON(url);
	        JSONObject obj = JSONObject.fromObject(json);
	        if(obj.get("status").toString().equals("0")){
	        	double lng=obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
	        	double lat=obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
	        	map.put("lng", lng);
	        	map.put("lat", lat);
	        	//System.out.println("经度："+lng+"---纬度："+lat);
	        }else{
	        	//System.out.println("未找到相匹配的经纬度！");
	        }
		return map;
	}
    	
	 public static String loadJSON (String url) {
	        StringBuilder json = new StringBuilder();
	        try {
	            URL oracle = new URL(url);
	            URLConnection yc = oracle.openConnection();
	            BufferedReader in = new BufferedReader(new InputStreamReader(
	                                        yc.getInputStream()));
	            String inputLine = null;
	            while ( (inputLine = in.readLine()) != null) {
	                json.append(inputLine);
	            }
	            in.close();
	        } catch (MalformedURLException e) {
	        } catch (IOException e) {
	        }
	        return json.toString();
	    }

}
