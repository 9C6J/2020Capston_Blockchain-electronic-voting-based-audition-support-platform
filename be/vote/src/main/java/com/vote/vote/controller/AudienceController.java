package com.vote.vote.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;
import javax.validation.Valid;

import com.google.gson.JsonObject;
import com.vote.vote.config.CustomUserDetails;
import com.vote.vote.db.dto.ADetaiId;
import com.vote.vote.db.dto.ADetail;
import com.vote.vote.db.dto.ApplyResult;
import com.vote.vote.db.dto.Audience;
import com.vote.vote.db.dto.Member;
import com.vote.vote.db.dto.Program;
import com.vote.vote.db.dto.ProgramManager;
import com.vote.vote.db.dto.Rfile;
import com.vote.vote.repository.ADetailRepository;
import com.vote.vote.repository.ApplyResultJpaRepository;
import com.vote.vote.repository.AudienceJpaRepository;
import com.vote.vote.repository.CustomProgramRepositoryImpl;
import com.vote.vote.repository.MemberJpaRepository;
import com.vote.vote.repository.MemberRepository;
import com.vote.vote.repository.MemberRepositoryImpl;
import com.vote.vote.repository.ProgramManagerJpaRepository;
import com.vote.vote.repository.ReplyRepository;
import com.vote.vote.repository.RfileRepository;
import com.vote.vote.service.AudienceService;
import com.vote.vote.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import java.security.Principal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.springframework.util.StringUtils;

@Controller
public class AudienceController {

    @Autowired
    CustomProgramRepositoryImpl pg;

    @Autowired
    public MemberRepositoryImpl mr;

    private AudienceService audienceService;

    @Autowired
    ADetailRepository aDetailRepository;

    @Autowired
    AudienceJpaRepository audienceJpaRepository;

    @Autowired
    private MemberJpaRepository memberRepository;

    @Autowired
    private ProgramManagerJpaRepository pmRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ApplyResultJpaRepository applyResultRepository;

    @Autowired
    public AudienceController(AudienceService audienceService) {
        this.audienceService = audienceService;

    }

    SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");

    @RequestMapping(value = { "/main" })
    public String test2(Principal user) {
        return "audience/main";
    }

    // 리액트 -------------------------------------사용자
    // 모든프로그램 게시글 리스트 보기 + ajax
    @RequestMapping(value = { "/audience/ulist2" })
    public String audienceAllList2() {
        return "audience/uList2";
    }

    @GetMapping(value = { "/audience/axios", "/audience/list/axios" })
    @ResponseBody
    public JSONArray audienceAllListJson() {
        JSONArray result = new JSONArray();
        List<Audience> audienceList = audienceJpaRepository.findAll();

        for (Audience audience : audienceList) {
            JSONObject json = new JSONObject();
            json.put("applyId", audience.getApplyId());
            json.put("aDate", format.format(audience.getADate()));
            json.put("aRecruits", audience.getARecruits());
            json.put("aTitle", audience.getATitle());
            json.put("aViewCount", audience.getAViewCount());
            json.put("img", audience.getImg());
            json.put("aContent", audience.getAContent());
            result.add(json);
        }
        return result;
    }

    @RequestMapping(value = { "/audience/read2/{applyId}" })
    public String test(Principal user) {
        return "audience/uRead2";
    }

    // -----------------------------------------사용자
    // 모든프로그램 게시글 리스트
    @GetMapping(value = { "/audience/", "/audience/list" })
    public String audienceAllList(@PageableDefault Pageable pageable, Model model) {

        Page<Audience> boardList = audienceService.getBoardList(pageable);
        model.addAttribute("boardList", boardList);

        return "audience/uList";
    }

    // 게시글 보기
    @RequestMapping("/audience/read/{applyId}")
    public String read(Model model, @PathVariable int applyId) {
        model.addAttribute("audience", audienceJpaRepository.findById(applyId));
        Audience audience = audienceJpaRepository.findById(applyId);
        audience.setAViewCount(audience.getAViewCount() + 1);
        audienceJpaRepository.saveAndFlush(audience);
        model.addAttribute("result", applyResultRepository.findByApplyId(applyId));

        return "audience/uRead";
    }

