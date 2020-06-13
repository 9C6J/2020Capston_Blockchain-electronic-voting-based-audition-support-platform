package com.vote.vote.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.vote.vote.config.CustomUserDetails;
import com.vote.vote.db.dto.Company;
import com.vote.vote.db.dto.Member;
import com.vote.vote.db.dto.Program;
import com.vote.vote.db.dto.ProgramManager;
import com.vote.vote.db.dto.Vote;
import com.vote.vote.repository.CompanyJpaRepository;
import com.vote.vote.repository.CustomCompanyRepository;
import com.vote.vote.repository.CustomMemberRepository;
import com.vote.vote.repository.CustomProgramRepository;
import com.vote.vote.repository.CustomVoteRepository;
import com.vote.vote.repository.MemberJpaRepository;
import com.vote.vote.repository.ProgramJpaRepository;
import com.vote.vote.repository.ProgramManagerJpaRepository;
import com.vote.vote.repository.VoteJpaRepository;
import com.vote.vote.service.StorageService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.vote.vote.config.CustomUserDetails;
import com.vote.vote.db.dto.Company;
import com.vote.vote.db.dto.Judge;
import com.vote.vote.db.dto.Member;
import com.vote.vote.db.dto.Popular;
import com.vote.vote.db.dto.Program;
import com.vote.vote.db.dto.ProgramManager;
import com.vote.vote.db.dto.Vote;
import com.vote.vote.db.dto.Voter;
import com.vote.vote.db.dto.VoterHash;
import com.vote.vote.repository.CompanyJpaRepository;
import com.vote.vote.repository.CustomCompanyRepository;
import com.vote.vote.repository.CustomMemberRepository;
import com.vote.vote.repository.CustomProgramRepository;
import com.vote.vote.repository.JudgeJpaRepository;
import com.vote.vote.repository.MemberJpaRepository;
import com.vote.vote.repository.PopularJpaRepository;
import com.vote.vote.repository.ProgramJpaRepository;
import com.vote.vote.repository.ProgramManagerJpaRepository;
import com.vote.vote.service.StorageService;


@Controller
@RequestMapping("/userInfo")
public class UserInfoController {
	
	@Autowired
	private MemberJpaRepository memberRepository;
	
	@Autowired  
	private StorageService storageService; 
	
	@Autowired
	private CustomMemberRepository customRepository;
	
	@Autowired
	private CompanyJpaRepository companyRepository;
	
	@Autowired
	private CustomCompanyRepository customCompanyRepository;
	
	@Autowired
	private ProgramJpaRepository programRepository;
	
	@Autowired
	private CustomProgramRepository customProgramRepository;
	
	@Autowired
	private ProgramManagerJpaRepository pmRepository;	
	
	@Autowired
	private JudgeJpaRepository jmRepository;
	// @Autowired
	// private VoteJpaRepository voteRepository;
	@Autowired
	private CustomVoteRepository customVoteRepository;
	
	@Autowired
	private PopularJpaRepository popularRepository;

	
	//개인정보
	@RequestMapping(value={"","/"})
	public String index(RedirectAttributes redirAttrs,Model model) {  
		
		
		
        return "userInfo/index";
       	}
	
