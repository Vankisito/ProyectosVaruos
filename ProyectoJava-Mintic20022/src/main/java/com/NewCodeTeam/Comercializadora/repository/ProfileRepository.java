package com.NewCodeTeam.Comercializadora.repository;


import com.NewCodeTeam.Comercializadora.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
}

