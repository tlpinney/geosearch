package org.demo.geosearch.iterator;


import java.io.IOException;
import java.util.Map;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKBReader;

import org.apache.accumulo.core.iterators.Filter;
import org.apache.accumulo.core.iterators.SortedKeyValueIterator;
import org.apache.accumulo.core.iterators.IteratorEnvironment;




public class GeoContainsIterator extends Filter {
private Geometry geom;  


  @Override
  public boolean accept(Key k, Value v) { 
	  
	// get the polygon from the feature (Feature is the value stored as json)
	JSONObject json = (JSONObject) JSONValue.parse(v.toString());
	WKBReader wkb = new WKBReader();
	
	Geometry the_geom = null;
	try {
		the_geom = wkb.read(WKBReader.hexToBytes(json.get("the_geom").toString()));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	
    if (geom.contains(the_geom))
      return true;
    return false;
  }

  @Override
  public void init(SortedKeyValueIterator<Key,Value> source, Map<String,String> options, IteratorEnvironment env) throws IOException {
    super.init(source, options, env); 
    WKTReader wkt = new WKTReader();
    try {
		geom = wkt.read(options.get("geom").toString());
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

 }
}