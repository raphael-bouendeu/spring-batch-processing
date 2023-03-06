package com.itbcafrica.springbatch.writer;

import com.itbcafrica.springbatch.model.StudentJdbc;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FirstItemWriter implements ItemWriter<StudentJdbc> {
/*    @Override
    public void write(List<? extends StudentCsv> list) throws Exception {
        System.out.println ( "Inside Item Writer" );
        list.stream ().forEach ( System.out::println );
    }*/

    @Override
    public void write(List<? extends StudentJdbc> list) throws Exception {
        System.out.println ( "Inside Item Writer" );
        list.stream ().forEach ( System.out::println );
    }
}
