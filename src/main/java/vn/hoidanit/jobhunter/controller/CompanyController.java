package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    CompanyService companyService;
    
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }


    @PostMapping("/companies")
    public ResponseEntity<Company> createACompany(@Valid @RequestBody Company companyPostman) {
        Company createCompany = this.companyService.handleSaveCompany(companyPostman);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCompany);
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> findACompany (@PathVariable("id") Long id) {
        Company fetchCompany = this.companyService.handleFindACompany(id);
        return ResponseEntity.ok(fetchCompany);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteACompany (@PathVariable("id") Long id){
        this.companyService.handleDeleteACompany(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete company successfully!!!");
    }
    
    //sua
    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> readAllCompany(
        @Filter Specification<Company> spec, Pageable pageable
    ) {
        return ResponseEntity.ok(this.companyService.handleFindAllCompany(spec, pageable));
    }

    @PutMapping("/companies")
    public ResponseEntity<String> updateAUser(@RequestBody Company updateCompany) {
        this.companyService.handleUpdateACompany(updateCompany);
        return ResponseEntity.status(HttpStatus.OK).body("Update Company successfully");
    }
}
