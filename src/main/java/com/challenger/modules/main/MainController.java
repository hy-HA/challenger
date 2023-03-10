package com.challenger.modules.main;

import com.challenger.modules.account.AccountRepository;
import com.challenger.modules.account.CurrentAccount;
import com.challenger.modules.account.Account;
import com.challenger.modules.event.repository.EnrollmentRepository;
import com.challenger.modules.study.domain.Study;
import com.challenger.modules.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final StudyRepository studyRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model) {
        if (account != null) {
            Account accountLoaded = accountRepository.findAccountWithTagsAndZonesById(account.getId());
            model.addAttribute(accountLoaded);
            model.addAttribute("enrollmentList",
                    enrollmentRepository.findByAccountAndAcceptedOrderByEnrolledAtDesc(
                            accountLoaded, true));
            model.addAttribute("studyList",
                    studyRepository.findByAccount(
                    accountLoaded.getTags()));
            model.addAttribute("studyManagerOf",
                    studyRepository.findFirst5ByManagersContainingAndClosedOrderByPublishedDateTimeDesc(
                            account, false));
            model.addAttribute("studyMemberOf",
                    studyRepository.findFirst5ByMembersContainingAndClosedOrderByPublishedDateTimeDesc(
                            account, false));
            model.addAttribute("numberOfChallenger",enrollmentRepository.count());
            model.addAttribute("numberOfUser",accountRepository.count());
            return "index-after-login";
        }

        model.addAttribute("studyList",
                studyRepository.findFirst9ByPublishedAndClosedOrderByPublishedDateTimeDesc(
                        true, false));
        model.addAttribute("numberOfChallenger",enrollmentRepository.count());
        model.addAttribute("numberOfUser",accountRepository.count());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/search/study")
    public String searchStudy(String keyword, Model model,
              @PageableDefault(size = 9, sort = "publishedDateTime", direction = Sort.Direction.DESC)
              Pageable pageable) {
        Page<Study> studyPage = studyRepository.findByKeyword(keyword, pageable);
        model.addAttribute("studyPage", studyPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortProperty",
                pageable.getSort().toString()
                        .contains("publishedDateTime") ? "publishedDateTime" : "memberCount");
        return "search";
    }

}















