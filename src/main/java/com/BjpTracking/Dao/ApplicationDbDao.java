package com.BjpTracking.Dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.BjpTracking.model.Assembly;
import com.BjpTracking.model.Block;
import com.BjpTracking.model.BoothAssociation;
import com.BjpTracking.model.BoothInfo;
import com.BjpTracking.model.BoothReport;
import com.BjpTracking.model.District;
import com.BjpTracking.model.DropDownKey;
import com.BjpTracking.model.GmUser;
import com.BjpTracking.model.Habitation;
import com.BjpTracking.model.KeyValueModel;
import com.BjpTracking.model.KeyValueModelList;
import com.BjpTracking.model.LogonHistory;
import com.BjpTracking.model.Parliment;
import com.BjpTracking.model.User;
import com.BjpTracking.model.Village;
import com.BjpTracking.model.Volunteer;
import com.BjpTracking.model.VoluntersInfo;


public interface ApplicationDbDao {
	
	public User login(String user, String password);
	
	public List<User> selectByUser();
	
	public int saveUser(User user);

	public User findByUser(String id);
	 	
	public KeyValueModelList updateState(KeyValueModel stateInfo);
	
	public KeyValueModelList updateDistrict(District district);
	
	public KeyValueModelList updateBlock(Block block);

	public KeyValueModelList updateVillage(Village village);
	
	public KeyValueModelList updateHabitation(Habitation habitation );
	
	public KeyValueModelList getDropDownData(DropDownKey DropDownKey);
	
	public BoothInfo updateBooth(BoothAssociation booth);
	
	public KeyValueModelList asscoiateBoothWithWard(BoothAssociation booth);
	
	public BoothInfo getAssociatedfBooth(int habitationId);
	
	public VoluntersInfo updateValunteer(Volunteer volunteer);
		
	public VoluntersInfo getVolunteers(String boothNo, int userId);
	
	int deleteVolunteers(int volunteer_id);
	
	public List<KeyValueModel> getRoleForVolunteers();
	
	public int volunteerVerified(Volunteer volunteer);
	
	public int changePassword(String userId,String password);
	
	public List<Map<String, Object>> getVolunteersBySDBVH(DropDownKey dropDownKey);
	
	public List<BoothReport> getBoothByDBVH(DropDownKey dropDownKey);
	
	public int updateLogonHistory(LogonHistory history);
	
	int getUserIdByuserName(String userName);
	
	List<LogonHistory> getLastLoginHistory();
	
	public Map<String,Integer> getVolunteerStatistics();

	List<Map<String, Object>> getVolunteerbyConstrainTest(String key, int constrain);

	List<Map<String, Object>> getVolunteersRatio();

	Assembly updateAssembly(Assembly assembly);

	List<KeyValueModel> getUnAssociatedBlocks(int distId);

	Parliment updateParliment(Parliment Parliment);

	List<KeyValueModel> getUnAssociatedBlocksParliment(int distId);


	Assembly getAssemblyInfo(int distId, int assemblyId);

	List<KeyValueModel> getAssemblyByDist(int distId);

	List<KeyValueModel> getParlimentByDist(int distId);


	Parliment getParlimentInfo(int distId, int parlimentId);

	int updateGmUser(GmUser user);
	
}
