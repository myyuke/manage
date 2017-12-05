package com.example.demo.control;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@EnableAutoConfiguration
public class ViewControl {
    @RequestMapping("/login")
    public String login() {
        return "login";
    }
    @RequestMapping("/index")
    public String index() {
        return "index";
    }
    @RequestMapping("/list")
    public String list() {
        return "member-list";
    }
    @RequestMapping("/add")
    public String memeberadd() {
        return "member-add";
    }
    @RequestMapping(value="/edit")
    public String memberedit(Model model,@RequestParam("personId") Integer personId){
        System.out.println(personId);
        model.addAttribute("personId",personId);
        return "member-edit";
    }
    @RequestMapping("/article-list")
    public String articleList() {
        return "article-list";
    }
    @RequestMapping("/picture-list")
    public String pictureList() {
        return "picture-list";
    }

    @RequestMapping("/product-brand")
    public String productBrand() {
        return "product-brand";
    }
    @RequestMapping("/product-category")
    public String productCategory() {
        return "product-category";
    }
    @RequestMapping("/product-list")
    public String productList() {
        return "product-list";
    }
    @RequestMapping("/feedback-list")
    public String feedbackList() {
        return "feedback-list";
    }
    @RequestMapping("/charts-1")
    public String charts1() {
        return "charts-1";
    }
    @RequestMapping("/charts-2")
    public String charts2() {
        return "charts-2";
    }
    @RequestMapping("/charts-3")
    public String charts3() {
        return "charts-3";
    }
    @RequestMapping("/charts-4")
    public String charts4() {
        return "charts-4";
    }
    @RequestMapping("/charts-5")
    public String charts5() {
        return "charts-5";
    }
    @RequestMapping("/charts-6")
    public String charts6() {
        return "charts-6";
    }
    @RequestMapping("/system-category")
    public String systemCategory() {
        return "system-category";
    }
    @RequestMapping("/system-data")
    public String systemData() {
        return "system-data";
    }
    @RequestMapping("/system-shielding")
    public String systemShielding() {
        return "system-shielding";
    }
    @RequestMapping("/system-log")
    public String systemLog() {
        return "system-log";
    }
    @RequestMapping("/system-base")
    public String systemBase() {
        return "system-base";
    }

}