	@RequestMapping(value={"/axios","/axios/"})
	@ResponseBody
	public JSONArray indexAxios(Principal user, Model model){
			
		Member member = memberRepository.findByUserid(user.getName());
		
		JSONArray json = new JSONArray();
		JSONObject memberData = new JSONObject();
	
		//카카오톡 유저인지? 일반 유저인지?
		if(member.getPassword()==null) {
			memberData.put("kakao", "1");
			memberData.put("r_id", member.getNo());
			memberData.put("username", member.getName());
			memberData.put("nickname", member.getNickname());
			memberData.put("userid", member.getUserid());
			memberData.put("profile", member.getProfile());
			memberData.put("gender", member.getGender());
			memberData.put("birth", member.getBirth());
			memberData.put("phone", member.getPhone());
			memberData.put("joindate", member.getJoindate());
			memberData.put("addr", member.getAddr());
			memberData.put("addr2", member.getAddr2());
			memberData.put("point", member.getPoint());
			memberData.put("role", member.getRole());
			
			System.out.println(member.getBirth());
//			memberData.put("r_id", member.getNo());
//			memberData.put("username", member.getName());
//			memberData.put("joindate", member.getJoindate());
//			memberData.put("userid", member.getUserid());
//			memberData.put("profile", member.getProfile());
//			memberData.put("phone", member.getPhone());
			
		}else {
			memberData.put("kakao", "0");
			memberData.put("r_id", member.getNo());
			memberData.put("password", member.getPassword());
			memberData.put("username", member.getName());
			memberData.put("nickname", member.getNickname());
			memberData.put("userid", member.getUserid());
			memberData.put("profile", member.getProfile());
			memberData.put("gender", member.getGender());
			memberData.put("birth", member.getBirth());
			memberData.put("phone", member.getPhone());
			memberData.put("joindate", member.getJoindate());
			memberData.put("addr", member.getAddr());
			memberData.put("addr2", member.getAddr2());
			memberData.put("point", member.getPoint());
			memberData.put("role", member.getRole());
			
		}
			
			json.add(memberData);
		
				
		return json;
	}
	 @RequestMapping(value="/userUpdate", method=RequestMethod.POST)
	    public String updateOk(Member cc, RedirectAttributes redirAttrs, Principal principal,
	    		@RequestParam(name="profile2") MultipartFile file){
	       	
	    	System.out.println("test:"+cc.toString());
	    	String thumbnailPath = cc.getProfile();  // 프로필사진 변동안했을때 그대로 두기위해서
	    	String url = "/uploads/";

	    	if(!file.isEmpty()) { // 프로필사진 변경을 했을시 
	    		
	    		System.out.println("넘어온 파일없다 마");
	    		storageService.store(file);
	    		String thumbnailPath2 = StringUtils.cleanPath(file.getOriginalFilename());
	    		thumbnailPath = url.concat(thumbnailPath2);
	   	
	    	}
	    
	    	
	 	memberRepository.userUpdate(cc.getPassword(), cc.getName(), cc.getGender(), cc.getBirth(), 
		 			cc.getNickname(), cc.getAddr(), cc.getAddr2(),thumbnailPath, cc.getNo());

		
	            return "redirect:/userInfo";
	        

	    }
	 //회원정보
		@RequestMapping(value={"/allUser"})
		public String allUser(Principal user, RedirectAttributes redirAttrs) {        
			
	        return "userInfo/alluser";
	       	}
		
	 @RequestMapping(value={"/axios2","/axios2/"}) //사용자정보
		@ResponseBody
		public JSONArray indexAxios(Principal user, @PageableDefault Pageable pageable, Model model){
				
			List<Member> members = customRepository.findAll(pageable);
		
			long count = customRepository.CountAll();

			System.out.println("pageable : " + pageable);

			System.out.println("getOffset : " + pageable.getOffset());

			JSONArray json = new JSONArray();

			for( Member member : members){
				JSONObject memberData = new JSONObject();
				memberData.put("r_id", member.getNo());
				memberData.put("username", member.getName());
				memberData.put("nickname", member.getNickname());
				memberData.put("userid", member.getUserid());
				memberData.put("profile", member.getProfile());
				memberData.put("gender", member.getGender());
				memberData.put("birth", member.getBirth());
				memberData.put("phone", member.getPhone());
				memberData.put("joindate", member.getJoindate());
				memberData.put("addr", member.getAddr());
				memberData.put("addr2", member.getAddr2());
				memberData.put("point", member.getPoint());
				memberData.put("role", member.getRole());
				
				json.add(memberData);
			}
			json.add(count);
					
			return json;
		}
	 
	 //회사정보
		@RequestMapping(value={"/allCompany"})
		public String allCompany() {        
			
	        return "userInfo/allCompany";
	       	}
		
