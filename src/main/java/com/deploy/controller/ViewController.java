package com.deploy.controller;

import com.deploy.dto.response.JobRes;
import com.deploy.service.JobService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/view")
public class ViewController {

    private final JobService jobService;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }


    @GetMapping("/profile")
    public String profile() {
        return "user/profile";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings/settings";
    }

    @GetMapping("/settings/credential")
    public String credential() {
        return "settings/credential";
    }

    @GetMapping("/settings/credential-edit")
    public String credentialEdit(String id, Model model) {
        model.addAttribute("id", id);
        return "settings/credential-edit";
    }

    @GetMapping("/settings/credentials")
    public String credentials() {
        return "settings/credentials";
    }

    @GetMapping("/settings/notifications")
    public String notifications() {
        return "settings/notifications";
    }

    @GetMapping("/settings/notification")
    public String notificaion() {
        return "settings/notification";
    }

    @GetMapping("/settings/notification-edit")
    public String scmNotificationEdit(String id, Model model) {
        model.addAttribute("id", id);
        return "settings/notification-edit";
    }

    @GetMapping("/settings/scmConfigs")
    public String scmConfigs() {
        return "settings/scmConfigs";
    }

    @GetMapping("/settings/scmConfig")
    public String scmConfig() {
        return "settings/scmConfig";
    }

    @GetMapping("/settings/scmConfig-edit")
    public String scmConfigEdit(String id, Model model) {
        model.addAttribute("id", id);
        return "settings/scmConfig-edit";
    }

    @GetMapping("/project/newProject")
    public String newProject() {
        return "project/newProject";
    }

    @GetMapping("/project/myProject")
    public String myProject(HttpSession httpSession, Model model) {
        Long userId = (Long) httpSession.getAttribute("userId");
        List<JobRes> jobRes = jobService.searchMyJob(userId);

        model.addAttribute("projects", jobRes);
        return "project/myProject";
    }

    @GetMapping("/project/project")
    public String project(String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("showDashboard", true);
        return "project/project";
    }

    @GetMapping("/project/project-dashboard")
    public String projectDashboard(String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("showDashboard", true);
        return "project/project";
    }

    @GetMapping("/project/project-rename")
    public String projectRename(String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("showRename", true);
        return "project/project";
    }

    @GetMapping("/project/project-configure")
    public String projectConfigure(String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("showConfigure", true);
        return "project/project";
    }

    @GetMapping("/project/project-history")
    public String projectHistory(String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("showHistory", true);
        return "project/project";
    }

    @GetMapping("/project/project-buildFile")
    public String projectBuildFiles(String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("showBuildFile", true);
        return "project/project";
    }


    @GetMapping("/project/project-notification")
    public String projectNotification(String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("showNotification", true);
        return "project/project";
    }

    @GetMapping("/project/project-schedule")
    public String projectSchedule(String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("showSchedule", true);   // 사이드바 active 효과
        model.addAttribute("showScheduleList", true);
        return "project/project";
    }

    @GetMapping("/project/project-schedule-add")
    public String projectScheduleAdd(String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("showSchedule", true);   // 사이드바 active 효과
        model.addAttribute("showScheduleAdd", true);
        return "project/project";
    }

    @GetMapping("/project/project-schedule-edit")
    public String projectScheduleEdit(String id, String scheduleId, Model model) {
        model.addAttribute("id", id);   // job id
        model.addAttribute("scheduleId", scheduleId);   // schedule id
        model.addAttribute("showSchedule", true);   // 사이드바 active 효과
        model.addAttribute("showScheduleEdit", true);
        return "project/project";
    }

}
