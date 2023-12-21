package com.switchfully.eurder_db.service;

import com.switchfully.eurder_db.entity.Admin;
import com.switchfully.eurder_db.exception.UnknownAdminEmailException;
import com.switchfully.eurder_db.exception.WrongPasswordException;
import com.switchfully.eurder_db.repository.AdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AdminService {
    private AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin authenticate(String email, String password){
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            throw new UnknownAdminEmailException();
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(password, admin.getPassword())){
            throw new WrongPasswordException();
        }
        return admin;
    }
}