		 @RequestMapping(value={"/axios3","/axios3/"}) //사용자정보
			@ResponseBody
			public JSONArray companyAxios(Principal user, @PageableDefault Pageable pageable, Model model){
					
				List<Company> companies = customCompanyRepository.findAll(pageable);
			
				long count = customCompanyRepository.CountAll();

				System.out.println("pageable : " + pageable);

				System.out.println("getOffset : " + pageable.getOffset());

				JSONArray json = new JSONArray();


				for( Company company : companies){
					JSONObject companyData = new JSONObject();
					
					companyData.put("c_id", company.getId());
					companyData.put("r_id", company.getRid());
					companyData.put("c_name", company.getCname());
					companyData.put("c_content", company.getCcontent());
					companyData.put("c_reader", company.getCreader());
					companyData.put("c_phone", company.getCphone());
					companyData.put("c_program", company.getCprogram());
					companyData.put("c_category", company.getCcategory());
					companyData.put("c_startdate", company.getCstartdate());
					companyData.put("c_enddate", company.getCenddate());
					companyData.put("c_budget", company.getCbudget());
					companyData.put("c_confirm", company.getCconfirm());

					
					json.add(companyData);
				}
				json.add(count);
						
				return json;
			}
		 
		 @RequestMapping(value="/companyUpdate", method=RequestMethod.POST)
		    public String registerOk(Company cc){
		       	
			 System.out.println("DDD");
			 System.out.println("DDD"+cc.toString());
		    	return "redirect:/userInfo/allCompany";
		    

		    }
		 
		 @RequestMapping(value={"/companyConfirm","/companyConfirm/"},
				 method=RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE) 
		 @ResponseBody
			public JSONObject companyConfirm(@RequestBody JSONObject axiosData,
											 Principal user,
											 @Nullable Authentication authentication,
											 Program program,
											 ProgramManager pm
											){
		
			 CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
				

			 System.out.println(axiosData.get("select"));  // 관리자가 선택한 프로그램의 사업자번호
			 int data =  (int) axiosData.get("select");
			 JSONObject result = new JSONObject();
			 
			// 처음 투표하는 사람인지 확인하기 위한 voter
			Company company = customCompanyRepository.findByConfirm(data);
			String confirm = company.getCconfirm();
				if(confirm.equals("0")) { // 승인을 받은적이 없으면
					System.out.println("승인되지 않은 프로그램입니다.");
					
					Company cc = companyRepository.findById(data); // 해당 사업자번호의 회사정보를 찾는다.
					
					program.setCategory(cc.getCcategory());   //카테고리
					program.setImg("defaultProfile.png"); //기본이미지
					program.setName(cc.getCprogram());            //프로그램명
					
					
					programRepository.saveAndFlush(program);       // program 테이블에 추가
					
					Program pi = customProgramRepository.findByPK(cc.getCprogram()); // 해당 프로그램의 번호를 가져옴
									
					System.out.println(""+cc.getRid()+"/"+pi.getId());
					pm.setId(cc.getRid());
					pm.setProgramId(pi.getId());
					
					pmRepository.saveAndFlush(pm); // 프로그램 권한자 등록
					
					customCompanyRepository.updateByConfirm(data); // 프로그램 confirm 1로 변경
					memberRepository.managerUpdate(cc.getRid()); // 회원테이블 role 3 으로 변경 (매니저)
					
					System.out.println(program.toString());
					
					
					result.put("message","승인완료 및 매니저권한을 부여하였습니다.");
					
					
				}else {
					result.put("errorMessage","이미 승인한 프로그램입니다.");
					System.out.println("이미 승인한 프로그램입니다.");
				}
		

				return result;
				
			}
	
	@RequestMapping(value={"/voter","/voter/"})
	public String voterVoteList(Principal user){// 내가 투표한 투표 리스트  뷰
		
		
		return "/userInfo/voterVoteList";
	}


