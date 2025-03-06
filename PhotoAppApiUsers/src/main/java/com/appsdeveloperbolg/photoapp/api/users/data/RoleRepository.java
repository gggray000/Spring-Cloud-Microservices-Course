package com.appsdeveloperbolg.photoapp.api.users.data;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    // It doesn't exist in CrudRepository interface, so needs to be specified.
    // "findByXX" must correspond to the entity's column name.
    RoleEntity findByName(String name);
}
