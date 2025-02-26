package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    
    public Company handleSaveCompany(Company company){
        return this.companyRepository.save(company);
    }

    public Company handleFindACompany(Long id){
        Optional<Company> currentCompany = this.companyRepository.findById(id);
        if(currentCompany.isPresent()){
            return currentCompany.get();
        }
        return null;
        
    }

    public Boolean isCompanyExist(Long id){
        if(id ==null){
            return false;
        }
        return this.companyRepository.existsById(id);
    }

    public ResultPaginationDTO handleFindAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> pCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pCompany.getTotalPages());
        mt.setTotal(pCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pCompany.getContent());
        return rs;
    }

    public Company handleUpdateACompany(Company company){
        Company updateC = this.handleFindACompany(company.getId());
        if (updateC !=null){
            updateC.setLogo(company.getLogo());
            updateC.setName(company.getName());
            updateC.setDescription(company.getDescription());
            updateC.setAddress(company.getAddress());
            return this.companyRepository.save(updateC);
        }
        return null;
    }

    public void handleDeleteACompany(Long id){
        this.companyRepository.deleteById(id);
    }

    
}
