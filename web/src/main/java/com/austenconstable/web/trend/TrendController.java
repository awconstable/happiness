package com.austenconstable.web.trend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by awconstable on 26/02/2017.
 */
@Controller
public class TrendController
    {

    @Value("${kibana.url}")
    private String kibanaUrl;

    @RequestMapping("/")
    public String trend(Model model)
        {
        model.addAttribute("kibanaUrl", kibanaUrl);
        return "trend";
        }
    }
