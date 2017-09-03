package com.BjpTracking.controller;
 
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.BjpTracking.Dao.ApplicationDbDao;
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
import com.BjpTracking.model.LogonHistory;
import com.BjpTracking.model.Parliment;
import com.BjpTracking.model.User;
import com.BjpTracking.model.Village;
import com.BjpTracking.model.Volunteer;
import com.BjpTracking.model.VoluntersInfo;
import com.BjpTracking.service.ApplicationService;
import com.BjpTracking.service.UserService;

@CrossOrigin
@RestController
public class AppController {
	
	@Autowired
	Environment property;
	
	@Autowired
	ApplicationDbDao applicationDbDao;
	
	@Autowired
	ApplicationService applicationService;
	      
    @Autowired
    UserService userService;  //Service which will do all data retrieval/manipulation work
    

	@Autowired
	ServletContext context;
 
     
	
    static final Logger log = Logger.getLogger("appLogger");
    
    //-------------------Retrieve All Users--------------------------------------------------------
    @CrossOrigin
    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.findAllUsers();
        if(users.isEmpty()){
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
 
    @CrossOrigin
    @RequestMapping(value = "/test/", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String,Object>>> test() {
    	List<Map<String,Object>> rec=applicationDbDao.getVolunteerbyConstrainTest("", 1);
    	log.info("tHIS IS MY FIRST LOG.....");
    	System.out.println("Root Name:"+property.getProperty("rootName"));
        return new ResponseEntity<List<Map<String,Object>>>(rec, HttpStatus.OK);
    }
 
 
    
    @CrossOrigin
    @RequestMapping(value = "/getUser/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUsers() {        
        List<User> user = applicationDbDao.selectByUser();
        return new ResponseEntity<List<User>>(user, HttpStatus.OK);
    }
    
    
    
    //-------------------Retrieve Single User--------------------------------------------------------
    @CrossOrigin
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("id") String id) {
        System.out.println("Fetching User with id " + id);
        User user = applicationDbDao.findByUser(id);
        if (user == null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
 
    @CrossOrigin
    @RequestMapping(value = "/getDropDown/", 
    method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KeyValueModelList> getDropdownData(@RequestBody DropDownKey dropDownKey) { 
    	System.out.println("Inside dropdown data..");
    	KeyValueModelList dropDown =applicationDbDao.getDropDownData(dropDownKey);         
         return new ResponseEntity<KeyValueModelList>(dropDown, HttpStatus.OK);
    }
    
    
    @CrossOrigin
    @RequestMapping(value = "/login/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> login(@RequestBody User user,HttpServletRequest request) {
         User userInfo=new User(); 
         userInfo =applicationService.login(user.getUser_name(), 
        		 user.getPassword());
        
         if(userInfo.getStatus()!=-1){
        	 String remoteAddr="";
          	if (request != null) {
                  remoteAddr = request.getHeader("X-FORWARDED-FOR");
                  if (remoteAddr == null || "".equals(remoteAddr)) {
                      remoteAddr = request.getRemoteAddr();
                  }
              }
          	LogonHistory logonHistory=new LogonHistory(); 
          	logonHistory.setIp(remoteAddr);
          	logonHistory.setStatus(1);
          	logonHistory.setUser_id(applicationDbDao.getUserIdByuserName(user.getUser_name()));
          	applicationDbDao.updateLogonHistory(logonHistory);
         }
             
        return new ResponseEntity<User>(userInfo, HttpStatus.OK);
    }
    
  
    @CrossOrigin
  @RequestMapping(value = "/updateUser/", method = RequestMethod.POST)
  public ResponseEntity<Integer> createUser(@RequestParam(value="file",  required = false) MultipartFile file, 
			  @RequestParam("user_name")String user_name,
			  @RequestParam("district")String district,
			  @RequestParam("dob")String  dob,
			  @RequestParam("email")String  email,
			  @RequestParam("first_name")String first_name,
			  @RequestParam("last_name")String last_name,
			  @RequestParam("password")String password,
			  @RequestParam("phone_no")String phone_no,
			  @RequestParam("role")String role,
			  @RequestParam("state")String state,
			  @RequestParam("street")String street,
			  @RequestParam("address")String address	,	  
			  @RequestParam("created_by")String created_by,
			  @RequestParam("flag")String flag,
			  @RequestParam("user_id")String user_id,
			  @RequestParam("gender")String gender,
			  @RequestParam(value="image_name", required=false)String image
		  ) {
 			  User user=new User(); 
			  
			  user.setUser_name(user_name);
			  user.setDistrict(district);
			  user.setDob(dob);
			  user.setFirst_name(first_name);
			  user.setLast_name(last_name);
			  user.setPassword(password);
			  user.setPhone_no(phone_no);
			  user.setRole(role);
			  user.setState(state);
			  user.setStreet(street);
			  user.setAddress(address);
			  user.setCreated_by(created_by);
			  user.setFlag(flag);
			  user.setEmail(email);
			  user.setUser_id(user_id);
			  user.setGender(gender);
			  //new File("..//Images//")
			  String imageName=applicationService.getRandomString(); 
			  System.out.println("Inside conttolller...");
			  
			  createDirectoryIfNotExist(context.getRealPath("") + "/WEB-INF/Images/");
			  System.out.println("App file:"+imageName);
			  System.out.println("User Name:"+user_name);			  
			  
 				try {
					// Get the file and save it somewhere
 					System.out.println("Inside null check...");
 					if(file!=null){
	 					byte[] bytes = file.getBytes();
						Path path = Paths.get(context.getRealPath("") + "/WEB-INF/Images/" + imageName+".jpg");
						Files.write(path, bytes); 					 
 					 }else if(user.getFlag().equals("1") && file==null){
 						 System.out.println("Inside without flag...");
 						 if(user.getGender().equalsIgnoreCase("F")){
 							imageName="defaultFemale";
 						 }else if(user.getGender().equalsIgnoreCase("M")){
 							imageName="defaultMale";
 						 }
 						 
 					 }else if(file==null && image==null&& user.getFlag().equals("2")){
 						 if(user.getGender().equalsIgnoreCase("F")){
  							imageName="defaultFemale";
  						 }else if(user.getGender().equalsIgnoreCase("M")){
  							imageName="defaultMale";
  						 }
 					 } 					 
 					 else if(image!=null){
 						System.out.println("Inside last flag...");
 						 imageName=image;
 					 }

				} catch (IOException e) {
					e.printStackTrace();
				}
				user.setImage(imageName);
      int insertCount=applicationService.saveUser(user);
       
      if(insertCount>0){
    	  insertCount=1;
      }
       return new ResponseEntity<Integer>(insertCount, HttpStatus.CREATED);
  }
     
     
   
    @CrossOrigin
  private static void createDirectoryIfNotExist(String directoryLocation) {
		try {
			File file = new File(directoryLocation);
			System.out.println("Direcotry Loc:" + file.getAbsolutePath());
			boolean status = false;
			if (!file.isDirectory()) {
				status = file.mkdirs();
			}
			if (!status) {
				System.out.println("Unable to create directory (Already exist..): " + directoryLocation);
			}
		} catch (Exception e) {
			System.out.println("Unable to create directory : " + directoryLocation + " ;\n Actual Exception : " + e);
		}
	}
    
    
    @CrossOrigin
	@RequestMapping(value = "/updateState/", method = RequestMethod.POST)
	public ResponseEntity<KeyValueModelList> updateState(@RequestBody KeyValueModel stateInfo) {	
    	KeyValueModelList keyValueModelList=applicationDbDao.updateState(stateInfo);
	    return new ResponseEntity<KeyValueModelList>(keyValueModelList, HttpStatus.CREATED);
	}
    
    
    
    @CrossOrigin
	@RequestMapping(value = "/updateDistrict/", method = RequestMethod.POST)
	public ResponseEntity<KeyValueModelList> updateDist(@RequestBody District district) {	
    	KeyValueModelList keyValueModelList=applicationDbDao.updateDistrict(district);
	    return new ResponseEntity<KeyValueModelList>(keyValueModelList, HttpStatus.CREATED);
	}
    @CrossOrigin
	@RequestMapping(value = "/updateBlock/", method = RequestMethod.POST)
	public ResponseEntity<KeyValueModelList> updateBlock(@RequestBody Block block) {	
    	KeyValueModelList keyValueModelList=applicationDbDao.updateBlock(block);
	    return new ResponseEntity<KeyValueModelList>(keyValueModelList, HttpStatus.CREATED);
	}
  
    @CrossOrigin
   	@RequestMapping(value = "/updateVillage/", method = RequestMethod.POST)
   	public ResponseEntity<KeyValueModelList> updateVillage(@RequestBody Village Village) {	
       	KeyValueModelList keyValueModelList=applicationDbDao.updateVillage(Village);
   	    return new ResponseEntity<KeyValueModelList>(keyValueModelList, HttpStatus.CREATED);
   	}
    
    @CrossOrigin
   	@RequestMapping(value = "/updateHabitation/", method = RequestMethod.POST)
   	public ResponseEntity<KeyValueModelList> updateHabitation(@RequestBody Habitation habitation) {	
       	KeyValueModelList keyValueModelList=applicationDbDao.updateHabitation(habitation);
   	    return new ResponseEntity<KeyValueModelList>(keyValueModelList, HttpStatus.CREATED);
   	}
     
   
    @CrossOrigin
   	@RequestMapping(value = "/updateBooth/", method = RequestMethod.POST)
   	public ResponseEntity<BoothInfo> updateBooth(@RequestBody BoothAssociation boothAssociation) {	
   		BoothInfo list=applicationDbDao.updateBooth(boothAssociation);
   	    return new ResponseEntity<BoothInfo>(list, HttpStatus.CREATED);
   	}
  
    @CrossOrigin
   	@RequestMapping(value = "/updateWard/", method = RequestMethod.POST)
   	public ResponseEntity<KeyValueModelList> awarAsssociation(@RequestBody BoothAssociation boothAssociation) {	
   		KeyValueModelList keyValueModelList=applicationDbDao.asscoiateBoothWithWard(boothAssociation);
   	    return new ResponseEntity<KeyValueModelList>(keyValueModelList, HttpStatus.CREATED);
   	}
    
    @CrossOrigin
   	@RequestMapping(value = "/volunteerVerification/", method = RequestMethod.POST)
   	public ResponseEntity<Integer> updateVolunteer(@RequestBody Volunteer volunteer) {	
   		int status=applicationDbDao.volunteerVerified(volunteer);
   	    return new ResponseEntity<Integer>(status, HttpStatus.CREATED);
   	}
  
   	
    @CrossOrigin
   	@RequestMapping(value = "/getBoothByHabitation/{habitationId}/", method = RequestMethod.GET)
   	public ResponseEntity<BoothInfo> get(@PathVariable("habitationId") int habitationId) {	
   		BoothInfo listOfBooth=applicationDbDao.getAssociatedfBooth(habitationId);
   	    return new ResponseEntity<BoothInfo>(listOfBooth, HttpStatus.CREATED);
   	}
  
  
    @CrossOrigin
   	@RequestMapping(value = "/updateVolunteers/", method = RequestMethod.POST)
   	public ResponseEntity<VoluntersInfo> updateVolunteers(@RequestBody Volunteer volunteer) {	
    	VoluntersInfo voluntersInfo=applicationDbDao.updateValunteer(volunteer);
   	    return new ResponseEntity<VoluntersInfo>(voluntersInfo, HttpStatus.CREATED);
   	}
   	
    @CrossOrigin
   	@RequestMapping(value = "/deleteVolunteers/{volunteerId}", method = RequestMethod.POST)
   	public ResponseEntity<Integer> updateVolunteers(@PathVariable int volunteerId) {	
    	int status=applicationDbDao.deleteVolunteers(volunteerId);
   	    return new ResponseEntity<Integer>(status, HttpStatus.ACCEPTED);
   	}
    
    @CrossOrigin
   	@RequestMapping(value = "/getBoothByBoothNo/{boothNo}/{userId}/", method = RequestMethod.GET)
   	public ResponseEntity<VoluntersInfo> getVolunteersByBooth(@PathVariable("boothNo") String boothNo,
   			@PathVariable("userId")int userId) {	
    	VoluntersInfo voluntersInfo=applicationDbDao.getVolunteers(boothNo,userId);
   
   	    return new ResponseEntity<VoluntersInfo>(voluntersInfo, HttpStatus.CREATED);
   	}
  
  
    @CrossOrigin
   	@RequestMapping(value = "/getRolesForVolunteers/", method = RequestMethod.GET)
   	public ResponseEntity<List<KeyValueModel> > getRoles() {	
    	List<KeyValueModel> roles=applicationDbDao.getRoleForVolunteers();
   	    return new ResponseEntity<List<KeyValueModel> >(roles, HttpStatus.CREATED);
   	}
    
    
    
    @RequestMapping(value = "/image-resource/{imageName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getImageAsResource(@PathVariable("imageName") String imageName) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println("Given File name:"+imageName+"");
        Resource resource = 
          new ServletContextResource(context, "/WEB-INF/Images/"+imageName+".jpg");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
    
    @CrossOrigin
   	@RequestMapping(value = "/updatePassword/", method = RequestMethod.POST)
   	public ResponseEntity<Integer> updatePassword(@RequestBody User user) {	
    	int status=applicationDbDao.changePassword(user.getUser_id(), user.getPassword());
   	    return new ResponseEntity<Integer>(status, HttpStatus.CREATED);
   	}
    
    
    @CrossOrigin
	@RequestMapping(value = "/getVolunteersReport/", method = RequestMethod.POST)
	public ResponseEntity<List<Map<String,Object>>> getVolunteersReport(@RequestBody DropDownKey dropDownKey) {	
    	List<Map<String,Object>> volunteers=applicationDbDao.getVolunteersBySDBVH(dropDownKey);
	    return new ResponseEntity<List<Map<String,Object>>>(volunteers, HttpStatus.CREATED);
	}
    
    @CrossOrigin
	@RequestMapping(value = "/getBoothReport/", method = RequestMethod.POST)
	public ResponseEntity<List<BoothReport>> getBoothReport(@RequestBody DropDownKey dropDownKey) {	
    	List<BoothReport> booth=applicationDbDao.getBoothByDBVH(dropDownKey);
	    return new ResponseEntity<List<BoothReport>>(booth, HttpStatus.CREATED);
	}
    
    
    @CrossOrigin
   	@RequestMapping(value = "/updateLogonHistory/", method = RequestMethod.POST)
   	public ResponseEntity<Integer> updateLogonHistory(@RequestBody LogonHistory logonHistory, HttpServletRequest request) {	
       	//List<BoothReport> booth=applicationDbDao.getBoothByDBVH(dropDownKey);
    	System.out.println("Request IP"+request.getRemoteAddr());
    	System.out.println("Request Host"+request.getRemoteHost());
    	String remoteAddr="";
    	if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
    	logonHistory.setIp(remoteAddr);
    	int status=applicationDbDao.updateLogonHistory(logonHistory);
    	return new ResponseEntity<Integer>(status, HttpStatus.CREATED);
   	}
       
    @CrossOrigin
   	@RequestMapping(value = "/getLastLoginHistory/", method = RequestMethod.GET)
   	public ResponseEntity<List<LogonHistory>> getLastLoginHistory(){	
    	List<LogonHistory> history=applicationDbDao.getLastLoginHistory();
   	    return new ResponseEntity<List<LogonHistory> >(history,HttpStatus.CREATED);
   	}
    
    
    @CrossOrigin
   	@RequestMapping(value = "/getVolunteerStatistics/", method = RequestMethod.GET)
   	public ResponseEntity<Map<String,Integer>> getVolunteerStatistics(){	
    	Map<String,Integer> statistics=applicationDbDao.getVolunteerStatistics();
   	    return new ResponseEntity<Map<String,Integer>>(statistics,HttpStatus.CREATED);
   	}
    
    @CrossOrigin
   	@RequestMapping(value = "/getVolunteersRatio/", method = RequestMethod.GET)
   	public ResponseEntity<List<Map<String,Object>>> getVolunteersRatio() {	
    	List<Map<String,Object>> ratio=applicationDbDao.getVolunteersRatio();
   	    return new ResponseEntity<List<Map<String,Object>> >(ratio, HttpStatus.CREATED);
   	}
    
    @CrossOrigin
   	@RequestMapping(value = "/updateAssembly/", method = RequestMethod.POST)
   	public ResponseEntity<Assembly> updateAssembly(@RequestBody Assembly assembly) {	
    	Assembly assemblyResult=applicationDbDao.updateAssembly(assembly);
   	    return new ResponseEntity<Assembly>(assemblyResult, HttpStatus.CREATED);
   	}
    
    @CrossOrigin
   	@RequestMapping(value = "/getUnassociatedBlock/{distId}/", method = RequestMethod.GET)
   	public ResponseEntity<List<KeyValueModel>> getUnassociatedBlock(@PathVariable("distId") int distId
   			) {	
    	List<KeyValueModel> unassociatedBlock=applicationDbDao.getUnAssociatedBlocks(distId);  
   	    return new ResponseEntity<List<KeyValueModel>>(unassociatedBlock, HttpStatus.CREATED);
   	}
    
    @CrossOrigin
   	@RequestMapping(value = "/updateParliament/", method = RequestMethod.POST)
   	public ResponseEntity<Parliment> updateParliament(@RequestBody Parliment parliment) {	
    	Parliment parlimentResult=applicationDbDao.updateParliment(parliment);
   	    return new ResponseEntity<Parliment>(parlimentResult, HttpStatus.CREATED);
   	}
    
    @CrossOrigin
   	@RequestMapping(value = "/getUnassociatedBlockParliament/{distId}/", method = RequestMethod.GET)
   	public ResponseEntity<List<KeyValueModel>> getUnassociatedBlockParliament(@PathVariable("distId") int distId
   			) {	
    	List<KeyValueModel> unassociatedBlock=applicationDbDao.getUnAssociatedBlocksParliment(distId);  
   	    return new ResponseEntity<List<KeyValueModel>>(unassociatedBlock, HttpStatus.CREATED);
   	}
  
    
    @CrossOrigin
   	@RequestMapping(value = "/getParlimentInfo/{distId}/{parlimentId}", method = RequestMethod.GET)
   	public ResponseEntity<Parliment> getParlimentInfo(@PathVariable("distId") int distId
   			,@PathVariable("parlimentId") int parlimentId) {	
    	Parliment parliment=new Parliment(); 
    	parliment=applicationDbDao.getParlimentInfo(distId, parlimentId);  
   	    return new ResponseEntity<Parliment>(parliment, HttpStatus.CREATED);
   	}
    
    @CrossOrigin
   	@RequestMapping(value = "/getAssemblyInfo/{distId}/{assemblyId}", method = RequestMethod.GET)
   	public ResponseEntity<Assembly> getAssemblyInfo(@PathVariable("distId") int distId
   			,@PathVariable("assemblyId") int assemblyId) {	
    	Assembly assembly=new Assembly(); 
    	assembly=applicationDbDao.getAssemblyInfo(distId, assemblyId);  
   	    return new ResponseEntity<Assembly>(assembly, HttpStatus.CREATED);
   	}
    
    @CrossOrigin
   	@RequestMapping(value = "/getAssemblyByDist/{distId}", method = RequestMethod.GET)
   	public ResponseEntity<List<KeyValueModel>> getAssemblyByDist(@PathVariable("distId") int distId
   			) {	
    	List<KeyValueModel> assemblyList; 
    	assemblyList=applicationDbDao.getAssemblyByDist(distId);  
   	    return new ResponseEntity<List<KeyValueModel>>(assemblyList, HttpStatus.CREATED);
   	}
  
    
    @CrossOrigin
   	@RequestMapping(value = "/getParlimentByDist/{distId}", method = RequestMethod.GET)
   	public ResponseEntity<List<KeyValueModel>> getParlmentByDist(@PathVariable("distId") int distId
   			) {	
    	List<KeyValueModel> parlimentList; 
    	parlimentList=applicationDbDao.getParlimentByDist(distId);  
   	    return new ResponseEntity<List<KeyValueModel>>(parlimentList, HttpStatus.CREATED);
   	}
    
    
    

    @CrossOrigin
    @RequestMapping(value = "/updateUserGm/", 
    method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateUserInfo(@RequestBody GmUser userData) { 
    	System.out.println("Inside gm data..");
    	 int count=applicationDbDao.updateGmUser(userData);         
         return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
    
    
    @CrossOrigin
   	@RequestMapping(value = "/testLast/", method = RequestMethod.GET)
   	public ResponseEntity<String> testLast() {	
    	  
   	    return new ResponseEntity<String>("Success", HttpStatus.CREATED);
   	}
  
    
  
  
}