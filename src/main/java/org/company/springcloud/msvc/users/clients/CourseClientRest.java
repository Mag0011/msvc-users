package org.company.springcloud.msvc.users.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-courses", url = "course-service:8002")
public interface CourseClientRest {

    @DeleteMapping("deleteUser/{user_id}")
    void deleteUserCourseById(@PathVariable("user_id") Long id);

}
