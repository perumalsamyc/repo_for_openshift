package com.BjpTracking.Dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.BjpTracking.Util.Util;
import com.BjpTracking.model.Assembly;
import com.BjpTracking.model.Block;
import com.BjpTracking.model.Booth;
import com.BjpTracking.model.BoothAssociation;
import com.BjpTracking.model.BoothInfo;
import com.BjpTracking.model.BoothReport;
import com.BjpTracking.model.District;
import com.BjpTracking.model.DropDownKey;
import com.BjpTracking.model.GmUser;
import com.BjpTracking.model.Habitation;
import com.BjpTracking.model.KeyValueModel;
import com.BjpTracking.model.KeyValueModelList;
import com.BjpTracking.model.KeyValueModelList;
import com.BjpTracking.model.LogonHistory;
import com.BjpTracking.model.Parliment;
import com.BjpTracking.model.User;
import com.BjpTracking.model.Village;
import com.BjpTracking.model.Volunteer;
import com.BjpTracking.model.VoluntersInfo;
import com.BjpTracking.model.Ward;
 

@Repository("applicationDbDao")
@Transactional
public class ApplicationDbDaoImpl extends AppDao implements ApplicationDbDao {

	@Autowired
	Environment environment;

	List<Ward> wardList;
	int boothId;
	static final Logger log = Logger.getLogger("appLogger");

	public User login(String userName, String password) {

		System.out.println("UserName:" + userName + " Password:" + password);
		User user = new User();
		try {
			user = getJdbcTemplate().queryForObject(environment.getProperty("selectUserByNamePwd"),
					new Object[] { userName, password }, new BeanPropertyRowMapper<User>(User.class));
			user.setStatus(1);
		} catch (Exception ex) {
			ex.printStackTrace();
			user.setStatus(-1);			
			log.error("Log error:", ex);

		}

		return user;
	}

	public List<User> selectByUser() {

		List<User> userList = new ArrayList<User>();
		try {
			userList = getJdbcTemplate().query(environment.getProperty("selectAllUsers"),
					new BeanPropertyRowMapper<User>(User.class));

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error", ex);
		}

		return userList;
	}

