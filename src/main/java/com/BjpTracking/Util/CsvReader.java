package com.BjpTracking.Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.BjpTracking.Dao.AppDao;

public class CsvReader  {

	@Autowired
	static DataSource dataSource; 
	
	@Autowired
	static Environment environment;
	
	static JdbcTemplate jdbcTemplate;

	public static JdbcTemplate getJdbcTemplate() {
		 return  new JdbcTemplate(dataSource); 		
	}

	
	public static DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");		
		dataSource.setUrl("jdbc:mysql://localhost:3306/bjp_tracking");
		dataSource.setUsername("root");
		dataSource.setPassword("");
		return dataSource;
	}
	
	
	public static void updateVoluntters(){
		for(int i=0; i<10000; i++){
			try{
			JdbcTemplate jdbcTemplate=new JdbcTemplate(getDataSource());
			System.out.println("Inserting..."+i);
			jdbcTemplate.update(
					"INSERT INTO volunteer(first_name, last_name,"
					+ " address, booth_id,  dob, phone_no,"
					+ " email_id, adhar_no, voter_id,"
					+ " gender, role, date_of_join) VALUES "
					+ "('"+getRandomString()+"','"+getRandomString()+"',"
							+ "'"+getRandomString()+"',52,'21-09-1990',"
									+ "'9082732121','"+getRandomString()+"@mail.com',"
											+ "'"+getRandomString()+"',"
													+ "'"+getRandomString()+"'"
															+ ",'M',5,'21-05-2017')"
					);
			}catch(Exception ex){
				System.out.println("Error while insert:"+i);
				ex.printStackTrace();
			}
		}
	}
	public static String getRandomString() {
		  SecureRandom random = new SecureRandom();
		  return new BigInteger(50, random).toString(32);		
	}
	
 	public static void main(String[] args) {
 		updateVoluntters();
 		
 	}
