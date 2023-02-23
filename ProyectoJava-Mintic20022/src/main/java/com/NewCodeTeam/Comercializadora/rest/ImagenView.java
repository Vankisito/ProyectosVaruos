package com.NewCodeTeam.Comercializadora.rest;

import com.NewCodeTeam.Comercializadora.Service.EmployeeService;
import com.NewCodeTeam.Comercializadora.Service.ProfileService;
import com.NewCodeTeam.Comercializadora.model.Employee;
import com.NewCodeTeam.Comercializadora.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class ImagenView {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ProfileService profileService;

    public Profile imgView (){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String email=auth.getName();
        Employee employee = employeeService.findByEmail(email);
        Profile profileUs = new Profile();
        if(employee.getProfile() != null){
            profileUs = profileService.findById(employee.getProfile().getId());
            if (profileUs.getImage() == null){
                profileUs.setImage("perfil.png");
            }
        }else{
            profileUs.setImage("perfil.png");
        }
        return profileUs;
    }
}