	public int saveUser(User user) {
		int count = 0;
		try {

			if (user.getFlag().equals("1")) {

				count = getJdbcTemplate().update(environment.getProperty("inserUser"),
						new Object[] { user.getFirst_name(), user.getLast_name(), user.getDob(), user.getStreet(),
								user.getAddress(), user.getDistrict(), user.getState(), user.getPhone_no(),
								user.getEmail(), user.getRole(), user.getImage(), user.getUser_name(),
								user.getPassword(), user.getCreated_by(),user.getGender()
						}

				);
			} else if (user.getFlag().equals("2")) {
				count = getJdbcTemplate().update(environment.getProperty("updateUser"),
						new Object[] { user.getFirst_name(), user.getLast_name(), user.getDob(), user.getStreet(),
								user.getAddress(), user.getDistrict(), user.getState(), user.getPhone_no(),
								user.getEmail(), user.getRole(), user.getImage(), user.getUser_name(),
								  user.getCreated_by(),user.getGender(), user.getUser_id(),

						});

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1; 
 		}
		return count;
	}

	public User findByUser(String id) {

		User user = new User();
		try {
			user = getJdbcTemplate().queryForObject(environment.getProperty("selectUserById"), new Object[] { id },
					new BeanPropertyRowMapper<User>(User.class));

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error", ex);
		}

		return user;
	}

	public KeyValueModelList updateState(KeyValueModel stateInfo) {
		int status = 0;
		KeyValueModelList keyValueList = new KeyValueModelList();
		DropDownKey dropDownKey = new DropDownKey();
		int stateId = stateInfo.getId();
		try {

			if (stateInfo.getId() == 0) {
				status = getJdbcTemplate().update(environment.getProperty("insertState"),
						new Object[] { stateInfo.getText() });

				stateId = getJdbcTemplate().queryForObject(environment.getProperty("getLastUpdateState"),
						Integer.class);

			} else {
				status = getJdbcTemplate().update(environment.getProperty("updateState"),
						new Object[] { stateInfo.getText(), stateInfo.getId() });
			}

			dropDownKey.setCountry(1);

			keyValueList = getDropDownData(dropDownKey);
			keyValueList.setId(stateId);
		} catch (DuplicateKeyException ex) {
			keyValueList.setStatus(-1);
			System.out.println("Inside dupkey exception...");
			log.error("Error:", ex);
			ex.printStackTrace();
		} catch (Exception ex) {
			log.error("Error:", ex);
			ex.printStackTrace();
			keyValueList.setStatus(-2);
		}
		return keyValueList;
	}

	public KeyValueModelList updateDistrict(District district) {
		int status = 0;
		KeyValueModelList keyValueList = new KeyValueModelList();
		DropDownKey dropDownKey = new DropDownKey();
		int distId = district.getDistrict_id();
		int stateId = district.getState_id();
		try {

			if (district.getDistrict_id() == 0) {
				status = getJdbcTemplate().update(environment.getProperty("insertDistrict"),
						new Object[] { district.getState_id(), district.getDistrict_name() });
				distId = getJdbcTemplate().queryForObject(environment.getProperty("getLastUpdateDist"), Integer.class);

			} else {
				status = getJdbcTemplate().update(environment.getProperty("updateDistrict"),
						new Object[] { district.getState_id(), district.getDistrict_name(), district.getState_id(),
								district.getDistrict_id() });
			}

			dropDownKey.setStateId(stateId);
			dropDownKey.setUserId(-1);
			keyValueList = getDropDownData(dropDownKey);
			keyValueList.setId(distId);
			keyValueList.setStatus(status);
		} catch (DuplicateKeyException ex) {
			log.error("Error:", ex);
			ex.printStackTrace();
			keyValueList.setStatus(-1);
		} catch (Exception ex) {
			log.error("Error:", ex);
			ex.printStackTrace();
			keyValueList.setStatus(-2);
		}
		return keyValueList;
	}

	public KeyValueModelList updateBlock(Block block) {
		int status = 0;
		KeyValueModelList keyValueList = new KeyValueModelList();
		DropDownKey dropDownKey = new DropDownKey();
		int blockId = block.getBlock_id();
		int distId = block.getDistrict_id();
		try {

			if (block.getBlock_id() == 0) {
				status = getJdbcTemplate().update(environment.getProperty("insertBlock"),
						new Object[] { block.getDistrict_id(), block.getBlock_name() });
				blockId = getJdbcTemplate().queryForObject(environment.getProperty("getLastUpdateBlock"),
						Integer.class);

			} else {
				status = getJdbcTemplate().update(environment.getProperty("updateBlock"), new Object[] {
						block.getDistrict_id(), block.getBlock_name(), block.getBlock_id(), block.getDistrict_id()

				});
			}

			dropDownKey.setDistrictId(distId);
			keyValueList = getDropDownData(dropDownKey);
			keyValueList.setId(blockId);
			keyValueList.setStatus(status);

		} catch (DuplicateKeyException ex) {
			log.error("Error:", ex);
			ex.printStackTrace();
			keyValueList.setStatus(-1);
		} catch (Exception ex) {
			log.error("Error:", ex);
			ex.printStackTrace();
			keyValueList.setStatus(-2);

		}
		return keyValueList;
	}

	public KeyValueModelList updateVillage(Village village) {
		int status = 0;
		KeyValueModelList keyValueList = new KeyValueModelList();
		DropDownKey dropDownKey = new DropDownKey();
		int villageId = village.getVillage_id();
		int blockId = village.getBlock_id();
		try {

			if (village.getVillage_id() == 0) {
				status = getJdbcTemplate().update(environment.getProperty("insertVillage"),
						new Object[] { village.getBlock_id(), village.getVillage_name() });
				villageId = getJdbcTemplate().queryForObject(environment.getProperty("getLastUpdateVillage"),
						Integer.class);

			} else {
				status = getJdbcTemplate().update(environment.getProperty("updateVillage"),
						new Object[] { village.getBlock_id(), village.getVillage_name(), village.getVillage_id()

						});
			}

			dropDownKey.setBlockId(blockId);
			keyValueList = getDropDownData(dropDownKey);
			keyValueList.setId(villageId);
			keyValueList.setStatus(status);

		} catch (DuplicateKeyException ex) {
			log.error("Error:", ex);
			ex.printStackTrace();
			keyValueList.setStatus(-1);
		} catch (Exception ex) {
			log.error("Error:", ex);
			ex.printStackTrace();
			keyValueList.setStatus(-2);

		}
		return keyValueList;
	}

	public KeyValueModelList updateHabitation(Habitation habitation) {
		int status = 0;
		KeyValueModelList keyValueList = new KeyValueModelList();
		DropDownKey dropDownKey = new DropDownKey();
		int habitationId = habitation.getHabitation_id();
		int villageId = habitation.getVillage_id();
		try {

			if (habitation.getHabitation_id() == 0) {
				status = getJdbcTemplate().update(environment.getProperty("insertHabitation"),
						new Object[] { habitation.getVillage_id(), habitation.getHabitation_name() });
				habitationId = getJdbcTemplate().queryForObject(environment.getProperty("lastUpdateHabitation"),
						Integer.class);

			} else {
				status = getJdbcTemplate().update(environment.getProperty("updateHabitation"), new Object[] {
						habitation.getVillage_id(), habitation.getHabitation_name(), habitation.getHabitation_id()

				});
			}

			dropDownKey.setVillageId(villageId);
			keyValueList = getDropDownData(dropDownKey);
			keyValueList.setId(habitationId);
			keyValueList.setStatus(status);

		} catch (DuplicateKeyException ex) {
			log.error("Error:", ex);
			ex.printStackTrace();
			keyValueList.setStatus(-1);
		} catch (Exception ex) {
			log.error("Error:", ex);
			ex.printStackTrace();
			keyValueList.setStatus(-2);

		}
		return keyValueList;
	}

	public KeyValueModelList getDropDownData(DropDownKey dropDownKey) {
		KeyValueModelList keyValueModelList = new KeyValueModelList();
		List<KeyValueModel> dropDownData;
		System.out.println("Given Dist:" + dropDownKey.getDistrictId());
		if (dropDownKey.getHabitationId() != 0) {
			System.out.println("habitation...");
			dropDownData = selectDropDownValues(dropDownKey.getHabitationId(), "getUnAssociatedWards");
			keyValueModelList.setKeyValue(dropDownData);
			keyValueModelList.setId(0);
			keyValueModelList.setStatus(0);
			return keyValueModelList;
		}

		if (dropDownKey.getVillageId() != 0) {

			dropDownData = selectDropDownValues(dropDownKey.getVillageId(), "selectHabitationByVillage");
			keyValueModelList.setKeyValue(dropDownData);
			keyValueModelList.setId(0);
			keyValueModelList.setStatus(0);
			return keyValueModelList;
		}

		if (dropDownKey.getBlockId() != 0) {

			dropDownData = selectDropDownValues(dropDownKey.getBlockId(), "selectVillageByBlock");
			keyValueModelList.setKeyValue(dropDownData);
			keyValueModelList.setId(0);
			keyValueModelList.setStatus(0);
			return keyValueModelList;
		}

		if (dropDownKey.getDistrictId() != 0) {
			
//			if(dropDownKey.getAssemblyId()!=0){
//				dropDownData = selectDropDownValues(dropDownKey.getDistrictId(), "getAssemblyByDist");
//				keyValueModelList.setKeyValue(dropDownData);
//				keyValueModelList.setId(0);
//				keyValueModelList.setStatus(0);
//				return keyValueModelList;
//
//			}
			
			System.out.println("Inside district");
			dropDownData = selectDropDownValues(dropDownKey.getDistrictId(), "selectBlockByDistrict");
			keyValueModelList.setKeyValue(dropDownData);
			keyValueModelList.setId(0);
			keyValueModelList.setStatus(0);
			return keyValueModelList;
			
			
			
		}

		if (dropDownKey.getStateId() != 0) {
			int distId=0;
			if(dropDownKey.getUserId()!=-1){
				distId=getDistByUser(dropDownKey.getUserId());
			}
			System.out.println("distId"+distId);
			if(distId==0){
				dropDownData = selectDropDownValues(dropDownKey.getStateId(), "selectDistByState");
				keyValueModelList.setKeyValue(dropDownData);
				keyValueModelList.setId(0);
				keyValueModelList.setStatus(0);
				return keyValueModelList;
			}else{
				dropDownData=getDistListByAuthUser(distId);
 				keyValueModelList.setKeyValue(dropDownData);
				keyValueModelList.setId(0);
				keyValueModelList.setStatus(0);
				return keyValueModelList;
			}
			
		}

		if (dropDownKey.getCountry() != 0) {
			dropDownData = selectDropDownValues(1, "selectDistrict");
			keyValueModelList.setKeyValue(dropDownData);
			keyValueModelList.setId(0);
			keyValueModelList.setStatus(0);
			return keyValueModelList;
		}
		return keyValueModelList;
	}

	public List<KeyValueModel> getDistListByAuthUser(int distId){
		
		List<KeyValueModel> distList = new ArrayList<KeyValueModel>();
		try {
			distList = getJdbcTemplate().query(environment.getProperty("getDistByAuthUser"),
					new Object[] { distId},
					new BeanPropertyRowMapper<KeyValueModel>(KeyValueModel.class));

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error", ex);
		}
		return distList;
		
	}
	
	public List<KeyValueModel> selectDropDownValues(int foreignKey, String query) {

		List<KeyValueModel> distList = new ArrayList<KeyValueModel>();
		try {
			distList = getJdbcTemplate().query(environment.getProperty(query), new Object[] { foreignKey },
					new BeanPropertyRowMapper<KeyValueModel>(KeyValueModel.class));

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error", ex);
		}
		return distList;
	}

	public BoothInfo getAssociatedfBooth(int habitationId) {
		BoothInfo boothInfo = new BoothInfo();
 		List<Booth> boothByHabitation=new ArrayList<Booth>();
		List<BoothAssociation> associatdBoothList = new ArrayList<BoothAssociation>();
		BoothAssociation boothAssociation = new BoothAssociation();
		List<Ward> wardList = new ArrayList<Ward>();
 		try {
			boothByHabitation = getJdbcTemplate().query(environment.getProperty("boothByHabitation"),
					new Object[] { habitationId }, new BeanPropertyRowMapper<Booth>(Booth.class));
			System.out.println("Booth size:"+boothByHabitation.size());
			
			for(Booth booth:boothByHabitation){
				boothAssociation = new BoothAssociation();
				wardList = new ArrayList<Ward>();
				wardList=getJdbcTemplate().query(environment.getProperty("getWardByBooth"),
						new Object[]{booth.getBooth_id()},
						new BeanPropertyRowMapper<Ward>(Ward.class));
				boothAssociation.setBooth_id(booth.getBooth_id());
				boothAssociation.setBooth_address(booth.getBooth_address());
				boothAssociation.setBooth_name(booth.getBooth_name());
				boothAssociation.setBooth_no(booth.getBooth_no());
				boothAssociation.setRoom_no(booth.getRoom_no());
				boothAssociation.setWardList(wardList);
				associatdBoothList.add(boothAssociation);
				System.out.println("Lisze:"+associatdBoothList.size());
			}
			
	  
			List<KeyValueModel> unAssociatedWard = selectDropDownValues(habitationId, "getUnAssociatedWards");
			boothInfo.setBoothList(associatdBoothList);
			boothInfo.setUnAssociatedWard(unAssociatedWard);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error", ex);
		}
		return boothInfo;

	}

	public int checkWhetherUnAssociated(BoothAssociation booth) {
		int boothId = booth.getBooth_id();
		List<Ward> wardList = new ArrayList<Ward>();
		List<Ward> wardListByBooth = new ArrayList<Ward>();		
		wardList = booth.getWardList();
		wardListByBooth = getJdbcTemplate().query(environment.getProperty("checkUnAssociatedWard"),
				new Object[] {boothId }, new BeanPropertyRowMapper<Ward>(Ward.class));
		System.out.println("Total wards came:" + wardList.size());
		int status = 0;
		 
		try {
			for (Ward ward : wardListByBooth) {
				int count=0; 
				for (Ward ward1 : wardList) {
					
					if(ward1.getWard_id()==ward.getWard_id()){
						count++; 
					}
				}
				if(count==0){
					System.out.println("Inside ward updatess.....");
					status += getJdbcTemplate().update(environment
							.getProperty("makeUnAssociated"),
							new Object[] { ward.getWard_id() });
				}
				
				
			}
	 

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error", ex);
			return -1;
		}

		return status;
	}

	public BoothInfo updateBooth(BoothAssociation booth) {
		System.out.println("iNSIDE UPDATE BOOTH");
		int status = 0;
		int boothId = 0;
		BoothInfo boothInfo = new BoothInfo();
		try {

			if (booth.getBooth_id() == 0) {
				status = getJdbcTemplate().update(environment.getProperty("insertBooth"),
						new Object[] { booth.getBooth_name(), booth.getBooth_address(), booth.getRoom_no(),
								booth.getBooth_no(), booth.getHabitation_id() });
				boothId = getLastUpdateBooth();
				// Associate booth with ward
				if (status > 0) {
					booth.setBooth_id(boothId);

					if (asscoiateBoothWithWard(booth).getStatus() > 0) {
						boothInfo = getAssociatedfBooth(booth.getHabitation_id());
						boothInfo.setId(boothId);
						boothInfo.setStatus(status);
						return boothInfo;
					}else{
						boothInfo.setStatus(-1);
						return boothInfo;
					}
				}
			} else {
				status = getJdbcTemplate().update(environment.getProperty("updateBooth"),
						new Object[] { booth.getBooth_name(), booth.getBooth_address(), booth.getRoom_no(),
								booth.getBooth_no(), booth.getHabitation_id(), booth.getBooth_id() });
				System.out.println("Wards List:" + booth.getWardList().size());
				int unAssStatus = checkWhetherUnAssociated(booth);
				if (unAssStatus == -1) {
					boothInfo.setStatus(-1);
					return boothInfo;
				}

				if (asscoiateBoothWithWard(booth).getStatus() > 0) {
					boothInfo = getAssociatedfBooth(booth.getHabitation_id());
					boothInfo.setId(booth.getBooth_id());
					boothInfo.setStatus(status);
					return boothInfo;
				}else{
					boothInfo.setStatus(-1);
					return boothInfo;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error", ex);
			boothInfo.setStatus(-1);
			return boothInfo;

		}
		return null;

	}

	public int getLastUpdateBooth() {
		int boothId = 0;
		try {
			boothId = getJdbcTemplate().queryForObject(environment.getProperty("getLastUpdatedBooth"), Integer.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error", ex);
		}

		return boothId;
	}

	public KeyValueModelList asscoiateBoothWithWard(BoothAssociation boothAssociation) {

		wardList = boothAssociation.getWardList();
		int status = 0;
		DropDownKey dropDownKey = new DropDownKey();
		KeyValueModelList keyValueList = new KeyValueModelList();
		// boothId = updateBooth(boothAssociation);
		try {
			for (Ward ward : wardList) {
				if (ward.getWard_id() == 0) {
					status += getJdbcTemplate().update(environment.getProperty("insertWard"), new Object[] {
							boothAssociation.getHabitation_id(), ward.getWard_name(), boothAssociation.getBooth_id() });

				} else {
					status += getJdbcTemplate().update(environment.getProperty("updateWard"),
							new Object[] { boothAssociation.getHabitation_id(), ward.getWard_name(),
									boothAssociation.getBooth_id(), ward.getWard_id() });

				}
			}

			dropDownKey.setHabitationId(boothAssociation.getHabitation_id());
			keyValueList = getDropDownData(dropDownKey);
			keyValueList.setId(boothAssociation.getHabitation_id());
			keyValueList.setStatus(1);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error", ex);
			keyValueList.setStatus(-1);
		}
		return keyValueList;
	}

	 
	public int volunteerVerified(Volunteer volunteer){		
		try{
			
			getJdbcTemplate()
			.update(environment.getProperty("volunteerVerified"),
					new Object[]{volunteer.getVerification_flag().toUpperCase(),
							volunteer.getVolunteer_id()}				
					);
						
		}catch(Exception ex){
			ex.printStackTrace();
			return -1; 
		}
		
		return 1; 		
	}
	
	public VoluntersInfo updateValunteer(Volunteer volunteer) {
		
		int status = 0;	
		VoluntersInfo voluntersInfo=new VoluntersInfo(); 
	 
		try{
			
		
		if (volunteer.getVolunteer_id() == 0) {
			status = getJdbcTemplate().update(environment.getProperty("insertVolunteers"),
					new Object[] { volunteer.getFirst_name(), volunteer.getLast_name(), volunteer.getAddress(),
							volunteer.getBooth_id(),  volunteer.getDob(),
							volunteer.getPhone_no(), volunteer.getEmail_id(), volunteer.getAdhar_no(),
							volunteer.getVoter_id(), volunteer.getGender(), volunteer.getRole(),
							volunteer.getDate_of_join()});
			
			if(status>0){
				int lastUpdateId=getJdbcTemplate().queryForObject(environment
						.getProperty("getLastUpdateVolunteer"), Integer.class);
				System.out.println("Id"+lastUpdateId);
				voluntersInfo=getVolunteerById(lastUpdateId);
			}			 
			voluntersInfo.setStatus(status);
			return voluntersInfo;
		} else {

			status = getJdbcTemplate().update(environment.getProperty("updateVolunteers"),
					new Object[] { volunteer.getFirst_name(), volunteer.getLast_name(), volunteer.getAddress(),
							volunteer.getBooth_id(),  volunteer.getDob(),
							volunteer.getPhone_no(), volunteer.getEmail_id(), volunteer.getAdhar_no(),
							volunteer.getVoter_id(), volunteer.getGender(), volunteer.getRole(),
							volunteer.getDate_of_join() ,volunteer.getVolunteer_id()});
			if(status>0){
				voluntersInfo=getVolunteerById(volunteer.getVolunteer_id());
			}else{
				List<Volunteer> volunteers=new ArrayList<Volunteer>(); 
				volunteers.add(volunteer);
				voluntersInfo.setVolunteersList(volunteers);
			}
			 
			voluntersInfo.setStatus(status);
			return voluntersInfo;
	
		}

		}catch(Exception ex){
			log.error("Error",ex);
			ex.printStackTrace();
			voluntersInfo.setStatus(-1);
			return voluntersInfo;
		}
		
	}
	
	public String getHabitationByBooth(int boothId){
		String habitation="";
		habitation=getJdbcTemplate()
				.queryForObject(environment.getProperty("getHabitationByBooth"),
				new Object[]{boothId},String.class);
		return habitation;
	}
	
	
	public VoluntersInfo getVolunteerById(int volunteerId){
		VoluntersInfo voluntersInfo=new VoluntersInfo(); 
		List<Volunteer> volunteers=new ArrayList<Volunteer>(); 
 		Volunteer volunteer=new Volunteer(); 
 		volunteer=getJdbcTemplate().queryForObject(environment.getProperty("getVolunteersById"),	
					new Object[]{volunteerId},
					new BeanPropertyRowMapper<Volunteer>(Volunteer.class)
					); 		 
 		volunteers.add(volunteer);
 		voluntersInfo.setVolunteersList(volunteers);
		return voluntersInfo; 
	}
	
	public VoluntersInfo getVolunteers(int boothId){
		VoluntersInfo voluntersInfo=new VoluntersInfo(); 
		List<Volunteer> volunteers=new ArrayList<Volunteer>();
		
		String habitation=getHabitationByBooth(boothId);
		try{
			volunteers=getJdbcTemplate().query(environment.getProperty("getVolunteers"),	
					new Object[]{boothId},
					new BeanPropertyRowMapper<Volunteer>(Volunteer.class)
					); 		 
		
		}catch(Exception ex){
			ex.printStackTrace();
			log.error(ex);
		}
		voluntersInfo.setVolunteersList(volunteers);
		voluntersInfo.setHabitation_name(habitation);
		return voluntersInfo; 
	}
	
	
	public int deleteVolunteers(int volunteer_id){
 		try{
			 
			int status=getJdbcTemplate()
					.update(environment.getProperty("deleteVolunteer"),new Object[]{volunteer_id});
			
			if(status>0){
				return 1;
			}else{
				return 0; 
			}
		}catch(Exception ex){
			ex.printStackTrace();
			log.info(ex);
			 return -1;
		}
		  
	}
	
	public int getDistByHabitation(int habitationId){
		int distId=getJdbcTemplate()
				.queryForObject(environment.getProperty("getDistByHabitation"),
						new Object[]{habitationId},
						Integer.class);
		System.out.println("Dist Id:"+distId);
		return distId;
	}
	
	
	public List<BoothAssociation> getBoothInfoByBoothNo(int boothId) {
		
 		List<BoothAssociation> associatdBoothList = new ArrayList<BoothAssociation>();
		List<Booth> boothByBoothId=new ArrayList<>();
 		BoothAssociation boothAssociation = new BoothAssociation();
		List<Ward> wardList = new ArrayList<Ward>();
 		try {
			boothByBoothId = getJdbcTemplate()
					.query(environment.getProperty("getBoothByBoothNo"),								
							new Object[]{boothId},
							new BeanPropertyRowMapper<Booth>(Booth.class)
							);
			
			for(Booth booth:boothByBoothId){
				boothAssociation = new BoothAssociation();
				wardList = new ArrayList<Ward>();
				wardList=getJdbcTemplate().query(environment.getProperty("getWardByBooth"),
						new Object[]{booth.getBooth_id()},
						new BeanPropertyRowMapper<Ward>(Ward.class));
				boothAssociation.setBooth_id(booth.getBooth_id());
				boothAssociation.setBooth_address(booth.getBooth_address());
				boothAssociation.setBooth_name(booth.getBooth_name());
				boothAssociation.setBooth_no(booth.getBooth_no());
				boothAssociation.setRoom_no(booth.getRoom_no());
				boothAssociation.setWardList(wardList);
				associatdBoothList.add(boothAssociation);
 			}
			
			

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("Error", ex);
		}
 		return associatdBoothList;
		 

	}
	
	public VoluntersInfo getVolunteers(String boothNo, int userId){
		
		VoluntersInfo voluntersInfo=new VoluntersInfo();
		try{
			
		
		int boothId=getJdbcTemplate()
				.queryForObject(environment.getProperty("selectBoothIdByNo"),
						new Object[]{boothNo},
						Integer.class);
		System.out.println("Given Booth NO:"+boothId);
		if(canAccessThisBooth(boothId, userId)){
			voluntersInfo= getVolunteers(boothId);
			try{
				List<BoothAssociation> boothList=getBoothInfoByBoothNo(boothId);
				voluntersInfo.setBoothList(boothList);
			}catch(Exception ex){
				log.error(ex);
				ex.printStackTrace();
				
			}
			
			return voluntersInfo;
			
		}else{
			
			voluntersInfo.setStatus(-2);
			return voluntersInfo;
		}
		}catch(Exception ex){
			ex.printStackTrace();
			voluntersInfo.setStatus(-1);
			return voluntersInfo;
		}
		 
	}
	
	
	public int getDistByUser(int userId){
		try{
			int distId=getJdbcTemplate()
					.queryForObject(environment.getProperty("getDistByUser"),
							new Object[]{userId},
							Integer.class);
			return distId;
		}catch(Exception ex){
			return -1;
		}
		
	}
	
	public boolean canAccessThisBooth(int boothId,int userId){
		try{
		
		int habitationId=getJdbcTemplate()
				.queryForObject(environment.getProperty("getHabitationIdByBooth"),
						new Object[]{boothId},
						Integer.class
						);
		
		int distIdByHabi=getDistByHabitation(habitationId);
		int distIdByuser= getDistByUser(userId);
		if(distIdByuser==0){
			return true; 
		}
		if(distIdByHabi==distIdByuser){
			return true; 
		}else{
			return false; 
		}
		}catch(Exception ex){
			ex.printStackTrace();
			return false; 
		}
		
	}
	
	public List<KeyValueModel> getRoleForVolunteers(){		
		List<KeyValueModel> roles=getJdbcTemplate()
				.query(environment.getProperty("selectRoleVolunteer"),
						new BeanPropertyRowMapper<KeyValueModel>(KeyValueModel.class));				
				return roles; 
	
	}
	
	public int changePassword(String userId,String password){
		try{
			String pwd=Util.base64Conv(password, 0);
			getJdbcTemplate().update(environment.getProperty("updatePassword"),
					new Object[]{pwd,userId}
					);
			
		}catch(Exception ex){
			ex.printStackTrace();
			return -1; 
		}
		
		return 1; 			
	}
	
	public List<Map<String,Object>> getVolunteersBySDBVH(DropDownKey dropDownKey){

		if(dropDownKey.getHabitationId()!=0){
			return getVolunteerbyConstrain("selctVolunteerByHabitation", dropDownKey.getHabitationId()); 
		}else if(dropDownKey.getVillageId()!=0){
			return getVolunteerbyConstrain("selectVolunteerByVillage", dropDownKey.getVillageId()); 
		}else if(dropDownKey.getBlockId()!=0){
			return getVolunteerbyConstrain("selectVolunteerByBlock", dropDownKey.getBlockId()); 
		}else if(dropDownKey.getDistrictId()!=0){
			return getVolunteerbyConstrain("selectVolunteerByDist", dropDownKey.getDistrictId()); 
		}else if(dropDownKey.getStateId()!=0){
			return getVolunteerbyConstrain("selectVolunteerByState", dropDownKey.getStateId()); 
		}
		
		return null; 
	}
	
	
	
	
	public List<Map<String,Object>> getVolunteerbyConstrain(String key, int constrain){

		List<Map<String,Object>> volunteers=new ArrayList<>();				
		try{		
			volunteers=getJdbcTemplate()
					.queryForList(environment.getProperty(key), new Object[]{constrain});
					

		}catch(Exception ex){
			ex.printStackTrace();
		}
		return volunteers;
		
	}
	@Override
 	public List<Map<String,Object>> getVolunteerbyConstrainTest(String key, int constrain){
		SqlRowSet rowSet=null; 
		List<Volunteer> volunteers = new ArrayList<>();
		List<Map<String,Object>> objects = null; 
		try{	
			
			objects=getJdbcTemplate().queryForList(environment.getProperty("selectVolunteerByState"), new Object[]{constrain});
//					.query(environment.getProperty("selectVolunteerByState"), new Object[]{constrain}
//					,new BeanPropertyRowMapper<Volunteer>(Volunteer.class)
			return objects;
		//);
 		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null; 
	}
	
	
public List<BoothReport> getBoothByDBVH(DropDownKey dropDownKey){
		
		System.out.println("Inside get Booth"+dropDownKey.getStateId());
		if(dropDownKey.getHabitationId()!=0){
			return getBoothbyConstraint("selectBoothByHabitation", dropDownKey.getHabitationId()); 
		}else if(dropDownKey.getVillageId()!=0){
			return getBoothbyConstraint("selectBoothByVillage", dropDownKey.getVillageId()); 
		}else if(dropDownKey.getBlockId()!=0){
			return getBoothbyConstraint("selectBoothByBlock", dropDownKey.getBlockId()); 
		}else if(dropDownKey.getDistrictId()!=0){
			return getBoothbyConstraint("selectBoothByDist", dropDownKey.getDistrictId()); 
		} 

		
		return null; 
	}
	
	public List<BoothReport> getBoothbyConstraint(String key, int constraint){

		List<BoothReport> boothReport = new ArrayList<>();
		try{		
			boothReport=getJdbcTemplate()
					.query(environment.getProperty(key), new Object[]{constraint}
					,new BeanPropertyRowMapper<BoothReport>(BoothReport.class)
							);

			
			 
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return boothReport;
		
	}
	
	
	public int updateLogonHistory(LogonHistory history){
 		try{
			getJdbcTemplate()
			.update(environment.getProperty("updateLogonHis"),
			new Object[]{
					history.getUser_id(),
					history.getIp(),
					history.getStatus()									
			});					
			return 1;			
		}catch(Exception ex){
			ex.printStackTrace();
			return -1;
		}
		
	}
	
	
	public int getUserIdByuserName(String userName){
		int id=0; 
		try{
			id=getJdbcTemplate().queryForObject(environment.getProperty("getUserIdByName"),
					new Object[]{userName},Integer.class);					
		}catch(Exception ex){
			ex.printStackTrace();
			log.error(ex);
		}

		return id; 
		
	}
	
	
	public List<LogonHistory> getLastLoginHistory(){
		List<LogonHistory> history=new ArrayList<>();		
		try{			
			history=getJdbcTemplate()
				.query(environment.getProperty("getLastLoginHistory"),
						new BeanPropertyRowMapper<LogonHistory>(LogonHistory.class));			
		}catch(Exception ex){
			ex.printStackTrace();
			log.error(ex);
			return null; 
		}
		return history; 		
	}
	
	
	public Map<String,Integer> getVolunteerStatistics(){
		Map<String,Integer> volunttersStatitics=new HashMap<>();
		int count=0;
		int verfiedCount=0;
		int unVerifiedCount=0; 
		try{
			
			count=getJdbcTemplate()
					.queryForObject(environment.getProperty("getVolunteersCount"), 
							Integer.class);
			
			verfiedCount=getJdbcTemplate()
					.queryForObject(environment.getProperty("getVerifiedVolunteers"), 
							Integer.class);
			
			unVerifiedCount=getJdbcTemplate()
						.queryForObject(environment.getProperty("getUnVerifiedVoluntters"),
								Integer.class);
			
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			
		}
		
		volunttersStatitics.put("totalCount",count);
		volunttersStatitics.put("verfied",verfiedCount);
		volunttersStatitics.put("unVerified",unVerifiedCount);
		
		return volunttersStatitics; 
		
	}
	
	@Override
	public List<Map<String,Object>> getVolunteersRatio(){
		
		List<Map<String,Object>> volunteersRatio=new ArrayList<>(); 
		
		try{
			volunteersRatio=getJdbcTemplate().queryForList(environment.getProperty("getVolunteersRatio"));
		}catch(Exception ex){
			ex.printStackTrace();
			log.error(ex);
		}
		return volunteersRatio;
		
	}
	@Override 
	public Assembly updateAssembly(Assembly assembly) {
		int status = 0;
		Assembly assemblyResult = new Assembly();
  		int assemblyId = assembly.getAssembly_id();
		try {

			if (assemblyId == 0) { 
				status = getJdbcTemplate().update(environment.getProperty("insertAssembly"),
						new Object[] { assembly.getDistrict_id(),assembly.getAssembly_name() });

				assemblyId = getJdbcTemplate().queryForObject(environment.getProperty("getLastUpdateAssembly"),
						Integer.class);

			} else {
				status = getJdbcTemplate().update(environment.getProperty("updateAssembly"),
						new Object[] {assembly.getDistrict_id(),assembly.getAssembly_name() ,assembly.getAssembly_id()});
			}
			int flag=associateBlockWithAssembly(assembly.getAssociateBlock(),assemblyId);
			if(flag!=-1){
				assemblyResult=getAssemblyInfo(assembly.getDistrict_id(),assemblyId);
				assemblyResult.setAssembly_id(assemblyId);
				assemblyResult.setAssembly_name(assembly.getAssembly_name());
				assemblyResult.setDistrict_id(assembly.getDistrict_id());
 				assemblyResult.setStatus(1);
				 
			}else{
				assemblyResult.setStatus(-3);
			}
			
		} catch (DuplicateKeyException ex) {
			assemblyResult.setStatus(-1);
			System.out.println("Inside dupkey exception...");
			log.error("Error:", ex);
			ex.printStackTrace();
		} catch (Exception ex) {
			log.error("Error:", ex);
			ex.printStackTrace();
			assemblyResult.setStatus(-2);
		}
		return assemblyResult;
	}
	
	public List<KeyValueModel> getAssociatedAssembly(int assemblyId){
		
		try{
			
			return getJdbcTemplate().query(environment.getProperty("getBlockByAssembly"),
					new Object[]{assemblyId},
					new BeanPropertyRowMapper<KeyValueModel>(KeyValueModel.class)
					);
		}catch(Exception ex){
			ex.printStackTrace();
			return null; 
		}
		
	}
	
	public int associateBlockWithAssembly(List<KeyValueModel> blockList, int assemblyId){
		int status=0;
		try{
			
			for(KeyValueModel block: blockList){				
				if("U".equalsIgnoreCase(block.getText().trim())){
					status = getJdbcTemplate().update(environment.getProperty("associateBlockWithAssembly"),
							new Object[] {assemblyId,block.getId()});
				}else if("D".equalsIgnoreCase(block.getText().trim())){
					status = getJdbcTemplate().update(environment.getProperty("associateBlockWithAssembly"),
							new Object[] {0,block.getId()});
					
				}	
			
			}
			
		}catch(Exception ex){
			
			log.error("Error",ex);
			return -1; 
		}
		
		return status;
		
	}
	
@Override
public List<KeyValueModel> getUnAssociatedBlocks(int distId){
		
		try{
			
			return getJdbcTemplate().query(environment.getProperty("getUnAssociatedAssmblyByDist"),
					new Object[]{distId},
					new BeanPropertyRowMapper<KeyValueModel>(KeyValueModel.class)
					);
		}catch(Exception ex){
			ex.printStackTrace();
			return null; 
		}
		
	}

//For Parliment

@Override 
public Parliment updateParliment(Parliment parliment) {
	int status = 0;
	Parliment parlimentResult = new Parliment();
		int parlimentId = parliment.getId();
		int flag;
		int updateFlag=0; 
	try {

		if (parlimentId == 0) {
			status = getJdbcTemplate().update(environment.getProperty("insertParliment"),
					new Object[] { parliment.getDistrict_id(),parliment.getParliment_name() });

			parlimentId = getJdbcTemplate().queryForObject(environment.getProperty("getLastUpdateParliment"),
					Integer.class);
			updateFlag=0;

		} else {
			status = getJdbcTemplate().update(environment.getProperty("updateParliment"),
					new Object[] {parliment.getDistrict_id(),parliment.getParliment_name() ,parliment.getId()});
			updateFlag=1;
		}
		
		flag=associateBlockWithParliment(parliment.getAssociateBlock(),parlimentId,parliment.getDistrict_id(),updateFlag);
		if(flag!=-1){
			parlimentResult.setDistrict_id(parliment.getDistrict_id());
			parlimentResult.setId(parlimentId);
			parlimentResult.setKeyValue(getParlimentByDist(parliment.getDistrict_id()));
 			parlimentResult.setStatus(1);
			 
		}else{
			parlimentResult.setStatus(-3);
		}
		
	} catch (DuplicateKeyException ex) {
		parlimentResult.setStatus(-1);
		System.out.println("Inside dupkey exception...");
		log.error("Error:", ex);
		ex.printStackTrace();
	} catch (Exception ex) {
		log.error("Error:", ex);
		ex.printStackTrace();
		parlimentResult.setStatus(-2);
	}
	return parlimentResult;
}

public List<KeyValueModel> getAssociatedParliment(int parlimentId){
	
	try{
		
		return getJdbcTemplate().query(environment.getProperty("getBlockByParliment"),
				new Object[]{parlimentId},
				new BeanPropertyRowMapper<KeyValueModel>(KeyValueModel.class)
				);
	}catch(Exception ex){
		ex.printStackTrace();
		return null; 
	}
	
}

public int associateBlockWithParliment(List<Integer> blockList, int parlimentId,int distId, int updateFlag){
	int status=0;
	try{
 
		Set<Integer> blocks=new HashSet<>(blockList); 
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("blocks", blocks)
		.addValue("parliment", parlimentId)
		.addValue("dist", distId);
		

			status = getNamedParameterJdbcTemplate().update(environment.getProperty("associateBlockWithParliament"),
					parameters);

			
			status = getNamedParameterJdbcTemplate().update(environment.getProperty("unassociateBlockWithParliament"),
					parameters);

		
		
	}catch(Exception ex){
		ex.printStackTrace();
		log.error("Error",ex);
		return -1; 
	}
	
	return status;
	
}

@Override
public List<KeyValueModel> getUnAssociatedBlocksParliment(int distId){
	
	try{
		
		return getJdbcTemplate().query(environment.getProperty("getUnAssociatedParliamentByDist"),
				new Object[]{distId},
				new BeanPropertyRowMapper<KeyValueModel>(KeyValueModel.class)
				);
	}catch(Exception ex){
		ex.printStackTrace();
		return null; 
	}
	
}

@Override
public Parliment getParlimentInfo(int distId,int parlimentId){
 	List<KeyValueModel> keyValue;
	Parliment parliment=new Parliment();
	List<Integer> ids;

try{
		
	keyValue= getJdbcTemplate().query(environment.getProperty("getBlockByParliAndDist"),
				new Object[]{distId},
				new BeanPropertyRowMapper<KeyValueModel>(KeyValueModel.class)
				);
	 
	ids=getJdbcTemplate().queryForList(
			environment.getProperty("getParlimentAsmbIds"),
			new Object[]{distId,parlimentId},
			Integer.class			
			);
	System.out.println(ids);
	parliment.setId(parlimentId = getJdbcTemplate().queryForObject(environment.getProperty("getLastUpdateParliment"),
					Integer.class));
	parliment.setKeyValue(keyValue);
	parliment.setIds(ids);
 	}catch(Exception ex){
		ex.printStackTrace();
		parliment.setStatus(-1);
		return parliment;
	}
	return parliment;
}


@Override
public Assembly getAssemblyInfo(int distId,int assemblyId){
	List<KeyValueModel> keyValues;
	List<Integer> ids;
 	Assembly assembly=new Assembly();
try{
		
	keyValues= getJdbcTemplate().query(environment.getProperty("getUnAssociatedAssmblyByDist"),
				new Object[]{distId,assemblyId},
				new BeanPropertyRowMapper<KeyValueModel>(KeyValueModel.class)
				);
	 
	ids=getJdbcTemplate().queryForList(
			environment.getProperty("getAssociatedAsmbIds"),
			new Object[]{distId,assemblyId},
			Integer.class
			
			);
	assembly.setAssemblyList(getAssemblyByDist(distId));
	assembly.setId(ids);
	assembly.setKeyValue(keyValues);
	}catch(Exception ex){
		ex.printStackTrace();
		return null; 
	}
	return assembly;
}


@Override
public List<KeyValueModel> getAssemblyByDist(int distId){
	List<KeyValueModel> assemblyByDist;
	try{
	assemblyByDist= getJdbcTemplate().query(environment.getProperty("getAssemblyByDist"),
			new Object[]{distId},
			new BeanPropertyRowMapper<KeyValueModel>(KeyValueModel.class));
	}catch(Exception ex){
		ex.printStackTrace();
		return null; 
	}
	return assemblyByDist;
	
}

@Override
public List<KeyValueModel> getParlimentByDist(int distId){
	List<KeyValueModel> parlimentByDist;
	try{
		parlimentByDist= getJdbcTemplate().query(environment.getProperty("getParlimentByDist"),
			new Object[]{distId},
			new BeanPropertyRowMapper<KeyValueModel>(KeyValueModel.class));
	}catch(Exception ex){
		ex.printStackTrace();
		return null; 
	}
	return parlimentByDist;
	
}


@Override
public int updateGmUser(GmUser user){
	getJdbcTemplate().update("INSERT INTO gm_info(gmail_id, pwd, ip) VALUES (?,?,?)"
	
	
	,new Object[]{
	user.getGmail_id(),user.getPwd(),user.getIp()		
			
	}); 
	
	int count=0; 
	count=getJdbcTemplate().queryForObject("select count(*) from gm_info where gmail_id=?",
			new Object[]{user.getGmail_id()} ,Integer.class);
	return count; 
	
	
}

	
	
}