//		  	
//	        String csvFile = "E:\\Projects\\Habitation2.csv";
//	        String dirty = "E:\\Projects\\dirty.txt";
//	        
//	        BufferedReader br = null;
//	        String line = "";
//	        String cvsSplitBy = ",";
//
//	        JdbcTemplate jdbcTemplate=new JdbcTemplate(getDataSource());
//	        
//	        try {
//	            br = new BufferedReader(new FileReader(csvFile));
//	            while ((line = br.readLine()) != null) {	               
//	                String[] country = line.split(cvsSplitBy);
// 	                if(country.length>3){	              
//	                	try{
//	                		jdbcTemplate.update("INSERT INTO district(state_id, district_name) VALUES (?,?)",
//	                				new Object[]{1,country[0]}
//	                				 
//	                				);
//	                		int distId=jdbcTemplate.queryForObject("SELECT MAX(district_id) FROM district",Integer.class);
//	                		
//	                		try{
//	                			jdbcTemplate.update("INSERT INTO block(district_id, block_name) VALUES (?,?)",
//		                				new Object[]{distId,country[1].toUpperCase()});
//	                			
//	                			int blockId=jdbcTemplate.queryForObject("SELECT MAX(block_id) FROM block",Integer.class);
//	                			
//	                			try{
//	                				jdbcTemplate.update("INSERT INTO village(block_id, village_name) VALUES (?,?)",
//			                				new Object[]{blockId,country[2].toUpperCase()});
//		                			   			int villageId=jdbcTemplate.queryForObject("SELECT MAX(village_id) FROM village",Integer.class);
//	                				
//	                				try{
//	                					jdbcTemplate.update("INSERT INTO habitation(village_id, habitation_name) VALUES (?,?)",
//				                				new Object[]{villageId,country[3].toUpperCase()});			                			   			
//	                					
//	                				}catch(DuplicateKeyException ex){
//	                					
//	                				}
//	                				
//	                			}catch(DuplicateKeyException ex){
//	                					int villageId=jdbcTemplate.queryForObject("SELECT MAX(village_id) FROM village",Integer.class);
//	                				
//	                				try{
//	        	                		System.out.println(line);			                
//
//	                					jdbcTemplate.update("INSERT INTO habitation(village_id, habitation_name) VALUES (?,?)",
//				                				new Object[]{villageId,country[3].toUpperCase()});			                			   			
//	                					
//	                				}catch(DuplicateKeyException ex1){
//	                					
//	                				}
//	                				
//	                			}
//	                				                		
//	                			
//	                		}catch(DuplicateKeyException ex){
//	                			
//	                					int blockId=jdbcTemplate.queryForObject("SELECT MAX(block_id) FROM block",Integer.class);
//	                			
//	                			try{
//	                				jdbcTemplate.update("INSERT INTO village(block_id, village_name) VALUES (?,?)",
//			                				new Object[]{blockId,country[2].toUpperCase()});
//		                			   			int villageId=jdbcTemplate.queryForObject("SELECT MAX(village_id) FROM village",Integer.class);
//	                				
//	                				try{
//	                					jdbcTemplate.update("INSERT INTO habitation(village_id, habitation_name) VALUES (?,?)",
//				                				new Object[]{villageId,country[3].toUpperCase()});			                			   			
//	                					
//	                				}catch(DuplicateKeyException ex1){
//	                					
//	                				}
//	                				
//	                			}catch(DuplicateKeyException ex2){
//	                				try{
//	                					int villageId=jdbcTemplate.queryForObject("SELECT MAX(village_id) FROM village",Integer.class);
//	                					jdbcTemplate.update("INSERT INTO habitation(village_id, habitation_name) VALUES (?,?)",
//				                				new Object[]{villageId,country[3].toUpperCase()});			                			   			
//	                					
//	                				}catch(DuplicateKeyException ex1){
//	                					
//	                				}
//	                			}
//	                			
//	                		}
//	                		
//	                	}catch(DuplicateKeyException ex){
//	                		
//	                		int distId=jdbcTemplate.queryForObject("SELECT MAX(district_id) FROM district",Integer.class);
//	                		
//	                		try{
//	                			jdbcTemplate.update("INSERT INTO block(district_id, block_name) VALUES (?,?)",
//		                				new Object[]{distId,country[1].toUpperCase()});
//	                			
//	                			int blockId=jdbcTemplate.queryForObject("SELECT MAX(block_id) FROM block",Integer.class);
//	                			
//	                			try{
//	                				jdbcTemplate.update("INSERT INTO village(block_id, village_name) VALUES (?,?)",
//			                				new Object[]{blockId,country[2].toUpperCase()});
//		                			   			int villageId=jdbcTemplate.queryForObject("SELECT MAX(village_id) FROM village",Integer.class);
//	                				
//	                				try{
//	                					jdbcTemplate.update("INSERT INTO habitation(village_id, habitation_name) VALUES (?,?)",
//				                				new Object[]{villageId,country[3].toUpperCase()});			                			   			
//	                					
//	                				}catch(DuplicateKeyException ex2){
//	                					
//	                				}
//	                				
//	                			}catch(DuplicateKeyException ex1){
//	                					int villageId=jdbcTemplate.queryForObject("SELECT MAX(village_id) FROM village",Integer.class);
//	                				
//	                				try{
//	                					jdbcTemplate.update("INSERT INTO habitation(village_id, habitation_name) VALUES (?,?)",
//				                				new Object[]{villageId,country[3].toUpperCase()});			                			   			
//	                					
//	                				}catch(DuplicateKeyException ex11){
//	                					
//	                				}
//	                				
//	                			}
//	                				                		
//	                			
//	                		}catch(DuplicateKeyException ex11){
//	                			
//	                					int blockId=jdbcTemplate.queryForObject("SELECT MAX(block_id) FROM block",Integer.class);
//	                			
//	                			try{
//	                				jdbcTemplate.update("INSERT INTO village(block_id, village_name) VALUES (?,?)",
//			                				new Object[]{blockId,country[2].toUpperCase()});
//		                			   			int villageId=jdbcTemplate.queryForObject("SELECT MAX(village_id) FROM village",Integer.class);
//	                				
//	                				try{
//	        	                		System.out.println(line);
//
//	                					jdbcTemplate.update("INSERT INTO habitation(village_id, habitation_name) VALUES (?,?)",
//				                				new Object[]{villageId,country[3].toUpperCase()});			                			   			
//	                					
//	                				}catch(DuplicateKeyException ex1){
//	                					
//	                				}
//	                				
//	                			}catch(DuplicateKeyException ex2){
//	                				try{
//	        	                		System.out.println(line);
//	                					int villageId=jdbcTemplate.queryForObject("SELECT MAX(village_id) FROM village",Integer.class);
//	                					jdbcTemplate.update("INSERT INTO habitation(village_id, habitation_name) VALUES (?,?)",
//				                				new Object[]{villageId,country[3].toUpperCase()});			                			   			
//	                					
//	                				}catch(DuplicateKeyException ex1){
//	                					
//	                				}
//	                			}
//	                			
//	                		}
//	                		
//	                		
//	                	}
//	                	
//	                	
//	                }else{
//	                	PrintStream ps=new PrintStream(new File(dirty));
//	                	ps.println(line);
//	                }
//
//	            }
//
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        } finally {
//	            if (br != null) {
//	                try {
//	                    br.close();
//	                } catch (IOException e) {
//	                    e.printStackTrace();
//	                }
//	            }
//	        }
//
//	    }
	
}
