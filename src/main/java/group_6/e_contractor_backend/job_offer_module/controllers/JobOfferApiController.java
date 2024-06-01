package group_6.e_contractor_backend.job_offer_module.controllers;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.repositories.*;
import group_6.e_contractor_backend.job_offer_module.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("job-offer")
@RequiredArgsConstructor
public class JobOfferApiController {

    private final JobOfferService jobOfferService;

    @PostMapping("create")
    public JobOffer createJobOffer(@RequestBody JobOffer jobOffer){
        return  jobOfferService.createJobOffer(jobOffer);
    }
}
