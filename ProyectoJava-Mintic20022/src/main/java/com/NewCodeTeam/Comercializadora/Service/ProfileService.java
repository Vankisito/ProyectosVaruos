package com.NewCodeTeam.Comercializadora.Service;

import com.NewCodeTeam.Comercializadora.model.Enterprise;
import com.NewCodeTeam.Comercializadora.model.Profile;
import com.NewCodeTeam.Comercializadora.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public Profile findById(Long id) {
        return profileRepository.findById(id).get();
    }

    public <S extends Profile> S save(S entity) {
        return profileRepository.save(entity);
    }

    public boolean deleteById(Long id) {
        profileRepository.deleteById(id);
        if (this.profileRepository.findById(id).isPresent()) {
            return false;
        }
        return true;
    }
}
