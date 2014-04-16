package cn.yikuair.arithmetic;

public class LongitudeLatitude {
	private static final double EARTH_RADIUS = 6378.137;//地球半径
	private static double rad(double d)	{
	   return d * Math.PI / 180.0;
	}
	public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lng1) - rad(lng2);
	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * EARTH_RADIUS;
	   s = Math.round(s * 10000) / 10000;
	   return s;
	}
	
	/**  
     * 生成以中心点为中心的四方形经纬度  
     *   
     * @param lat 纬度  
     * @param lon 精度  
     * @param raidus 半径（以米为单位）  
     * @return  
     */    
    public static double[] getAround(double lat, double lon, int raidus) {    
    
        Double latitude = lat;    
        Double longitude = lon;    
    
        Double degree = (24901 * 1609) / 360.0;    //??   查找附近  没有懂？？？
        double raidusMile = raidus;    
    
        Double dpmLat = 1 / degree;    
        Double radiusLat = dpmLat * raidusMile;    
        Double minLat = latitude - radiusLat;    
        Double maxLat = latitude + radiusLat;    
    
        Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));    
        Double dpmLng = 1 / mpdLng;                 
        Double radiusLng = dpmLng * raidusMile;     
        Double minLng = longitude - radiusLng;      
        Double maxLng = longitude + radiusLng;      
        return new double[] { minLat, minLng, maxLat, maxLng };    
    } 
    
    //里面的 radius 就代表搜索 1km 之内，单位km
    public static double [] getRanage(double myLat,double myLng , int radius){
    	double range = 180 / Math.PI * radius / 6372.797; //里面的 1 就代表搜索 1km 之内，单位km
    	double lngR = range / Math.cos(myLat * Math.PI / 180.0);
    	double maxLat = myLat + range;
    	double minLat = myLat - range;
    	double maxLng = myLng + lngR;
    	double minLng = myLng - lngR;
    	return new double[] { minLat, minLng, maxLat, maxLng };
    }
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
