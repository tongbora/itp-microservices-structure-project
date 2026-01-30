package com.tongbora.iptspringauthorizationserver.feature.role;

import com.tongbora.iptspringauthorizationserver.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
