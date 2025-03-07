package com.appsdeveloperbolg.photoapp.api.users.data;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name="authorities")
public class AuthorityEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -345928904875040402L;
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false, length = 20)
    private String name;
    @ManyToMany(mappedBy = "authorities")
    private Collection<RoleEntity> roles;

    public AuthorityEntity() {
    }

    public AuthorityEntity(String name) {
        this.name=name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleEntity> roles) {
        this.roles = roles;
    }
}
