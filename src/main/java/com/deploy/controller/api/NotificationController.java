package com.deploy.controller.api;

import com.deploy.dto.CustomPageable;
import com.deploy.dto.request.NotificationCreateReq;
import com.deploy.dto.request.NotificationSearchCond;
import com.deploy.dto.request.NotificationUpdateReq;
import com.deploy.dto.response.NotificationRes;
import com.deploy.dto.response.TuiGridRes;
import com.deploy.dto.response.handler.ResponseHandler;
import com.deploy.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping
    public TuiGridRes search(NotificationSearchCond condition, CustomPageable customPageable) {
        Page<NotificationRes> list = notificationService.search(condition, customPageable);
        return new TuiGridRes(list.getContent(), customPageable.getPage(), (int) list.getTotalElements(), customPageable.getPerPage());
    }

    @GetMapping("/searchAll")
    public ResponseEntity<?> searchAll() {
        List<NotificationRes> list = notificationService.searchAll();
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findNotification(@PathVariable Long id) {
        NotificationRes NotificationRes = notificationService.findNotification(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", NotificationRes);
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody @Valid NotificationCreateReq request) {
        Long id = notificationService.createNotification(request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotification(@PathVariable Long id, @RequestBody @Valid NotificationUpdateReq request) {
        notificationService.updateNotification(id, request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }
}
