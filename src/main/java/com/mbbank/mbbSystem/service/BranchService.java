package com.mbbank.mbbSystem.service;

import com.mbbank.mbbSystem.model.Branch;
import com.mbbank.mbbSystem.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Optional<Branch> getBranch(Long id) {
        return branchRepository.findById(id);
    }

    public Optional<Branch> getBranchByMa(String maChiNhanh) {
        return branchRepository.findByMaChiNhanh(maChiNhanh);
    }

    public Branch createBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    public Branch updateBranch(Long id, Branch updated) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi nhánh"));
        if (updated.getMaChiNhanh() != null) branch.setMaChiNhanh(updated.getMaChiNhanh());
        if (updated.getBranchName() != null) branch.setBranchName(updated.getBranchName());
        if (updated.getBranchAddress() != null) branch.setBranchAddress(updated.getBranchAddress());
        return branchRepository.save(branch);
    }

    public void deleteBranch(Long id) {
        branchRepository.deleteById(id);
    }
}
