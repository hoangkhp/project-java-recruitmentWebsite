package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
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

    public Company handleFindACompany(long id){
        Optional<Company> c= this.companyRepository.findById(id);
        if(c.isPresent()){
            return c.get();
        }
        return null;
    }

    public ResultPaginationDTO handleFindAllCompany(Pageable pageable){
        Page<Company> pageCompany = this.companyRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();
        mt.setPage(pageCompany.getNumber() + 1);
        mt.setPageSize(pageCompany.getSize());
        
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());

        return rs;
    }

    public Company handleUpdateACompany(Company company){
        Company updateC = this.handleFindACompany(company.getId());
        if (updateC != null){
            updateC.setName(company.getName());
            updateC.setDescription(company.getDescription());
            updateC.setAddress(company.getAddress());
            updateC.setLogo(company.getLogo());
        }
        return updateC;
    }

    public void handleDeleteAComany(Long id){
        this.companyRepository.deleteById(id);
    }
}
