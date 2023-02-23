package com.NewCodeTeam.Comercializadora.rest;

import com.NewCodeTeam.Comercializadora.Service.EmployeeService;
import com.NewCodeTeam.Comercializadora.Service.EnterpriseService;
import com.NewCodeTeam.Comercializadora.model.Employee;
import com.NewCodeTeam.Comercializadora.model.Enterprise;
import com.NewCodeTeam.Comercializadora.model.Profile;
import com.NewCodeTeam.Comercializadora.model.Transaction;
import com.NewCodeTeam.Comercializadora.model.enumeration.EnumRoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.aspectj.util.LangUtil.isEmpty;

@Controller
@RequestMapping("/api")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private  ImagenView imagenView;

    @GetMapping("/enterprises")
    public String getAllEnterprise(Model model, @ModelAttribute("mensaje") String mensaje){
        Profile image = imagenView.imgView();
        model.addAttribute("image",image);
        List<Enterprise> enterpriseList=enterpriseService.findAll();
        model.addAttribute("listEnterprise",enterpriseList);
        model.addAttribute("mensaje",mensaje);
        return "enterprises";
    }

    @GetMapping("/newEnterprise")
    public String newEnterprise(Model model, @ModelAttribute("mensaje") String mensaje){
        Profile image = imagenView.imgView();
        model.addAttribute("image",image);
        Enterprise emp= new Enterprise();
        model.addAttribute("emp",emp);
        model.addAttribute("mensaje",mensaje);
        return "newEnterprise";
    }

    @PostMapping("/saveEnterprise")
    public String saveEnterprise (Enterprise emp, RedirectAttributes redirectAttributes){
        try {
            enterpriseService.save(emp);
            redirectAttributes.addFlashAttribute("mensaje","saveOK");
            return "redirect:/api/enterprises";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mensaje","saveError");
            return "redirect:/api/newEnterprise";
        }
    }

    @GetMapping("/editEnterprise/{id}")
    public String editEnterprise(Model model, @PathVariable("id") Long id, @ModelAttribute("mensaje") String mensaje){
        Profile image = imagenView.imgView();
        model.addAttribute("image",image);
        Enterprise emp= enterpriseService.findById(id);
        model.addAttribute("emp",emp);
        model.addAttribute("mensaje", mensaje);
        return "editEnterprise";
    }

    @PostMapping("/updateEnterprise")
    public  String updateEnterprise (@ModelAttribute("emp") Enterprise emp, RedirectAttributes redirectAttributes){
        try {
            enterpriseService.save(emp);
            redirectAttributes.addFlashAttribute("mensaje","updateOK");
            return "redirect:/api/enterprises";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mensaje","updateError");
            return "redirect:/api/editEnterprise/"+emp.getId();
        }
    }

    @GetMapping(value = "/deleteEnterprise/{id}")
    public  String deleteEnterprise (@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        if (enterpriseService.deleteById(id)==true){
            redirectAttributes.addFlashAttribute("mensaje","deleteOK");
        }else{
            redirectAttributes.addFlashAttribute("mensaje", "deleteError");
        }
        return "redirect:/api/enterprises";
    }

    //Movimientos:

    @GetMapping ("enterprise/{id}/movements")
    public String getAllmovementsByEnterprise (Model model, @PathVariable("id") Long id, @ModelAttribute("mensaje") String mensaje){
        Profile image = imagenView.imgView();
        model.addAttribute("image",image);
        List<Transaction> movimentsList=enterpriseService.findMovimentsEnterpriseByIdEnterprise(id);
        float total = enterpriseService.sumMoviments(id);
        model.addAttribute("listMoviments",movimentsList);
        List<Enterprise> empre= enterpriseService.findByIdList(id);
        model.addAttribute("empre",empre);
        model.addAttribute("mensaje",mensaje);
        model.addAttribute("suma", total);
        return "movementsEnterprise";
    }

    @GetMapping("enterprise/{id}/newMovements")
    public String newMoviments(Model model,@PathVariable("id") Long id, @ModelAttribute("mensaje") String mensaje){
        Profile image = imagenView.imgView();
        model.addAttribute("image",image);
        Transaction movement= new Transaction();
        model.addAttribute("movement",movement);
        model.addAttribute("mensaje",mensaje);
        List<Enterprise> empre= enterpriseService.findByIdList(id);
        model.addAttribute("empre",empre);
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String email=auth.getName();
        Long idEmployee=enterpriseService.findByEmail(email);
        model.addAttribute("idEmployee",idEmployee);
        return "newMovements";
    }

    @PostMapping("/enterprise/saveMovementEnterprise")
    public  String saveMovementEnterprise (Transaction transaction, RedirectAttributes redirectAttributes){
        try {
            enterpriseService.saveTransaction(transaction);
            redirectAttributes.addFlashAttribute("mensaje","saveOK");
            return "redirect:/api/enterprise/"+transaction.getEnterprises().getId()+"/movements";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mensaje","saveError");
            return "redirect:/api/enterprise/"+transaction.getEnterprises().getId()+"/newMovements";
        }
    }

    @GetMapping("/editMovementEnterprise/{id}")
    public String editMovementEnterprise(Model model, @PathVariable("id") Long id, @ModelAttribute("mensaje") String mensaje){
        Profile image = imagenView.imgView();
        model.addAttribute("image",image);
        Transaction mov= enterpriseService.findByIdTransaction(id);
        model.addAttribute("mov",mov);
        model.addAttribute("mensaje", mensaje);
        List<Enterprise> empre= enterpriseService.findByIdList(mov.getEnterprises().getId());
        model.addAttribute("empre",empre);
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String email=auth.getName();
        Employee employee = employeeService.findByEmail(email);
        if(employee.getRole().equals(EnumRoleName.ROLE_ADMIN)) {
            List<Employee> employeeList = employeeService.findEmployeesByIdEnterprise(mov.getEnterprises().getId());
            model.addAttribute("employees",employeeList);
        }else{
            List<Employee> employeeList = employeeService.findByIdListEmployees(employee.getId());
            model.addAttribute("employees",employeeList);
        }
        return "editMovements";
    }

    @PostMapping("/enterprise/updateMovementEnterprise")
    public  String updateMovementEnterprise (@ModelAttribute("mov") Transaction mov,RedirectAttributes redirectAttributes){
        try {
            Authentication auth= SecurityContextHolder.getContext().getAuthentication();
            String email=auth.getName();
            Employee employee=employeeService.findByEmail(email);
            Transaction transactionObject = enterpriseService.findByIdTransaction(mov.getId());
            if(employee.getRole().equals(EnumRoleName.ROLE_ADMIN)) {
                enterpriseService.saveTransaction(mov);
                redirectAttributes.addFlashAttribute("mensaje","updateOK");
                return "redirect:/api/enterprise/"+mov.getEnterprises().getId()+"/movements";
            }else if(transactionObject.getUser().getEmail().equals(mov.getUser().getEmail())){
                enterpriseService.saveTransaction(mov);
                redirectAttributes.addFlashAttribute("mensaje","updateOK");
                return "redirect:/api/enterprise/"+mov.getEnterprises().getId()+"/movements";
            }else{
                redirectAttributes.addFlashAttribute("mensaje","updateErrorUsuario");
                return "redirect:/api/editMovementEnterprise/"+mov.getId();
            }
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mensaje","updateError");
            return "redirect:/api/editMovementEnterprise/"+mov.getId();
        }
    }

    @GetMapping(value = "/enterprise/{idEnterprise}/deleteMovement/{id}")
    public  String deleteMovementEnterprise (@PathVariable("idEnterprise") Long idEnterprise,@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String email=auth.getName();
        Employee employee=employeeService.findByEmail(email);
        Transaction movi = enterpriseService.findByIdTransaction(id);
        if(employee.getRole().equals(EnumRoleName.ROLE_ADMIN)){
            if (enterpriseService.deleteMovementById(id) == true){
                redirectAttributes.addFlashAttribute("mensaje","deleteOK");
            }else{
                redirectAttributes.addFlashAttribute("mensaje", "deleteError");
            }
        }else if(movi.getUser().getId().equals(employee.getId())){
            if (enterpriseService.deleteMovementById(id) == true){
                redirectAttributes.addFlashAttribute("mensaje","deleteOK");
            }else{
                redirectAttributes.addFlashAttribute("mensaje", "deleteError");
            }
        }else {
            redirectAttributes.addFlashAttribute("mensaje", "deleteErrorUser");
        }
        return "redirect:/api/enterprise/"+idEnterprise+"/movements";
    }

    @GetMapping ("/employee/movements")
    public String getAllmovementsByEmployee (Model model, @ModelAttribute("mensaje") String mensaje, RedirectAttributes redirectAttributes){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String email=auth.getName();
        Employee employee=employeeService.findByEmail(email);
        List<Transaction> movimentsList=enterpriseService.findMovimentsEnterpriseByIdEnterprise(employee.getEnterprises().getId());
        float total = enterpriseService.sumMoviments(employee.getId());
        model.addAttribute("listMoviments",movimentsList);
        List<Enterprise> empre= enterpriseService.findByIdList(employee.getId());
        model.addAttribute("empre",empre);
        model.addAttribute("mensaje",mensaje);
        model.addAttribute("suma", total);
        return "redirect:/api/enterprise/"+employee.getEnterprises().getId()+"/movements";
    }

    @GetMapping("/enterpriseFilter/{name}")
    public String enterpriseFilter (@PathVariable("name") String name, Model model,RedirectAttributes redirectAttributes){
        Profile image = imagenView.imgView();
        model.addAttribute("image",image);
        try {
            List<Enterprise> enterpriseList = enterpriseService.findByNameEnterprises(name);
            boolean isEmpty = isEmpty(enterpriseList);
            if (isEmpty) {
                redirectAttributes.addFlashAttribute("mensaje","filterError");
                return "redirect:/api/enterprises";
            } else {
                model.addAttribute("listEnterprise",enterpriseList);
                return "enterprises";
            }
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("mensaje","filterError");
            return "redirect:/api/enterprises";
        }
    }
}