package com.deploy.repository;

import com.deploy.entity.Job;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JobRepositoryTest {

    @Autowired
    JobRepository jobRepository;


    @Test
    public void saveTest() {
        //given
        Job job = Job.builder()
                .name("JOB-1")
                .description("desc11")
                .build();

        //when
        jobRepository.save(job);

        //then
        Job findJob = jobRepository.findById(job.getId()).get();

        assertThat(findJob).isEqualTo(job);

    }


    @Test
    public void searchTest() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);


        //when
        Page<Job> search = jobRepository.search(null, pageRequest);

        //then
        for (Job job : search) {
            System.out.println("job = " + job);
        }

    }
}