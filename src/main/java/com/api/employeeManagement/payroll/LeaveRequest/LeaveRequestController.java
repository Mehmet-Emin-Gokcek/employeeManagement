package com.api.employeeManagement.payroll.LeaveRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "api")
class LeaveRequestController {

    private final RequestRepository repository;
    private final RequestModelAssembler assembler;

    LeaveRequestController(RequestRepository repository, RequestModelAssembler assembler) {

        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/leave_requests")
    CollectionModel<EntityModel<LeaveRequest>> all() {

        List<EntityModel<LeaveRequest>> requests = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(requests, //
                linkTo(methodOn(LeaveRequestController.class).all()).withSelfRel());
    }

    @GetMapping("/leave_requests/{id}")
    EntityModel<LeaveRequest> one(@PathVariable Long id) {

        LeaveRequest request = repository.findById(id) //
                .orElseThrow(() -> new RequestNotFoundException(id));

        return assembler.toModel(request);
    }

    @PostMapping("/leave_requests")
    ResponseEntity<EntityModel<LeaveRequest>> newLeaveRequest(@RequestBody LeaveRequest request) {

        request.setStatus(Status.UNDER_REVIEW);
        LeaveRequest newLeaveRequest = repository.save(request);

        return ResponseEntity //
                .created(linkTo(methodOn(LeaveRequestController.class).one(newLeaveRequest.getId())).toUri()) //
                .body(assembler.toModel(newLeaveRequest));
    }

    @DeleteMapping("/leave_requests/{id}/cancel")
    ResponseEntity<?> reject(@PathVariable Long id) {

        LeaveRequest request = repository.findById(id) //
                .orElseThrow(() -> new RequestNotFoundException(id));

        if (request.getStatus() == Status.UNDER_REVIEW) {
            request.setStatus(Status.REJECTED);
            return ResponseEntity.ok(assembler.toModel(repository.save(request)));
        }

        return ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail("You can't cancel an request that is in the " + request.getStatus() + " status"));
    }

    @PutMapping("/leave_requests/{id}/complete")
    ResponseEntity<?> approve(@PathVariable Long id) {

        LeaveRequest request = repository.findById(id) //
                .orElseThrow(() -> new RequestNotFoundException(id));

        if (request.getStatus() == Status.UNDER_REVIEW) {
            request.setStatus(Status.APPROVED);
            return ResponseEntity.ok(assembler.toModel(repository.save(request)));
        }

        return ResponseEntity //
                .status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body(Problem.create() //
                        .withTitle("Method not allowed") //
                        .withDetail("You can't complete an request that is in the " + request.getStatus() + " status"));
    }
}