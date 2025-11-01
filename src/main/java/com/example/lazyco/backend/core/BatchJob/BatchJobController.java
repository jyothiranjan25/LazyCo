package com.example.lazyco.backend.core.BatchJob;

import com.example.lazyco.backend.core.AbstractClasses.Controller.AbstractController;
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

  @Override
  public ResponseEntity<?> resolvePatchAction(String action, BatchJobDTO batchJobDTO) {
    switch (BatchJobDTO.APIAction.valueOf(action)) {
      case TERMINATE:
        batchJobDTO = batchJobService.terminateJob(batchJobDTO);
        break;
      case RESTART:
        batchJobDTO = batchJobService.restartJob(batchJobDTO);
        break;
      case PAUSE:
        batchJobDTO = batchJobService.pauseJob(batchJobDTO);
        break;
      case RESUME:
        batchJobDTO = batchJobService.restartJob(batchJobDTO); // Resume is same as restart
        break;
      default:
        batchJobDTO = batchJobService.update(batchJobDTO);
    }
    return ResponseUtils.sendResponse(batchJobDTO);
  }

  public List<CRUDEnums> restrictCRUDAction() {
    return List.of(CRUDEnums.POST, CRUDEnums.DELETE);
  }
}
