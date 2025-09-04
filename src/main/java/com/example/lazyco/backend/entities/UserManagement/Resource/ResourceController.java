package com.example.lazyco.backend.entities.UserManagement.Resource;

import com.example.lazyco.backend.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.backend.core.AbstractDocumentClasses.ServiceDocument.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("resource")
public class ResourceController extends AbstractController<ResourceDTO> {

    public ResourceController(IAbstractService<ResourceDTO, ?> abstractService) {
        super(abstractService);
    }
}
