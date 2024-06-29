package group_6.e_contractor_backend.job_offer_module.controllers;

import group_6.e_contractor_backend.job_offer_module.entities.JobApplication;
import group_6.e_contractor_backend.job_offer_module.entities.JobFile;
import group_6.e_contractor_backend.job_offer_module.entities.JobOffer;
import group_6.e_contractor_backend.job_offer_module.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("job-application")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class JobApplicationApiController {
    private final JobApplicationService jobApplicationService;

    @PostMapping("create/{offerId}/{studentId}")
    public JobApplication createJobApplication(@RequestBody JobApplication jobApplication,@PathVariable Long offerId, @PathVariable Long studentId){
        return  jobApplicationService.createJobApplication(jobApplication,offerId,studentId);
    }

    @PostMapping("upload-file")
    public JobFile uploadApplicationFile(@RequestParam("file") MultipartFile file) {
        return jobApplicationService.uploadJobFile(file);
    }

    @GetMapping("download-file/{fileName:.+}")
    public ResponseEntity<Resource> downloadApplicationFile(@PathVariable String fileName) {
        Resource resource = jobApplicationService.downloadJobFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


}
