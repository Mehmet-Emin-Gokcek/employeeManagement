package com.api.employeeManagement.payroll.LeaveRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import java.util.ArrayList;
import java.util.function.Consumer;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Configuration
public class RequestConfig {

    private static final Logger log = LoggerFactory.getLogger(RequestConfig.class);
    @Bean
    CommandLineRunner leaveRequestCommandLineRunner (RequestRepository repository)
    {
        return args -> {
            ArrayList<LeaveRequest> orders = new ArrayList<>();

            orders.add(new LeaveRequest(
                    "Professional Development",
                    Status.REJECTED
            ));
            orders.add(new LeaveRequest(
                    "Personal Day",
                    Status.UNDER_REVIEW
            ));

            orders.add(new LeaveRequest(
                    "Doctor's Appointment",
                    Status.APPROVED
            ));


            Consumer<LeaveRequest> saveRequest = new Consumer<LeaveRequest>() {
                @Override
                public void accept(LeaveRequest request) {

                    ExampleMatcher modelMatcher = ExampleMatcher.matching()
                            .withIgnorePaths("id")
                            .withMatcher("description", ignoreCase());

                    Example<LeaveRequest> example = Example.of(request, modelMatcher);

                    boolean exists = repository.exists(example);
                    if(!exists) {
                        repository.save(request);
                    }
                }
            };
            orders.forEach(saveRequest);
        };
    }
}
