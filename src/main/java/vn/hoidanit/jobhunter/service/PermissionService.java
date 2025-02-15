package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission handleSavePermission(Permission p){
       return this.permissionRepository.save(p);
    }

    public Permission fetchAPermission(Long id){
        Optional<Permission> p = this.permissionRepository.findById(id);
        if(p.isPresent()){
            return p.get();
        }
        return null;
    }

    public boolean isPermissionExist(Permission p){
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(
            p.getModule(),
            p.getApiPath(),
            p.getMethod()
        );
    }

    public Permission update(Permission p){
        Permission permissionDB = this.fetchAPermission(p.getId());
        if(permissionDB != null){
            permissionDB.setName(p.getName());
            permissionDB.setApiPath(p.getApiPath());
            permissionDB.setMethod(p.getMethod());
            permissionDB.setModule(p.getModule());
        }
        permissionDB = this.permissionRepository.save(p);
        return null;
    }
    public void delete(long id) {
        // delete permission_role
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));
        // delete permission
        this.permissionRepository.delete(currentPermission);
    }

    public ResultPaginationDTO getPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pPermissions = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pPermissions.getTotalPages());
        mt.setTotal(pPermissions.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pPermissions.getContent());
        return rs;
    }
}