			//  내 정보 페이지 투표 관련
	@RequestMapping(value={"/voter/axios","/voter/axios/"})
	@ResponseBody
	public JSONArray voterVoteListAxios(Principal user, @PageableDefault Pageable pageable){// 내가 투표한 투표 리스트  정보
		Member member = memberRepository.findByUserid(user.getName());
		// System.out.println("memer id :"+member.getNo());
		List<Vote>  votes = customVoteRepository.getVotesByR_id(pageable, member.getNo());
		int count = customVoteRepository.getVotesByR_idCount(member.getNo());

		
		JSONArray result = new JSONArray();

		for(Vote vote : votes){
			JSONObject voteInfo = new JSONObject();
			voteInfo.put("no", vote.getId());
			voteInfo.put("title", vote.getTitle());
			voteInfo.put("startTime",vote.getLongStartTime());
			voteInfo.put("endTime",vote.getLongEndTime());
			voteInfo.put("resultTime",vote.getLongResultShowTime());
			result.add(voteInfo);
		}
		result.add(count);
		
		return result;
	}
	// 투표 관리 페이지
	@RequestMapping(value={"/manage/vote","/manage/vote/"})
	public String manageVoteView(){// 내가 투표한 투표 리스트  뷰
		
		
		return "/userInfo/manageVote";
	}


			//  내 정보 페이지 투표 관련
	@RequestMapping(value={"/manage/vote/axios","/manage/vote/axios/"})
	@ResponseBody
	public JSONArray manageVoteAxios(Principal user, @PageableDefault Pageable pageable){// 내가 투표한 투표 리스트  정보
		Member member = memberRepository.findByUserid(user.getName());
		
		List<Vote>  votes = customVoteRepository.getMyVotes(pageable, member.getNo());
		int count = customVoteRepository.getMyVotesCount(member.getNo());

		// System.out.println("ㅁㅁㅁㅁ"+votes);
		
		JSONArray result = new JSONArray();

		for(Vote vote : votes){
			JSONObject voteInfo = new JSONObject();
			voteInfo.put("no", vote.getId());
			voteInfo.put("title", vote.getTitle());
			voteInfo.put("startTime",vote.getLongStartTime());
			voteInfo.put("endTime",vote.getLongEndTime());
			voteInfo.put("resultTime",vote.getLongResultShowTime());
			result.add(voteInfo);
		}
		result.add(count);
		
		return result;
	}
		 
		 //나의 프로그램
			@RequestMapping(value={"/myProgram"})
			public String myProgram() {        
				
		        return "userInfo/myProgram";
		       	}
			
		 
		 @RequestMapping(value={"/myProgram/axios","/myProgram/axios/"}) //사용자정보
			@ResponseBody
			public JSONObject myProgramAxios(){
					
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			CustomUserDetails sessionUser = (CustomUserDetails)principal;
			
			 
		
		  if(sessionUser.getROLE().equals("2")) {
		  
		  ProgramManager pm = pmRepository.findById(sessionUser.getR_ID()); 
		  
//		  System.out.println( pm.getId());
		 
		  
		  Program program = programRepository.findById(pm.getProgramId());
	  
//		  System.out.println(program.toString());
		  
		

		
				JSONObject programData = new JSONObject();
				
				programData.put("id", program.getId());
				programData.put("name", program.getName());
				programData.put("img", program.getImg());
				programData.put("category", program.getCategory());

				
				
					
			return programData;
		  
		  
		  }else { 
			  System.out.println("매니저가아닙니다.");
			  
		  }
		  
		return null;
		 
				
				
				
			

			 	
			}
		 
