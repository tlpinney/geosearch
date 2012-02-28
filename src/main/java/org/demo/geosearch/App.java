package org.demo.geosearch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.json.simple.JSONObject;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;




public class App 

{
	public static double R = 6378137.0;   // earth radius
	public static int tile_size = 256;
	public static double min_lat = -85.0511287;  
    public static double max_lat = 85.0511287;
    public static double min_lon = -180.0;
    public static double max_lon = 180.0;
	public static double init_res = 2.0 * Math.PI / (double) tile_size;

	
	public static double  resolution(int z) {
	    return init_res / (Math.pow(2, z));
	}
	
	public static void print_res_list( ) {
		for (int i=0; i < 20; i++) {
			print(resolution(i));
		}
	}
	

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		WKTReader reader = new WKTReader( );
		WKBReader breader = new WKBReader( );
		WKBWriter bwriter = new WKBWriter();
		
		
		//print(point);
		//System.out.println(point);
		
		//print_res_list();
		//System.exit(0);
		
		
        File file = new File("/home/ubuntu/buildings.shp");
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource fs = store.getFeatureSource();

        
        //print("Bounds: " + fs.getBounds());
        //print(fs.getFeatures());
        SimpleFeatureCollection sfc = fs.getFeatures();
        
        SimpleFeatureIterator i = sfc.features();
        
        BufferedWriter out = new BufferedWriter(new FileWriter("/home/ubuntu/buildings.tsv"));
        
        
        while( i.hasNext()  ){
            Feature feature = i.next();
            //print(feature.getName());
            //print(feature.getIdentifier());
            //print(feature.getBounds());
            //print(feature.getValue());
            //print(feature.getDefaultGeometryProperty());
            //print(feature.getProperty("the_geom"));
            
            
            BoundingBox bbox = feature.getBounds();
            double minx = bbox.getMinX();
            double miny = bbox.getMinY();
            double maxx = bbox.getMaxX();
            double maxy = bbox.getMaxY();
            
            // find the greatest side  ( need to scale this) 
            double width = maxx - minx;
            double height = maxy - miny;
            
            double size = 0;
            
            if (width > height) {
            	size = width;
            } else {
            	size = height;
            }
            
            // find the zoom level that is appropriate to be converted to quadtiles
            // output other quadtiles if possible 
            // print out to stdin (stub quadtile 0000) for now
            
            JSONObject json = new JSONObject();
            
            
            for (Property p : feature.getProperties()) {
            	//print(p);
            	String name = p.getName().toString();
            	String value = p.getValue().toString();
            	
            	if (name == "the_geom") {
            		Geometry the_geom = reader.read(value);
            		value = WKBWriter.bytesToHex(bwriter.write(the_geom));
            	}
            	//print(name + ": " + value);
            	json.put(name, value);
            	
            }
            
            String quadtile = "0000"; 
            
            //print(quadtile + "\tbuilding\t" + json);
           
            out.write(quadtile + "\tbuilding\t" + json.get("osm_id") + "\t" + json + "\n");
            
           //System.exit(0);
            
            
        }     
        
        out.close();
        
        
        
    }
	
	public static void print(Object arg) {
		System.out.println(arg);
	}
	
}
