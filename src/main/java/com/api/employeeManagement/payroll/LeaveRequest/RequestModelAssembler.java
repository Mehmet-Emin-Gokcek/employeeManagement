package com.api.employeeManagement.payroll.LeaveRequest;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class RequestModelAssembler implements RepresentationModelAssembler<LeaveRequest, EntityModel<LeaveRequest>> {

    @Override
    public EntityModel<LeaveRequest> toModel(LeaveRequest request) {

        // Unconditional links to single-item resource and aggregate root

        EntityModel<LeaveRequest> requestModel = EntityModel.of(request,
                linkTo(methodOn(LeaveRequestController.class).one(request.getId())).withSelfRel(),
                linkTo(methodOn(LeaveRequestController.class).all()).withRel("requests"));

        // Conditional links based on state of the request

        if (request.getStatus() == Status.UNDER_REVIEW) {
            requestModel.add(linkTo(methodOn(LeaveRequestController.class).reject(request.getId())).withRel("reject"));
            requestModel.add(linkTo(methodOn(LeaveRequestController.class).approve(request.getId())).withRel("approve"));
        }

        return requestModel;
    }
}