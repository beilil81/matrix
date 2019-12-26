package com.humanup.matrix.controllers;

import com.humanup.matrix.bs.ProfileBS;
import com.humanup.matrix.vo.ProfileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProfileController {
    @Autowired
    private ProfileBS profileBS;



    @RequestMapping(value="/profile", method= RequestMethod.POST)
    @ResponseBody

    public ResponseEntity createProfile(@RequestBody ProfileVO profile){
        Optional<Object> findProfile = Optional.ofNullable(profileBS.findProfileByTitle(profile.getProfileTitle()));

        if(findProfile.isPresent()){
            return ResponseEntity.status(HttpStatus.FOUND).body("This Profile is Founded");
        }
        profileBS.createProfile(profile);
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }

    @RequestMapping(value="/profile", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getProfileInfo(@RequestParam(value="title", defaultValue="Spring Developer") String profileTitle){
        Optional<ProfileVO> findProfile = Optional.ofNullable(profileBS.findProfileByTitle(profileTitle));
        if(findProfile.isEmpty()){
            Optional<List<ProfileVO>> findListProfilesByTitle = Optional.ofNullable(profileBS.findListProfilesByTitle(profileTitle));
            if(findListProfilesByTitle.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This Title not Found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(findListProfilesByTitle.get());
        }
        return ResponseEntity.status(HttpStatus.OK).body(findProfile.get());
    }

    @RequestMapping(value="/profile/all", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAllProfileInfo(){
        List<ProfileVO> findProfiles = profileBS.findListProfile();

        if(findProfiles.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(findProfiles);
    }

}