    // 응모 ajax
    @GetMapping("/audience/apply")
    @ResponseBody
    public String result(Audience audience, Principal principal, ADetail aDetail) {
        Member member = memberRepository.findByUserid(principal.getName());

        Audience audi = audienceJpaRepository.findById(audience.getApplyId());
        if (audi.getResult() == 1)
            return "이미 추첨이 완료된 응모입니다.";
        aDetail.setApplyId(audience.getApplyId());
        aDetail.setRId(member.getNo());
        // aDetaiId.setApplyId(audience.getApplyId());
        // aDetaiId.setRId(member.getNo());
        // aDetail.setADetaiId(aDetaiId);
        if (member.getPoint() < audience.getAPrice()) {
            return "포인트가 부족합니다.";
        } else if (aDetailRepository.countByApplyIdAndRId(audience.getApplyId(), member.getNo()) == audience
                .getALimit()) {
            return "응모횟수를 초과하셨습니다.";
        } else {
            int a = member.getPoint() - audience.getAPrice();
            try {
                memberRepository.updatePoint(a, member.getNo());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            aDetailRepository.saveAndFlush(aDetail);
            return "응모완료!";
        }
    }

    // --------------------------------------------------------관리자
    // 게시글 업로드(관리자)
    @GetMapping("/audience/create")
    public String mUpload(Model model) {
        model.addAttribute("audience", new Audience());
        return "audience/mCreate";
    }

    @PostMapping("/audience/create")
    public String mUpload(@Valid Audience audience, BindingResult bindingResult, SessionStatus sessionStatus,
            Principal principal, Model model, RedirectAttributes redirAttrs,
            @RequestParam(name = "filename") MultipartFile filename) {
        if (bindingResult.hasErrors()) {
            System.out.println("바인딩에러");
            return "audience/mCreate";
        } else {

            // Rfile rfile = new Rfile();
            // String filenamePath = StringUtils.cleanPath(filename.getOriginalFilename());
            Member member = memberRepository.findByUserid(principal.getName());
            ProgramManager pm = pmRepository.findById(member.getNo());

            // 게시글저장
            audience.setProgramId(pm.getProgramId());
            audience.setRId(member.getNo());
            audience.setADate(new Date());
            audience.setImg(storageService.store2(filename));
            audienceJpaRepository.saveAndFlush(audience);

            // 파일 저장

            storageService.store2(filename);

            // rfile.setApplyid(audience.getApplyId());
            // rfile.setFilename(filenamePath);
            // rfileRepository.saveAndFlush(rfile);
            // sessionStatus.setComplete();
            System.out.println("게시글업로드완료");
            return "redirect:/userInfo/audience/mlist";
        }
    }

    // 게시글 삭제
    @GetMapping("/audience/delete/{applyId}")
    public String delete(@PathVariable int applyId, Model model) {
        aDetailRepository.deleteByApplyId(applyId);
        audienceJpaRepository.deleteById(applyId);
        return "redirect:/userInfo/audience/mlist";
    }

    // 게시글 수정
    @GetMapping("/audience/update/{applyId}")
    public String update(@PathVariable int applyId, Model model) {
        model.addAttribute("audience", audienceJpaRepository.findById(applyId));
        model.addAttribute("newAudience", new Audience());
        return "audience/mUpdate";
    }

    @PostMapping("/audience/update")
    public String update(@Valid Audience audience, BindingResult bindingResult, SessionStatus sessionStatus,
            Principal principal, Model model, RedirectAttributes redirAttrs,
            @RequestParam(name = "filename") MultipartFile filename) {
        System.out.println(audience.getApplyId());
        System.out.println(audience.getATitle());
        storageService.store2(filename);
        audienceJpaRepository.audienceUpdate(audience.getATitle(), audience.getAStartdate(), audience.getAEnddate(),
                audience.getARecruits(), audience.getALimit(), audience.getAPrice(), storageService.store2(filename),
                audience.getAContent(), audience.getApplyId());
        System.out.println("수정햇엉");

        return "redirect:/userInfo/audience/mlist";
    }

    // 내가 작성한 게시글(관리자)
    @GetMapping(value = { "/userInfo/audience/mlist" })
    public String mList(Principal principal, Pageable pageable, Model model) {
        Member member = memberRepository.findByUserid(principal.getName());
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "applyId")); // <- Sort 추가

