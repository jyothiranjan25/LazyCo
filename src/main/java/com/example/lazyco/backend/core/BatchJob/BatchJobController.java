package com.example.lazyco.backend.core.BatchJob;

import com.example.lazyco.backend.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.Utils.CRUDEnums;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch_job")
public class BatchJobController extends AbstractController<BatchJobDTO> {

  private final IBatchJobService batchJobService;

  public BatchJobController(IBatchJobService batchJobService) {
    super(batchJobService);
    this.batchJobService = batchJobService;
  }

  @PostMapping("/audit_logs")
  public ResponseEntity<?> getAuditLogs(@RequestBody BatchJobDTO batchJobDTO) {
    FileDTO fileDTO = batchJobService.getCsvAuditFileForJob(batchJobDTO.getId());
    return ResponseUtils.sendResponse(fileDTO);
  }

  public List<CRUDEnums> restrictCRUDAction() {
    return List.of(CRUDEnums.POST, CRUDEnums.DELETE);
  }
}
