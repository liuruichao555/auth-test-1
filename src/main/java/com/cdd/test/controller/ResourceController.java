package com.cdd.test.controller;

import com.cdd.test.model.History;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * ResourceController
 *
 * @author liuruichao
 * Created on 2018/7/3 11:28
 */
@Controller
@RequestMapping("/resource")
public class ResourceController {
    @GetMapping("/history/list")
    @PreAuthorize("hasRole('NORMAL')")
    @ResponseBody
    public List<History> resource1() {
        List<History> list = new ArrayList<>();
        list.add(new History("玄门大师", "观看至五分五秒"));
        list.add(new History("走火", "观看至五分五秒"));
        list.add(new History("钟馗捉妖记", "观看至五分五秒"));
        return list;
    }

    @GetMapping("/vip/info")
    @PreAuthorize("hasRole('VIP')")
    @ResponseBody
    public String resource2() {
        return "Level: 至尊";
    }

    @GetMapping("/basic/info")
    @PreAuthorize("hasRole('NORMAL')")
    @ResponseBody
    public String resource3() {
        String html = "姓名：刘瑞超<br/>年龄：20<br/>";
        return html;
    }
}