        Page<Audience> boardList = audienceJpaRepository.findAllByrId(pageable, member.getNo());
        model.addAttribute("boardList", boardList);

        return "audience/mList";
    }

    // 내가 작성한 게시글 보기(관리자)
    @RequestMapping("/userInfo/audience/mread/{applyId}")
    public String mRead(Model model, @PathVariable int applyId) {
        model.addAttribute("audience", audienceJpaRepository.findById(applyId));
        Audience audience = audienceJpaRepository.findById(applyId);
        audience.setAViewCount(audience.getAViewCount() + 1);
        audienceJpaRepository.saveAndFlush(audience);

        model.addAttribute("result", applyResultRepository.findByApplyId(applyId));

        return "audience/mRead";
    }

    // 응모인원 리스트 ajax
    @GetMapping("/audience/showList")
    @ResponseBody
    public JSONArray showList(Model model, Audience audience) {

        List<Member> list = new ArrayList<>();
        list = mr.getInfo(audience.getApplyId());

        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        for (Member list2 : list) {
            obj = new JSONObject();
            obj.put("name", list2.getName());
            obj.put("phone", list2.getPhone());
            array.add(0, obj);
        }
        return array;
    }

    @GetMapping("/audience/showResult")
    @ResponseBody
    public JSONObject showResult(Model model, Audience audience) {
        Audience audi = audienceJpaRepository.findById(audience.getApplyId());

        JSONObject json = new JSONObject();

        if (audi.getResult() == 1) {
            json.put("message", "이미 추첨한 방청권 응모입니다.");
            json.put("state", 1);
            return json;
        }

        int people = audi.getARecruits(); // 추첨 인원
        List<Member> list = new ArrayList<>();
        List<Member> result = new ArrayList<>();
        List<Member> result2 = new ArrayList<>();
        // list = mr.getInfoNoDistincList(audience.getApplyId()); // 응모 리스트
        list = mr.getInfo(audi.getApplyId());// 중복제거
        // JSONObject obj = new JSONObject();
        // JSONArray array = new JSONArray();
        if (people >= list.size()) {
            // list = mr.getInfo(audience.getApplyId());// 중복제거
            for (Member list2 : list) {
                // obj = new JSONObject();
                // obj.put("name", list2.getName());
                // obj.put("phone", list2.getPhone());
                // array.add(obj);
                ApplyResult applyResult = new ApplyResult();
                applyResult.setApplyId(audi.getApplyId());
                applyResult.setName(list2.getName());
                applyResult.setPhone(list2.getPhone());
                applyResultRepository.saveAndFlush(applyResult);

            }
            audi.setResult(1);// 추첨 완료
            audienceJpaRepository.saveAndFlush(audi);
            json.put("message", "추첨 완료");
            json.put("state", 0);
            return json;

        } else {
            list = mr.getInfoNoDistincList(audi.getApplyId()); // 응모 리스트
            while (result2.size() < people) {
                double randomValue = Math.random();
                int ran = (int) (randomValue * list.size());
                result.add(list.remove(ran));
                for (int i = 0; i < result.size(); i++) {
                    if (!result2.contains(result.get(i))) {
                        result2.add(result.get(i));
                    }
                }
            }
            System.out.println(result2);
            for (Member list2 : result2) {
                // obj = new JSONObject();
                // obj.put("name", list2.getName());
                // obj.put("phone", list2.getPhone());
                // array.add(obj);

                ApplyResult applyResult = new ApplyResult();
                applyResult.setApplyId(audi.getApplyId());
                applyResult.setName(list2.getName());
                applyResult.setPhone(list2.getPhone());
                applyResultRepository.saveAndFlush(applyResult);
            }
        }
        audi.setResult(1);// 추첨 완료
        audienceJpaRepository.saveAndFlush(audi);
        json.put("message", "추첨 완료");
        json.put("state", 0);
        return json;
    }

}