		 @RequestMapping(value="/programUpdate", method=RequestMethod.POST)
		    public String programUpdate(Program p, 
		    		RedirectAttributes redirAttrs, Principal principal,
		    		@RequestParam(name="file") MultipartFile file){
		       	
			 
			 
		    	String thumbnailPath = p.getImg();


		    	if(!file.isEmpty()) { // 프로필사진 변경을 했을시 
		    		
		    		storageService.store(file);
			    	
			    	thumbnailPath =  StringUtils.cleanPath(file.getOriginalFilename());
		   	
		    	}	

		 
			 programRepository.programUpdate(p.getId(), p.getName(), thumbnailPath, p.getCategory());
			
			 
		    	return "redirect:/userInfo/myProgram";   

		    }
		 
		 
		 //팬클럽 관리
			@RequestMapping(value={"/myCommunity"})
			public String myCommunity() {        
				System.out.println("팬클럽관리");
		        return "userInfo/myCommunity";
		        
		       	}
		 
			 @RequestMapping(value={"/myCommunity/axios","/myCommunity/axios/"}) //사용자정보
				@ResponseBody
				public JSONArray myCommunityAxios(Principal user, @PageableDefault Pageable pageable, Model model){
						
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				CustomUserDetails sessionUser = (CustomUserDetails)principal;
		
				if(sessionUser.getROLE().equals("2")) {
			  
					ProgramManager pm = pmRepository.findById(sessionUser.getR_ID()); 
			  
//				  System.out.println( pm.getId());
		  
					Program program = programRepository.findById(pm.getProgramId());
	  
     				System.out.println("gd"+pm.getProgramId());
     				
					List<Popular> populares = popularRepository.findByPid(program.getId());
		
								
					JSONArray json = new JSONArray();
					
					for( Popular popular : populares){
						JSONObject popularData = new JSONObject();
						
						popularData.put("id", popular.getId());
						popularData.put("name", popular.getName());
						popularData.put("img", popular.getImg());
						popularData.put("p_id", popular.getPid());

						
						json.add(popularData);
					}
				
					//json.add(count);
					json.add(pm.getProgramId());
					return json;
			  
			  
			  }else { 
				  System.out.println("잘못된 접근입니다.");
				  
			  }
			  
			return null;	 	
				}
			 
			 
			@RequestMapping(value="/insertPopular", method=RequestMethod.POST)
			    public String insertOk(Popular pp, RedirectAttributes redirAttrs, Principal principal
			    		,@RequestParam(name="img2") MultipartFile file
			    		){
			       	
					
			    	System.out.println("test:"+pp.toString());


	
			    		storageService.store(file);
			    		String thumbnailPath = StringUtils.cleanPath(file.getOriginalFilename());

			    		pp.setImg(thumbnailPath);
			    		
			    		System.out.println("test:"+pp.toString());
			    	
			    		popularRepository.saveAndFlush(pp);
				
			            return "redirect:/userInfo/myCommunity";
			        

			    }	
			
			@RequestMapping(value="/updatePopular", method=RequestMethod.POST)
		    public String updateOk(Popular pp, RedirectAttributes redirAttrs, Principal principal
		    		,@RequestParam(name="img2") MultipartFile file
		    		){
		       	
				System.out.println(pp.toString());
		    	
		    	
		    	String thumbnailPath = pp.getImg();  // 프로필사진 변동안했을때 그대로 두기위해서
		    	String url = "/uploads/";

		    	if(!file.isEmpty()) { // 프로필사진 변경을 했을시 

		    		storageService.store(file);
		    		thumbnailPath = StringUtils.cleanPath(file.getOriginalFilename());
		    		
		   	
		    	}
		    	
		    	popularRepository.popularUpdate(pp.getName(), thumbnailPath, pp.getPid(), pp.getId());
				
		    	
		    	
		            return "redirect:/userInfo/myCommunity";
		        

		    }	
			
			@RequestMapping(value="/deletePopular", method=RequestMethod.POST)
		    public String deleteOk(Popular pp, RedirectAttributes redirAttrs, Principal principal
		    		){
		       	
				System.out.println(pp.toString());
		    			    	
		    	
		    	popularRepository.deleteByid(pp.getId());
				
		    	
		    	
		            return "redirect:/userInfo/myCommunity";
		        

		    }	
		
			 

}