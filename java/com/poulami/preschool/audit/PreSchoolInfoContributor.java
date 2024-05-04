package com.poulami.preschool.audit;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PreSchoolInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, String> preMap = new HashMap<String, String>();
        preMap.put("App Name", "PreSchool");
        preMap.put("App Description", "Pre School Web Application for Students and Admin");
        preMap.put("App Version", "1.0.0");
        preMap.put("Contact Email", "info@preschool.com");
        preMap.put("Contact Mobile", "+1(21) 673 4587");
        builder.withDetail("preschool-info", preMap);
    }

}
