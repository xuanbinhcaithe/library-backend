package com.xuanbinh.library.repo;

import com.xuanbinh.library.common.VfData;
import com.xuanbinh.library.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class RoleRepoTest {

    @Autowired
    RoleRepo roleRepo;

    @Test
    void findByNameTest() {

        Role role = new Role(1L, "ROLE_ADMIN");
        roleRepo.save(role);
        Role result = roleRepo.findByName("ROLE_ADMIN");
        assertThat(result).isEqualTo(new Role(1L, "ROLE_ADMIN"));

    }

    @Test
    void findRoleUserTest() {
    }
}