package com.ecinema.app.user;

import com.ecinema.app.security.AppUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Admin extends AppUser {}
