package org.demo.geosearch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.ZooKeeperInstance;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.security.Authorizations;

public class Loader {

	public static long maxMemory = 1000000L;
	public static long maxLatency = 1000L;
	public static int maxWriteThreads = 10;
	
	
	public static void main(String[] args) throws Exception {
	
		// load from a tsv the rowids (quadtiles) and json (values)
		
		Instance instance = new ZooKeeperInstance("cloud", "127.0.0.1");	
		Connector conn = instance.getConnector("root", "root".getBytes());
		
		BatchWriter w = conn.createBatchWriter("buildings", maxMemory, maxLatency, maxWriteThreads);
		Mutation m;
		//// loop through the tsv file and fill up the accumulo buildings table 
		BufferedReader in = new BufferedReader(new FileReader("/home/ubuntu/buildings.tsv"));
		
		String line;
		while ( (line = in.readLine()) != null) {
			//print(line);
			String [] lline = line.split("\t");
			
			m = new Mutation(lline[0]);
			m.put(lline[1], lline[2], lline[3]);
			w.addMutation(m);
		}
		
		w.close();
		
	}
	
	
	public static void print(Object arg) {
		System.out.println(arg);
	}
	
}
